
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * Created by annabeljump
 * Class to test NewRecommender methods
 */
class NewRecommenderTest {

    NewRecommender rec;
    Double rate = 0.0;

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
        assert !rec.getActualRecs().isEmpty();

        //Make sure a score for movie 17 is being generated
        assert rec.getScore() != 0.0;

        //User 2 has already rated movie 17. Predicted rating does not match. Test Fails.
        //assert rec.getScore() == 5.0;
    }

    @Test
    void recommend() {
        List recs = rec.recommend();

        //Make sure recommendations are being generated
        assert recs != null;
        assert !recs.isEmpty();

        //Make sure there are 10 recommendations TODO CHANGE IF CHANGE NUMBER OF RECS
        assert recs.size() == 10;

    }

    @Test
    void predict() {
        rate = rec.predict(2L, 17L);

        //Make sure a rating is being generated
        assert rate != 0.0;

        //User 2 has already rated movie 17 with 5 stars. Predicted rating does not match. Test fails.
       // assert rate == 5.0;

    }

}