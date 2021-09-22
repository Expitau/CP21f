public class Main{
  public static void main(String[] args){
    Player p1 = new Player("Superman");
    Player p2 = new Player("Batman");
    Fight fi = new Fight(p1, p2);

    while(!fi.isFinished()){
      fi.proceed();
    }
    Player winner = fi.getWinner();
    System.out.println(winner.userId+" is the winner!");
  }
}
