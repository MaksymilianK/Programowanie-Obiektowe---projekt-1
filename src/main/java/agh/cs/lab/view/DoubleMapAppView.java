package agh.cs.lab.view;

import javafx.fxml.FXML;
import javafx.scene.Parent;

public class DoubleMapAppView extends PrimaryView {

    @FXML
    private Parent appView;

    @FXML
    private WorldMapView firstMapView;

    @FXML
    private SimulationControlView firstControlView;

    @FXML
    private SimulationStatisticsView firstStatisticsView;

    @FXML
    private AnimalDetailsView firstAnimalDetailsView;

    @FXML
    private WorldMapView secondMapView;

    @FXML
    private SimulationControlView secondControlView;

    @FXML
    private SimulationStatisticsView secondStatisticsView;

    @FXML
    private AnimalDetailsView secondAnimalDetailsView;

    public WorldMapView getFirstMapView() {
        return firstMapView;
    }

    public SimulationControlView getFirstControlView() {
        return firstControlView;
    }

    public SimulationStatisticsView getFirstStatisticsView() {
        return firstStatisticsView;
    }

    public AnimalDetailsView getFirstAnimalDetailsView() {
        return firstAnimalDetailsView;
    }

    public WorldMapView getSecondMapView() {
        return secondMapView;
    }

    public SimulationControlView getSecondControlView() {
        return secondControlView;
    }

    public SimulationStatisticsView getSecondStatisticsView() {
        return secondStatisticsView;
    }

    public AnimalDetailsView getSecondAnimalDetailsView() {
        return secondAnimalDetailsView;
    }

    @Override
    public void reset() {
        firstMapView.reset();
        firstControlView.reset();
        firstStatisticsView.reset();
        firstAnimalDetailsView.reset();

        secondMapView.reset();
        secondControlView.reset();
        secondStatisticsView.reset();
        secondAnimalDetailsView.reset();
    }
}
