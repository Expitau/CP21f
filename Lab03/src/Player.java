public class Player{
  String userId;

  int health = 50;

  Player(String userId){
    this.userId = userId;
  }

  public void attack(Player opponent){
    int delta = (int) (Math.random() * 5) + 1;
    opponent.health -= delta;
    if(opponent.health < 0) opponent.health = 0;
  }

  public void heal(){
    int delta = (int) (Math.random() * 3) + 1;

    health += delta;
    if(health > 50) health = 50;
  }

  public boolean alive(){
    return this.health > 0;
  }

  public char getTactic(){
    if(Math.random() < 0.7) return 'a';
    return 'h';
  }
}
