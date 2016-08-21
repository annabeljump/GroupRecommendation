import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.RatingPredictor;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.baseline.BaselineScorer;
import org.grouplens.lenskit.baseline.ItemMeanRatingItemScorer;
import org.grouplens.lenskit.baseline.UserMeanBaseline;
import org.grouplens.lenskit.baseline.UserMeanItemScorer;
//import org.grouplens.lenskit.config.ConfigHelpers;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
//import org.grouplens.lenskit.core.RecommenderConfigurationException;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annabeljump.
 * New attempt at building the base recommender for
 * group recommendation plugin to be built on top.
 */
public class NewRecommender implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NewRecommender.class);


    public static void main(String[] args) {
        NewRecommender hi = new NewRecommender();
        try {
            hi.run();
        } catch (RuntimeException e) {
            //Prints a trace of the RuntimeException for debugging
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private String dataFile = "src/ml-latest-small/ratings.csv";
    private List<Long> users;


    //Constructor - but where does the args come from?
    public NewRecommender() {
        users = new ArrayList<>();
    }

    public void run() {
        LenskitConfiguration config = new LenskitConfiguration();
        //try {
        //    config = ConfigHelpers.load(new File("src/main/etc/LenskitConfiguration.groovy"));
        //} catch (IOException e) {
        //    e.printStackTrace();
        //    throw new RuntimeException("could not load configuration", e);
        //} catch (RecommenderConfigurationException e) {
        //    e.printStackTrace();
        //}

// Use item-item CF to score items
        config.bind(ItemScorer.class).to(ItemItemScorer.class);
// let's use personalized mean rating as the baseline/fallback predictor.
// 2-step process:
// First, use the user mean rating as the baseline scorer
        config.bind(BaselineScorer.class, ItemScorer.class)
                .to(UserMeanItemScorer.class);
// Second, use the item mean rating as the base for user means
        config.bind(UserMeanBaseline.class, ItemScorer.class)
                .to(ItemMeanRatingItemScorer.class);
// and normalize ratings by baseline prior to computing similarities
        config.bind(UserVectorNormalizer.class)
                .to(BaselineSubtractingUserVectorNormalizer.class);

        config.bind(EventDAO.class).to(new SimpleFileRatingDAO(new File(dataFile), ","));

        LenskitRecommender newRec = null;
        try {
            newRec = LenskitRecommender.build(config);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }

        assert newRec != null;

        ItemRecommender itemRec = newRec.getItemRecommender();

        //generates recommendations to users.
        //Iterate along users list to generate for all? Next impl.


        //Insert random User to generate recs.
        List<ScoredId> actualRecs = itemRec.recommend(168, 10);

        System.out.println("Now Printing Recommended Items:");
        for (int i = 0; i < actualRecs.size(); i++) {
            System.out.println(actualRecs.get(i));
        }
        System.out.println("Finished");

        //Test Rating Predictor
        RatingPredictor pred = newRec.getRatingPredictor();
        double score = pred.predict(42, 17);
        System.out.println("Now predicting rating for movie 17:" + score);

    }
}
