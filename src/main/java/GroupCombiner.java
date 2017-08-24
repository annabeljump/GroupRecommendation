import org.grouplens.lenskit.scored.ScoredId;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump.
 * Group Creator for Rec Method 2
 */
public class GroupCombiner implements GroupCreator {

    private List<Long> userList = new ArrayList();
    private Map<Long, Map<Long, Double>> userRates;
    private Long host;

    public Boolean error = false;


    /**
     * This generator does not generate recommendations using the recommender
     * At this point the GroupCombiner retrieves the ratings for the users in the group
     */
    @Override
    public void getIndividualRecs() {

        //Read in from the ratings.csv file
            //Add ratings for users in Group to a map
            //There will need to be a map within a map

        BufferedReader bff = null;
        String bx = "";
        String splitter = ","; //Don't need lengthy regex here, no commas within quotes
        String ratePath = "src/ml-latest-small/ratings.csv";

        Map<Long, String> currentRatings = new HashMap<>();
        userRates = new HashMap<>();

        //First parse all ratings, add to map. String consists of movie ID and rating
        try {
            bff = new BufferedReader(new FileReader(ratePath));
            while((bx = bff.readLine()) != null) {
                String[] ratingDetails = bx.split(splitter);
                Long uID = Long.valueOf(ratingDetails[0]);
                String movrat = ratingDetails[1]+","+ratingDetails[2];
                currentRatings.put(uID, movrat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Long, Double> thisUserRatings = new HashMap<>();

        //Now cycle through users and the map of ratings to retrieve only those for the group
        for(int i=0; i < userList.size(); i++) {
            Long currentUser = userList.get(i);
            for(Map.Entry<Long, String> entry : currentRatings.entrySet()){
                if(entry.getKey() == currentUser){
                    String br = entry.getValue();
                    String[] again = br.split(splitter);
                    Long mID = Long.valueOf(again[0]);
                    Double score = Double.parseDouble(again[1]);
                    thisUserRatings.put(mID, score);
                }
            }
            //Assuming there are ratings, put them in the map
            if(!thisUserRatings.isEmpty()){
                userRates.put(currentUser, thisUserRatings);
            } else {
                System.out.println("Uh oh! Was user " + currentUser + " already registered?");
                System.out.println("If so, something went wrong!");
                System.out.println("Really sorry, we are going to have to start again!");
                error = true;
                break;
            }
        }

    }

    //Constructors

    public GroupCombiner(List<Long> users, Long h) {
        this.userList = users;
        this.host = h;
    }

    public GroupCombiner(List<Long> users) {
        this.userList = users;
        this.host = 0L;
    }

    public GroupCombiner() {
        this.userList = null;
        this.host = 0L;
    }

    //Getters + Setters

    public Map getUserRatings() {
        return this.userRates;
    }

    public List<Long> getUserList() {
        return this.userList;
    }

    public Long getHost() {
        return this.host;
    }

    public void setHost(Long h) {
        this.host = h;
    }
}
