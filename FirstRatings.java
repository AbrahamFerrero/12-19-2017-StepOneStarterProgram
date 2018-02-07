import edu.duke.*;
import java.util.*;
import org.apache.commons.csv.*;
/**
 * This class has been created as a requirement for this Assignment.
 * 
 * @author (Abraham Ferrero) 
 * @version (19/DIC/2017)
 */
public class FirstRatings {
    
    //I created this first method as a previous one to prepare the one the Assignment asks for:
    private ArrayList<Movie> csvmethod(CSVParser parser){
        ArrayList<Movie> MovieData = new ArrayList<Movie>();
        //Converting a csvrecord to a movie class value. Storing it to an arraylist
        for(CSVRecord movie : parser){
            Movie newMovie = new Movie(movie.get("id"),movie.get("title"),movie.get("year"), movie.get("genre"),movie.get("country"),movie.get("director"),Integer.parseInt(movie.get("minutes")),movie.get("poster"));
            MovieData.add(newMovie);
        }
        return MovieData;
    }
    //Having the csv record converted to movies in an Arraylist, now we can load a fileresource with the filename we want when we call loadMovies(filename):
    private ArrayList<Movie> loadMovies(String filename){
        FileResource fr = new FileResource("data/" + filename);
        CSVParser parser = fr.getCSVParser();
        ArrayList<Movie> MovieData = csvmethod(parser);
        return MovieData;
    }
    //And here we test everything and we can manipulate the arraylist to taste.
    
    private HashMap<String,Integer> DirectorAndMovies(ArrayList<Movie> films){
        HashMap<String,Integer> mapDirector = new HashMap<String,Integer>();
        for(Movie film : films){
            String Director = film.getDirector();
            //Testing stuff: System.out.println("Whole thing: " + Director);
            String new1 = "";
            int index = 0;
            if (Director.contains(",")){
                    new1 = Director.substring(index,Director.indexOf(","));
                    //Testing stuff: System.out.println("this is the first new1: " + new1);
                    //index empieza en la segunda
                    index = new1.length()+2;
                    //rest es el resto quitando al primer dire
                    String rest = Director.substring(index);
                    //mientras el resto tenga comas (capturar todos entre medias)
                    int index2= 0;
                    /*I am specially proud of this while loop, because I've been stuck for 2 days with
                    * it and now it just works perfectly, leaving variable rest to add to the hash and
                    * also adding new1 everytime it gets generated for when we have many directors for
                    * some movies.
                    */
                    while(rest.contains(",")){
                       new1 = rest.substring(index2,rest.indexOf(","));
                       // testing stuff: System.out.println("this is the looped new1: " + new1);
                       if(!mapDirector.containsKey(new1)){
                           mapDirector.put(new1,1);
                        }
                       else{
                           mapDirector.put(new1,mapDirector.get(new1)+1);
                        }
                       index2 = new1.length()+2;
                       rest= rest.substring(index2);
                       index2 = 0;
                    }
                    // Testing stuff: System.out.println("rest de multiples: " + rest);
                    if(!mapDirector.containsKey(rest)){
                        mapDirector.put(rest,1);
                    }
                    else{
                        mapDirector.put(rest,mapDirector.get(rest)+1);
                    }
            }
            else{
                new1 = Director;
            }
            if(!mapDirector.containsKey(new1)){
                mapDirector.put(new1,1);
            }
            else{
                mapDirector.put(new1,mapDirector.get(new1)+1);
            }
        }
        return mapDirector;
    }
    
    public void testLoadMovies(){
        ArrayList<Movie> MovieData = loadMovies("ratedmoviesfull.csv");
        
        //PART1: Printing the arraylist movie values to a string list.
        int count = 0;
        int comedy = 0;
        int minutesLong = 0;
        for(Movie move : MovieData){
            count++;
            System.out.println(move.getCountry());
            System.out.println(move.toString());
            //Get the comedy movies if comedy is in the genre.
            if (move.getGenres().contains("Comedy")){
                comedy++;
            }
            //add 1 to the variable minuteslong if the movie iterated is longer than 150 min:
            if(move.getMinutes() > 150){
                minutesLong++;
            }
        }
        System.out.println("Total movies = " + count);
        System.out.println("Total comedies = " + comedy);
        System.out.println("Movies longer than 150min = " + minutesLong);
        
        
        //PART 2:Creating a hashmap to determine max number of movies by director and which director directed that many movies.
        HashMap<String,Integer>mapDirector = DirectorAndMovies(MovieData);
        ArrayList<String> MaxDirectors = new ArrayList<String>();
        int max = 0;
        String maxDirector = "";
        for(String s : mapDirector.keySet()){
            /**To print the whole HashMap to test:
            System.out.println(s + " " + mapDirector.get(s));
            **/
            //This iteration gives you the max number of movies
            if (mapDirector.get(s) > max){
                max = mapDirector.get(s);
            }
        }
        for(String s : mapDirector.keySet()){
            if (mapDirector.get(s) == max){
                MaxDirectors.add(s);
            }
        }
        /**If you want to find a director/s with a specific amount of movies:
        for(String s : mapDirector.keySet()){
            if(mapDirector.get(s) == 15){
                System.out.println(s);
            }
        }**/
        System.out.println("Maximum number of movies by any director is: " + max);
        System.out.println("Directors who directed this many movies: \n");
        for(String s : MaxDirectors){
            System.out.println(s);
        }
       
    }
    
