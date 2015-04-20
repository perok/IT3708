package gameworlds.tracker;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Per�yvind on 20/04/2015.
 */
public class Tracker {

    int currentTimestep = 0;

    Random random;


    int height = 30;
    int width = 15;

    int tileLeftPos;
    int tileLength;
    int tileHeightPos;

    int positive = 0;
    int negative = 0;

    int platformLeftPos;
    int platformLength = 5;

    public Tracker(){
        random = new Random();

        platformLeftPos = random.nextInt(width - platformLength);

    }

    public List<Double> getSensory(){
        List<Double> output = new LinkedList<>();
        for (int i = platformLeftPos; i < platformLeftPos + platformLength; i++) {
            if(i >= tileLeftPos && i <= tileLeftPos + tileLength)
                output.add(1.0);
            else
                output.add(0.0);
        }
        return output;
    }

    public void newStep(Movement movement){
        // Move platform
        switch (movement) {
            case LEFT:
                if (platformLeftPos > 0)
                    platformLeftPos--;
                break;
            case RIGHT:
                if (platformLeftPos + platformLength < width)
                    platformLength++;
        }

        // Move falling tile. If crash then register result and start again.
        tileHeightPos--;

        if (tileHeightPos < 1) {
            // Check if capture
            if(tileLeftPos >= platformLeftPos && (tileLeftPos + tileLength) <= (platformLeftPos + platformLength)){

                // Score if hit
                if(tileLength < 5){
                    positive++;
                } else {
                    negative++;
                }

            } else {
                // score if not hit
                if(tileLength < 5){
                    positive++;
                } else {
                    negative++;
                }
            }

            createNewTile();
        }


    }

    private void createNewTile(){
        tileLength = random.nextInt(6) + 1;
        tileLeftPos = random.nextInt(width - tileLength);
        tileHeightPos = height;
    }

    public double getStats(){
        return positive / (double)(positive + negative);
    }


    public enum Movement {
        LEFT, RIGHT
    }
}