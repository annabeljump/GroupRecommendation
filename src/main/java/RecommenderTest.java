/*

Main Method to run the recommenders
testing Lenskit basic implementation
* indicates comments from original code
Using HelloLenskit basic code - in comment at bottom
@author Lenksit

*/


import com.google.common.base.Throwables;
import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.config.ConfigHelpers;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.core.LenskitRecommenderEngine;
import org.grouplens.lenskit.core.RecommenderConfigurationException;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.eval.data.CSVDataSourceBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class RecommenderTest implements Runnable {

    //Initialise a Logger to log progress/errors - doesn't work
    //private static final Logger logger = LoggerFactory.getLogger(RecommenderTest.class);


    public static void main(String[] args) {
        RecommenderTest hello = new RecommenderTest(args);
        try {
            hello.run();
        } catch (RuntimeException e) {
            //Prints a trace of the RuntimeException for debugging
            System.err.println(e.toString());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    //this path is to the data file - bound in config file. Remove from config.
    private String dataFile = "~/Documents/GroupRecommendation/src/ml-100k/u.data";
    private List<Long> users;


    //Constructor - but where does the args come from?
    public RecommenderTest(String[] args) {
        users = new ArrayList<>(args.length);
        for (String arg : args) {
            users.add(Long.parseLong(arg));
        }
    }

    public void run() {
        // * We first need to configure the data access.
        // * We will load data from a static data source; you could implement your own DAO
        // * on top of a database of some kind
        EventDAO dao;
        //try {
        EventDAO data;
        data = new SimpleFileRatingDAO(new File(dataFile), ",");
        //* get the data from the DAO
        dao = data;
        // } catch (IOException e) {
        //logger.error("cannot load data", e);
        //TODO - look this up - what is this particular Throwable
        //    System.out.println("cannot load data");
        //    e.printStackTrace();
        //    throw Throwables.propagate(e);
        //}

        //* Next: load the LensKit algorithm configuration
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


        LenskitRecommenderEngine rec;
        try {
            rec = null;
            rec = LenskitRecommenderEngine.build(config);
        } catch (RecommenderBuildException e) {
            e.printStackTrace();
            throw new RuntimeException("could not build recommender engine");
        }

        LenskitRecommender reco = null;

        try {
            reco = rec.createRecommender(config);
        } catch (RecommenderConfigurationException e) {
            e.printStackTrace();
        }


        //Make recommendations for Items
        ItemRecommender irec = reco.getItemRecommender();

        //TODO check configuration of hello lenskit and compare
        assert irec != null; // not null because we configured one

// for users
        for (long user : users) {
// get 10 recommendation for the user
            ResultSet recs = irec.recommend(user, 10, null, null);
            System.out.format("Recommendations for user %d:\n", user);
            for (Result item : recs) {
                Entity itemData = dao.lookupEntity(CommonTypes.ITEM, item.getId());
                String name = null;
                if (itemData != null) {
                    name = itemData.maybeGet(CommonAttributes.NAME);
                }
                System.out.format("\t%d (%s): %.2f\n", item.getId(), name, item.getScore());
            }
        }
    }
}

//public class HelloLenskit implements Runnable {

//  private static final Logger logger = LoggerFactory.getLogger(Recommender.class);


//  public static void main(String[] args) {
//      HelloLenskit hello = new HelloLenskit(args);
//      try {
//          hello.run();
//      } catch (RuntimeException e) {
//          System.err.println(e.toString());
//          e.printStackTrace(System.err);
//          System.exit(1);
//      }
//  }

//  private Path dataFile = Paths.get("data/movielens.yml");
//  private List<Long> users;

//  public HelloLenskit(String[] args) {
//      users = new ArrayList<>(args.length);
//      for (String arg: args) {
//          users.add(Long.parseLong(arg));
//      }
//  }

//  public void run() {
// We first need to configure the data access.
// We will load data from a static data source; you could implement your own DAO
// on top of a database of some kind
//      DataAccessObject dao;
//      try {
//          StaticDataSource data = StaticDataSource.load(dataFile);
// get the data from the DAO
//          dao = data.get();
//      } catch (IOException e) {
//          logger.error("cannot load data", e);
//          throw Throwables.propagate(e);
//      }

// Next: load the LensKit algorithm configuration
//      LenskitConfiguration config = null;
//      try {
//          config = ConfigHelpers.load(new File("etc/item-item.groovy"));
//      } catch (IOException e) {
//          throw new RuntimeException("could not load configuration", e);
//      }


// There are more parameters, roles, and components that can be set. See the
// JavaDoc for each recommender algorithm for more information.

// Now that we have a configuration, build a recommender engine from the configuration
// and data source. This will compute the similarity matrix and return a recommender
// engine that uses it.
//      LenskitRecommenderEngine engine = LenskitRecommenderEngine.build(config, dao);
//      logger.info("built recommender engine");

// Finally, get the recommender and use it.
//      try (LenskitRecommender rec = engine.createRecommender(dao)) {
//          logger.info("obtained recommender from engine");
// we want to recommend items
//          ItemRecommender irec = rec.getItemRecommender();
//          assert irec != null; // not null because we configured one
// for users
//          for (long user : users) {
// get 10 recommendation for the user
//              ResultList recs = irec.recommendWithDetails(user, 10, null, null);
//              System.out.format("Recommendations for user %d:\n", user);
//              for (Result item : recs) {
//                  Entity itemData = dao.lookupEntity(CommonTypes.ITEM, item.getId());
//                  String name = null;
//                  if (itemData != null) {
//                      name = itemData.maybeGet(CommonAttributes.NAME);
//                  }
//                  System.out.format("\t%d (%s): %.2f\n", item.getId(), name, item.getScore());
//              }
//          }
//      }
//  }
//}