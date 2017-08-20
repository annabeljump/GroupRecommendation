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
import org.grouplens.lenskit.knn.NeighborhoodSize;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.knn.user.UserUserItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.MeanCenteringVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.VectorNormalizer;
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

    /**
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

     */

    private String dataFile = "src/ml-latest-small/ratings.csv";
    private List<Long> users;
    private Long userID;


    //Constructor
    public NewRecommender() {
        this.userID = null;
    }


    //New constructor so that recommendations can be generated for unspecified user
    public NewRecommender(Long user) {
        this.userID = user;
    }

    public void run() {
        LenskitConfiguration config = new LenskitConfiguration();


// Use item-item CF to score items
        config.bind(ItemScorer.class).to(UserUserItemScorer.class);
// let's use personalized mean rating as the baseline/fallback predictor.
// 2-step process:
// First, use the user mean rating as the baseline scorer
// Second, use the item mean rating as the base for user means
// and normalize ratings by baseline prior to computing similarities

        config.bind(BaselineScorer.class, ItemScorer.class).to(UserMeanItemScorer.class);
        config.bind(UserMeanBaseline.class, ItemScorer.class).to(ItemMeanRatingItemScorer.class);

        config.within(UserVectorNormalizer.class).bind(VectorNormalizer.class).to(MeanCenteringVectorNormalizer.class);

        config.set(NeighborhoodSize.class).to(30);

        EventDAO dao = SimpleFileRatingDAO.create(new File(dataFile), ",");
        config.bind(EventDAO.class).to(dao);

        LenskitRecommender newRec = null;
        try {
            newRec = LenskitRecommender.build(config);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }

        assert newRec != null;

        ItemRecommender itemRec = newRec.getItemRecommender();

        //Insert random User to generate recs.
        List<ScoredId> actualRecs = itemRec.recommend(userID, 10);

        System.out.println("Now Printing Recommended Items:");
        for (int i = 0; i < actualRecs.size(); i++) {
            System.out.println(actualRecs.get(i));
        }
        System.out.println("Finished");

        //Test Rating Predictor
        RatingPredictor pred = newRec.getRatingPredictor();
        double score = pred.predict(userID, 17);
        System.out.println("Now predicting rating for movie 17:" + score);

    }
    public List recommend() {
        LenskitConfiguration config = new LenskitConfiguration();

        config.bind(ItemScorer.class).to(UserUserItemScorer.class);


        config.bind(BaselineScorer.class, ItemScorer.class).to(UserMeanItemScorer.class);
        config.bind(UserMeanBaseline.class, ItemScorer.class).to(ItemMeanRatingItemScorer.class);

        config.within(UserVectorNormalizer.class).bind(VectorNormalizer.class).to(MeanCenteringVectorNormalizer.class);

        config.set(NeighborhoodSize.class).to(30);

        EventDAO dao = SimpleFileRatingDAO.create(new File(dataFile), ",");
        config.bind(EventDAO.class).to(dao);

        LenskitRecommender newRec = null;
        try {
            newRec = LenskitRecommender.build(config);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }

        assert newRec != null;

        ItemRecommender itemRec = newRec.getItemRecommender();

        List<ScoredId> actualRecs = itemRec.recommend(userID, 10);

        return actualRecs;

    }

    /**
     * Tyis is a new method to predict a rating for a specific movie
     * @param u the user ID of the user
     * @param m the number of the movie for which the rating should be generated
     * @return the rating user u gives movie m
     */

    public Double predict(Long u, Long m) {

        Double rating;

        LenskitConfiguration config = new LenskitConfiguration();

        config.bind(ItemScorer.class).to(UserUserItemScorer.class);

        config.bind(BaselineScorer.class, ItemScorer.class).to(UserMeanItemScorer.class);
        config.bind(UserMeanBaseline.class, ItemScorer.class).to(ItemMeanRatingItemScorer.class);

        config.within(UserVectorNormalizer.class).bind(VectorNormalizer.class).to(MeanCenteringVectorNormalizer.class);

        config.set(NeighborhoodSize.class).to(30);

        EventDAO dao = SimpleFileRatingDAO.create(new File(dataFile), ",");
        config.bind(EventDAO.class).to(dao);

        LenskitRecommender newRec = null;
        try {
            newRec = LenskitRecommender.build(config);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }

        assert newRec != null;

        RatingPredictor pred = newRec.getRatingPredictor();
        double score = pred.predict(u, m);

        rating = score;

        return rating;
    }
}
