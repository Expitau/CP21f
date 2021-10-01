package Platform.Games;

public class Dice{
  public int playGame(){
    int user = rollDice(0, 100);
    int opponent = rollDice(0, 100);
    System.out.println(user + " " + opponent);

    if(user > opponent) return 1;
    else if(user < opponent) return -1;
    return 0;
  }

  private int rollDice(int s, int e){
    return (int) (Math.random() * (e - s)) + s;
  }
}
