package gameworlds.tracker;

import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Per�yvind on 20/04/2015.
 */
public class Tracker {

    int currentTimestep = 0;
    int createTiles = 0;
    int createdPositiveTiles = 0;


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

    GameType gameType = GameType.NORMAL;
    Color background = Color.WHITE;

    public Tracker(GameType gameType){
        this.gameType = gameType;

        random = new Random();

        platformLeftPos = random.nextInt(width - platformLength);

    }

    public List<Double> getSensory(){
        List<Double> output = new LinkedList<>();

        if(gameType.equals(GameType.NOWRAP)) {
            if(platformLeftPos == 0)
                output.add(1.0);
            else
                output.add(0.0);
        }

        for (int i = platformLeftPos; i < platformLeftPos + platformLength; i++) {

            int tileUpperLimit = tileLeftPos + tileLength;

            if(i >= tileLeftPos && i <= tileUpperLimit)
                output.add(1.0);
            else
                output.add(0.0);
        }

        if(gameType.equals(GameType.NOWRAP)) {
            if(platformLeftPos + platformLength == width)
                output.add(0.0);
            else
                output.add(1.0);
        }

        return output;
    }

    public void newStep(Movement movement){
        currentTimestep++;

        // Move platform
        switch (movement) {
            case LEFT:
                if((platformLeftPos > 0) || (!gameType.equals(GameType.NOWRAP)))
                    platformLeftPos--;
                break;
            case RIGHT:
                if((platformLeftPos + platformLength < width) || (!gameType.equals(GameType.NOWRAP)))
                    platformLeftPos++;
                break;
        }

        platformLeftPos = (((platformLeftPos % width) + width) % width);

        // Move falling tile. If crash then register result and start again.
        tileHeightPos--;

        if (tileHeightPos < 1) {
            // Check if touching platform
            if((tileLeftPos >= platformLeftPos && tileLeftPos <= (platformLeftPos + platformLength))){

                // if tile is fully contained by the platform
                if(tileLeftPos + tileLength <= platformLeftPos + platformLength) {

                    // Score based on tile size
                    if(isSmallTile()){
                        positive++;
                    } else {
                        negative++;
                    }
                }

            } else if(!(tileLeftPos + tileLength > platformLeftPos)) {
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

    private void createNewTile() {
        createTiles++;
        tileLength = random.nextInt(6) + 1;
        if(tileLength < 5)
            createdPositiveTiles++;

        if (!gameType.equals(GameType.NOWRAP)) {
            tileLeftPos = random.nextInt(width);
            tileLeftPos = (((tileLeftPos % width) + width) % width);
        } else {
            tileLeftPos = random.nextInt(width - tileLength);
        }

        tileHeightPos = height;
    }

    public double getStats(){
        return Math.max(positive - Math.pow(negative, 2), 0) / (double)createdPositiveTiles;
    }

    public boolean isSmallTile() {
        return tileLength < 5;
    }

    public enum Movement {
        LEFT, RIGHT, PULLDOWN
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

    public enum GameType {
        NORMAL, NOWRAP, PULLDOWN
    }
}
