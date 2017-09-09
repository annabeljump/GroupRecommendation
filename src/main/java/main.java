import org.grouplens.lenskit.scored.ScoredId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by annabeljump.
 * Class to provide Main method.
 */
public class main {

    public static void main(String[] args) {

       RecMethod1 gr = new RecMethod1();

       gr.recommendMovies();

       System.out.println();
//*/
       //NewRecommender test = new NewRecommender(129L);

       //System.out.println(test.recommend());

      // double pred = gr.predictRating(10L);

     //  System.out.println(pred);
/*
        //This is to test the basic individual recommender for relevance
        NewRecommender test = new NewRecommender(187L);

        List<ScoredId> testList = test.recommend();
        for(int i=0; i < testList.size(); i++) {
            ScoredId s = testList.get(i);
            Long mov = s.getId();
            double sc = s.getScore();
            if(sc > 5.0){
                sc = 5.0;
            }

            System.out.println(mov + " : " + String.format("%.02f", sc));
        }


        System.out.println();

        double s = test.predict(187L, 60074L);

        System.out.println("60074 : " + String.format("%.02f", s));

        double t = test.predict(187L, 44761L);

        System.out.println("44761 : " + String.format("%.02f", t));

        double v = test.predict(187L, 7153L);

        System.out.println("7153 : " + String.format("%.02f", v));

        double w = test.predict(187L, 6187L);

        System.out.println("6187 : " + String.format("%.02f", w));

        double x = test.predict(187L, 2599L);

        System.out.println("2599 : " + String.format("%.02f", x));
*/
        //The below method was created for the purpose of testing the recommendations
        //(testing and evaluating)
/*
        Boolean finished = false;

        do {
            System.out.println("Please enter the user ID of the user to predict:");

            Scanner sc = new Scanner(System.in);

            String usr = sc.nextLine();

            Boolean intFinished = false;

            if (usr.equals("Q")) {
                finished = true;
            } else {

                do {

                    Long us = Long.valueOf(usr);

                    System.out.println("Thank you. Please enter the movie to predict:");
                    String movie = sc.nextLine();
                    if(movie.equals("Q")){
                        intFinished = true;
                    } else {
                        Long m = Long.valueOf(movie);

                        NewRecommender testPred = new NewRecommender();

                        double score = testPred.predict(us, m);

                        System.out.println("Your predicted rating is: " + score);
                    }
                } while (!intFinished);
            }
        } while (!finished);
//*/
    }


    }
