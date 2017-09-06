import java.io.*;
import java.util.*;

/**
 * Created by annabeljump.
 * Group Creator for Rec Method 2
 */
public class GroupCombiner implements GroupCreator {

    private List<Long> userList;
    private Map<Long, Map<Long, Double>> userRates;
    private Long host;

    private List<Long> movieList;
    private List<Long> otherMovies;
    private Map<Long, Double> averagedRatings;
    private Map<Long, Double> unCommonRatings;



    /**
     * This generator does not generate recommendations using the recommender
     * At this point the GroupCombiner retrieves the ratings for the users in the group
     */
    @Override
    public void getIndividualRecs() {

        //Read in from the ratings.csv file
        //Add ratings for users in Group to a map
        //There will need to be a map within a map


        Map<Long, List<String>> currentRatings = new HashMap<>();
        userRates = new HashMap<>();

        String filePath = "src/ml-latest-small/ratings.csv";
        BufferedReader bff = null;
        String bx = "";

        String splitter = ","; //Don't need lengthy regex here, no commas within quotes

        List<String> t = new ArrayList<>();


        try {
            Long u = 105L;
            bff = new BufferedReader(new FileReader(filePath));
            while((bx = bff.readLine()) != null) {
                String[] li = bx.split(splitter);
                Long uID = Long.parseLong(li[0]);
                String st = li[1] + "," + li[2];
                if(u==uID) {
                    t.add(st);
                } else {
                    currentRatings.put(u, new ArrayList<>(t));
                    u = uID;
                    t.clear();
                    t.add(st);
                }
            } currentRatings.put(u, new ArrayList<>(t));

        } catch (IOException e) {
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
               if (userID == currentUser) {
                    for (int j = 0; j < br.size(); j++) {
                        String blah = br.get(j);
                        String[] again = blah.split(",");
                        Long mID = Long.parseLong(again[0]);
                        Double score = Double.parseDouble(again[1]);
                        thisUserRatings.put(mID, score);
                    }
                }
            }
            userRates.put(currentUser, new HashMap<>(thisUserRatings));
            thisUserRatings.clear();
        }


        averageRatings(userRates);

        addBack();
    }


    /**
     * This borrows code from CommonDenominator (as it essentially does the same thing)
     * As well as WeightingGenerator, as here is where the weighting is done
     */
    private void averageRatings(Map<Long, Map<Long, Double>> rat) {

        averagedRatings = new HashMap<>();

        movieList = new ArrayList<>();

        otherMovies = new ArrayList<>();

        Map<Long, Map<Long, Double>> internalRatings = new HashMap<>(rat);

        Map<Long, Double> userMap;

        Map.Entry<Long, Map<Long, Double>> entry = internalRatings.entrySet().iterator().next();

        userMap = entry.getValue();

        internalRatings.remove(entry.getKey());

        for(Map.Entry<Long, Double> e : userMap.entrySet()) {

            Boolean isPresent = true;

            Long m = e.getKey();

            HashSet<Long> movies = new HashSet<>();

            //Check if it is present in other lists
            for (Map.Entry<Long, Map<Long, Double>> ent : internalRatings.entrySet()) {
                Map<Long, Double> sco = ent.getValue();

                for (Map.Entry<Long, Double> longDoubleEntry : sco.entrySet()) {
                    Long mID = longDoubleEntry.getKey();

                    movies.add(mID);
                }

            }

            if (!movies.contains(m)) {
                isPresent = false;
            }

            if(isPresent) {
                movieList.add(m);

            } else {
                otherMovies.add(m);
            }

        }

        internalRatings.put(entry.getKey(), entry.getValue());

        for (Long aCommonRec : movieList) {

            List<Double> scores = new ArrayList<>();

            Boolean isHost = false;

            for (Map.Entry<Long, Map<Long, Double>> dentry : internalRatings.entrySet()) {
                Long h = dentry.getKey();

                if(h == host) {
                    isHost = true;
                }

                Map<Long, Double> internalMap = dentry.getValue();

                for(Map.Entry<Long, Double> inEntry : internalMap.entrySet()) {
                    Double sc = inEntry.getValue();

                    Long ho = inEntry.getKey();

                    if((ho == aCommonRec) && isHost){
                            if(sc > 2.5) {
                                Double hostscore = sc * 2;
                                scores.add(hostscore);
                            } else {
                                Double badscore = sc / 2;
                                scores.add(badscore);
                            }

                        } else if((ho == aCommonRec) && !isHost){
                            scores.add(sc);
                        }
                }
                if(isHost) {
                    isHost = false;
                }
            }

            Double averageScore;
            Double total = 0.0;

            for (Double score : scores) {
                total += score;
            }


            averageScore = total / scores.size();

            int no = userList.size();

            int highScore = 0;
            int lowScores = 0;

            for (Double score : scores) {
                if (score < 2.0) {
                    lowScores++;
                } else if (score > 3.0) {
                    highScore++;
                }
            }

            if(lowScores > (no/2)){
                averageScore = averageScore - 1.0;
            } else if(highScore > (no/2)) {
                averageScore = averageScore + 1.0;
            }

            //Need to make sure there are no ratings over 5
            //and no ratings below 0

            if(averageScore > 5.0){
                averageScore = 5.0;
            } else if (averageScore < 0.0){
                averageScore = 0.0;
            }

            averagedRatings.put(aCommonRec, averageScore);
        }
    }

    /**
     * There will be ratings rated by only one user
     * These ratings should be added back to the group ratings
     * Host weighting should be applied here
     */
    public void addBack() {

        unCommonRatings = new HashMap<>();

        for(Long movie : otherMovies){
            for (Map.Entry<Long, Map<Long, Double>> dentry : userRates.entrySet()) {

                Map<Long, Double> internalMap = dentry.getValue();

                Long h = dentry.getKey();

                for (Map.Entry<Long, Double> inEntry : internalMap.entrySet()) {
                    Double sc = inEntry.getValue();
                    Long mID = inEntry.getKey();

                    if (movie.equals(mID) && h.equals(host)) {
                        if (sc < 2.5) {
                            Double score = sc-1.0;
                            if(score > 5.0){
                                score = 5.0;
                            } else if (score < 0.0) {
                                score = 0.0;
                            }
                            averagedRatings.put(mID, (score));
                            unCommonRatings.put(mID, (score));
                        } else if (sc > 2.5) {
                            Double score = sc-1.0;
                            if(score > 5.0){
                                score = 5.0;
                            } else if (score < 0.0) {
                                score = 0.0;
                            }
                            averagedRatings.put(mID, score);
                            unCommonRatings.put(mID, score);
                        }
                    } else if (movie.equals(mID)) {
                        if(sc > 5.0){
                            sc = 5.0;
                        } else if (sc < 0.0) {
                            sc = 0.0;
                        }
                        averagedRatings.put(mID, sc);
                        unCommonRatings.put(mID, sc);
                    }
                }
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

    public Map<Long, Double> getAveragedRatings() { return this.averagedRatings; }

    public List<Long> getMovieList() {return this.movieList; }

    public List<Long> getOtherMovies() { return this.otherMovies;}

    public Map<Long, Double> getUnCommonRatings() { return this.unCommonRatings; }

    public Long getHost() {
        return this.host;
    }

    public void setHost(Long h) {
        this.host = h;
    }
}
