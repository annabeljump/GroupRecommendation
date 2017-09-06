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

    List<Long> users = new ArrayList<>(Arrays.asList(4L, 19L, 73L, 105L));
    Long h = 4L;

    GroupCombiner g;

    @BeforeEach
    void setUp() {
        g = new GroupCombiner(users, h);
    }

    @Test
    void getIndividualRecs() {
        g.getIndividualRecs();

        assert !g.getUserRatings().isEmpty();

        assertEquals(g.getUserRatings().size(), 4);

        Map<Long, Map<Long, Double>> ratings = g.getUserRatings();

        Map<Long, Double> rate = ratings.get(4L);

        //System.out.println(rate);

        assert !rate.isEmpty();

        assert !g.getMovieList().isEmpty();

        //System.out.println(g.getMovieList().size());

        assert !g.getAveragedRatings().isEmpty();

        //System.out.println(g.getAveragedRatings());
        //System.out.println(g.getUnCommonRatings());

    }

}