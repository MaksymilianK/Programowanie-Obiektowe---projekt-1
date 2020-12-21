package agh.cs.lab.view;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class SingleMapAppController extends PrimaryController {

    @FXML
    private Parent app;

    @FXML
    private ScrollPane mapContainer;

    @FXML
    private MapController mapController;

    @FXML
    private Button menuButton;

    @FXML
    private SimulationControlController controlController;

    @FXML
    private SimulationStatisticsController statisticsController;

    @FXML
    private AnimalDetailsController animalDetailsController;

    public void onMenu(Runnable onMenu) {
        menuButton.setOnAction(event -> onMenu.run());
    }

    public MapController getMapController() {
        return mapController;
    }

    public SimulationControlController getControlController() {
        return controlController;
    }

    public SimulationStatisticsController getStatisticsController() {
        return statisticsController;
    }

    public AnimalDetailsController getAnimalDetailsController() {
        return animalDetailsController;
    }

    public void setMapSize() {
        mapController.setViewSize(mapContainer.getWidth(), mapContainer.getHeight());
    }

    @Override
    public void reset() {
        mapController.reset();
        controlController.reset();
        statisticsController.reset();
        animalDetailsController.reset();
    }

    @Override
    public void init() {
        setScene(app);

        mapController.init();
        controlController.init();
        statisticsController.init();
        animalDetailsController.init();
    }
}
