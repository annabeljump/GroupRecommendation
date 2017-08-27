import org.grouplens.lenskit.scored.ScoredId;

import java.io.IOException;
import java.util.*;

/**
 * Created by annabeljump
 * Class to build and run the Second
 * of Two group recommendation methods
 */
public class RecMethod2 implements GroupRecGenerator{


    private List<Long> userList;
    private List<ScoredId> recommendations;
    private List appropriate;
    private Map<Long, Double> ratings;
    private Long iD;

    private GroupCombiner group;
    private AgeRestrictor age;
    private UserWriter writer;



    @Override
    public void recommendMovies() {

    //Step 1: Create group profile + write out to ratings.csv

        createGroup();

    //Step 2: Recommend

        NewRecommender morrison = new NewRecommender(iD);

        recommendations = morrison.recommend();

    }

    public void createGroup() {
        userList = new ArrayList<>();

        System.out.println("Please enter the user IDs of the users in your group (press Q when done):");

        Scanner scanner = new Scanner(System.in);

        String user = scanner.nextLine();

        if(!user.equals("Q")) {
            boolean finished = false;
            do {
                Long user1 = Long.valueOf(user);
                userList.add(user1);
                System.out.println("Next user please (Q to quit):");
                user = scanner.nextLine();
                if(user.equals("Q")){
                    finished = true;
                }
            } while (!finished);
        }

        System.out.println("Thank you. Please enter the host's user ID:");
        String host = scanner.nextLine();
        Long hoster = Long.valueOf(host);

        System.out.println("Calculating...");

        group = new GroupCombiner(userList, hoster);

        group.getIndividualRecs();

        ratings = new HashMap<>();

        ratings = group.getAveragedRatings();

        writer = new UserWriter(group, ratings);

        writer.readLastUser();

        iD = writer.getGroupID();

        try {
            writer.writeGroupRatings();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
