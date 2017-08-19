import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 * Class to generate and apply weightings
 * returns final recommendations to the group
 */
public class WeightingGenerator {

    private Map userRecs;
    private UserGroup users;
    private List appropriateMovies;
    private List unseenMovies;
    private Map averagedCommonRecs;



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
