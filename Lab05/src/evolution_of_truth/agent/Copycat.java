package evolution_of_truth.agent;

import evolution_of_truth.match.Match;
import population.Individual;

public class Copycat extends Agent {
    public Copycat() {
        super("Copycat");
    }

    @Override
    public Individual clone(){
        return new Copycat();
    }

    @Override
    public int choice(int[] previousOpponentChoices) {
        if (previousOpponentChoices[0] == Match.UNDEFINED)
            return Match.COOPERATE;
        else
            return previousOpponentChoices[0];
    }
}
