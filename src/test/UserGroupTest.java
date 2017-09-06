import org.grouplens.lenskit.scored.ScoredId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Class to test UserGroups.
 */
class UserGroupTest {

    //For test purposes, pick 3 random users, pick 1 host
    List<Long> users = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    Long h = 5L;

    UserGroup u;
    UserGroup v;

    @BeforeEach
    void setUp() {
        //Make 2 user groups, one with host, one without
        u = new UserGroup(users, h);
        v = new UserGroup(users);
    }

    @AfterEach
    void tearDown() {
        u = null;
        v = null;
    }

    @Test
    void testUserGroup() {
        //Test the User Group has been set up correctly
        assert u.getHost().equals(5L);
        assert v.getHost().equals(0L);

        assert u.getUserList().size() == 3;
        assert v.getUserList().size() == 3;
    }

    @Test
    void getIndividualRecs() {
        u.getIndividualRecs();

        //Test that Map with recs is initialised
        assert !u.getUserRecs().isEmpty();

        //There should be 3 key-value pairs, as 3 users
       assert u.getUserRecs().size() == 3;

        //Each list of ScoredId should have a size of 10 (TODO CHANGE TEST IF NUMBER OF RECS CHANGED)
        List<ScoredId> l = (List<ScoredId>)u.getUserRecs().get(5L);
       assert l.size() == 10;
    }


    @Test
    void setHost() {
        v.setHost(23L);
        assert v.getHost().equals(23L);
    }

}