public class Fight{
  int timeLimit = 100;
  int currRound = 0;

  Player p1, p2;

  Fight(Player p1, Player p2){
    this.p1 = p1;
    this.p2 = p2;
  }

  public void proceed(){
    System.out.println("Round " + currRound);
    attackHeal();
    System.out.println(p1.userId + " health: " + p1.health);
    System.out.println(p2.userId + " health: " + p2.health);
    currRound++;
  }

  private void attackHeal(){
    if(p1.getTactic() == 'a') p1.attack(p2);
    else p1.heal();

    if(p2.getTactic() == 'a') p2.attack(p1);
    else p2.heal();
  }

  public boolean isFinished(){
    boolean limitReached = currRound > timeLimit;
    boolean p1Died = !p1.alive();
    boolean p2Died = !p2.alive();
    return limitReached || p1Died || p2Died;
  }

  public Player getWinner(){
    if(p1.health > p2.health) return p1;
    return p2;
  }
}
