package gameworlds.tracker;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Per�yvind on 20/04/2015.
 */
public class Tracker {

    int currentTimestep = 0;
    int createTiles = 0;

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
        currentTimestep++;

        // Move platform
        switch (movement) {
            case LEFT:
                if (platformLeftPos > 0)
                    platformLeftPos--;
                break;
            case RIGHT:
                if (platformLeftPos + platformLength < width)
                    platformLeftPos++;
        }

        // Move falling tile. If crash then register result and start again.
        tileHeightPos--;

        if (tileHeightPos < 1) {
            // Check if touching platform
            if(tileLeftPos >= platformLeftPos && tileLeftPos <= (platformLeftPos + platformLength)){

                // if tile is fully contained by the platform
                if(tileLeftPos + tileLength <= platformLeftPos + platformLength) {

                    // Score based on tile size
                    if(isSmallTile()){
                        positive++;
                    } else {
                        negative++;
                    }
                } else {
                    // Else: if not fully contained by platform
                    if(isSmallTile()){
                        negative++;
                    } else {
                        positive++;
                    }
                }

            } else {
                // Else: if not inside at all
                if(isSmallTile()){
                    negative++;
                } else {
                    positive++;
                }
            }

            createNewTile();
        }


    }

    private void createNewTile(){
        createTiles++;
        tileLength = random.nextInt(6) + 1;
        tileLeftPos = random.nextInt(width - tileLength);
        tileHeightPos = height;
    }

    public double getStats(){
        return (positive - negative) / (double)createTiles;
    }

    public boolean isSmallTile() {
        return tileLength < 5;
    }

    public enum Movement {
        LEFT, RIGHT
    }

    public int getTileLeftPos() {
        return tileLeftPos;
    }

    public int getTileLength() {
        return tileLength;
    }

    public int getTileHeightPos() {
        return tileHeightPos;
    }

    public int getPlatformLeftPos() {
        return platformLeftPos;
    }

    public int getPlatformLength() {
        return platformLength;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentTimestep() {
        return currentTimestep;
    }
}
