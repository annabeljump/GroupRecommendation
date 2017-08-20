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
    private Map kidRecs;
    private Map usersRecs;
    private AgeRestrictor a;
    private AgeRestrictor b;
    private Map<Long, List<ScoredId>> userRecs = new HashMap<>();

    @BeforeEach
    void setUp() {
        kids = new UserGroup(l);
        users = new UserGroup(m);

        kids.getIndividualRecs();
        users.getIndividualRecs();

        kidRecs = kids.getUserRecs();
        usersRecs = users.getUserRecs();

        //I want two of these, one with a kid, one without
            //Constructor with only userGroup does not seem to work
            //Now using constructor with Map
        a = new AgeRestrictor(kids, kidRecs);
        b = new AgeRestrictor(users, usersRecs);
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

        assert kidRecs != null;

        List one = a.retrieveMovies();

        assert a.test != null;
        System.out.println(a.test);

        List two = b.retrieveMovies();

        //movieList should have a size of 30 for b, and 40 for a (each person recc'd 10 movies)
        assert one.size() == 40;
        assert two.size() == 30;
    }

    @Test
    void retrieveUsers() {
        a.retrieveUsers();

        //Check that there is a userList and that it is the correct size (we know how many users here)

        assert a.getUserList() != null;

        assert a.getUserList().size() == 4;

    }

    @Test
    void checkAndRemove() {
        //Setting up to Check and Remove
        a.retrieveMovies();
        a.retrieveUsers();
        a.checkAndRemove();

        //Check that isSmallChildren evaluates to true (30L is 7 years old)
        assert a.isSmallChildren();

        //Check that the appropriate movie list is not empty.
        assert !a.getAppropriateMovies().isEmpty();

        //Now test with b
        b.retrieveMovies();
        b.retrieveUsers();
        b.checkAndRemove();

        //No children in this group. Check boolean.
        assert !b.isSmallChildren();

        //Check that the appropriate movie list is not empty.
        assert !b.getAppropriateMovies().isEmpty();

        //No movies should have been removed, so size should be 30
        assert b.getAppropriateMovies().size() == 30;
    }

    @Test
    void getAppropriateMovies() {
    }

    @Test
    void isSmallChildren() {
    }

}