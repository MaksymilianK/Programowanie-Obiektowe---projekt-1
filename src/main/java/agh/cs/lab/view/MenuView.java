package agh.cs.lab.view;

import agh.cs.lab.engine.SimulationSettings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MenuView extends PrimaryView {

    private final Set<Consumer<SimulationSettings>> onSingleSimulationChoice = new HashSet<>();
    private final Set<Consumer<SimulationSettings>> onDoubleSimulationChoice = new HashSet<>();

    @FXML
    private Parent form;

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
        onSingleSimulationChoice.add(onChoice);
    }

    public void onDoubleSimulationChoice(Consumer<SimulationSettings> onChoice) {
        onDoubleSimulationChoice.add(onChoice);
    }

    public Parent getForm() {
        return form;
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

    @FXML
    private void initialize() {
        setScene(form);

        singleSimulationButton.setOnAction(event ->
                onSingleSimulationChoice.forEach(callback -> callback.accept(getNewSettings())));

        doubleSimulationButton.setOnAction(event ->
                onDoubleSimulationChoice.forEach(callback -> callback.accept(getNewSettings())));
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
