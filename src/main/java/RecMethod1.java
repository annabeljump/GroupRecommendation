import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.scored.ScoredId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 * Class to create and run the First of
 * Two group recommendation algorithms
 */
public class RecMethod1 implements GroupRecGenerator {

    private List<Long> usersList = new ArrayList();
    private Map usersRecs = new HashMap<>();

    //We want to pass an item recommender so that unseen movies can have ratings generated
    //for the group.
    private ItemRecommender rec;


    @Override
    public void recommendMovies() {

        //Step 1a: Put all users in the group into a UserGroup

        System.out.println("Please enter the user IDs of the users in your group (press Q when done):");
        String user = System.console().readLine();
        if(!user.equals("Q")) {
            boolean finished = false;
            do {
                Long user1 = Long.valueOf(user);
                usersList.add(user1);
                System.out.println("Next user please (Q to quit):");
                user = System.console().readLine();
                if(user.equals("Q")){
                    finished = true;
                }
            } while (!finished);
        }

        System.out.println("Thank you. Please enter the host's user ID:");
        String host = System.console().readLine();
        Long hoster = Long.valueOf(host);

        UserGroup usersGroup = new UserGroup(usersList, hoster);

        //Step 1b: Now generate their recommendations

        usersGroup.getIndividualRecs();

        //Step 2: Age restrictions

        usersRecs = usersGroup.getUserRecs();

        AgeRestrictor ages = new AgeRestrictor(usersGroup, usersRecs);

        ages.retrieveMovies();
        ages.retrieveUsers();
        ages.checkAndRemove();

        //Step 2 (i) : now we have a list of appropriate movies
            //We will have to use this later, when the recommender returns the predicted ratings

        List appropriate = ages.getAppropriateMovies();


        //Step 3: Remove Seen movies.
            //Again this will need to be used later when predicted ratings returned

        SeenYou iSeeYou = new SeenYou(usersGroup, usersRecs);

        List notSeen = iSeeYou.getUnseenMovies();


        //Step 4: Carry out averaging of common recommended movies

        CommonDenominator averageCommonMovies = new CommonDenominator(usersRecs);

        Map predictedAveraged = averageCommonMovies.allRatedAndAverage();

        //Step 5: generate ratings for all other movies - add to group profile
        //This INCLUDES WEIGHTING

        //Step 5a: HashSet recommended movies?
        //Step 5b: Remove seen movies
        //Step 5c: remove age inappropriate movies
        //Step 5d: generate recommendations for all remaining movies for each user

        //Step 5e: amalgamate ratings using weightings



        //Step 6: print out recommendations using movie name and end process recommendations.


    }

    //Constructors
    public RecMethod1(){
        this.usersList = null;
        this.usersRecs = null;
        this.rec = null;
    }

    public RecMethod1(ItemRecommender itemRec){
        this.rec = itemRec;
    }
}
