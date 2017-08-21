import org.grouplens.lenskit.RatingPredictor;
import org.grouplens.lenskit.scored.ScoredId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by annabeljump
 * Class to generate and apply weightings
 * returns final recommendations to the group
 */
public class WeightingGenerator {

    private Map<Long, List<ScoredId>> userRecs;
    private UserGroup users;
    private List<Long> appropriateMovies;
    private List<Long> unseenMovies;
    private Map<Long, Double> averagedCommonRecs;
    private HashSet<Long> movieList;
    private List<Long> recMovies;
    private Map<Long, Double> finalRecs;
    private Long test = null;

    private List<List<ScoredId>> interimList = new ArrayList();
    private List<ScoredId> interimList2 = new ArrayList();
    private List<Long> movieRecList = new ArrayList();

    //Main method to call other methods
    public Map<Long, Double> youHaveBeenWeighed() {
        youHaveBeenMeasured();
        andYouHaveBeenFound();
        return finalRecs;
    }


    /**
     * This method extracts the movies which have been recommended
     * Then removes movies which have been seen
     * And movies which are inappropriate for the ages present
     */
    public void youHaveBeenMeasured() {
        //Make a HashSet of recommended movies to remove any repetition

        //Extract info from map of recommendations using method from other classes
        //Get the ScoredId lists out of the Map
        //Get the ScoredId lists out of the Map
        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            Long in = entry.getKey();
            this.interimList.add(userRecs.get(in));
        }

        //Get the ScoredIds out of the ScoredId Lists NOT USING STREAMS
        for(int a=0; a < interimList.size(); a++){
            List<ScoredId> b = interimList.get(a);
            this.interimList2.addAll(b);
        }

        movieList = new HashSet<>();

        //Get the IDs
        for(ScoredId item : interimList2) {
            test = item.getId();
            movieList.add(test);
        }

        //Convert list into HashSet to get rid of repeated entries
        //this.movieList = new HashSet(movieRecList);

        //Going to have to convert it back to iterate through it
        List<Long> movies = new ArrayList<Long>(movieList);


        //Remove seen movies from the list
        for(int i = 0; i < movies.size(); i++){
            Long mov = movies.get(i);
            if(!unseenMovies.contains(mov)){
                movies.remove(i);
            }
        }


        //Remove age inappropriate movies from the list
        for(int j = 0; j < movies.size(); j++){
            Long m = movies.get(j);
            if(!appropriateMovies.contains(j)){
                movies.remove(j);
            }
        }

