package agh.cs.lab.view;

import javafx.fxml.FXML;
import javafx.scene.Parent;

public class SingleMapAppView extends PrimaryView {

    @FXML
    private Parent appView;

    @FXML
    private WorldMapView mapView;

    @FXML
    private SimulationControlView controlView;

    @FXML
    private SimulationStatisticsView statisticsView;

    @FXML
    private AnimalDetailsView animalDetailsView;

    public WorldMapView getMapView() {
        return mapView;
    }

    public SimulationControlView getControlView() {
        return controlView;
    }

    public SimulationStatisticsView getStatisticsView() {
        return statisticsView;
    }

    public AnimalDetailsView getAnimalDetailsView() {
        return animalDetailsView;
    }

    @Override
    public void reset() {
        mapView.reset();
        controlView.reset();
        statisticsView.reset();
        animalDetailsView.reset();
    }

    @FXML
    private void initialize() {
        setScene(appView);
    }
}
