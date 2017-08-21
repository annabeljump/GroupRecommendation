import org.grouplens.lenskit.scored.PackedScoredIdList;
import org.grouplens.lenskit.scored.ScoredId;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by annabeljump
 * Class that checks for common recommendations and averages ratings
 * Then adds to a movie list.
 */

public class CommonDenominator {

    private Map<Long, List<ScoredId>> recMap = new HashMap<>();
    private List<ScoredId> originalList = new ArrayList<>();
    private Map<Long, Double> finalRecs = new HashMap<>();
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

            ScoredId sc = originalList.get(i);

            long m = sc.getId();

            //Check if it is present in all other lists
            for(Map.Entry<Long, List<ScoredId>> entry : recMap.entrySet()) {
                List<ScoredId> sco = entry.getValue();

                HashSet moovies = new HashSet<>();

                for(int j=0; j < sco.size(); j++) {
                    ScoredId mov = sco.get(j);

                    long moo = mov.getId();

                    moovies.add(moo);
                }

                if(!moovies.contains(m)){
                    isPresent = false;
                }
            }

            //Movie should be added to list here, after all lists examined
            if(isPresent) {
                commonRec.add(sc);
            }
        }

        System.out.println(commonRec);

        //What if no items in common were recommended?
        //TODO deal with this in each recmethod
       // if(!commonRec.isEmpty()) {
            //now call helper to update finalRecs

           // obtainFinalRecs();
       // } else return;
    }

    /**
     * Method to set up the map and list to search for common movies
     */

    public void begin() {
        //Pull out first list of recommendations
        Map.Entry<Long, List<ScoredId>> entry = recMap.entrySet().iterator().next();
        List list = entry.getValue();
        //TODO do we want to add u's list back to recMap after checking in isAllRecommended() ?
        Long u = entry.getKey();
        this.originalList = list;

        //Remove that list from the map
        recMap.remove(u);

        Map.Entry<Long, List<ScoredId>> e = recMap.entrySet().iterator().next();

        List l = e.getValue();

        for(int i=0; i < l.size(); i++) {
            ScoredId it = (ScoredId) l.get(i);
            ScoredId item = (ScoredId) list.get(i);
            System.out.println((it.getId()) + " ; " + item.getId());
        }
    }

    /**
     * Method to return the map of final recommendations
     * - this will return the map of commonly recommended movies
     * (with their average predicted rating?)
     * does not need to return finalRecs, only update it
     */
    public void obtainFinalRecs(){

        //Step 1 - obtain all scores and put in a data structure
            //Step 1a - get movie ID from commonRec
            //Step 1b - initialise data structure
            //Step 1c - search originalList for movie ID and get score
            //Step 1d - search ... recMap for movie ID and get scores?
                //pull each list out and search that
                //put each score into a list

        for (ScoredId aCommonRec : commonRec) {
            Long mID = aCommonRec.getId();
            Double score1 = aCommonRec.getScore();

            ArrayList<Double> scores = new ArrayList<>();
            scores.add(score1);

            for (Map.Entry<Long, List<ScoredId>> entry : recMap.entrySet()) {

                List<ScoredId> tempList = entry.getValue();

                for (ScoredId aTempList : tempList) {

                    if (aTempList.getId() == mID) {
                        Double tempscore = aTempList.getScore();
                        scores.add(tempscore);
                    }
                }

            }


            //Step 2 - average all scores

            Double averageScore;
            Double total = 0.0;

            for (Double score : scores) {
                total += score;
            }

            averageScore = total / scores.size();

            //Step 3 - put into finalRecs - Long = movie ID, Double = average predicted score

            finalRecs.put(mID, averageScore);
        }

    }

    //Constructor
    public CommonDenominator(Map m) {
        this.recMap = m;
    }

    //Getters

    public List<ScoredId> getOriginalList() { return this.originalList; }

    public List<ScoredId> getCommonRec() { return this.commonRec; }
}
