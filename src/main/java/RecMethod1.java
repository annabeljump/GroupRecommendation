import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.scored.ScoredId;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by annabeljump
 * Class to create and run the First of
 * Two group recommendation algorithms
 */
public class RecMethod1 implements GroupRecGenerator {

    private List<Long> usersList;
    private Map usersRecs;
    private Map<Long, Double> recommendations;

    private UserGroup usersGroup;
    private List appropriate;
    private List notSeen;
    private Map predictedAveraged;
    private Map<String, Double> moviesToWatch;

    private AgeRestrictor ages;
    private SeenYou iSeeYou;
    private CommonDenominator averageCommonMovies;
    private WeightingGenerator weighter;

    //We want to pass an item recommender so that unseen movies can have ratings generated
    //for the group.
    private ItemRecommender rec;


    @Override
    public void recommendMovies() {

        //Step 1a: Put all users in the group into a UserGroup

        createUserGroup();

       // usersGroup = u;

       // usersGroup.getIndividualRecs();

       // usersRecs = usersGroup.getUserRecs();


        //Step 2: Generate list of appropriate movies

        ageFilter();


        //Step 3: Remove Seen movies.

        iCanSeeYou();

        //Step 4: Carry out averaging of common recommended movies

        uncommonNumerator();

        //Step 5: generate ratings for all other movies - add to group profile
        //This INCLUDES WEIGHTING

        geoffreyChaucer();

        //Step 6: print out recommendations using movie name and end process recommendations.

        sirWilliam();

    }

    public double predictRating(Long movie) {
        NewRecommender newsies = new NewRecommender();
        double score = 0.0;
        for(int i=0; i < usersList.size(); i++){
            score += newsies.predict(usersList.get(i), movie);
        }
        double scored = score / usersList.size();
        return scored;
    }

    public void createUserGroup() {

        usersList = new ArrayList<>();

        System.out.println("Please enter the user IDs of the users in your group (press Q when done):");

        Scanner scanner = new Scanner(System.in);

        String user = scanner.nextLine();

        if(!user.equals("Q")) {
            boolean finished = false;
            do {
                Long user1 = Long.valueOf(user);
                usersList.add(user1);
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
        System.out.println();

        usersGroup = new UserGroup(usersList, hoster);

        //Step 1b: Now generate their recommendations

        usersGroup.getIndividualRecs();

        //Step 2: Age restrictions

        usersRecs = new HashMap<>();

        usersRecs = usersGroup.getUserRecs();
    }

    public void ageFilter(){
        ages = new AgeRestrictor(usersGroup, usersRecs);

        ages.retrieveMovies();
        ages.retrieveUsers();
        ages.checkAndRemove();

        //Step 2 (i) : now we have a list of appropriate movies
        //We will have to use this later, when the recommender returns the predicted ratings

        appropriate = new ArrayList<>();

        appropriate = ages.getAppropriateMovies();
        // Boolean areChildrenPresent = ages.isSmallChildren();
    }

    public void iCanSeeYou() {

        iSeeYou = new SeenYou(usersGroup, usersRecs);

        notSeen = new ArrayList<>();

        notSeen = iSeeYou.getUnseenMovies();

    }

    public void uncommonNumerator() {
        averageCommonMovies = new CommonDenominator(usersRecs);

        predictedAveraged = new HashMap<>();

        predictedAveraged = averageCommonMovies.allRatedAndAverage();

    }

    public void geoffreyChaucer() {

        weighter = new WeightingGenerator(usersGroup);
        weighter.setAppropriateMovies(appropriate);
        weighter.setAveragedCommonRecs(predictedAveraged);
        weighter.setUnseenMovies(notSeen);
        weighter.setUserRecs(usersRecs);

        recommendations = new HashMap<>();

        recommendations = weighter.youHaveBeenWeighed();


    }

    public void sirWilliam() {

        List<Long> movies = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();

        for(Map.Entry<Long, Double> entry : recommendations.entrySet()) {
            movies.add(entry.getKey());
            ratings.add(entry.getValue());
        }

        NameGetter names = new NameGetter(movies);

        Map<Long, String> movieNames = names.getMovieNames();
        moviesToWatch = new HashMap<>();

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
            System.out.println("Predicted rating: " + String.format("%.02f", t.getValue()));
            System.out.println();
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
