package net.tnn1nja.movieNight.data.objects;

public class Film {


    //Attributes
    public int ID;
    public int YEAR;
    public int RATING;
    public int[] GENRES;
    public String TITLE;
    public String SYNOPSIS;
    public String TMDBID;

    public String DIRECTOR;
    public String[] CAST;

    public Boolean SAVED;
    public Boolean SEEN;
    public Boolean LIKED;

    public int[] PROVIDERS;


    //Constructor
    public Film(int Id, String Title, String Synopsis, int Year, int Rating, int[] Genres, String TmdbId, Boolean Saved,
                Boolean Liked, Boolean Seen, String Director, String[] Cast, int[] Providers){

        ID = Id;
        TITLE = Title;
        SYNOPSIS = Synopsis;
        YEAR = Year;
        RATING = Rating;
        GENRES = Genres;
        TMDBID = TmdbId;
        SAVED = Saved;
        LIKED = Liked;
        SEEN = Seen;
        DIRECTOR = Director;
        CAST = Cast;
        PROVIDERS = Providers;

    }


    //Static Constants
    private static final String baseURL = "\\media\\film";

    //Get Cover URL
    public String getRelativeCoverPath(){
        return baseURL + "\\" + TMDBID;
    }

}
