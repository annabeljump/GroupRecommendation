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
    private List<Long> appropriate;
    private List<Long> movies;
    private Map<Long, Double> ratings;
    private Map<String, Double> moviesToWatch;
    private Long iD;

    private GroupCombiner group;
    private GroupAges age;
    private UserWriter writer;



    @Override
    public void recommendMovies() {

    //Step 1: Create group profile + write out to ratings.csv

        createGroup();

    //Step 2: Recommend

        NewRecommender morrison = new NewRecommender(iD);

        recommendations = morrison.recommend();

    //Step 3: filter ages

        age = new GroupAges(group, recommendations);

        List<Long> movieList = new ArrayList<>();

        movieList = age.retrieveMovies();

        age.retrieveUsers();

        appropriate = new ArrayList();

        appropriate = age.checkAndRemove();

        //Now iterate through recommendation list and remove those movies not on the appropriate list

        for(int i=0; i < recommendations.size(); i++) {
            ScoredId rec = recommendations.get(i);
            Long mID = rec.getId();
            if(!appropriate.contains(mID)){
                recommendations.remove(i);
            } else if(appropriate.contains(mID)){
                movies.add(mID);
            }
        }

    //Step 4: Get names and print recommendations!

        NameGetter names = new NameGetter(movies);
        Map<Long, String> movieNames = names.getMovieNames();
        moviesToWatch = new HashMap<>();

        for(int j=0; j < recommendations.size(); j++) {
            ScoredId recs = recommendations.get(j);
            Long mov = recs.getId();
            Double score = recs.getScore();
            for(Map.Entry<Long, String> en : movieNames.entrySet()){
                if(mov.equals(en.getKey())){
                    moviesToWatch.put(en.getValue(), score);
                }
            }
        }

        System.out.println("We recommend you should watch:");
        for(Map.Entry<String, Double> t : moviesToWatch.entrySet()){
            System.out.println("Movie: " + t.getKey());
            System.out.println("Predicted rating: " + t.getValue());
        }

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
