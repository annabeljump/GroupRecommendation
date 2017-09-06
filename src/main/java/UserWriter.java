import java.io.*;
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

    private String filePath = "src/ml-latest-small/ratings.csv";

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
     * @throws IOException
     */
    public void writeGroupRatings() throws IOException {

        FileWriter writer = new FileWriter(filePath, true);


        try {

            String iD = this.groupID.toString();


            for(Map.Entry<Long, Double> entry : ratings.entrySet()){
                String movie = entry.getKey().toString();
                String rate = entry.getValue().toString();

                writer.append(NEW_LINE_SEPARATOR);
                writer.write(iD);
                writer.write(COMMA_DELIMITER);
                writer.write(movie);
                writer.write(COMMA_DELIMITER);
                writer.write(rate);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
