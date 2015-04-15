package project3;

import algorithms.ea.individual.Individual;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Perÿyvind on 13/04/2015.
 */
public class gui extends Application{

    Image food;
    Image poisong;
    Image robot;

    GraphicsContext gc;

    int width = 400;
    int height = 400;



    List<Individual> population;

    public static void main(String[] args) {
        launch(args);
        Double.doubleToRawLongBits()
    }

    @Override
    public void start(Stage stage) {
        // ================================================
        // Create AI
        // ================================================

        population = createPopulation();

        // ================================================
        // Setup GUI
        // ================================================

        stage.setTitle("Flatland - EANN");

        Group root = new Group();

        final Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        redraw();

        Scene scene = new Scene(root, 800, 800, Color.BLACK);

        stage.setScene(scene);
        stage.show();

        // ================================================
        // Start AI
        // ================================================
    }


    private void redraw() {
        gc.clearRect(0, 0, width, height);

        /*for (int y = 0; y <; y++) {
            for (int x = 0; x < ; x++) {

            }
        }*/

    }


    private List<Individual> createPopulation(){
        Double.
        return null;
    }
}
