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

    private List<Long> movieList;


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


        try {
            File f = new File(filePath);
            Scanner sc = new Scanner(f);

            Long u = 1L;

            while (sc.hasNextLine()){
                String line = sc.nextLine();
                String[] li = line.split(splitter);
                Long uID = Long.valueOf(li[0]);
                String st = li[1] + "," + li[2];
                if(u.equals(uID)) {
                    t.add(st);
                } else {
                    currentRatings.put(uID, new ArrayList<>(t));
                    u = uID;
                    t.clear();
                    t.add(st);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Map<Long, Double> thisUserRatings = new HashMap<>();
        Long currentUser;
        Long userID;
        List<String> br;

        //Now cycle through users and the map of ratings to retrieve only those for the group
        for (int i = 0; i < userList.size(); i++) {
            currentUser = userList.get(i);
            for (Map.Entry<Long, List<String>> entry : currentRatings.entrySet()) {
                userID = entry.getKey();
                br = entry.getValue();
               if (userID.equals(currentUser)) {
                    for (int j = 0; j < br.size(); j++) {
                        String blah = br.get(j);
                        String[] again = br.get(j).split(",");
                        Long mID = Long.parseLong(again[0]);
                        Double score = Double.parseDouble(again[1]);
                        thisUserRatings.put(mID, score);
                    }
                }

            }
            userRates.put(currentUser, new HashMap<>(thisUserRatings));
            thisUserRatings.clear();
        }

        weightProfile();
    }

    /**
     * Method to generate weighted user profile
     * - will average ratings of common movies
     * - then will apply weightings based on hosts etc
     */
    public void weightProfile() {
        //First, call method to average common ratings
        averageRatings();
    }

    /**
     * This borrows code from CommonDenominator (as it essentially does the same thing)
     */
    public void averageRatings() {
        Map<Long, Double> userMap;
        userList = new ArrayList<>();

        Map.Entry<Long, Map<Long, Double>> entry = userRates.entrySet().iterator().next();
        Map map = entry.getValue();
        Long u = entry.getKey();
        userMap = map;

        for(Map.Entry<Long, Double> e : userMap.entrySet()) {
            Boolean isPresent = true;

            Long m = e.getKey();

            HashSet movies = new HashSet<>();

            //Check if it is present in all other lists
            for (Map.Entry<Long, Map<Long, Double>> ent : userRates.entrySet()) {
                Map<Long, Double> sco = ent.getValue();


                for (Map.Entry<Long, Double> longDoubleEntry : sco.entrySet()) {
                    Long mID = longDoubleEntry.getKey();

                    movies.add(mID);
                }

                if (!movies.contains(m)) {
                    isPresent = false;
                }
            }

            if(isPresent) {
                movieList.add(m);
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
