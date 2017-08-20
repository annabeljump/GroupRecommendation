import org.grouplens.lenskit.scored.ScoredId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Class to test age restrictor methods
 */

class AgeRestrictorTest {

    //User 30 is 7 years old
    private List<Long> l = new ArrayList<>(Arrays.asList(2L, 5L, 23L, 30L));
    private List<Long> m = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private UserGroup users;
    private UserGroup kids;
    private AgeRestrictor a;
    private AgeRestrictor b;
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();

    @BeforeEach
    void setUp() {
        kids = new UserGroup(l);
        users = new UserGroup(m);

        kids.getIndividualRecs();
        users.getIndividualRecs();

        Map kidRecs = kids.getUserRecs();
        Map userRecs = users.getUserRecs();

        //I want two of these, one with a kid, one without
            //Constructor with only userGroup does not seem to work
            //Now using constructor with Map
        a = new AgeRestrictor(kids, kidRecs);
        b = new AgeRestrictor(users, userRecs);
    }

    @AfterEach
    void tearDown() {
        a = null;
        b = null;

        users = null;
        kids = null;
    }

    @Test
    void retrieveMovies() {
        List one = a.retrieveMovies();
        List two = b.retrieveMovies();

        //movieList should have a size of 30 for b, and 40 for a (each person recc'd 10 movies)
        assert one.size() == 40;
        assert two.size() == 30;
    }

    @Test
    void retrieveUsers() {
    }

    @Test
    void checkAndRemove() {
    }

    @Test
    void getAppropriateMovies() {
    }

    @Test
    void getAllMovies() {
    }

    @Test
    void isSmallChildren() {
    }

}