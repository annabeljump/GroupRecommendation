import org.grouplens.lenskit.scored.ScoredId;

import java.io.File;
import java.io.FileNotFoundException;
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

        //This code is borrowed from AgeRestrictor

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

    @Override
    public List<Long> checkAndRemove() {
        return null;
    }


    //Constructor
    public GroupAges(GroupCombiner u, List<ScoredId> l){
        this.group = u;
        this.recommendations = l;
        this.userList = u.getUserList();
    }

}
