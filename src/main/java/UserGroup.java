import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump.
 * Implementation of GroupCreator.
 */
public class UserGroup implements GroupCreator {

    private List userList = new ArrayList<>();
    private Map userRecs = new HashMap<>();

    public UserGroup(List users) {
        this.userList = users;
    }


    /**
     * getInvididualRecs to generate recommendations for users
     * individually, stored in a Map with the user IDs as the
     * key values, and the list of Recommendations as the object values
     * The Map for the UserGroup is a private field.
     */
    @Override
    public void getIndividualRecs() {

        NewRecommender morrison = new NewRecommender();
        morrison.run();


    }
}
