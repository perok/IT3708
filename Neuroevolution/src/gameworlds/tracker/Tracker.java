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

    boolean hasPositiveTurn = false;

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

            int tileRightPos = tileLeftPos + tileLength - 1;
            int platformRightPos = platformLeftPos + platformLength - 1;

            boolean isTileLeftPosInside = tileLeftPos >= platformLeftPos && tileLeftPos <= platformRightPos;
            boolean isTileRightPosInside = tileRightPos >= platformLeftPos && tileRightPos <= platformRightPos;

            // Check if tile fully contained by platform
            if(isTileLeftPosInside && isTileRightPosInside){
                System.out.println("INSIDE: YES");
                if(isSmallTile()) {
                    if (tileLeftPos >= platformLeftPos && tileRightPos <= platformLeftPos + platformLength) {
                        System.out.println("AWARD");
                        positive += 1;
                        hasPositiveTurn = true;
                    }
                } else {
                    System.out.println("PENALTY");
                    // If touching: Always give a penalty on large tiles
                    negative += 1;
                    hasPositiveTurn = false;
                }

            } else if(isTileLeftPosInside || isTileRightPosInside) {
                System.out.println("INSIDE: HALF");
                // Else if part of tile is inside platform

                    negative += 5;
                    hasPositiveTurn = false;
            } else {
                // Else: if not inside at all
                if(isSmallTile()){
                    System.out.println("PENALTY");
                    negative += 1;
                    hasPositiveTurn = false;
                } else {
                    positive += 1;
                    hasPositiveTurn = true;
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

    public boolean isAtBottom() { return getTileHeightPos() < 1; }

    public boolean hasPositiveTurn() { return hasPositiveTurn;}
}
