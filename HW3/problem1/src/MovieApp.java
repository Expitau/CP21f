
import java.util.*;

public class MovieApp {
    private Map<String, Movie> movies = new HashMap<String, Movie>();
    private Map<String, User> users = new HashMap<String, User>();
    private Map<String, Map<String, Integer>> ratings = new HashMap<>();
    private Map<String, List<List<Movie>>> searchHistories = new HashMap<>();

    public boolean addMovie(String title, String[] tags) {
        // check title is available
        if(title == null || movies.containsKey(title)) return false;

        // check tags is not empty
        if(tags == null || tags.length == 0) return false;

        // make new Movie and store it
        Set<String> tagSet = new HashSet<>();
        for(String tag : tags) tagSet.add(tag);
        Movie movie = new Movie(title, tagSet);
        
        movies.put(title, movie);
        return true;
    }

    public boolean addUser(String name) {
        // check name is available
        if(name == null || users.containsKey(name)) return false;

        User user = new User(name);
        users.put(name, user);
        return true;
    }

    public Movie findMovie(String title) {
        return movies.get(title); // if no key with title return null....
    }

    public User findUser(String username) {
        return users.get(username); // if no key with username return null....
    }

    public List<Movie> findMoviesWithTags(String[] tags) {
        // check
        List<Movie> ret = new LinkedList<Movie>();
        if(tags == null || tags.length == 0) return ret;

        //search
        for(String key : movies.keySet()){
            Movie movie = movies.get(key);
            if(movie.containsTag(tags)) ret.add(movie);
        }
        return ret;
    }

    public boolean rateMovie(User user, String title, int rating) {
        // check 
        if(title == null || user == null) return false;
        String username = user.toString();
        if(findUser(username) == null || findMovie(title) == null) return false;
        if(rating < 0 || rating > 5) return false;

        // rate
        if(!ratings.containsKey(username)) ratings.put(username, new HashMap<String, Integer>());
        Map<String, Integer> rateList = ratings.get(username);
        rateList.put(title, rating);
        findMovie(title).updateRating(username, rating);

        return true;
    }

    public int getUserRating(User user, String title) {
        // check
        if(title == null || user == null) return -1;
        String username = user.toString();
        if(findUser(username) == null || findMovie(title) == null) return -1;

        // search
        if(!ratings.containsKey(username)) return 0;
        Map<String, Integer> rateList = ratings.get(username);
        if(!rateList.containsKey(title)) return 0;
        return rateList.get(title);
    }

    public List<Movie> findUserMoviesWithTags(User user, String[] tags) {
        // TODO sub-problem 4
        List<Movie> ret = new LinkedList<>();
        if(user == null || tags == null) return ret;
        String username = user.toString();
        if(findUser(username) == null) return ret;

        ret = findMoviesWithTags(tags);
        if(!searchHistories.containsKey(username)) searchHistories.put(username, new LinkedList<List<Movie>>());
        List<List<Movie>> history = searchHistories.get(username);
        history.add(ret);
        return ret;
    }

    public List<Movie> recommend(User user) {
        // TODO sub-problem 4
        List<Movie> ret = new LinkedList<>();
        if(user == null) return ret;
        String username = user.toString();
        if(findUser(username) == null) return ret;

        List<List<Movie>> history = searchHistories.get(username);
        if(history == null) return ret;
        
        Map<String, Integer> movieCount = new HashMap<>();
        for(List<Movie> movies : history){
            if(movies == null) continue;
            for(Movie movie : movies){
                Integer count = movieCount.get(movie.toString());
                if(count == null) count = 0;
                movieCount.put(movie.toString(), count+1);
            }
        }
        
        for(String title : movieCount.keySet()){
            Movie movie = findMovie(title);
            for(int i=0; i<3; i++){
                if(ret.size() <= i){
                    ret.add(movie);
                    break;
                }
                if(movieCount.get(title) < movieCount.get(ret.get(i).toString())) continue;
                if(movie.getDoubledMedieanRate() < ret.get(i).getDoubledMedieanRate()) continue;
                if(movie.toString().compareTo(ret.get(i).toString()) > 0) continue;
                ret.add(i, movie);
                break;
            }
        }
        if(ret.size() > 3) ret = ret.subList(0, 3);

        return ret;
    }
}
