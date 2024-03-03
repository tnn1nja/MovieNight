package net.tnn1nja.movieNight.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;

import static net.tnn1nja.movieNight.Main.db;
import static net.tnn1nja.movieNight.Main.log;

public class APIs {

    //Constants
    private static final String address = "https://streaming-availability.p.rapidapi.com/search/basic";
    private static final String APIKey = "55a3606031mshcf4633bebae51abp130e52jsnc5476f35f166";
    static final String[] providers = {"netflix", "disney", "prime.subscription", "iplayer", "all4"};


    //Out-Facing Methods
    public void test(){
        String jsonString ="{\"results\":[{\"imdbID\":\"tt6119504\",\"tmdbID\":\"455656\",\"imdbRating\":51,\"imdbVoteCount\":6846,\"tmdbRating\":63,\"backdropPath\":\"/smgZYp49OB6xo4hZewxzryrh5xN.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/smgZYp49OB6xo4hZewxzryrh5xN.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/smgZYp49OB6xo4hZewxzryrh5xN.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/smgZYp49OB6xo4hZewxzryrh5xN.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/smgZYp49OB6xo4hZewxzryrh5xN.jpg\"},\"originalTitle\":\"#realityhigh\",\"genres\":[35],\"countries\":[\"US\"],\"year\":2017,\"runtime\":99,\"cast\":[\"Nesta Cooper\",\"Keith Powers\",\"Alicia Sanz\",\"Jake Borelli\",\"Anne Winters\",\"Patrick Davis Alarcón\",\"Michael Provost\"],\"significants\":[\"Fernando Lebrija\"],\"title\":\"#realityhigh\",\"overview\":\"When nerdy high schooler Dani finally attracts the interest of her longtime crush, she lands in the cross hairs of his ex, a social media celebrity.\",\"tagline\":\"\",\"video\":\"3Sy7RofBmrs\",\"posterPath\":\"/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/iZliPeiiDta9KbONAhdFSXhTxrO.jpg\"},\"age\":15,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/80125979/\",\"added\":1653790351,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt2614684\",\"tmdbID\":\"252178\",\"imdbRating\":72,\"imdbVoteCount\":59748,\"tmdbRating\":67,\"backdropPath\":\"/aTloiKdNs2c8vlstbx3wBWD6Thi.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/aTloiKdNs2c8vlstbx3wBWD6Thi.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/aTloiKdNs2c8vlstbx3wBWD6Thi.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/aTloiKdNs2c8vlstbx3wBWD6Thi.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/aTloiKdNs2c8vlstbx3wBWD6Thi.jpg\"},\"originalTitle\":\"'71\",\"genres\":[28,80,18],\"countries\":[\"GB\"],\"year\":2014,\"runtime\":99,\"cast\":[\"Jack O'Connell\",\"Sean Harris\",\"Paul Anderson\",\"Sam Reid\",\"Sam Hazeldine\",\"Barry Keoghan\",\"Charlie Murphy\"],\"significants\":[\"Yann Demange\"],\"title\":\"'71\",\"overview\":\"A young British soldier must find his way back to safety after his unit accidentally abandons him during a riot in the streets of Belfast.\",\"tagline\":\"\",\"video\":\"kIYDNyEkJP4\",\"posterPath\":\"/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/xjorsS84euahsmGlnEEeE3LFSVZ.jpg\"},\"age\":15,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/70301585/\",\"added\":1694622331,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt2385126\",\"tmdbID\":\"412186\",\"imdbRating\":55,\"imdbVoteCount\":279,\"tmdbRating\":59,\"backdropPath\":\"/5MnTrwEBHkSlktid9bkY9W4o98G.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/5MnTrwEBHkSlktid9bkY9W4o98G.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/5MnTrwEBHkSlktid9bkY9W4o98G.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/5MnTrwEBHkSlktid9bkY9W4o98G.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/5MnTrwEBHkSlktid9bkY9W4o98G.jpg\"},\"originalTitle\":\"'76\",\"genres\":[18,10749],\"countries\":[\"NG\"],\"year\":2016,\"runtime\":118,\"cast\":[\"Rita Dominic\",\"Ramsey Nouah\",\"Efetobore Afatakpa\",\"Nenye Eke\",\"Nelly Ekwereogo\",\"Ibinabo Fiberesima\",\"Chidi Mokeme\"],\"significants\":[\"Izu Ojukwu\"],\"title\":\"'76\",\"overview\":\"Nollywood superstars Ramsey Nouah, Rita Dominic, and Chidi Mokeme headline this gripping drama set against the backdrop of the attempted 1976 military coup against the government of General Murtala Mohammed.\",\"tagline\":\"\",\"video\":\"WuWL33z7brM\",\"posterPath\":\"/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/2TOA28EMpBjz4jmlpBPKvZLdwAf.jpg\"},\"age\":12,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/81412227/\",\"added\":1653676890,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt6210996\",\"tmdbID\":\"505177\",\"imdbRating\":51,\"imdbVoteCount\":11424,\"tmdbRating\":53,\"backdropPath\":\"/9QWBstBOwv36cuv0hK4esNyFnvo.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/9QWBstBOwv36cuv0hK4esNyFnvo.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/9QWBstBOwv36cuv0hK4esNyFnvo.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/9QWBstBOwv36cuv0hK4esNyFnvo.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/9QWBstBOwv36cuv0hK4esNyFnvo.jpg\"},\"originalTitle\":\"10 x 10\",\"genres\":[18,9648,53],\"countries\":[\"GB\"],\"year\":2018,\"runtime\":86,\"cast\":[\"Luke Evans\",\"Kelly Reilly\",\"Noel Clarke\",\"Olivia Chenery\",\"Jason Maza\",\"Jill Winternitz\",\"Benjamin Hoetjes\"],\"significants\":[\"Suzi Ewing\"],\"title\":\"10x10\",\"overview\":\"Lewis is an outwardly ordinary guy, but in reality he is hiding an obsession ? revenge ? against Cathy. Lewis kidnaps Cathy in broad daylight and takes her to his home, where he locks her in a soundproof cell and attempts to extract a dark secret from her past.\",\"tagline\":\"There are some secrets we cannot escape\",\"video\":\"3ZaMk8t98H8\",\"posterPath\":\"/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/egMETBYual2JtfFGigXTA0tGkME.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/egMETBYual2JtfFGigXTA0tGkME.jpg\"},\"age\":16,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/80988902/\",\"added\":1694622244,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt8032828\",\"tmdbID\":\"877726\",\"imdbRating\":37,\"imdbVoteCount\":14,\"tmdbRating\":60,\"backdropPath\":\"\",\"backdropURLs\":{},\"originalTitle\":\"100 milioni di bracciate\",\"genres\":[1],\"countries\":[],\"year\":2017,\"runtime\":87,\"cast\":[\"Gian Marco Tavani\",\"Paola Onofri\",\"William Angiuli\",\"Cristiana Bianchetti\",\"Nicola Bizzarri\",\"Regina Zappala\",\"Riccardo Fasoli\"],\"significants\":[\"Donatella Cervi\"],\"title\":\"100 Millions Swimming\",\"overview\":\"When a swimmer becomes famous for his epic and social commitment: the story of Leo Callone, swimmer able to cross the English channel, to travel around the world twice and builds a hospital in Guatemala in memory of his son. (IMDB)\",\"tagline\":\"\",\"video\":\"\",\"posterPath\":\"/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/rXo5qzx1pk5Qhbwsyn2wHVpmCqY.jpg\"},\"age\":-1,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/81409062/\",\"added\":1694621817,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt2024544\",\"tmdbID\":\"76203\",\"imdbRating\":81,\"imdbVoteCount\":735977,\"tmdbRating\":79,\"backdropPath\":\"/4Bb1kMIfrT2tYRZ9M6Jhqy6gkeF.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/4Bb1kMIfrT2tYRZ9M6Jhqy6gkeF.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/4Bb1kMIfrT2tYRZ9M6Jhqy6gkeF.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/4Bb1kMIfrT2tYRZ9M6Jhqy6gkeF.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/4Bb1kMIfrT2tYRZ9M6Jhqy6gkeF.jpg\"},\"originalTitle\":\"12 Years a Slave\",\"genres\":[1,18,36],\"countries\":[\"US\",\"GB\"],\"year\":2013,\"runtime\":134,\"cast\":[\"Chiwetel Ejiofor\",\"Michael Fassbender\",\"Lupita Nyong'o\",\"Benedict Cumberbatch\",\"Paul Dano\",\"Sarah Paulson\",\"Adepero Oduye\"],\"significants\":[\"Steve McQueen\"],\"title\":\"12 Years a Slave\",\"overview\":\"In the pre-Civil War United States, Solomon Northup, a free black man from upstate New York, is abducted and sold into slavery. Facing cruelty as well as unexpected kindnesses Solomon struggles not only to stay alive, but to retain his dignity. In the twelfth year of his unforgettable odyssey, Solomon?s chance meeting with a Canadian abolitionist will forever alter his life.\",\"tagline\":\"The extraordinary true story of Solomon Northup\",\"video\":\"z02Ie8wKKRg\",\"posterPath\":\"/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/xdANQijuNrJaw1HA61rDccME4Tm.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/xdANQijuNrJaw1HA61rDccME4Tm.jpg\"},\"age\":15,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/70284282/\",\"added\":1694621730,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt7843930\",\"tmdbID\":\"1000809\",\"imdbRating\":63,\"imdbVoteCount\":310,\"tmdbRating\":61,\"backdropPath\":\"/u3Kv2rcE9gSQDEggm2aO7lMBE5o.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/u3Kv2rcE9gSQDEggm2aO7lMBE5o.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/u3Kv2rcE9gSQDEggm2aO7lMBE5o.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/u3Kv2rcE9gSQDEggm2aO7lMBE5o.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/u3Kv2rcE9gSQDEggm2aO7lMBE5o.jpg\"},\"originalTitle\":\"13 Hours That Saved Britain\",\"genres\":[99,36,10752],\"countries\":[\"NL\",\"GB\"],\"year\":2011,\"runtime\":49,\"cast\":[\"Raymond Binks\",\"Vera Lynn\",\"Nicholas Parsons\",\"Brian Sewell\"],\"significants\":[\"Stephen Saunders\"],\"title\":\"13 Hours That Saved Britain\",\"overview\":\"In this documentary, experts dissect the Battle of Britain, which took place on Sept. 15, 1940 ? a day that determined the fate of the nation.\",\"tagline\":\"\",\"video\":\"\",\"posterPath\":\"/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/mHfsMjLduYpnLVisVqax6lAISde.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/mHfsMjLduYpnLVisVqax6lAISde.jpg\"},\"age\":12,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/81466189/\",\"added\":1694622277,\"leaving\":0}},\"prime\":{\"gb\":{\"link\":\"https://www.amazon.co.uk/gp/video/detail/0QBQXP42I6Q1HCN0C02I61Q51W/\",\"added\":1661510668,\"leaving\":0}}},\"originalLanguage\":\"en\"},{\"imdbID\":\"tt16379224\",\"tmdbID\":\"913179\",\"imdbRating\":65,\"imdbVoteCount\":1158,\"tmdbRating\":63,\"backdropPath\":\"/pwveCZhMhXm7nGPFCtBTYcT6MyE.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/pwveCZhMhXm7nGPFCtBTYcT6MyE.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/pwveCZhMhXm7nGPFCtBTYcT6MyE.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/pwveCZhMhXm7nGPFCtBTYcT6MyE.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/pwveCZhMhXm7nGPFCtBTYcT6MyE.jpg\"},\"originalTitle\":\"137 Shots\",\"genres\":[99,80],\"countries\":[\"US\"],\"year\":2021,\"runtime\":104,\"cast\":[\"Mansfield Frazier\",\"Alonzo Mitchell\",\"Mike DeWine\",\"Timothy McGinty\",\"Connie Schultz\",\"Steve Loomis\",\"Michelle Russell\"],\"significants\":[\"Michael Milano\"],\"title\":\"137 Shots\",\"overview\":\"In this documentary, law enforcement faces scrutiny as Americans demand justice after police violence claims multiple Black lives in Cleveland.\",\"tagline\":\"\",\"video\":\"\",\"posterPath\":\"/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/c62odansbfa1zQ01lnyN4tIu6Dc.jpg\"},\"age\":16,\"streamingInfo\":{\"netflix\":{\"gb\":{\"link\":\"https://www.netflix.com/title/80172819/\",\"added\":1640122462,\"leaving\":0}}},\"originalLanguage\":\"en\"}],\"total_pages\":294}\n";
        JSONObject jsonObject = new JSONObject(jsonString);
        ArrayList<JSONFilm> film = extractData(jsonObject);
        saveData(film.get(0), "netflix");
    }

