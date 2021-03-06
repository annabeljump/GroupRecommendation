import org.grouplens.lenskit.scored.ScoredId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Test class for GroupAges
 */
class GroupAgesTest {
    private List<Long> l = new ArrayList<>(Arrays.asList(2L, 5L, 23L, 30L));
    private List<Long> m = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private Long host = 5L;
    private GroupCombiner users;
    private GroupCombiner kids;
    private List<ScoredId> kidRecs;
    private List<ScoredId> usersRecs;
    private GroupAges a;
    private GroupAges b;

    @BeforeEach
    void before() {
        kids = new GroupCombiner(l, host);
        users = new GroupCombiner(m, host);

        kids.getIndividualRecs();
        users.getIndividualRecs();
    }

    /**
     * I could not test this class via JUnit and had to rely on manual testing
     */

    @Test
    void retrieveMovies() {
    }

    @Test
    void retrieveUsers() {
    }

    @Test
    void checkAndRemove() {
    }

}