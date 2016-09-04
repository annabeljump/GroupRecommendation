import org.grouplens.lenskit.scored.ScoredId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 * Class that checks for common recommendations and averages ratings
 * Then adds to a movie list.
 */

public class CommonDenominator {

    private Map<Long, List<ScoredId>> recMap = new HashMap<>();
    private List<ScoredId> originalList = new ArrayList<>();
    private Map<Long, Long> finalRecs = new HashMap<>();


    /**
     * Method to be the only call needed from this class
     * Will set up and then run the comparison and average
     * of the recommended movies
     * @return Map of movie ID and predicted average rating
     */
    public Map allRatedAndAverage() {
        begin();

        return finalRecs;
    }


    /**
     * Method to set up the map and list to search for common movies
     */
    public void begin() {
        //Pull out first list of recommendations
        Map.Entry<Long, List<ScoredId>> entry = recMap.entrySet().iterator().next();
        List list = entry.getValue();
        Long u = entry.getKey();
        this.originalList = list;

        //Remove that list from the map
        recMap.remove(u);
    }


    //Constructor
    public CommonDenominator(Map m) {
        this.recMap = m;
    }
}
