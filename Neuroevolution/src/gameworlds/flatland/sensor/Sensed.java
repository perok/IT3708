package gameworlds.flatland.sensor;

/**
 * Created by PerØyvind on 03/04/2015.
 */
public class Sensed {
    public Sides side;
    public Items item;
    public Sensed(Sides side, Items item){
        this.side = side;
        this.item = item;
    }
}
