package Platform;

import Platform.Games.RockPaperScissors;
import Platform.Games.Dice;

import java.util.Scanner;

public class Platform{
  private Dice dice;
  private RockPaperScissors rsp;
  private int rounds = 1;
  private boolean onGoing = false;

  private int randInt(int s, int e){
    return (int) (Math.random() * (e - s)) + s;
  }

  private int playGame(int gamemode){
    if(gamemode == 0){
      return dice.playGame();
    }else if(gamemode == 1){
      return rsp.playGame();
    }
    return -1;
  }

  private int setup(){
    Scanner scanner = new Scanner(System.in);
    int input = scanner.nextInt();
    if(input == 0) dice = new Dice();
    else if(input == 1) rsp = new RockPaperScissors();
    return input;
  }

  public float run(){
    setRounds(randInt(5, 11));

    int gamemode = setup(), sum = 0;
    for(int i = 0; i < rounds; i++){
      int status = playGame(gamemode);
      if(status == 1) sum += 1;
    }
    onGoing = false;

    return ((float) sum / (float) rounds);
  }

  public void setRounds(int num){
    if(!onGoing){
      rounds = num;
      onGoing = true;
    }
  }
}
