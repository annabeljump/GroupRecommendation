import org.grouplens.lenskit.scored.ScoredId;

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


    //Constructor
    public Eliminator(UserGroup u) {
        this.users = u;
        this.recommendations = u.getUserRecs();
        this.userList = u.getUserList();
    }
}
