import org.grouplens.lenskit.scored.ScoredId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump
 * Class to test common rating averaging class
 */
class CommonDenominatorTest {

    private List<Long> l = new ArrayList<>(Arrays.asList(2L, 5L));
    private List<Long> m = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private UserGroup users;
    private UserGroup kids;
    private Map<Long, List<ScoredId>> kidRecs;
    private Map<Long, List<ScoredId>> usersRecs;
    private CommonDenominator a;
    private CommonDenominator b;

    @BeforeEach
    void setUp() {
        kids = new UserGroup(l);
        users = new UserGroup(m);

        kids.getIndividualRecs();
        users.getIndividualRecs();

        kidRecs = kids.getUserRecs();
        usersRecs = users.getUserRecs();

        a = new CommonDenominator(kidRecs);
        b = new CommonDenominator(usersRecs);
    }

    @AfterEach
    void tearDown() {
        a = null;
        b = null;

        users = null;
        kids = null;
    }

    /**
     * I have refactored the class so that the only method call needed is
     * allRatedAndAverage() and each method calls the next.
     * I have done this after all tests below have passed.
     */
    @Test
    void allRatedAndAverage() {
        a.allRatedAndAverage();
        b.allRatedAndAverage();

        assert !a.getFinalRecs().isEmpty();
        assert !b.getFinalRecs().isEmpty();
    }

    @Test
    void isAllRecommended() {

        a.begin();
        a.isAllRecommended();

        b.begin();
        b.isAllRecommended();

        //Recommending 10 items is not enough!

        assert !b.getCommonRec().isEmpty();

        assert !a.getCommonRec().isEmpty();

    }

    @Test
    void begin() {

        a.begin();

        List one = a.getOriginalList();

        assert !one.isEmpty();
    }

    @Test
    void obtainFinalRecs(){
        a.begin();
        a.isAllRecommended();
        a.obtainFinalRecs();

        System.out.println(a.getFinalRecs());
    }



}