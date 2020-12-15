package agh.cs.lab.view;

import agh.cs.lab.element.animal.Gene;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimulationStatisticsView implements View {

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

    public void setLivingAnimals(int livingAnimals) {
        this.livingAnimals.setText(Integer.toString(livingAnimals));
    }

    public void setLivingPlants(int livingPlants) {
        this.livingPlants.setText(Integer.toString(livingPlants));
    }

    public void setMostCommonGenes(Set<Gene> mostCommonGenes) {
        var genes = mostCommonGenes.stream()
                .limit(3)
                .map(gene -> new Text(gene.toString()))
                .collect(Collectors.toSet());

        this.mostCommonGenes.getChildren().clear();
        this.mostCommonGenes.getChildren().addAll(genes);
    }

    public void setAverageEnergy(float averageEnergy) {
        this.averageEnergy.setText(Float.toString(averageEnergy));
    }

    public void setAverageLifeTime(float averageLifeTime) {
        this.averageLifeTime.setText(Float.toString(averageLifeTime));
    }

    public void setAverageChildren(float averageChildren) {
        this.averageChildren.setText(Float.toString(averageChildren));
    }

    @Override
    public void reset() {
        livingAnimals.setText("0");
        livingPlants.setText("0");
        mostCommonGenes.getChildren().clear();
        averageEnergy.setText("0");
        averageLifeTime.setText("0");
        averageChildren.setText("0");
    }

    @FXML
    private void initialize() {
        reset();
    }
}
