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
    private List interimList2 = new ArrayList<>();
    private List movieList = new ArrayList<>();
    private List<Long> appropriateMovies = new ArrayList<Long>();

    @Override
    public void retrieveMovies() {

        //Get the ScoredId lists out of the Map
        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            this.interimList.add(entry.getValue());
        }

        //Retrieve the scoredIds from the scoredId lists
        for(int i = 0; i < this.interimList.size(); i++) {
            this.interimList2.add(interimList.get(i));
        }

        //Now retrieve the move Ids
        for(int i = 0; i < this.interimList2.size(); i++) {
            Object temp = null;
            temp = interimList2.get(i);
            this.movieList.add(temp);
        }

        Object temp2 = this.movieList.get(0);
        temp2.getClass();

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
