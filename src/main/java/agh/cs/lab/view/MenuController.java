package agh.cs.lab.view;

import agh.cs.lab.engine.SimulationSettings;
import com.sun.scenario.Settings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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

    @FXML
    private Text error;

    private SimulationSettings defaultSettings;

    public void setDefault(SimulationSettings defaultSettings) {
        this.defaultSettings = defaultSettings;
    }

    public void onSingleSimulationChoice(Consumer<SimulationSettings> onChoice) {
        singleSimulationButton.setOnAction(event -> this.onChoice(onChoice));
    }

    public void onDoubleSimulationChoice(Consumer<SimulationSettings> onChoice) {
        doubleSimulationButton.setOnAction(event -> this.onChoice(onChoice));
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

    private void onChoice(Consumer<SimulationSettings> onChoice) {
        try {
            onChoice.accept(getNewSettings());
            error.setText("");
        } catch (NumberFormatException e) {
            error.setText("Niepoprawne wartosci");
        }
    }

    private SimulationSettings getNewSettings() {
        int mapWidth = Integer.parseInt(this.mapWidth.getText());
        int mapHeight = Integer.parseInt(this.mapHeight.getText());
        int startEnergy = Integer.parseInt(this.startEnergy.getText());
        int moveEnergy = Integer.parseInt(this.moveEnergy.getText());
        int plantEnergy = Integer.parseInt(this.plantEnergy.getText());
        float jungleRatio = Float.parseFloat(this.jungleRatio.getText());

        if (mapWidth <= 0) {
            throw new NumberFormatException("Map width must be a positive number");
        }
        if (mapHeight <= 0) {
            throw new NumberFormatException("Map height must be a positive number");
        }
        if (startEnergy <= 0) {
            throw new NumberFormatException("Start energy must be a positive number");
        }
        if (moveEnergy <= 0) {
            throw new NumberFormatException("Move energy must be a positive number");
        }
        if (plantEnergy <= 0) {
            throw new NumberFormatException("Plant energy must be a positive number");
        }
        if (jungleRatio < 0.0f || jungleRatio > 1.0f) {
            throw new NumberFormatException("Jungle ratio must be between 0.0 and 1.0 energy must be a positive number");
        }

        return new SimulationSettings(mapWidth, mapHeight, startEnergy, moveEnergy, plantEnergy, jungleRatio);
    }
}
