import org.grouplens.lenskit.scored.ScoredId;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by annabeljump
 * Class to remove already seen movies from the map of recommendations
 */
public class SeenYou {

    private Map<Long, List<ScoredId>> userRecs = new HashMap();
    private List<List<ScoredId>> interimList = new ArrayList();
    private List<ScoredId> interimList2 = new ArrayList();
    private List<Long> movieRecList = new ArrayList();
    private UserGroup users = new UserGroup();
    private List<Long> userList = new ArrayList();
    private List<Long> unseenMovies = new ArrayList();
    private Map<String, String> userRatingMap = new HashMap();
    private HashSet<Long> seenMovies = new HashSet();


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
     * puts them in a map.
     */

    public void retrieveRatedMovies(){

        //Read in all movies a user has rated
            //this will create a huge map - TODO - review
        BufferedReader buff = null;
        String br = "";
        String split = "|";
        String filePath = "src/ml-latest-small/ratings.csv";

        try {
            buff = new BufferedReader(new FileReader(filePath));
            //Read file in line by line, with different fields separated
            while((br = buff.readLine()) != null) {
                String[] userRatings = br.split(split);
                this.userRatingMap.put(userRatings[0], userRatings[1]);
                userRatings = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Now make a List of movies per user and add each movie rated to
        //the movieSeen hashset - set ensures no duplicates!
        for(int i = 0; i < userList.size(); i++) {
            Long u = userList.get(i);


            //this.userAgeList.add(Long.parseLong(userAgeMap.get(Long.toString(u))));
        }
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
