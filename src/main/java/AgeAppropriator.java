import java.util.List;

/**
 * Created by annabeljump.
 * Class that retrieves rated movies and
 * filters them according to ages of users
 */
public interface AgeAppropriator {

    /**
     * When given a map of recommendations
     * Retrieve the movies recommended
     */
    List retrieveMovies();

    /**
     * Retrieve the user IDs
     */
    //TODO review if necessary to have this method
    void retrieveUsers();

    /**
     * Remove inappropriate movies,
     * dependent on age of users
     */
    List<Long> checkAndRemove();

}