    private HashMap<String, ArrayList<Rating>> csvLoadRaters(CSVParser parser){
        //This creates an ArrayList rater with the rater id. 
       HashMap<String, ArrayList<Rating>> idAndRatingsMap = new HashMap<String, ArrayList<Rating>>();
       for(CSVRecord rate : parser){
            Rater newRater = new Rater(rate.get("rater_id"));
            newRater.addRating(rate.get("movie_id"),Double.parseDouble(rate.get("rating")));
            /*getMyRatings is a method created by me in the POJO class Rater to add everything at
             * once in the HashMap (vote and movie id). */
            
            for(Rating  hey : newRater.getMyRatings()){
                if(!idAndRatingsMap.containsKey(newRater.getID())){
                    /*Basically if the map does not contain the id, add id and the rating with the getID and
                     * getMyRatings command.*/
                    idAndRatingsMap.put(newRater.getID(),newRater.getMyRatings());
                    //System.out.println("We are adding this to map: cause id is not there " + newRater.getID()+ " " + newRater.getMyRatings());
                }
                else{
                    /*But if the ID is already there, we have to access the arraylist.
                     * To do this we use get(getID), and once we accessed the arraylist,
                     * we add that rating using the arraylist method add.()*/
                    idAndRatingsMap.get(newRater.getID()).add(hey);
                }
            }
       }
       return idAndRatingsMap;
    }

    private HashMap<String, ArrayList<Rating>> loadRaters(String filename){
        /*First you create a fileresource calling the file asked for. Then you create a csvparser
         * after that you iterate in the method above over the parser to convert the parser in a 
         * edible arraylist rater with the elements in the file. Once you've got it, call the method
         * to have a proper object oriented class. And after that, you go to the test method. Create the
         * arraylist rater to iterate over it and call the method in class Rater getID() to have a proper
         * string of each element. 
         */
        FileResource f = new FileResource("data/" + filename);
        CSVParser parser = f.getCSVParser();
        HashMap<String, ArrayList<Rating>> loadRaters = csvLoadRaters(parser);
        return loadRaters;
    }
    //adivina que es esto y sacalo de aqui si no vale. 
    
    public void testLoadRaters(){
       String filename = "ratings.csv"; 
       HashMap<String, ArrayList<Rating>> loadRaters = loadRaters(filename);
       //Assingment part 2 question 1:
       for(String s : loadRaters.keySet()){
           //These two will display the actual entire hasmap:
           //System.out.println("The userID: " + s + " voted " + loadRaters.get(s).size() + " movies");
           //System.out.println("These are the id and rate: " + loadRaters.get(s));
       }
       System.out.println("Total raters = " + loadRaters.size());
       //Assignment part 2 question 2: 
       String specficRater = "193";
       for(String s : loadRaters.keySet()){
           if(s.equals(specficRater)){
              System.out.println("The user with the specific ID " + s + " voted " + loadRaters.get(s).size() + " movies");
           }
       }
       //Part 2 question 3:
       int max = 0;
       for(String s : loadRaters.keySet()){
           if(max < loadRaters.get(s).size()){
               max = loadRaters.get(s).size();
           }
       }
       System.out.println("Maximum number of movies rated per user: " + max);
       System.out.println("And they were voted by the users ID: ");
       int countMaxRaters = 0;
       for(String s : loadRaters.keySet()){
           if(loadRaters.get(s).size() == max){
               System.out.println(s);
               countMaxRaters++;
           }
       }
       System.out.println("Amount of TOP1 user/s by no. of votes: " + countMaxRaters);
       //Part 2 question 4:
       String movie_id = "1798709";
       int timesVoted = 0;
       for(ArrayList<Rating> totalRatings : loadRaters.values()){
           for(Rating eachMovie : totalRatings){
               /*Making use of the .getItem() method in the Rating class we access to the movies
                * of every arraylist Rating in the hashmap. Really easy from there to count the times
                * a certain movie has been voted: if the movie_id is in the arraylist, add one to the
                * count, then display the final count: */
               String moviesID = eachMovie.getItem();
               if(movie_id.equals(moviesID)){
                  timesVoted++;
               }
            }
       }
       System.out.println("The movie with the ID: " + movie_id + " has been voted "  + timesVoted + " times");
       //Part 2 question 5:
       ArrayList<String> list = new ArrayList<String>();
       for(ArrayList<Rating> totalRatings : loadRaters.values()){
           for(Rating eachMovie : totalRatings){
               String moviesID = eachMovie.getItem();
               if(!list.contains(moviesID)){
                   list.add(moviesID);
               }
           }
       }
       System.out.println("Number of total different movies rated: " + list.size());
     }
}