        //Now initialise list of recommended movies (after removal of seen/inappropriate movies)
        this.recMovies = movies;
    }

    /**
     * This method generates ratings for the final list of recommended movies
     * for each user in the group, ready for weightings to be applied
     * This method should call the weighting method because:
     * -- Otherwise would have to use a complicated data structure to store the ratings
     * -- This way weightings can be applied and only the final rating is stored for the movie
     */
    public void andYouHaveBeenFound() {

        //Make a new recommender so that predict() method can be used
        NewRecommender n = new NewRecommender();

        List<Long> userList = users.getUserList();
        Map<Long, Double> movieScores = new HashMap();

        for(int i = 0; i < recMovies.size(); i++){
            Long movie = recMovies.get(i);
            for(int j = 0; j < userList.size(); j++){
                Double score;
                Long user = userList.get(j);
                score = n.predict(user, movie);
                movieScores.put(user, score);
            }
            wanting(users, movie, movieScores);
        }

    }

    /**
     * Method to apply weightings and generate final group score for each movie
     * Each time this is called, it adds the movie ID and score to a Map of final recommendations
     * @param u the UserGroup for which recommendations are being made
     * @param mov the movieID of the movie for which the scores have been generated
     * @param m the list of scores for one movie
     */
    public void wanting(UserGroup u, Long mov, Map<Long, Double> m) {

        //Get the Host.
        Long host = u.getHost();


        //Least Misery - set a low rating threshold - if lowest rating is lower, do not add to final recs
        //Scores should be added to a list. List is only for this, as weighting needs to be done otherwise
        List<Double> scores = new ArrayList<Double>(m.values());

        Double lowScore = scores.get(0);
        for(int i = 0; i < scores.size(); i++){
            if(scores.get(i) < lowScore){
                lowScore = scores.get(i);
            }
        }

        if(lowScore <= 1.0){ return;}

        //Now add scores to a list for averaging.
        //if the user is Host, double the score if it is higher than 2.5, halve if lower.
        //this should double the impact on the averaged rating.

        List<Double> avScores = new ArrayList<Double>();

        for(Map.Entry<Long, Double> entry : m.entrySet()) {
            Double sc = entry.getValue();
            if(entry.getKey() == host){
                if(sc > 2.5) {
                    avScores.add(sc * 2);
                } else {
                    avScores.add(sc / 2);
                }
            } else {
                avScores.add(sc);
            }
        }

        //Average the scores
        Double averageScore;
        Double total = 0.0;

        for (Double score : avScores) {
            total += score;
        }

        averageScore = total / avScores.size();

        //if more than half the ratings are low, minus 1 to the average rating?
        //if more than half the ratings are high, add 1 to the average rating?

        int no = u.getUserList().size();

        int highScore = 0;
        int lowScores = 0;

        for (Double score : scores) {
            if (score < 2.0) {
                lowScores++;
            } else if (score > 3.0) {
                highScore++;
            }
        }

        if(lowScores > (no/2)){
            averageScore = averageScore - 1.0;
        } else if(highScore > (no/2)) {
            averageScore = averageScore + 1.0;
        }

        //Now factor in movies which were commonly recommended.

        assert !averagedCommonRecs.isEmpty();

        for(Map.Entry<Long, Double> e : averagedCommonRecs.entrySet()) {
            if(Objects.equals(e.getKey(), mov)){
                if(e.getValue() > 3.0){
                    averageScore = averageScore + 1.0;
                } else if(e.getValue() < 2.0){
                    averageScore = averageScore - 1.0;
                }
            }
        }

        //Make sure the average score is no higher than 5.0 or lower than 0.5
        //THIS NEEDS REVIEWING - ratings seem to be higher than 5

        //if(averageScore > 5.0){
        //    averageScore = 5.0;
        //} else if(averageScore < 0.5){
        //    averageScore = 0.5;
        //}

        //TODO figure out how to do something with the date here.

        finalRecs.put(mov, averageScore);
    }



    /**
     * Constructors below:
     * I have not put a full constructor with all fields
     * this is because there are two of each Map and List
     * It is better to pass specifically using Setters
     * so as to avoid incorrect assignments.
     *
     */

    public WeightingGenerator(){
        this.userRecs = null;
        this.users = null;
        this.appropriateMovies = null;
        this.unseenMovies = null;
        this.averagedCommonRecs = null;
    }

    public WeightingGenerator(UserGroup us){
        this.users = us;

    }


    //Getters + Setters

    public Map getUserRecs() {
        return userRecs;
    }

    public void setUserRecs(Map userRecs) {
        this.userRecs = userRecs;
    }

    public UserGroup getUsers() {
        return users;
    }

    public void setUsers(UserGroup users) {
        this.users = users;
    }

    public List getAppropriateMovies() {
        return appropriateMovies;
    }

    public void setAppropriateMovies(List appropriateMovies) {
        this.appropriateMovies = appropriateMovies;
    }

    public List getUnseenMovies() {
        return unseenMovies;
    }

    public void setUnseenMovies(List unseenMovies) {
        this.unseenMovies = unseenMovies;
    }

    public Map getAveragedCommonRecs() {
        return averagedCommonRecs;
    }

    public void setAveragedCommonRecs(Map averagedCommonRecs) {
        this.averagedCommonRecs = averagedCommonRecs;
    }

    public Map<Long, Double> getFinalRecs() {
        return finalRecs;
    }

    public void setFinalRecs(Map<Long, Double> finalRecs) {
        this.finalRecs = finalRecs;
    }
}
