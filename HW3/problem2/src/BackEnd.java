import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BackEnd extends ServerResourceAccessible {
    // Use getServerStorageDir() as a default directory
    // TODO sub-program 1 ~ 4 :
    // Create helper funtions to support FrontEnd class

    public boolean auth(String id, String pw){
        File pwFile = new File(getServerStorageDir()+id+"/password.txt");
        boolean ret = false;
        try {
            Scanner input = new Scanner(pwFile);
            String realPw = input.nextLine();
            ret = pw.equals(realPw);
            input.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    private int getPostID(File postFile){
        return Integer.parseInt(postFile.getName().split("\\.txt")[0]);
    }

    public int getNextPostID(User user){
        int ret = -1;
        File postFolder = new File(getServerStorageDir()+user.id+"/post");
        File[] postList = postFolder.listFiles();
        for(int i=0; i < postList.length; i++){
            if(postList[i].isFile()){
                int postId = getPostID(postList[i]);
                ret = Math.max(ret, postId);
            }
        }
        return ret + 1;
    }

    public void makePost(User user, Post post){
        File postFile = new File(getServerStorageDir()+user.id+"/post/"+post.getId());
        String outputString = String.format("%s\n%s\n\n%s", post.getDate(), post.getTitle(), post.getContent());
        try{
            FileWriter fileWriter = new FileWriter(postFile);
            fileWriter.write(outputString);
            fileWriter.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Post getPost(File postFile){
        String dateString = "", title = "", content = "";
        int id = getPostID(postFile);
        try {
            Scanner input = new Scanner(postFile);
            dateString = input.nextLine();
            title = input.nextLine();
            input.nextLine();

            content = "";
            while(input.hasNext()){
                content += input.nextLine() + "\n";
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Post ret = new Post(id, dateString, title, content);
        return ret;
    }

    public List<Post> getFriendsLeastNPostList(String userID, int N){
        List<Post> posts = new LinkedList<>();
        List<String> friendList = getUserFriendList(userID);

        for(String friendId : friendList){
            posts.addAll(getUserPostList(friendId));
        }

        Collections.sort(posts, new Comparator<Post>(){
            @Override
            public int compare(Post p1, Post p2){
                return p2.getDate().compareTo(p1.getDate());
            }
        });

        if(posts.size() > N) posts = posts.subList(0, N);
        
        return posts;
    }

    public List<String> getUserFriendList(String userID){
        List<String> friends = new LinkedList<>();
        File friendsFile = new File(getServerStorageDir()+userID+"/friend.txt");
        try{
            Scanner input = new Scanner(friendsFile);
            while(input.hasNextLine()){
                friends.add(input.nextLine());
            }
            input.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return friends;
    }

    public List<Post> getUserPostList(String userID){
        List<Post> ret = new LinkedList<>();

        File postFolder = new File(getServerStorageDir()+userID+"/post");
        File[] postList = postFolder.listFiles();
        for(File postFile : postList){
            if(postFile.isFile()){
                try {
                    Post post = getPost(postFile);
                    ret.add(post);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public List<String> getuserIDList(){
        File dataFolder = new File(getServerStorageDir());
        File[] userList = dataFolder.listFiles();

        List<String> userIDList = new LinkedList<>();
        for(File userFile : userList){
            if(userFile.isDirectory()){
                userIDList.add(userFile.getName());
            }
        }

        return userIDList;
    }

    private void sortPostKeywordCnt(List<PostKeywordCnt> posts){
        Collections.sort(posts, new Comparator<PostKeywordCnt>(){
            @Override
            public int compare(PostKeywordCnt o1, PostKeywordCnt o2){
                if(o1.keyword == o2.keyword){
                    if(o1.word == o2.word) return 0;
                    if(o1.word > o2.word) return -1;
                    return 1;
                }
                if(o1.keyword > o2.keyword) return -1;
                return 1;
            }
        });
    }

    public List<Post> getKeywordContainPostList(Set<String> keywords){

        List<String> userIDs = getuserIDList();
        List<PostKeywordCnt> posts = new LinkedList<>();
        for(String userID : userIDs){
            posts.addAll(getKeywordedUserPostList(userID, keywords));
        }
        sortPostKeywordCnt(posts);
        if(posts.size() > 10) posts = posts.subList(0, 10);

        List<Post> ret = new LinkedList<>();
        for(PostKeywordCnt post : posts){
            ret.add(post.post);
        }

        return ret;
    }

    public List<PostKeywordCnt> getKeywordedUserPostList(String userID, Set<String> keywords){ // it return less or same with 10 posts;
        List<Post> posts = getUserPostList(userID);
        List<PostKeywordCnt> proceccedPosts = new LinkedList<>();

        for(Post post : posts){
            List<String> words = new LinkedList<String>(Arrays.asList(post.getContent().split("\\s")));
            words.addAll(Arrays.asList(post.getTitle().split("\\s")));

            Set<String> wordSet = new HashSet<>(words);
            if(wordSet.containsAll(keywords)){
                int keywordCnt = 0;
                for(String word : words){
                    if(keywords.contains(word)) keywordCnt++;
                }
                proceccedPosts.add(new PostKeywordCnt(post, keywordCnt, words.size()));
            }
        }

        sortPostKeywordCnt(proceccedPosts);

        if(proceccedPosts.size() > 10) proceccedPosts = proceccedPosts.subList(0, 10);

        return proceccedPosts;
    }

}

class PostKeywordCnt{
    public Post post;
    public int keyword;
    public int word;
    public PostKeywordCnt(Post post, int keyword, int word){
        this.post = post;
        this.keyword = keyword;
        this.word = word;
    }
}
