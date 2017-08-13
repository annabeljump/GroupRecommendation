import org.grouplens.lenskit.scored.ScoredId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by annabeljump
 * Class to remove already seen movies from the map of recommendations
 */
public class SeenYou {

    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();
    private List<List<ScoredId>> interimList = new ArrayList<>();
    private List<ScoredId> interimList2 = new ArrayList<ScoredId>();
    private List<Long> movieRecList = new ArrayList<>();
    private UserGroup users = new UserGroup();
    private List<Long> userList = new ArrayList<Long>();
    private List<Long> unseenMovies = new ArrayList<Long>();

    /**
     * Eventually this will call all methods and
     * @return list of movies that are unseen
     */
    public List getUnseenMovies() {
        retrieveMovieRecs();
        retrieveRatedMovies();
        checkAndRemoveSeenMovies();
        return this.unseenMovies;
    }

    /**
     * Get the list of recommended movies from the Map
     * - same as in AgeRestrictor
     */
    public void retrieveMovieRecs() {

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

    }

    /**
     * Retrieves the movie IDs of the movies a user has rated
     * puts them in a list.
     */

    //TODO store the user ID as key and list of rated movies in a Map??

    public void retrieveRatedMovies(){

    }

    public void checkAndRemoveSeenMovies(){

    }

    //Constructors
    public SeenYou() {
        this.users = null;
        this.userRecs = null;
        this.movieRecList = null;
    }

    public SeenYou(UserGroup u, Map r) {
        this.users = u;
        this.userRecs = r;
        this.userList = u.getUserList();
        this.movieRecList = null;
    }


}
