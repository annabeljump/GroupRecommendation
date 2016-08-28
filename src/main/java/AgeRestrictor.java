import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 */
public class AgeRestrictor implements AgeAppropriator {
    private UserGroup users = new UserGroup();
    private Map userRecs = new HashMap<>();
    private List<Long> userList = new ArrayList<Long>();
    private List<Long> movieList = new ArrayList<Long>();
    private List<Long> appropriateMovies = new ArrayList<Long>();

    @Override
    public void retrieveMovies() {

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
