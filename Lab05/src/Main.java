import evolution_of_truth.environment.Tournament;

public class Main {
    public static void main(String args[]) {
        Tournament tournament = new Tournament();
        for(int i=0; i<10; i++){
            tournament.resetAgents();
            tournament.playAllGames(10);
            tournament.describe();
            tournament.evolvePopulation();
        }
    }
}
