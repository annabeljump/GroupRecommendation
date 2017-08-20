import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by annabeljump
 * Class to test NewRecommender methods
 */
class NewRecommenderTest {

    NewRecommender rec;

    @BeforeEach
    void setUp() {
        //Going to provide one user to test
        rec = new NewRecommender(2L);
    }

    @AfterEach
    void tearDown() {
        rec = null;
    }

    @Test
    void run() {
        rec.run();
        //Make sure recommendations are being generated
        assert rec.getActualRecs() != null;
        //Make sure a score for movie 17 is being generated
        assert rec.getScore() != 0.0;

        //User 2 has already rated movie 17. Predicted rating should match??
        assert rec.getScore() == 5.0;
    }

    @Test
    void recommend() {
    }

    @Test
    void predict() {
    }

}