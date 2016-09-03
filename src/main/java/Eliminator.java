import org.grouplens.lenskit.scored.ScoredId;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by annabeljump
 * Class to eliminate already seen movies from recommendations
 * for the group
 */
public class Eliminator {

    private UserGroup users;
    private Map<Long, List<ScoredId>> recommendations = new HashMap<>();
    private List<Long> userList = new ArrayList<>();
    private List<Long> movies = new ArrayList<>();
    private List<Long> seen = new ArrayList<>();
    private Map<Long, List<ScoredId>> filteredRecs = new HashMap<>();


    /**
     * This method is substantially the same as getMovies() in AgeRestrictor
     * This is because we need to unpack the Map of recommendations.
     * Here I have used two transient variables within the method,
     * which will enable the class field to be updated.
     */
    public void getRecommendedMovies() {

        List<List<ScoredId>> interimList = new ArrayList<>();
        List<ScoredId> interimList2;

        //Get the ScoredId lists out of the Map
        for(Map.Entry<Long, List<ScoredId>> entry : recommendations.entrySet()) {
            interimList.add(entry.getValue());
        }

        //FLATTEN the List<List<ScoredId>>
        interimList2 = interimList.stream().flatMap(l -> l.stream()).collect((Collectors.toList()));

        //Get the IDs
        for(ScoredId item : interimList2) {
            movies.add(item.getId());
        }

    }

    /**
     * Reads in the Ratings.csv file and searches for movies rated by each user in userList
     * Adds these movies to a list, to be used to remove these movies from the recommendation list.
     */
    public void getSeenMovies() {
        BufferedReader buff;
        String br;
        String split = ",";
        String filePath = "src/ml-latest-small/ratings.csv";

        try {
            buff = new BufferedReader(new FileReader(filePath));
            //Read file in line by line, with different fields separated
            while((br = buff.readLine()) != null) {
                String[] userMovies = br.split(split);
                //if the user is in our group, add the rated movie to our list of seen movies=-
                if(userList.contains(userMovies[0])) {
                seen.add(Long.parseLong(userMovies[1]));
                }
                userMovies = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Method that compares the movies which have been seen to the movies which have been
     * recommended and removes them from the recommendations
     * @return map of recommended *unseen* movies
     */
    public Map removeSeenMovies() {

        //Get the movie IDs from the seen list
        for(int i = 0; i < seen.size(); i++) {
            Long u = seen.get(i);
            //iterate through map of recommendations
            for(Map.Entry<Long, List<ScoredId>> entry : recommendations.entrySet()) {
                //check each list of recommendations for movie with ID and remove
                //extract user ID and List of ScoredIds to temp variables
                Long ux = entry.getKey();
                List<ScoredId> mx = entry.getValue();
                //check list of scoredIds for movie in seen list
                for(int ix = 0; ix < mx.size(); ix++) {
                    //remove that entry if present
                    if(mx.get(ix).getId() == u) {
                        mx.remove(ix);
                    }
                }
                //put new list of movies into new map
                filteredRecs.put(ux, mx);
            }
        }
        //return the new map of unseen movie recommendations
        return filteredRecs;
    }


    //Constructor
    public Eliminator(UserGroup u) {
        this.users = u;
        this.recommendations = u.getUserRecs();
        this.userList = u.getUserList();
    }
}
