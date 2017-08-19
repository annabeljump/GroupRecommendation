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
    private Map averagedCommonRecs;
    private HashSet<Long> movieList;
    private List<Long> recMovies;

    private List<List<ScoredId>> interimList = new ArrayList();
    private List<ScoredId> interimList2 = new ArrayList();
    private List<Long> movieRecList = new ArrayList();

    //Main method to call other methods
    public void youHaveBeenWeighed() {
        youHaveBeenMeasured();
        andYouHaveBeenFound();
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
        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            this.interimList.add(entry.getValue());
        }

        //FLATTEN the List<List<ScoredId>>
        this.interimList2 = interimList.stream().flatMap(l -> l.stream()).collect((Collectors.toList()));

        //Get the IDs
        for(ScoredId item : interimList2) {
            movieRecList.add(item.getId());
        }

        //Convert list into HashSet to get rid of repeated entries
        this.movieList = new HashSet(movieRecList);

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
        List<Double> movieScores = new ArrayList();

        for(int i = 0; i < recMovies.size(); i++){
            Long movie = recMovies.get(i);
            for(int j = 0; j < userList.size(); j++){
                Double score;
                Long user = userList.get(j);
                score = n.predict(user, movie);
                movieScores.add(score);
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
    public void wanting(UserGroup u, Long mov, List<Double> m) {

    }



    /**
     * Constructors below:
     * I have not put a full constructor with all fields
     * this is because there are two of each Map and List
     * It is better to pass specifically using Setters
     * so as to avoid incorrect assignments.
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
}
