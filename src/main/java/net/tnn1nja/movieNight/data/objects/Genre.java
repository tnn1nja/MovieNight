package net.tnn1nja.movieNight.data.objects;

import java.util.HashMap;
import java.util.Random;

public class Genre {

    //Genre Index
    private static final HashMap<Integer, String> index = new HashMap<Integer, String>();
    static{
        index.put(12, "Adventure");
        index.put(14, "Fantasy");
        index.put(16, "Animation");
        index.put(18, "Drama");
        index.put(27, "Horror");
        index.put(28, "Action");
        index.put(35, "Comedy");
        index.put(36, "History");
        index.put(37, "Western");
        index.put(53, "Thriller");
        index.put(80, "Crime");
        index.put(99, "Documentary");
        index.put(878, "Science Fiction");
        index.put(9648, "Mystery");
        index.put(10402, "Music");
        index.put(10749, "Romance");
        index.put(10751, "Family");
        index.put(10752, "War");
        index.put(10763, "News");
        index.put(10764, "Reality");
        index.put(10767, "Talk Show");
    }

    //Get Number of Genres
    public static int getCount(){
        return index.size();
    }

    //Get a Random Genre
    public static int getRandom(){
        Integer[] keys = index.keySet().toArray(new Integer[0]);
        return keys[new Random().nextInt(index.size())];
    }

    //Get Genre by ID
    public static String get(int id){
        return index.get(id);
    }

}
