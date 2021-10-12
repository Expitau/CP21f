package evolution_of_truth.match;

import evolution_of_truth.agent.Agent;

public class MistakeMatch extends Match{
    public MistakeMatch(Agent agentA, Agent agentB) {
        super(agentA, agentB);
    }

    @Override
    protected int getChoice(Agent agent, int[] previousChoices){
        int choice = agent.choice(previousChoices);
        if(java.lang.Math.random() <= 0.05) choice = 1-choice;
        return choice;
    }
}
