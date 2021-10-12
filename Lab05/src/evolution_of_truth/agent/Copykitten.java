package evolution_of_truth.agent;

import evolution_of_truth.match.Match;
import population.Individual;

public class Copykitten extends Agent{
    public Copykitten(){
        super("Copykitten");
    }
    @Override
    public int choice(int[] previousOpponentChoices) {
        if(previousOpponentChoices[0] == Match.CHEAT && previousOpponentChoices[1] == Match.CHEAT)
            return Match.CHEAT;
        return Match.COOPERATE;
    }

    @Override
    public Individual clone() {
        return new Copykitten();
    }
}
