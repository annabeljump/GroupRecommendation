import org.grouplens.lenskit.transform.normalize.MeanCenteringVectorNormalizer
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.VectorNormalizer
import org.lenskit.api.ItemScorer
import org.lenskit.baseline.BaselineScorer
import org.lenskit.baseline.ItemMeanRatingItemScorer
import org.lenskit.baseline.UserMeanBaseline
import org.lenskit.baseline.UserMeanItemScorer
import org.lenskit.data.dao.EventDAO
import org.lenskit.knn.NeighborhoodSize
import org.lenskit.knn.user.UserUserItemScorer


/**
 * File to configure LensKit recommender library
 * to build the recommender for the Group Recommendation extension
 * @author Lenskit - testing "getting started" configuration
 */

//I want to configure user-user CF:

bind ItemScorer to UserUserItemScorer

// use item-user mean when user-user fails
bind (BaselineScorer, ItemScorer) to UserMeanItemScorer
bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer

// normalize by subtracting the user's mean rating
within (UserVectorNormalizer) {
    // for normalization, center on user means
    bind VectorNormalizer to MeanCenteringVectorNormalizer
}


//Initial neighbourhood size set to 30
set NeighborhoodSize to 30

//bind the data source -REMOVED (bound in Recommender.java)
//config.bind(EventDAO.class).to(new SimpleFileRatingDAO(new File(""), ","));
//bind EventDAO to new SimpleFileRatingDAO("~/IdeaProjects/GroupRec/ml-100k/u.data", "/t");



//Keeping all old code for reference, studying LensKit Configuration files.

// LenskitConfiguration config = new LenskitConfiguration();

//config.bind(ItemScorer.class).to(ItemItemScorer.class);

// personalized mean rating used as the baseline/fallback predictor.
// 2-step process:
// First, use the user mean rating as the baseline scorer
//config.bind(BaselineScorer.class, ItemScorer.class)
//      .to(UserMeanItemScorer.class);
// Second, use the item mean rating as the base for user means
//config.bind(UserMeanBaseline.class, ItemScorer.class)
//      .to(ItemMeanRatingItemScorer.class);
// and normalize ratings by baseline prior to computing similarities
//config.bind(UserVectorNormalizer.class)
//        .to(BaselineSubtractingUserVectorNormalizer.class);