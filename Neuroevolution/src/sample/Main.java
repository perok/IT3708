package sample;

import algorithms.ea.Evolution;
import algorithms.ea.statistics.GenerationStatistics;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main extends Application {

    LineChart<Number,Number> lineChart;

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("cool.fxml"));
//        primaryStage.setTitle("Evolution!");
//        primaryStage.setScene(new Scene(root, 800, 600));
//        primaryStage.show();
//
//
////        EvolutionaryA a = new EvolutionaryA();
////        a.setPhenotypeBuilder(new OneToOnePhenotypeBuilder());
////        a.setFitnessFunction(new OneToOneFitness());
////        a.setMatingStrategy(Matings.FITNESS_PROPORTIONATE);
////        a.setAdultSelectionsStrategy(Selections.FULL);
////        a.setMutator(new MaybeMutator(0.1));
////        a.setCrossover(new OneCrossover());
////        a.makePopulationOf("101101", 20);
////
////        a.loop();
//    }

    BlockingQueue<GenerationStatistics> queue = new LinkedBlockingQueue<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Genetic algorithm!");

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Generation");
        //creating the chart
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);

        lineChart.setTitle("Fitness value");
        //defining a series
        XYChart.Series<Number, Number> seriesMean = new XYChart.Series<>();
        seriesMean.setName("Mean");
        XYChart.Series<Number, Number> seriesSD = new XYChart.Series<>();
        seriesSD.setName("Standard deviation");
        XYChart.Series<Number, Number> seriesFittest = new XYChart.Series<>();
        seriesFittest.setName("Fittest");
        //populating the series with data

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(seriesMean);
        lineChart.getData().add(seriesSD);
        lineChart.getData().add(seriesFittest);

        stage.setScene(scene);
        stage.show();

        Evolution a = ProblemCreatorHelper.problem3(32, 770, 1, false);
        //Evolution a = ProblemCreatorHelper.problem3(37, 91, 1, true);

        //Evolution a = ProblemCreatorHelper.problem3(10, 26, 1, true);

        a.setDataListener(queue);

        final LongProperty lastUpdate = new SimpleLongProperty();
        final long minUpdateInterval = 0; // nanoseconds.
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now - lastUpdate.get() > minUpdateInterval) {
                    final GenerationStatistics gs = queue.poll();
                    if (gs != null) {
                        seriesMean.getData().add(new XYChart.Data<>(gs.generation, gs.mean));
                        seriesSD.getData().add(new XYChart.Data<>(gs.generation, gs.SD));
                        seriesFittest.getData().add(new XYChart.Data<>(gs.generation, gs.bestIndividual.getFitness()));

                    }
                    lastUpdate.set(now);
                }
            }

        };

        new Thread(a).start();
        timer.start();

    }

    public static void main(String[] args) {

        //assert ProblemCreatorHelper.isGloballySurprising("ABCCBA") == true;
        //assert ProblemCreatorHelper.isGloballySurprising("AABCC") == false;
        //assert ProblemCreatorHelper.isGloballySurprising("ABBACCA") == false;

        launch(args);
    }


}
