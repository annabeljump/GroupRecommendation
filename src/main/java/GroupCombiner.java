import org.grouplens.lenskit.scored.ScoredId;

import java.io.*;
import java.util.*;

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

        String splitter = ","; //Don't need lengthy regex here, no commas within quotes

        Map<Long, List<String>> currentRatings = new HashMap<>();
        userRates = new HashMap<>();

        String filePath = "src/ml-latest-small/ratings.csv";

        List<Long> l = new ArrayList<>();
        List<String> t = new ArrayList<>();
        List<List<String>> tester = new ArrayList<>();


        try {
            File f = new File(filePath);
            Scanner sc = new Scanner(f);


            Long u = 1L;
           Boolean finished = false;

            while ((sc.hasNextLine()) && !finished){
                String line = sc.nextLine();
                String[] li = line.split(splitter);
                Long uID = Long.valueOf(li[0]);
                String st = li[1] + " , " + li[2];
                if(u.equals(uID)) {
                    t.add(st);
                } else {
                    tester.add(t);
                    t.clear(); //THIS SEEMS TO BE THE PROBLEM
                    u = uID;
                    if(uID.equals(3L)) {
                        finished = true;
                    }
                }
            }

            /**
             *  I really cannot see why this doesn't work.

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] details = line.split(splitter);
                Long uID = Long.valueOf(details[0]);
                String movrat = details[1] + "," + details[2];
                if(Objects.equals(u, uID)) {
                    l.add(movrat);
                } else {
                    currentRatings.put(u, l);
                    l.clear();
                    u = uID;
                    l.add(movrat);
                }
            }
             */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println(t);
        System.out.println(tester.get(1));

        /**
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
         */

        /**
         *  This clearly doesn't work and I am not sure why
         *
        Map<Long, Double> thisUserRatings = new HashMap<>();

        //Now cycle through users and the map of ratings to retrieve only those for the group
        for (int i = 0; i < userList.size(); i++) {
            Long currentUser = userList.get(i);
            for (Map.Entry<Long, List<String>> entry : currentRatings.entrySet()) {
                Long userID = entry.getKey();
                List<String> br = entry.getValue();
                if (Objects.equals(userID, currentUser)) {
                    for(int j=0; j < br.size(); j++) {
                        String[] again = br.get(j).split(",");
                        Long mID = Long.valueOf(again[0]);
                        Double score = Double.parseDouble(again[1]);
                        thisUserRatings.put(mID, score);
                    }
                }
            }

         */
            //Assuming there are ratings, put them in the map
  //          if (!thisUserRatings.isEmpty()) {
                //userRates.put(currentUser, thisUserRatings);
  //          } else {
  //              System.out.println("Uh oh! Was user " + currentUser + " already registered?");
 //               System.out.println("If so, something went wrong!");
 //               System.out.println("Really sorry, we are going to have to start again!");
//                error = true;
 //               break;
 //           }


        //}

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
