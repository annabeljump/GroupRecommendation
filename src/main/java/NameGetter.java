import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by annabeljump
 * Class to return the name of a movie
 */
public class NameGetter {
    private List<Long> movies = new ArrayList<>();
    private Map<Long, String> names = new HashMap<>();
    private Map<Long, String> movieNames = new HashMap<>();

    public Map<Long, String> getMovieNames(){
        //Use code from age restriction to pull movie details
            //This time, only add the name of movie
        BufferedReader bff = null;
        String bx = "";
        String splitter = ",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";
        String moviePath = "src/ml-latest-small/movies.csv";

        //Read in movie details
        //Add to list only the name (which will be the second entry in the array, at 1
        try {
            bff = new BufferedReader(new FileReader(moviePath));
            while((bx = bff.readLine()) != null) {
                String[] movieDetails = bx.split(splitter);
                names.put(Long.parseLong(movieDetails[0]), movieDetails[1]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Map.Entry<Long, String> e : names.entrySet()) {
            for(int i=0; i < movies.size(); i++) {
                if (Objects.equals(e.getKey(), movies.get(i))) {
                   movieNames.put(movies.get(i), e.getValue());
                }
            }
        }

        return movieNames;
    }


    public NameGetter(List<Long> m){
        this.movies = m;
    }

    public List<Long> getMovies() {
        return movies;
    }

    public void setMovies(List<Long> movies) {
        this.movies = movies;
    }
}
