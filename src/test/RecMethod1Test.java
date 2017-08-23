import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump
 * Test for RecMethod1
 */

class RecMethod1Test {

    GroupRecGenerator hello;

    private List<Long> u = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private Long h = 5L;
    private UserGroup users;


    @BeforeEach
    void setUp() {
        hello = new RecMethod1();

        users = new UserGroup(u, h);
    }

    @AfterEach
    void tearDown() {

        hello = null;
    }

    @Test
    void recommendMovies() {

        hello.recommendMovies(users);
    }

    @Test
    void createUserGroup() {
    }

    @Test
    void ageFilter() {
    }

    @Test
    void iCanSeeYou() {
    }

    @Test
    void uncommonNumerator() {
    }

    @Test
    void geoffreyChaucer() {
    }

    @Test
    void sirWilliam() {
    }

}