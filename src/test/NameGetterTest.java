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
 * Class to test name generator for list of movies
 */
class NameGetterTest {

    private List<Long> movies = new ArrayList<>(Arrays.asList(48L, 124L, 135L, 1266L));
    private NameGetter a;

    @BeforeEach
    void setUp() {
        a = new NameGetter(movies);
    }

    @AfterEach
    void tearDown() {
        a = null;
    }

    @Test
    void getMovieNames() {

        Map<Long, String> ma =  a.getMovieNames();

        String name = ma.get(48L);

        assert !a.getMovieNames().isEmpty();

        assertEquals(name, "Pocahontas (1995)");

    }


}