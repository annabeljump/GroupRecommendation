import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.config.ConfigHelpers;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.core.RecommenderConfigurationException;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.scored.ScoredId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annabeljump.
 * New attempt at building the base recommender for
 * group recommendation plugin to be built on top.
 */
public class NewRecommender implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RecommenderTest.class);


    public static void main(String[] args) {
        NewRecommender hi = new NewRecommender(args);
        try {
            hi.run();
        } catch (RuntimeException e) {
            //Prints a trace of the RuntimeException for debugging
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private String dataFile = "~/Documents/GroupRecommendation/src/ml-100k/u.data";
    private List<Long> users;


    //Constructor - but where does the args come from?
    public NewRecommender(String[] args) {
        users = new ArrayList<>(args.length);
        for (String arg : args) {
            users.add(Long.parseLong(arg));
        }
    }

    public void run() {
        LenskitConfiguration config = null;
        try {
            config = ConfigHelpers.load(new File("~/Documents/GroupRecommendation/etc/LenskitConfiguration.groovy"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("could not load configuration", e);
        } catch (RecommenderConfigurationException e) {
            e.printStackTrace();
        }

        config.bind(EventDAO.class).to(new SimpleFileRatingDAO(new File(dataFile), "/t"));

        LenskitRecommender newRec = null;
        try {
            newRec = LenskitRecommender.build(config);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
        }

        assert newRec != null;

        ItemRecommender itemRec = newRec.getItemRecommender();

        //generates recommendations to users. Iterate along users list to generate for all.

        List<ScoredId> actualRecs = itemRec.recommend(user, 10);

    }
}
