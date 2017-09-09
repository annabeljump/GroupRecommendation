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
    private List recommendations;
    private List<ScoredId> scoredIdList;
    private List<Long> appropriate;
    private List<Long> movies;
    private Map<Long, Double> ratings;
    private Map<String, Double> moviesToWatch;
    private Long iD;

    private GroupCombiner group;
    private GroupAges age;
    private UserWriter writer;


    public double predictRating(Long movie) {
        NewRecommender morrison = new NewRecommender(iD);

        return morrison.predict(iD, movie);

    }

    @Override
    public void recommendMovies() {

    //Step 1: Create group profile + write out to ratings.csv

        createGroup();

    //Step 2: Recommend

        recommendations = new ArrayList<>();

        NewRecommender morri = new NewRecommender(iD);

        recommendations = morri.recommend();

        scoredIdList = new ArrayList<>();

        scoredIdList.addAll(recommendations);



    //Step 3: filter ages

        age = new GroupAges(group, scoredIdList);

        List<Long> movieList;

        movieList = age.retrieveMovies();

        age.retrieveUsers();

        appropriate = new ArrayList();

        appropriate = age.checkAndRemove();

        movies = new ArrayList<>();

        //Now iterate through recommendation list and remove those movies not on the appropriate list

        for(int i=0; i < scoredIdList.size(); i++) {
            ScoredId rec = scoredIdList.get(i);
            Long mID = rec.getId();
            int a = i;
            if(!appropriate.contains(mID)){
                scoredIdList.remove(a);

            } else if(appropriate.contains(mID)){
                double low = 5.0;

                for (Long users : userList) {
                    NewRecommender pred = new NewRecommender();

                    double score = pred.predict(users, mID);

                    if(score < low){
                        low = score;
                    }

                }

                if(low < 1.5){
                    scoredIdList.remove(a);
                } else {
                    movies.add(mID);
                }
            }
        }



    //Step 4: Get names and print recommendations!

        NameGetter names = new NameGetter(movies);
        Map<Long, String> movieNames = names.getMovieNames();
        moviesToWatch = new HashMap<>();

        for(int j=0; j < scoredIdList.size(); j++) {
            ScoredId recs = scoredIdList.get(j);
            Long mov = recs.getId();
            Double score = recs.getScore();
            if(score > 5.0) {
                score = 5.0;
            } else if (score < 0.0) {
                score = 0.0;
            }
            for(Map.Entry<Long, String> en : movieNames.entrySet()){
                if(mov.equals(en.getKey())){
                    moviesToWatch.put(en.getValue(), score);
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
        System.out.println();

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
