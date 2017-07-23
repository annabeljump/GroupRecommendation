import org.grouplens.lenskit.scored.PackedScoredIdList;
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
    private Boolean isCommon = false;
    private List<ScoredId> commonRec = new ArrayList<>();


    /**
     * Method to be the only call needed from this class
     * Will set up and then run the comparison and average
     * of the recommended movies
     * @return Map of movie ID and predicted average rating
     */
    public Map allRatedAndAverage() {
        begin();
        isAllRecommended();

        return finalRecs;
    }


    /**
     * Method to check if movie contained in all recommendations
     */
    public void isAllRecommended() {
        //Get ScoredId
        for(int i = 0; i < originalList.size(); i++) {
            Boolean isPresent = true;

            //Check if it is present in all other lists
            for(Map.Entry<Long, List<ScoredId>> entry : recMap.entrySet()) {
                if(!entry.getValue().contains(originalList.get(i))) {
                    isPresent = false;
                }

            }

            //Movie should be added to list here(?)
            if(isPresent) {
                commonRec.add(originalList.get(i));
            }
        }

        //TODO Call helper method here to assist with generating finalRec list.
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

    /**
     * Method to return the map of final recommendations
     * - this will return the map of commonly recommended movies
     * (with their average predicted rating?)
     * @return Map of movie IDs and predicted rating
     */
    public Map obtainFinalRecs(){

        return finalRecs;
    }

    //Constructor
    public CommonDenominator(Map m) {
        this.recMap = m;
    }
}
