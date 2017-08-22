import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Class to test weightings
 */
class WeightingGeneratorTest {

    //Unfortunately, WeightingGenerator uses many classes etc to weight the movie recommendations...
    private List<Long> u = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private Long h = 5L;
    private UserGroup users;
    private Map usersRecs;
    private AgeRestrictor a;
    private List<Long> appropriate;
    private SeenYou see;
    private List<Long> seen;
    private CommonDenominator comm;
    private Map common;

    private WeightingGenerator weight;

    @BeforeEach
    void setUp() {
        users = new UserGroup(u, h);
        users.getIndividualRecs();
        usersRecs = users.getUserRecs();

        a = new AgeRestrictor(users, usersRecs);
        a.retrieveMovies();
        a.retrieveUsers();
        appropriate = a.checkAndRemove();

        see = new SeenYou(users, usersRecs);
        seen = see.getUnseenMovies();

        comm = new CommonDenominator(usersRecs);
        common = comm.allRatedAndAverage();

        weight = new WeightingGenerator();

        weight.setAppropriateMovies(appropriate);
        weight.setAveragedCommonRecs(common);
        weight.setUnseenMovies(seen);
        weight.setUserRecs(usersRecs);
        weight.setUsers(users);

    }

    @AfterEach
    void tearDown() {
        weight = null;
        comm = null;
        see = null;
        a = null;
        users = null;

        usersRecs = null;
        appropriate = null;
        seen = null;
        common = null;
    }


    @Test
    void youHaveBeenMeasured() {

        weight.youHaveBeenMeasured();

        assert !weight.getRecMovies().isEmpty();

        System.out.println(weight.getRecMovies());
    }

    @Test
    void andYouHaveBeenFound() {
        weight.youHaveBeenMeasured();
        weight.andYouHaveBeenFound();

      //wanting() must be tested within here, as method variables are passed to it,
        //and it is called from within andYouHaveBeenFound()

    }

    @Test
    void youHaveBeenWeighed() {
        //Test this last since it calls the other methods

        weight.youHaveBeenWeighed();

        assert !weight.getFinalRecs().isEmpty();

        System.out.println(weight.getFinalRecs());
    }


}