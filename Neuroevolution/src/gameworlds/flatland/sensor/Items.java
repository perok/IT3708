package gameworlds.flatland.sensor;

/**
 * Created by Perÿyvind on 03/04/2015.
 */
public enum Items {

    FOOD(1), POISON(-1), NOTHING(0);

    public int value;

    Items(int value){
        this.value = value;
    }
}
