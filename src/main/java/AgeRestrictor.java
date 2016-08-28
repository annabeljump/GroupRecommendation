import org.grouplens.lenskit.scored.ScoredId;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by annabeljump
 */
public class AgeRestrictor implements AgeAppropriator {
    private UserGroup users = new UserGroup();
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();
    private List<Long> userList = new ArrayList<Long>();
    private List<List<ScoredId>> interimList = new ArrayList<>();
    private List<ScoredId> interimList2 = new ArrayList<ScoredId>();
    private List<Long> movieList = new ArrayList<>();
    private List<Long> appropriateMovies = new ArrayList<Long>();
    private Map<String, String> userAgeMap = new HashMap<>();
    private List<Long> userAgeList = new ArrayList<Long>();

    @Override
    public void retrieveMovies() {

        //Get the ScoredId lists out of the Map
        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            this.interimList.add(entry.getValue());
        }

        //FLATTEN the List<List<ScoredId>>
        this.interimList2 = interimList.stream().flatMap(l -> l.stream()).collect((Collectors.toList()));

        //Get the IDs
        for(ScoredId item : interimList2) {
            movieList.add(item.getId());
        }

    }

    @Override
    public void retrieveUsers() throws NullPointerException {
        if(users != null) {
            this.userList = users.getUserList();
        } else throw new NullPointerException();
    }

    /**
     * Accesses users.csv (copied from u.user from MK100 dataset
     * as no user details in ml-latest-small)
     * Obtains User Ages
     * Obtains Movie Genres
     * Filters appropriately
     */
    @Override
    public void checkAndRemove() {

        //TODO - can this reading of user file be put in UserGroup?
        BufferedReader buff = null;
        String br = "";
        String split = "|";
        String filePath = "src/ml-latest-small/users.csv";

        try {
            buff = new BufferedReader(new FileReader(filePath));
            //Read file in line by line, with different fields separated
            while((br = buff.readLine()) != null) {
                String[] userDetails = br.split(split);
                this.userAgeMap.put(userDetails[0], userDetails[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Find and retrieve the ages of the users in the group
        for(int i = 0; i < userList.size(); i++) {
            Long u = userList.get(i);
            this.userAgeList.add(Long.parseLong(userAgeMap.get(Long.toString(u))));
        }

        //TODO booleans to check ages

        //TODO filter movies by tags/genres to remove inappropriate ones
    }

    //Constructors
    public AgeRestrictor() {
        this.users = null;
        this.userRecs = null;
        this.userList = null;
        this.movieList = null;
        this.appropriateMovies = null;
    }

    public AgeRestrictor(UserGroup u, Map r) {
        this.users = u;
        this.userRecs = r;
        this.userList = u.getUserList();
        this.movieList = null;
        this.appropriateMovies = null;
    }

    public AgeRestrictor(UserGroup u) {
        this.users = u;
        u.getIndividualRecs();
        this.userRecs = u.getUserRecs();
        this.userList = u.getUserList();
        this.movieList = null;
        this.appropriateMovies = null;
    }

    //Getters

    public List getAppropriateMovies() {
        return this.appropriateMovies;
    }

    public List getAllMovies() {
        return this.movieList;
    }
}
