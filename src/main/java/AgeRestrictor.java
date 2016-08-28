import org.grouplens.lenskit.scored.ScoredId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 */
public class AgeRestrictor implements AgeAppropriator {
    private UserGroup users = new UserGroup();
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();
    private List<Long> userList = new ArrayList<Long>();
    private List interimList = new ArrayList<>();
    private List<Long> movieList = new ArrayList<Long>();
    private List<Long> appropriateMovies = new ArrayList<Long>();

    @Override
    public void retrieveMovies() {

        //Get the ScoredId lists out of the Map
        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            this.interimList.add(entry.getValue());
        }

        //Retrieve the Movie Id from the ScoredId
        for(int i = 0; i < this.interimList.size(); i++) {

        }

    }

    @Override
    public void retrieveUsers() {

    }

    @Override
    public void checkAndRemove() {

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
