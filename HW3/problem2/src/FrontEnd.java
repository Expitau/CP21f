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

    public boolean auth(String authInfo) {
        String[] info = authInfo.split("\n"); // TODO check it's alright
        if (backend.auth(info[0], info[1])) {
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

    public void recommend(int N) {
        List<Post> posts = backend.getFriendsLeastNPostList(user.id, N);
        for (Post post : posts) {
            ui.println(post.toString());
        }
    }

    public void search(String command) {
        List<String> keywordList = new LinkedList<>(Arrays.asList(command.split("\\s")));
        keywordList.remove(0);
        List<Post> posts = backend.getKeywordContainPostList(new HashSet<String>(keywordList));

        for (Post post : posts) {
            ui.println(post.getSummary());
        }
    }

    User getUser() {
        return user;
    }
}
