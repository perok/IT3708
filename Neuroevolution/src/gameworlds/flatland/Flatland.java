package gameworlds.flatland;

import gameworlds.flatland.sensor.Items;
import gameworlds.flatland.sensor.Sensed;
import math.linnalg.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Perÿyvind on 03/04/2015.
 */
public class Flatland {

    private final int maxTimestamp = 60;
    private int timestamp = 0;

    private int foodEaten = 0;
    private int poisonEaten = 0;

    private Vector2 agentPosition;
    private Vector2 agentDirection;


    private Items[][] world;


    private ArrayList<Vector2> agentPositionHistory;

    /**
     * Contstructs the Flatland world in a NxN grid with Food-poison distribution of
     * f & p.
     * @param n
     * @param f
     * @param p
     */
    public Flatland(int n, double f, double p){
        Random random = new Random();

        agentPositionHistory = new ArrayList<>();

        // Upwards
        agentDirection = new Vector2(0, 1);

        // Create world
        world = createRandomWorld(random, n, f, p);

        // Robot placement
        agentPosition = new Vector2();
        do {
            agentPosition.x = (int) (random.nextDouble() * n);
            agentPosition.y = (int) (random.nextDouble() * n);
        } while(world[(int)agentPosition.x][(int)agentPosition.y] != Items.NOTHING);

        agentPositionHistory.add(agentPosition);
    }

    /**
     * Creates a new Flatland with an old board.
     * @param flatland
     */
    public Flatland(Flatland flatland){

    }


    /**
     * A move action returns sensory output after the movement is performed.
     * @param movement
     * @return
     */
    public List<Sensed> move(Movement movement) {

        switch (movement) {
            // Move in the same direction
            case FORWARD:
                break;
            // Rotate left, then forward
            case LEFTFORWARD:
                agentDirection.rotate(-90);
                break;
            // rotate right, then forward
            case RIGHTFORWARD:
                agentDirection.rotate(90);
                break;
        }

        // add the movement
        agentPosition.add(agentDirection);

        // Check if we have overflowed
        if(agentPosition.x < 0)
            agentPosition.x = world[0].length - 1;
        else if (agentPosition.x > world[0].length - 1)
            agentPosition.x = 0;

        if(agentPosition.y < 0)
            agentPosition.y = world.length - 1;
        else if (agentPosition.y > world.length - 1)
            agentPosition.y = 0;


        // Find sensed data
        //todo

        return null;
    }


    /**
     * Creates a new random world.
     *
     * @param n
     * @param f
     * @param p
     * @return
     */
    private Items[][] createRandomWorld(Random random, int n, double f, double p){
        Items[][] randomWorld = new Items[n][n];

        for (int i = 0; i < randomWorld.length; i++) {
            for (int j = 0; j < randomWorld[i].length; j++) {

                double chance = random.nextDouble();

                if (chance < f) {
                    randomWorld[i][j] = Items.FOOD;
                } else if (chance < f + p) {
                    randomWorld[i][j] = Items.POISON;
                } else {
                    randomWorld[i][j] = Items.NOTHING;
                }
            }
        }

        return randomWorld;
    }

}
