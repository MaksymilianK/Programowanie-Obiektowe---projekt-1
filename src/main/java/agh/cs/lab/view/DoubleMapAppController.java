package agh.cs.lab.view;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class DoubleMapAppController extends PrimaryController {

    @FXML
    private Parent app;

    @FXML
    private ScrollPane firstMapContainer;

    @FXML
    private ScrollPane secondMapContainer;

    @FXML
    private MapController firstMapController;

    @FXML
    private SimulationControlController firstControlController;

    @FXML
    private SimulationStatisticsController firstStatisticsController;

    @FXML
    private AnimalTrackerController firstAnimalDetailsController;

    @FXML
    private Button menuButton;

    @FXML
    private MapController secondMapController;

    @FXML
    private SimulationControlController secondControlController;

    @FXML
    private SimulationStatisticsController secondStatisticsController;

    @FXML
    private AnimalTrackerController secondAnimalDetailsController;

    public void onMenu(Runnable onMenu) {
        menuButton.setOnAction(event -> onMenu.run());
    }

    public MapController getFirstMapController() {
        return firstMapController;
    }

    public SimulationControlController getFirstControlController() {
        return firstControlController;
    }

    public SimulationStatisticsController getFirstStatisticsController() {
        return firstStatisticsController;
    }

    public AnimalTrackerController getFirstAnimalDetailsController() {
        return firstAnimalDetailsController;
    }

    public MapController getSecondMapController() {
        return secondMapController;
    }

    public SimulationControlController getSecondControlController() {
        return secondControlController;
    }

    public SimulationStatisticsController getSecondStatisticsController() {
        return secondStatisticsController;
    }

    public AnimalTrackerController getSecondAnimalDetailsController() {
        return secondAnimalDetailsController;
    }

    @Override
    public void reset() {
        firstMapController.reset();
        firstControlController.reset();
        firstStatisticsController.reset();
        firstAnimalDetailsController.reset();

        secondMapController.reset();
        secondControlController.reset();
        secondStatisticsController.reset();
        secondAnimalDetailsController.reset();
    }

    @Override
    public void init() {
        setScene(app);

        firstMapController.init();
        firstControlController.init();
        firstStatisticsController.init();
        firstAnimalDetailsController.init();

        secondMapController.init();
        secondControlController.init();
        secondStatisticsController.init();
        secondAnimalDetailsController.init();
    }

    public void setMapsSize() {
        firstMapController.setViewSize(firstMapContainer.getWidth(), firstMapContainer.getHeight());
        secondMapController.setViewSize(secondMapContainer.getWidth(), secondMapContainer.getHeight());
    }
}
