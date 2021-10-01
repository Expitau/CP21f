package Platform.Games;

import java.util.Scanner;

public class RockPaperScissors{
  private static String[] rpsEnum = {"scissor", "rock", "paper"};
  private static String ItoS(int n){
    if(0 <= n && n <= 2) return rpsEnum[n];
    return "";
  }
  private static int StoI(String str){
    for(int i = 0; i < 3; i++){
      if(str.equals(ItoS(i))) return i;
    }
    return -1;
  }

  private String randomAction(){
    return ItoS((int) (Math.random() * 3));
  }
  private int compare(String A, String B){
    return (StoI(A)-StoI(B)+4)%3 - 1;
  }

  public int playGame(){
    Scanner scanner = new Scanner(System.in);
    String user = scanner.next();
    String opponent = randomAction();
    System.out.println(user + " " + opponent);

    if(StoI(user) == -1) return -1;
    return compare(user, opponent);
  }
}
