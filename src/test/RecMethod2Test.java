import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump.
 * Tester for recmethod2
 */
class RecMethod2Test {

    GroupRecGenerator hello;

    private List<Long> u = new ArrayList<>(Arrays.asList(2L, 5L, 23L));
    private Long h = 5L;
    private GroupCombiner users;


    @BeforeEach
    void setUp() {

        hello = new RecMethod2();

        users = new GroupCombiner(u, h);
    }

    @AfterEach
    void tearDown() {
        hello = null;
    }

    @Test
    void recommendMovies() {
       hello.recommendMovies();
    }

    @Test
    void createGroup() {
    }

}