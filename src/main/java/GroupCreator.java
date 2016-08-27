import java.util.List;

/**
 * Created by annabeljump.
 * Interface to create the group of users for which recommendations
 * are needed.
 */
public interface GroupCreator {

    /**
     * Get recommendations for individual users in the group
     */

    void getIndividualRecs();
}
