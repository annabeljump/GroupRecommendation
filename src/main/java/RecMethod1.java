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

    private List<Long> userList = new ArrayList();

    //Step 2: Age suitability removal - are children present?


    //Step 3: Remove seen movies


    //Step 4: Multiple recommended movies average rating + added


    //Step 5: generate ratings for all other movies - add to group profile
    //This INCLUDES WEIGHTING


    @Override
    public void recommendMovies() {

        //Step 1a: Put all users in the group into a UserGroup

        System.out.println("Please enter the user IDs of the users in your group (press Q when done):");
        String user = System.console().readLine();
        if(!user.equals("Q")) {
            boolean finished = false;
            do {
                Long user1 = Long.valueOf(user);
                userList.add(user1);
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

        GroupCreator userGroup = new UserGroup(userList, hoster);

        //Step 1b: Now get their recommendations

        userGroup.getIndividualRecs();
    }


}
