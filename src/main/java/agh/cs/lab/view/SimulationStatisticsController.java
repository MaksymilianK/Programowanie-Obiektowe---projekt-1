package agh.cs.lab.view;

import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.shared.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class SimulationStatisticsController implements Controller {

    public static final int MOST_COMMON_GENES_MAX = 3;

    @FXML
    private Text currentEpoch;

    @FXML
    private Text livingAnimals;

    @FXML
    private Text livingPlants;

    @FXML
    private VBox mostCommonGenes;

    @FXML
    private Text averageEnergy;

    @FXML
    private Text averageLifeTime;

    @FXML
    private Text averageChildren;

    @FXML
    private Button mostCommonGenesButton;

    @FXML
    private Button saveButton;

    public void setCurrentEpoch(int currentEpoch) {
        this.currentEpoch.setText(Integer.toString(currentEpoch));
    }

    public void onMostCommonGenes(Runnable onMostCommonGenes) {
        mostCommonGenesButton.setOnAction(event -> onMostCommonGenes.run());
    }

    public void onSave(Runnable onSave) {
        saveButton.setOnAction(event -> onSave.run());
    }

    public void setLivingAnimals(int livingAnimals) {
        this.livingAnimals.setText(Integer.toString(livingAnimals));
    }

    public void setLivingPlants(int livingPlants) {
        this.livingPlants.setText(Integer.toString(livingPlants));
    }

    public void setMostCommonGenes(List<Pair<Gene, Integer>> mostCommonGenes) {
        this.mostCommonGenes.getChildren().clear();

        mostCommonGenes.forEach(gene -> {
            var geneDisplay = new HBox();
            geneDisplay.setSpacing(20);
            geneDisplay.getChildren().add(new Label(gene.first.toString()));
            geneDisplay.getChildren().add(new Text(gene.second.toString()));
            this.mostCommonGenes.getChildren().add(geneDisplay);
        });
    }

    public void setAverageEnergy(double averageEnergy) {
        this.averageEnergy.setText(Double.toString(averageEnergy));
    }

    public void setAverageLifeTime(double averageLifeTime) {
        this.averageLifeTime.setText(Double.toString(averageLifeTime));
    }

    public void setAverageChildren(double averageChildren) {
        this.averageChildren.setText(Double.toString(averageChildren));
    }

    public void setRunning(boolean isRunning) {
        mostCommonGenesButton.setDisable(isRunning);
        saveButton.setDisable(isRunning);
    }

    @Override
    public void reset() {
        setRunning(false);

        currentEpoch.setText("0");
        livingAnimals.setText("0");
        livingPlants.setText("0");
        mostCommonGenes.getChildren().clear();
        averageEnergy.setText("0");
        averageLifeTime.setText("0");
        averageChildren.setText("0");
    }

    @Override
    public void init() {
        reset();
    }
}
