import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by annabeljump
 * Class to return the name of a movie
 */
public class NameGetter {
    private List<Long> movies = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    public List<String> getMovieNames(){
        //Use code from age restriction to pull movie details
            //This time, only add the name of movie
        BufferedReader bff = null;
        String bx = "";
        String splitter = ",";
        String moviePath = "src/ml-latest-small/movies.csv";

        //Read in movie details as with users above
        //Add to map the movie ID and the genre tags
        try {
            bff = new BufferedReader(new FileReader(moviePath));
            while((bx = bff.readLine()) != null) {
                String[] movieDetails = bx.split(splitter);
                names.add(movieDetails[1]);
                movieDetails = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
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
