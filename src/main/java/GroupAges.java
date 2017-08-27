import org.grouplens.lenskit.scored.ScoredId;

import java.io.*;
import java.util.*;

/**
 * Created by annabeljump.
 * Age filter for RecMethod2
 */

public class GroupAges implements AgeAppropriator {

    private GroupCombiner group;
    private List<Long> userList;
    private List<ScoredId> recommendations = new ArrayList<>();
    private List<Long> movieList;

    private List<Long> userAgeList;
    private List<Long> appropriateMovieList;
    private Map<Long, String> movieTagMap;
    public Boolean under18 = false;
    public Boolean under15 = false;
    public Boolean under12 = false;


    /**
     * Retrieves movie IDs from the list of recommendations
     * Because there aren't several lists of recommendations,
     * this is much simpler than in AgeRestrictor (for RecMethod1)
     * @return List of Movie IDs
     */

    @Override
    public List retrieveMovies() {

        movieList = new ArrayList<>();

        for(int i=0; i < recommendations.size(); i++) {
            ScoredId rec = recommendations.get(i);
            Long mov = rec.getId();
            movieList.add(mov);
        }
        return this.movieList;

    }

    /**
     * Retrieves list of users
     * Creates a list of the ages of the users after reading from
     * users.csv
     * Initialises Age Booleans as appropriate
     */
    @Override
    public void retrieveUsers() {

        userAgeList = new ArrayList<>();

        if(userList == null){
            userList = group.getUserList();
        }


        String splitter = "\\|";
        String filePath = "src/ml-latest-small/users.csv";

        try {
            File f = new File(filePath);
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] details = line.split(splitter);
                Long uID = Long.parseLong(details[0]);
                Long age = Long.parseLong(details[1]);
                for(int i=0; i < userList.size(); i++) {
                    Long u = userList.get(i);
                    if (u.equals(uID)) {
                        userAgeList.add(age);
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Long b = 50L;

        //This code is from AgeRestrictor

        for(int i = 0; i < userAgeList.size(); i++) {

            Long a = userAgeList.get(i);
            if(a < b) {
                b = a;
            }
        }

        if(b < 12L) {
            under12 = true;
        } else if(b < 15L) {
            under15 = true;
        } else if(b < 18L) {
            under18 = true;
        }
    }

    /**
     * This checks the tags for the movies
     * and removes inappropriate ones - this code is the same as in AgeRestrictor
     * (as in AgeRestrictor, there are no BBFC or similar ratings for the movies)
     * (so filtering needs to be done by tags)
     * @return List of appropriate movies
     */
    @Override
    public List<Long> checkAndRemove() {

        movieTagMap = new HashMap<>();

        BufferedReader bff = null;
        String bx = "";
        String splitt = ",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";
        String moviePath = "src/ml-latest-small/movies.csv";


        try {
            bff = new BufferedReader(new FileReader(moviePath));
            while ((bx = bff.readLine()) != null) {
                String[] movieDetails = bx.split(splitt);
                String mID = movieDetails[0];
                String mGenre = movieDetails[2];
                Long movieID = Long.parseLong(mID);
                for (int j = 0; j < movieList.size(); j++) {
                    if (movieList.get(j).equals(movieID)) {
                        movieTagMap.put(movieID, mGenre);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String splitting = "\\|";
        if (under12) {

            //Remove all movies not tagged as "Children"

            for (Iterator<Map.Entry<Long, String>> it = movieTagMap.entrySet().iterator();
                 it.hasNext(); ) {
                Map.Entry<Long, String> entry = it.next();
                String hi = entry.getValue();
                String[] genres = hi.split(splitting);
                Boolean isChildren = false;
                for (int i = 0; i < genres.length; i++) {
                    if (genres[i] == "Children") {
                        isChildren = true;
                    }
                }
                if (!isChildren) {
                    it.remove();
                }
            }

        } else if (under15) {
            //remove "Horror" and "Thriller"
            for (Iterator<Map.Entry<Long, String>> iter = movieTagMap.entrySet().iterator();
                 iter.hasNext(); ) {
                Map.Entry<Long, String> entry = iter.next();
                String hey = entry.getValue();
                String[] genres = hey.split(splitting);
                Boolean isHorror = false;
                Boolean isThriller = false;
                for (int i = 0; i < genres.length; i++) {
                    if (genres[i] == "Horror") {
                        isHorror = true;
                    } else if (genres[i] == "Thriller") {
                        isThriller = true;
                    }
                }
                if (isHorror || isThriller) {
                    iter.remove();
                }
            }

        } else if (under18) {
            for (Iterator<Map.Entry<Long, String>> iterate = movieTagMap.entrySet().iterator();
                 iterate.hasNext(); ) {
                Map.Entry<Long, String> entry = iterate.next();
                String hai = entry.getValue();
                String[] genres = hai.split(splitting);
                Boolean isHorror = false;
                for (int i = 0; i < genres.length; i++) {
                    if (genres[i] == "Horror") {
                        isHorror = true;
                    }
                }
                if (isHorror) {
                    iterate.remove();
                }
            }
        }

            appropriateMovieList = new ArrayList<>();

            for (int i = 0; i < movieList.size(); i++) {
                Long mID = movieList.get(i);
                if (movieTagMap.containsKey(mID)) {
                    appropriateMovieList.add(mID);
                }

            }
            //TODO generate new recommendations if none are suitable!!
            if (appropriateMovieList.isEmpty()) {
                System.out.println("Oh no! No Children's movies were recommended!");
            }
            return appropriateMovieList;

    }

    //Constructor
    public GroupAges(GroupCombiner u, List<ScoredId> l){
        this.group = u;
        this.recommendations = l;
        this.userList = u.getUserList();
    }

    //Getters

    public List<Long> getUserAgeList() {
        return this.userAgeList;
    }

    public List<Long> getAppropriateMovieList() {
        return this.appropriateMovieList;
    }

}
