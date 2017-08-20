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
    private List<Long> movieRecList;
    public Long test = null;
    private List<Long> userList = new ArrayList();
    private Map<String, String> userRatingMap = new HashMap();
    private HashSet<Long> seenMovies;
    private List<Long> seenMovieList;


    /**
     * Eventually this will call all methods and
     * @return list of movies that are unseen
     */
    public List getUnseenMovies() {
        retrieveMovieRecs();
        retrieveRatedMovies();
        checkAndRemoveSeenMovies();
        return this.movieRecList;
    }

    /**
     * Get the list of recommended movies from the Map
     * - same as in AgeRestrictor
     */
    public void retrieveMovieRecs() {

        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            Long in = entry.getKey();
            this.interimList.add(userRecs.get(in));
        }

        //Get the ScoredIds out of the ScoredId Lists NOT USING STREAMS
        for(int a=0; a < interimList.size(); a++){
            List<ScoredId> b = interimList.get(a);
            this.interimList2.addAll(b);
        }

        movieRecList = new ArrayList<>();

        //Get the IDs
        for(ScoredId item : interimList2) {
            test = item.getId();
            movieRecList.add(test);
        }

    }

    /**
     * Retrieves the movie IDs of the movies a user has rated
     * puts them in a map.
     */

    public void retrieveRatedMovies(){

        //Read in all movies a user has rated
            //this will create a huge map - won't work!
        BufferedReader buff = null;
        String br = "";
        String split = ",";
        String filePath = "src/ml-latest-small/ratings.csv";

        seenMovies = new HashSet<>();

        try {
            buff = new BufferedReader(new FileReader(filePath));

            //Read file in line by line, with different fields separated
            while((br = buff.readLine()) != null) {
                String[] userRatings = br.split(split);

                Long uID = Long.parseLong(userRatings[0]);
                Long mID = Long.parseLong(userRatings[1]);
                //Compare user IDs, make lists of movies seen.
                //this can replace below - add movie straight to hashset??
                //this might be unwieldy
                //but hashset ensures no duplicate entries (once movie seen by one user, not added again if seen by another)
                for(int j = 0; j < userList.size(); j++) {
                    Long tempUser = userList.get(j);
                    if (tempUser.equals(uID)) {
                        seenMovies.add(mID);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.seenMovieList = new ArrayList<>(seenMovies);

        }

    /**
     * This takes the list of seen movies
     * and removes them from the list of recommendations
     */

    public void checkAndRemoveSeenMovies(){

        for(int i=0; i < movieRecList.size(); i++){
            for(int a=0; a < seenMovieList.size(); a++){
                if(movieRecList.get(i).equals(seenMovieList.get(a))){
                    movieRecList.remove(i);
                }
            }
        }

    }

    //Constructors
    public SeenYou() {
        this.userRecs = null;
        this.movieRecList = null;
    }

    public SeenYou(UserGroup u, Map r) {
        this.userRecs = r;
        this.userList = u.getUserList();
        this.movieRecList = null;
    }


    //Getters

    public List<Long> getMovieRecList() {
        return this.movieRecList;
    }

    public List<Long> getSeenMovieList(){
        return this.seenMovieList;
    }
}
