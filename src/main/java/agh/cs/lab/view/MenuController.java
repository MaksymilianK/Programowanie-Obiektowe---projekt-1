package agh.cs.lab.view;

import agh.cs.lab.engine.SimulationSettings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class MenuController extends PrimaryController {

    @FXML
    private Parent menu;

    @FXML
    private TextField mapWidth;

    @FXML
    private TextField mapHeight;

    @FXML
    private TextField startEnergy;

    @FXML
    private TextField moveEnergy;

    @FXML
    private TextField plantEnergy;

    @FXML
    private TextField jungleRatio;

    @FXML
    private Button singleSimulationButton;

    @FXML
    private Button doubleSimulationButton;

    private SimulationSettings defaultSettings;

    public void setDefault(SimulationSettings defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    public void onSingleSimulationChoice(Consumer<SimulationSettings> onChoice) {
        singleSimulationButton.setOnAction(event -> onChoice.accept(getNewSettings()));
    }

    public void onDoubleSimulationChoice(Consumer<SimulationSettings> onChoice) {
        doubleSimulationButton.setOnAction(event -> onChoice.accept(getNewSettings()));
    }

    @Override
    public void reset() {
        mapWidth.setText(Integer.toString(defaultSettings.getMapWidth()));
        mapHeight.setText(Integer.toString(defaultSettings.getMapHeight()));
        startEnergy.setText(Integer.toString(defaultSettings.getStartEnergy()));
        moveEnergy.setText(Integer.toString(defaultSettings.getMoveEnergy()));
        plantEnergy.setText(Integer.toString(defaultSettings.getPlantEnergy()));
        jungleRatio.setText(Float.toString(defaultSettings.getJungleRatio()));
    }

    @Override
    public void init() {
        reset();
        setScene(menu);
    }

    private SimulationSettings getNewSettings() {
         return new SimulationSettings(
                    Integer.parseInt(mapWidth.getText()),
                    Integer.parseInt(mapHeight.getText()),
                    Integer.parseInt(startEnergy.getText()),
                    Integer.parseInt(moveEnergy.getText()),
                    Integer.parseInt(plantEnergy.getText()),
                    Float.parseFloat(jungleRatio.getText())
         );
    }
}
