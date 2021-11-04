import java.util.*;

public class Movie {
    private String title;
    private Set<String> tags;
    private Map<String, Integer> rates = new HashMap<>();
    public Movie(String title) {this.title = title; this.tags = new HashSet<String>();}

    public Movie(String title, Set<String> tags) { this.title = title; this.tags = tags;}

    public boolean containsTag(String[] tags){
        Set<String> tagSet = new HashSet<>();
        for(String tag : tags) tagSet.add(tag);
        return containsTag(tagSet);
    }

    public boolean containsTag(Set<String> tags){
        return this.tags.containsAll(tags);
    }

    public void updateRating(String username, Integer rate){
        rates.put(username, rate);
    }

    public int getDoubledMedieanRate(){
        List<Integer> list = new ArrayList<>(rates.values());
        int n = list.size();
        if(n == 0) return 0;

        Collections.sort(list);  
        return (list.get((n-1)/2)+list.get(n/2));
    }
    
    @Override
    public String toString() {
        return title;
    }
}
