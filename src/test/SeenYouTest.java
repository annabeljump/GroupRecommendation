import org.grouplens.lenskit.scored.ScoredId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Class to test removal of seen movies
 */
class SeenYouTest {

    //Going to use the same users for the group as AgeRestrictorTest - May as well test with two types of users
    private List<Long> l = new ArrayList<>(Arrays.asList(2L, 5L, 23L, 30L));
    private List<Long> m = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private UserGroup users;
    private UserGroup kids;
    private Map kidRecs;
    private Map usersRecs;
    private SeenYou a;
    private SeenYou b;
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();

    @BeforeEach
    void setUp() {
        kids = new UserGroup(l);
        users = new UserGroup(m);

        kids.getIndividualRecs();
        users.getIndividualRecs();

        kidRecs = kids.getUserRecs();
        usersRecs = users.getUserRecs();

        a = new SeenYou(kids, kidRecs);
        b = new SeenYou(users, usersRecs);
    }

    @AfterEach
    void tearDown() {

        a = null;
        b = null;

        users = null;
        kids = null;

    }

    //Test this last as it involves all other methods
    @Test
    void getUnseenMovies() {

        List one = a.getUnseenMovies();
        List two = b.getUnseenMovies();

        assert !one.isEmpty();
        assert !two.isEmpty();

        System.out.println(one);
        System.out.println(two);

    }

    @Test
    void retrieveMovieRecs() {

        a.retrieveMovieRecs();

        assert a.test != null;
        System.out.println(a.test);

        b.retrieveMovieRecs();

        List one = a.getMovieRecList();
        List two = b.getMovieRecList();

        //movieList should have a size of 30 for b, and 40 for a (each person recc'd 10 movies)
        assert one.size() == 40;
        assert two.size() == 30;
    }

    @Test
    void retrieveRatedMovies() {
        a.retrieveRatedMovies();
        b.retrieveRatedMovies();

        assert !a.getSeenMovieList().isEmpty();
        assert !b.getSeenMovieList().isEmpty();

    }

    @Test
    void checkAndRemoveSeenMovies() {

        a.retrieveMovieRecs();
        a.retrieveRatedMovies();
        a.checkAndRemoveSeenMovies();

        assert !a.getMovieRecList().isEmpty();

    }

}