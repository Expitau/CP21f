public class Fight{
  int timeLimit = 100;
  int currRound = 0;

  Player p1;
  Player p2;

  Fight(Player p1, Player p2){
    this.p1 = p1;
    this.p2 = p2;
  }

  public void proceed(){
    System.out.println("Round " + this.currRound);

    if(this.p1.getTactic() == 'a'){
      this.p1.attack(this.p2);
    }else{
      this.p1.heal();
    }

    if(!isFinished()){
      if(this.p2.getTactic() == 'a'){
        this.p2.attack(this.p1);
      }else{
        this.p2.heal();
      }
    }

    System.out.println(this.p1.userId+" health: "+this.p1.health);
    System.out.println(this.p2.userId+" health: "+this.p2.health);
    this.currRound++;
  }

  public boolean isFinished(){
    if(this.currRound > this.timeLimit) return true;
    return !(this.p1.alive()&&this.p2.alive());
  }

  public Player getWinner(){
    Player ret = this.p2;
    if(this.p1.health > ret.health) ret = this.p1;
    return ret;
  }
}
