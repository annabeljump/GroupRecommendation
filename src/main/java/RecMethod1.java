import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.scored.ScoredId;

import java.util.*;

/**
 * Created by annabeljump
 * Class to create and run the First of
 * Two group recommendation algorithms
 */
public class RecMethod1 implements GroupRecGenerator {

    private List<Long> usersList = new ArrayList();
    private Map usersRecs = new HashMap<>();
    private Map<Long, Double> recommendations = new HashMap<>();

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

        System.out.println("Calculating...");

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
        Boolean areChildrenPresent = ages.isSmallChildren();


        //Step 3: Remove Seen movies.
            //Again this will need to be used later when predicted ratings returned

        SeenYou iSeeYou = new SeenYou(usersGroup, usersRecs);

        List notSeen = iSeeYou.getUnseenMovies();


        //Step 4: Carry out averaging of common recommended movies

        CommonDenominator averageCommonMovies = new CommonDenominator(usersRecs);

        Map predictedAveraged = averageCommonMovies.allRatedAndAverage();

        //Step 5: generate ratings for all other movies - add to group profile
        //This INCLUDES WEIGHTING

        WeightingGenerator weighter = new WeightingGenerator(usersGroup);
        weighter.setAppropriateMovies(appropriate);
        weighter.setAveragedCommonRecs(predictedAveraged);
        weighter.setUnseenMovies(notSeen);
        weighter.setUserRecs(usersRecs);

        recommendations = weighter.youHaveBeenWeighed();


        //Step 6: print out recommendations using movie name and end process recommendations.

        //Need to get names of movies

        List<Long> movies = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        for(Map.Entry<Long, Double> entry : recommendations.entrySet()) {
            movies.add(entry.getKey());
            ratings.add(entry.getValue());
        }

        NameGetter names = new NameGetter(movies);

        Map<Long, String> movieNames = names.getMovieNames();
        Map<String, Double> moviesToWatch = new HashMap<>();

        for(Map.Entry<Long, Double> e : recommendations.entrySet()) {
            for(Map.Entry<Long, String> en : movieNames.entrySet()){
                if(e.getKey().equals(en.getKey())){
                    moviesToWatch.put(en.getValue(), e.getValue());
                }
            }
        }


        System.out.println("We recommend you should watch:");
       for(Map.Entry<String, Double> t : moviesToWatch.entrySet()){
           System.out.println("Movie: " + t.getKey());
           System.out.println("Predicted rating: " + t.getValue());
       }

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
