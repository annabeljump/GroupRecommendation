import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by annabeljump.
 * Class to write our new User Ratings to ratings.csv
 * - so that recommendations can actually be generated
 */
public class UserWriter {

    private GroupCombiner group;
    private Long groupID = 0L;
    private Map<Long, Double> ratings = new HashMap<>();

    private String filePath = "src/ml-latest-small/ratings2.csv";

    private static final String COMMA_DELIMITER = ",";

    private static final String NEW_LINE_SEPARATOR = "\n";

    /**
     * Method to read in the last line of ratings.csv
     * and check the userID of the last user
     * Sets userID for group to last userID + 1
     */
    public void readLastUser() {

        String splitter = ",";
        String[] li = new String[4];

        try {
            File f = new File(filePath);
            Scanner sc = new Scanner(f);

            while (sc.hasNextLine()){
                String line = sc.nextLine();
                li = line.split(splitter);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //In theory li will now hold the last line of the file.

        groupID = Long.parseLong(li[0])+1L;

        System.out.println("Your new Group ID is: " + groupID);

    }

    /**
     * Method to write out the group's combined ratings to ratings.csv
     * (currently ratings2.csv for testing purposes)
     * @throws IOException
     */
    public void writeGroupRatings() throws IOException {

        try {

            String iD = this.groupID.toString();

            FileWriter writer = new FileWriter(filePath, true);

            writer.append(NEW_LINE_SEPARATOR);

            for(Map.Entry<Long, Double> entry : ratings.entrySet()){
                String movie = entry.getKey().toString();
                String rate = entry.getValue().toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Constructors
    public UserWriter(GroupCombiner g, Map<Long, Double> r) {
        this.group = g;
        this.ratings = r;
    }

    //Getters

    public Long getGroupID() { return this.groupID; }

}
