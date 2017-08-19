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
    }


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

    //Step 5d: generate recommendations for all remaining movies for each user

    //Step 5e: amalgamate ratings using weightings


    //Constructors

    public WeightingGenerator(){
        this.userRecs = null;
        this.users = null;
        this.appropriateMovies = null;
        this.unseenMovies = null;
        this.averagedCommonRecs = null;
    }

    /**
     * I have not put a full constructor with all fields
     * this is because there are two of each Map and List
     * It is better to pass specifically using Setters
     * so as to avoid incorrect assignments.
     * @param us the UserGroup of users.
     */
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
