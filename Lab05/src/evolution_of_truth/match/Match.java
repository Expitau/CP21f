package evolution_of_truth.match;

import evolution_of_truth.agent.Agent;

public class Match {
    public static int CHEAT = 0;
    public static int COOPERATE = 1;
    public static int UNDEFINED = -1;

    Agent agentA, agentB;
    int[] previousChoicesA, previousChoicesB;
    // Sets the value each player gets for all possible cases

    protected static int ruleMatrix[][][] = {
            {
                    {0, 0}, // A cheats, B cheats
                    {3, -1} // A cheats, B cooperates
            },
            {
                    {-1, 3}, // A cooperates, B cheats
                    {2, 2} // A cooperates, B cooperates
            }
    };

    public Match(Agent agentA, Agent agentB){
        this.agentA = agentA;
        this.agentB = agentB;
        previousChoicesA = new int[2];
        previousChoicesB = new int[2];
        for(int i=0; i<previousChoicesA.length; i++) previousChoicesA[i] = Match.UNDEFINED;
        for(int i=0; i<previousChoicesB.length; i++) previousChoicesB[i] = Match.UNDEFINED;
    }

    public void playGame() {
        int choiceA = getChoice(agentA, previousChoicesB);
        int choiceB = getChoice(agentB, previousChoicesA);
        agentA.setScore(agentA.getScore() + ruleMatrix[choiceA][choiceB][0]);
        agentB.setScore(agentB.getScore() + ruleMatrix[choiceA][choiceB][1]);
        previousChoicesA = getUpdatedChoices(previousChoicesA, choiceA);
        previousChoicesB = getUpdatedChoices(previousChoicesB, choiceB);
    }

    protected int getChoice(Agent agent, int[] previousChoices){
        return agent.choice(previousChoices);
    }

    private int[] getUpdatedChoices(int[] previousChoices, int newChoice){
        int[] res = new int[previousChoices.length];
        for(int i=1; i<previousChoices.length; i++) res[i] = previousChoices[i-1];
        res[0] = newChoice;
        return res;
    }
}
