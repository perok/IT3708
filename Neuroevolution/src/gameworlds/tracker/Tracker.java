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

    boolean wrapAround = false;

    public Tracker(){
        random = new Random();

        platformLeftPos = random.nextInt(width - platformLength);

    }

    public List<Double> getSensory(){
        List<Double> output = new LinkedList<>();
        for (int i = platformLeftPos; i < platformLeftPos + platformLength; i++) {

            int tileUpperLimit = tileLeftPos + tileLength;
            int i_tmp = i;

            if(wrapAround) {
                i_tmp = (((i % width) + width) % width);
                tileUpperLimit = (((tileUpperLimit % width) + width) % width);
            }

            if(i_tmp >= tileLeftPos && i_tmp <= tileUpperLimit)
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
                if(wrapAround || (platformLeftPos > 0))
                    platformLeftPos--;
                break;
            case RIGHT:
                if(wrapAround || (platformLeftPos + platformLength < width))
                    platformLeftPos++;
                break;
        }
        if(wrapAround)
            platformLeftPos = (((platformLeftPos % width) + width) % width);

        // Move falling tile. If crash then register result and start again.
        tileHeightPos--;

        if (tileHeightPos < 1) {

            int tileRightPos = tileLeftPos + tileLength - 1;
            int platformRightPos = platformLeftPos + platformLength - 1;

            boolean isTileLeftPosInside = tileLeftPos >= platformLeftPos && tileLeftPos <= platformRightPos;
            boolean isTileRightPosInside = tileRightPos >= platformLeftPos && tileRightPos <= platformRightPos;

            // Check if tile fully contained by platform
            if(isTileLeftPosInside && isTileRightPosInside){
                if(isSmallTile()) {
                    if (tileLeftPos >= platformLeftPos && tileRightPos <= platformLeftPos + platformLength) {
                        positive += 1;
                    }
                } else {
                    // If touching: Always give a penalty on large tiles
                    negative += 1;
                }

            } else if(isTileLeftPosInside || isTileRightPosInside) {
                // Else if part of tile is inside platform

                    negative += 5;

            } else {
                // Else: if not inside at all
                if(isSmallTile()){
                    negative += 1;
                } else {
                    positive += 1;
                }
            }

            createNewTile();
        }


    }

    private void createNewTile() {
        createTiles++;
        tileLength = random.nextInt(6) + 1;

        if (wrapAround) {
            tileLeftPos = random.nextInt(width);
            tileLeftPos = (((tileLeftPos % width) + width) % width);
        } else {
            tileLeftPos = random.nextInt(width - tileLength);
        }

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

    public boolean isWrapAround() { return wrapAround; }

    public void setWrapAround(boolean wrapAround) { this.wrapAround = wrapAround; }
}
