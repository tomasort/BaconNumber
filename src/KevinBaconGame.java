import java.util.*;
import java.io.*;
import java.lang.*;

public class KevinBaconGame {

    final int roles_sz = 3925000;
    final int movies_sz = 331500;

    HashMap <String, String[]> Actor2Movies; // A HashMap to keep all the movies that an actor was in
    HashMap <String, ArrayList<String>> Movie2Actors; //A HashMap to keep all the actors in a single movie
    HashMap<String, Integer> BaconNum; // A HashMap to keep the BaconNumber of each person in the map


    /**
     * Constructor for the Kevin Bacon Game
     *
     * @param dataFileName  Name of the file with the actors and the movies in which they appear.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public KevinBaconGame(String dataFileName) throws FileNotFoundException, IOException{
        // Read file and initialize the Actor2Movies Map
        try{
            FileInputStream fileIn = new FileInputStream(dataFileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Actor2Movies = (HashMap <String, String[]>) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i){
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c){
            c.printStackTrace();
            return;
        }
        // Initialize the Movie2Actors Map from the Actor2Movies Map
        Movie2Actors = new HashMap<String, ArrayList<String>>();
        for(String actor : Actor2Movies.keySet()){
            for(String movie : Actor2Movies.get(actor)){
                // we search every movie for every actor
                if (!Movie2Actors.containsKey(movie)){
                    // If the movie is not in the set of keys then we add it
                    Movie2Actors.put(movie, new ArrayList<String>());
                }
                Movie2Actors.get(movie).add(actor);
            }
        }
    }

    /**
     * Compute the Bacon number for the actor with name queryActor
     *
     * @param queryActor
     * @return the bacon number of queryActor
     */
    public int getBaconNumber(String queryActor){
        BaconNum = new HashMap<String, Integer>();
        Queue<String> q = new ArrayDeque<String>(); // Maintain a queue of actors
        Set<String> s = new HashSet<String>(); // Maintain a set of movies to avoid looking at a movie twice
        if(!Actor2Movies.containsKey(queryActor)){
            return -1;
        }

        // Since we know the Bacon number of Kevin Bacon, we set it and push him to the queue
        q.add("Bacon, Kevin");
        BaconNum.put("Bacon, Kevin", 0);

        while(!q.isEmpty()){
            String actor = q.poll();
            for(String movie: Actor2Movies.get(actor)){ //For each movie in which actor has a role
                if(s.add(movie)){
                    // Check if every actor in movie has a bacon number
                    for (String actorInMovie : Movie2Actors.get(movie)){
                        if(!BaconNum.containsKey(actorInMovie)){
                            BaconNum.put(actorInMovie, BaconNum.get(actor) + 1);
                            q.add(actorInMovie);
                        }
                        if(actorInMovie.equals(queryActor)){
                            return BaconNum.get(queryActor);
                        }
                    }
                }

            }
        }
        return -2;
    }
}