    //Save JSONFilm to Database
    public void saveData(JSONFilm film, String provider){

        //Build Film Insert Command
        String sqlCmd = "INSERT INTO Films(Title, Synopsis, Year, Rating, Genres, TmdbID) VALUES(" +
                "'" + film.TITLE + "'," +
                "'" + film.SYNOPSIS + "'," +
                film.YEAR + "," +
                film.RATING + "," +
                "'" + film.GENRES + "'," +
                "'" + film.TMDBID + "')";
        db.run(sqlCmd);

        //Download Cover
        downloadImage(film.COVERHTTP, film.TMDBID);
    }

    //Populate the Providers Table (Hardcoded)
    public void populateProviders(){
        try {
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(1, 'Netflix', " +
                    "'https://www.netflix.co.uk/', 'netflix.jpg', 'netflix')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(2, 'Disney Plus', " +
                    "'https://www.disneyplus.com/', 'disney.jpg', 'disney')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(3, 'Amazon Prime Video', " +
                    "'https://www.amazon.co.uk/', 'prime.jpg', 'prime.subscription')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(4, 'BBC iPlayer', " +
                    "'https://bbc.co.uk/iplayer/', 'iplayer.jpg', 'iplayer')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(5, 'All 4', " +
                    "'https://www.channel4.com/', 'all4.jpg', 'all4')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(6, 'Custom'," +
                    " null, 'custom.jpg', 'custom')");
            log.info("Providers Table Populated.");

        }catch (SQLException e){
            String violation = "[SQLITE_CONSTRAINT_PRIMARYKEY] A PRIMARY KEY constraint failed " +
                    "(UNIQUE constraint failed: Providers.ProviderID)";
            if (e.getMessage().equalsIgnoreCase(violation)) {
                log.finest("Providers Table Already Populated... Skipping");
            }else{
                log.severe("Failed to Populate Providers Table - SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    //Make API Call
    private String call(String prompt){ //&service=netflix&page=1
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address + "?country=gb&type=movie&output_language=en&language=en" +
                            "&order_by=popularity_alltime" + prompt))
                    .header("X-RapidAPI-Key", APIKey)
                    .header("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        }catch(InterruptedException | IOException e){
            log.severe("API Request Failed: " + e.getMessage());
            e.printStackTrace();
        }

        return response.body();
    }

    //Extract Data From JSONObject
    private ArrayList<JSONFilm> extractData(JSONObject jsonObject){
        ArrayList<JSONFilm> output = new ArrayList<JSONFilm>();

        JSONArray results = jsonObject.getJSONArray("results");
        for (Object item: results){
            JSONFilm jf = new JSONFilm();
            JSONObject jo = (JSONObject) item;
            jf.TITLE = jo.getString("title");
            jf.SYNOPSIS = jo.getString("overview");
            jf.YEAR = jo.getInt("year");
            jf.RATING = jo.getInt("imdbRating");
            jf.TMDBID = jo.getString("tmdbID");
            jf.DIRECTOR = jo.getJSONArray("significants").getString(0);

            StringBuilder sb = new StringBuilder();
            for(Object genre: jo.getJSONArray("genres")){
                int i = (int) genre;
                sb.append(i).append(",");
            }
            jf.GENRES = sb.toString();

            jf.CAST = new ArrayList<String>();
            for(Object castMember: jo.getJSONArray("cast")){
                jf.CAST.add((String) castMember);
            }

            jf.COVERHTTP = jo.getJSONObject("posterURLs").getString("185");
            output.add(jf);
        }

        return output;
    }

    //Download Image From HTTP Address
    public void downloadImage(String http, String filename){
        try {
            URL url = new URL(http);
            InputStream in = url.openStream();
            Path destination = Path.of(".\\media\\film\\" + filename + ".jpg");
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.severe("Failed to Download Image " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Film Data Class
    private static class JSONFilm{
        String TITLE;
        String SYNOPSIS;
        int YEAR;
        int RATING;
        String TMDBID;
        String DIRECTOR;
        String GENRES;
        ArrayList<String> CAST;
        String COVERHTTP;
    }

}
