import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump to test GroupCombiner.
 */
class GroupCombinerTest {

    List<Long> users = new ArrayList<>(Arrays.asList(5L, 4L));
    Long h = 5L;

    GroupCombiner g;

    @BeforeEach
    void setUp() {
        g = new GroupCombiner(users, h);
    }

    @Test
    void getIndividualRecs() {
        g.getIndividualRecs();

        assert !g.getUserRatings().isEmpty();

        assertEquals(g.getUserRatings().size(), 2);
    }

}