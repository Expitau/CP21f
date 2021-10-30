import java.util.*;
import java.time.LocalDateTime;

public class FrontEnd {
    private UserInterface ui;
    private BackEnd backend;
    private User user;

    public FrontEnd(UserInterface ui, BackEnd backend) {
        this.ui = ui;
        this.backend = backend;
    }
    
    public boolean auth(String authInfo){
        String[] info = authInfo.split("\n"); // TODO check it's alright
        if(backend.auth(info[0], info[1])){
            user = new User(info[0], info[1]);
            return true;
        }
        return false;
    }

    public void post(Pair<String, String> titleContentPair) {
        Post post = new Post(titleContentPair.key, titleContentPair.value);
        post.setId(backend.getNextPostID(user));
        backend.makePost(user, post);
    }
    
    public void recommend(int N){
        // TODO sub-problem 3
    }

    public void search(String command) {
        // TODO sub-problem 4
    }
    
    User getUser(){
        return user;
    }
}
