import org.grouplens.lenskit.scored.ScoredId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 * Class to eliminate already seen movies from recommendations
 * for the group
 */
public class Eliminator {

    private UserGroup users;
    private Map<Long, List<ScoredId>> recommendations = new HashMap<>();
    private List<Long> userList = new ArrayList<>();


    //Constructor
    public Eliminator(UserGroup u) {
        this.users = u;
        this.recommendations = u.getUserRecs();
        this.userList = u.getUserList();
    }
}
