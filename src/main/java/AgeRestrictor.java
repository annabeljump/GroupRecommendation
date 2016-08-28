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

}
