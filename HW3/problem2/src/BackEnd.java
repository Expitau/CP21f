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

    public int getNextPostID(User user){
        int ret = -1;
        File postFolder = new File(getServerStorageDir()+user.id+"/post");
        File[] postList = postFolder.listFiles();
        for(int i=0; i < postList.length; i++){
            if(postList[i].isFile()){
                int postId = Integer.parseInt(postList[i].getName().split("\\.txt")[0]);
                ret = Math.max(ret, postId);
            }
        }
        return ret + 1;
    }

    public void makePost(User user, Post post){
        File postFile = new File(getServerStorageDir()+user.id+"/post/"+post.getId());
        try{
            FileWriter fileWriter = new FileWriter(postFile);
            fileWriter.write(post.getDate());
            fileWriter.write(post.getTitle());
            fileWriter.write("");
            fileWriter.write(post.getContent());
            fileWriter.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
