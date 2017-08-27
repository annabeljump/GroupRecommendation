import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Class to test UserWriter
 */
class UserWriterTest {

    private GroupCombiner g;
    private List<Long> users = new ArrayList<>(Arrays.asList(3L, 6L, 432L));
    private Long host;
    private Map<Long, Double> ratings = new HashMap<>();

    private UserWriter uw;

    @BeforeEach
    void setUp() {
        g = new GroupCombiner(users, host);
        g.getIndividualRecs();
        ratings = g.getAveragedRatings();

        uw = new UserWriter(g, ratings);
    }

    @AfterEach
    void tearDown() {
        g = null;

        uw = null;
    }

    @Test
    void readLastUser() {
        uw.readLastUser();

    }

    @Test
    void writeGroupRatings() throws IOException {
        uw.readLastUser();
        uw.writeGroupRatings();
    }

}