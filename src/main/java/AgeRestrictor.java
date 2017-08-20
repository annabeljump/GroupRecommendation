import org.grouplens.lenskit.scored.ScoredId;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by annabeljump
 */
public class AgeRestrictor implements AgeAppropriator {

    private UserGroup users = new UserGroup();
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();
    private List<Long> userList = new ArrayList<Long>();
    private List<List<ScoredId>> interimList = new ArrayList<>();
    private List<ScoredId> interimList2 = new ArrayList<ScoredId>();
    private List<Long> movieList;
    public Long test = null;
    private List<Long> appropriateMovies = new ArrayList<Long>();
    private Map<String, String> userAgeMap;
    private List<Long> userAgeList;
    public Map<Long, String> movieRecMap = new HashMap<>();
    public Boolean under18 = false;
    public Boolean under15 = false;
    public Boolean under12 = false;

    @Override
    public List retrieveMovies() {

        //Get the ScoredId lists out of the Map
        for(Map.Entry<Long, List<ScoredId>> entry : userRecs.entrySet()) {
            Long in = entry.getKey();
            this.interimList.add(userRecs.get(in));
        }

        //this does not work
        //FLATTEN the List<List<ScoredId>>
        //this.interimList2 = interimList.stream().flatMap(List::stream).collect((Collectors.toList()));

        //Get the ScoredIds out of the ScoredId Lists NOT USING STREAMS
        for(int a=0; a < interimList.size(); a++){
            List<ScoredId> b = interimList.get(a);
            this.interimList2.addAll(b);
        }

        movieList = new ArrayList<>();

        //Get the IDs
        for(ScoredId item : interimList2) {
            test = item.getId();
            movieList.add(test);
        }

        return this.movieList;
    }

    @Override
    public void retrieveUsers() throws NullPointerException {
        if(users != null) {
            this.userList = users.getUserList();
        } else throw new NullPointerException();
    }

    /**
     * Accesses users.csv (copied from u.user from MK100 dataset
     * as no user details in ml-latest-small)
     * Obtains User Ages
     * Obtains Movie Genres
     * Filters appropriately
     * It may need to return the movie list
     */
    @Override
    public void checkAndRemove() {

        //BufferedReader buff = null;
        //String br;
        String splitter = "\\|";
        String filePath = "src/ml-latest-small/users.csv";

        userAgeMap = new HashMap<>();

        try {
            File f = new File(filePath);
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] details = line.split(splitter);
                String uID = details[0];
                String age = details[1];
                for(int i=0; i < userList.size(); i++) {
                    Long u = userList.get(i);
                    if (u == Long.parseLong(details[0])) {
                        this.userAgeMap.put(uID, age);
                    }

                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        userAgeList = new ArrayList<>();

        //Find and retrieve the ages of the users in the group
        for(int i = 0; i < userList.size(); i++) {
            Long u = userList.get(i);
            String ux = Long.toString(u);
            String uy = userAgeMap.get(ux);

            Long uz = Long.valueOf(uy);
            this.userAgeList.add(uz);
        }



        Long b = 50L;
        //Check if users under 18, 15, or 12, and set appropriate booleans to true
        for(int i = 0; i < userAgeList.size(); i++) {
            Long a = userAgeList.get(i);
            //Set the youngest age to variable b
            if(a < b) {
                b = a;
            }
        }

        //Set the booleans according to the youngest age
        //only the lowest age need be set to true
        if(b < 12L) {
            under12 = true;
        } else if(b < 15L) {
            under15 = true;
        } else if(b < 18L) {
            under18 = true;
        }

        //Unfortunately, movies in movies.csv do not carry BBFC (or similar) ratings
        //Therefore, filtering out will be done by the genre tags on the movies
        BufferedReader bff = null;
        String bx = "";
        String splitt = ",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";
        String moviePath = "src/ml-latest-small/movies.csv";

        //Read in movie details as with users above
        //Add to map the movie ID and the genre tags
        try {
            int i =0;
            bff = new BufferedReader(new FileReader(moviePath));
            while((bx = bff.readLine()) != null) {
                String[] movieDetails = bx.split(splitt);
                String mID = movieDetails[0];
                String mGenre = movieDetails[2];
                //movieDetailMap.put(movieDetails[0], movieDetails[2]);
                Long movieID = Long.parseLong(mID);
                for(int j=0; j < movieList.size(); j++){
                   if(movieList.get(j).equals(movieID)){
                       movieRecMap.put(movieID, mGenre);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String splitting = "\\|";
        //No need to deal in reverse, only one (if one) will be true
        if(under12){
            //Remove all movies not tagged as "Children"
            for(Map.Entry<Long, String> entry : movieRecMap.entrySet()) {
                String hi = entry.getValue();
                String[] genres = hi.split(splitting);
                Boolean isChildren = false;
                for(int i = 0; i < genres.length; i++) {
                    if(genres[i] == "Children") {
                        isChildren = true;
                    }
                }
                if(!isChildren){
                    movieRecMap.remove(entry.getKey());
                }
            }
        } else if(under15) {
            //remove "Horror" and "Thriller"
            for(Map.Entry<Long, String> entry : movieRecMap.entrySet()) {
                String hey = entry.getValue();
                String[] genres = hey.split(splitting);
                Boolean isHorror = false;
                Boolean isThriller = false;
                for(int i = 0; i < genres.length; i++) {
                    if(genres[i] == "Horror") {
                        isHorror = true;
                    } else if(genres[i] == "Thriller") {
                        isThriller = true;
                    }
                }
                if(isHorror || isThriller){
                    movieRecMap.remove(entry.getKey());
                }
            }
        } else if (under18) {
            for(Map.Entry<Long, String> entry : movieRecMap.entrySet()) {
                String hai = entry.getValue();
                String[] genres = hai.split(splitting);
                Boolean isHorror = false;
                for(int i = 0; i < genres.length; i++) {
                    if(genres[i] == "Horror") {
                        isHorror = true;
                    }
                }
                if(isHorror){
                    movieRecMap.remove(entry.getKey());
                }
            }
        }


        //this now needs to turn the Map of movies back into a movieList
        //this movieList will be appropriateMovies.
/**
        for(int i = 0; i < movieList.size(); i++) {
            if (movieRecMap.containsKey(movieList.get(i))) {
                appropriateMovies.add(movieList.get(i));
            }
        } */

    }

    //Constructors
    public AgeRestrictor() {
        this.users = null;
        this.userRecs = null;
        this.userList = null;
        this.movieList = null;
        this.appropriateMovies = null;
    }

    public AgeRestrictor(UserGroup u, Map r) {
        this.users = u;
        this.userRecs = r;
        this.userList = u.getUserList();
        this.movieList = null;
        this.appropriateMovies = null;
    }

    public AgeRestrictor(UserGroup u) {
        this.users = u;
        u.getIndividualRecs();
        this.userRecs = u.getUserRecs();
        this.userList = u.getUserList();
        this.movieList = null;
        this.appropriateMovies = null;
    }

    //Getters

    public List getAppropriateMovies() {
        return this.appropriateMovies;
    }

    public List getUserList() { return this.userList; }

    public List getAllMovies() {
        return this.movieList;
    }

    public Boolean isSmallChildren(){
        if(under12){
            return true;
        }
        else return false;
    }
}
