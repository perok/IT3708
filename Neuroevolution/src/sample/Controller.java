package sample;

import algorithms.ea.Evolution;
import algorithms.ea.statistics.GenerationStatistics;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {

    @FXML private LineChart<Number, Number> lineChart;
    @FXML private NumberAxis generationAxis;
    @FXML private NumberAxis fitnessAxis;

    @FXML protected void onHandleStartOneMaxClick(ActionEvent event) {

        Evolution a = ProblemCreatorHelper.problem1();
        //Evolution a = ProblemCreatorHelper.problem3(10, 26, 1, true);
        run(a);
    }
    @FXML protected void onHandleStartLOLZ(ActionEvent event) {

        Evolution a = ProblemCreatorHelper.problem2();
        //Evolution a = ProblemCreatorHelper.problem3(10, 26, 1, true);
        run(a);
    }
    @FXML protected void onHandleStartLocal(ActionEvent event) {

        Evolution a = ProblemCreatorHelper.problem3(20, 50, 1, false);
        //Evolution a = ProblemCreatorHelper.problem3(10, 26, 1, true);
        run(a);
    }
    @FXML protected void onHandleStartGlobal(ActionEvent event) {

        Evolution a = ProblemCreatorHelper.problem3(20, 50, 1, true);
        //Evolution a = ProblemCreatorHelper.problem3(10, 26, 1, true);
        run(a);
    }


    private void run(Evolution evolution) {

        evolution.setNUMBER_OF_ITERATIONS(1000);
        evolution.setCHIlDREN_POOL_SIZE(80);
        evolution.setPARENT_POOL_SIZE(40);



        final LongProperty lastUpdate = new SimpleLongProperty();
        final long minUpdateInterval = 0; // nanoseconds.

        BlockingQueue<GenerationStatistics> queue = new LinkedBlockingQueue<>();
        evolution.setDataListener(queue);

        lineChart.getData().remove(0, lineChart.getData().size());


        //defining a series
        XYChart.Series<Number, Number> seriesMean = new XYChart.Series<>();
        seriesMean.setName("Mean");
        XYChart.Series<Number, Number> seriesSD = new XYChart.Series<>();
        seriesSD.setName("Standard deviation");
        XYChart.Series<Number, Number> seriesFittest = new XYChart.Series<>();
        seriesFittest.setName("Fittest");

        lineChart.getData().add(seriesMean);
        lineChart.getData().add(seriesSD);
        lineChart.getData().add(seriesFittest);

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

        new Thread(evolution).start();
        timer.start();
    }
}
