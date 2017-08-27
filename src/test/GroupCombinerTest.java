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

    List<Long> users = new ArrayList<>(Arrays.asList(3L, 5L, 324L));
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

        //assertEquals(g.getUserRatings().size(), 2);

        assert !g.getMovieList().isEmpty();

        assert !g.getAveragedRatings().isEmpty();

        System.out.println(g.getAveragedRatings());
        System.out.println(g.getUnCommonRatings());

    }

}