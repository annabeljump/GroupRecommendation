import org.grouplens.lenskit.scored.ScoredId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump.
 * Implementation of GroupCreator.
 */
public class UserGroup implements GroupCreator {

    private List<Long> userList = new ArrayList();
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();

    public UserGroup(List<Long> users) {
        this.userList = users;
    }

    public UserGroup() {
        this.userList = null;
    }


    /**
     * getInvididualRecs to generate recommendations for users
     * individually, stored in a Map with the user IDs as the
     * key values, and the list of Recommendations as the object values
     * The Map for the UserGroup is a private field.
     */
    @Override
    public void getIndividualRecs() {

        for(Long u : userList) {
            NewRecommender morrison = new NewRecommender(u);
            userRecs.put(u, morrison.recommend());
        }
    }

    public Map getUserRecs() {
        return this.userRecs;
    }

    public List<Long> getUserList() {
        return this.userList;
    }
}
