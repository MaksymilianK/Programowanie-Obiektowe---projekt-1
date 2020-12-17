package agh.cs.lab.view;

import agh.cs.lab.element.animal.Gene;
import agh.cs.lab.shared.Pair;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class SimulationStatisticsController implements Controller {

    public static final int MOST_COMMON_GENES_MAX = 3;

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

    @Override
    public void init() {
        reset();
    }
}
