package agh.cs.lab.view;

import agh.cs.lab.engine.SimulationSettings;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {

    private final Stage stage;
    private final MenuView menu;
    private final SingleMapAppView singleMapView;
    private final DoubleMapAppView doubleMapView;

    private View currentView;

    private ViewManager(Stage stage, MenuView menu, SingleMapAppView singleMapView, DoubleMapAppView doubleMapView) {
        this.stage = stage;
        this.menu = menu;
        this.singleMapView = singleMapView;
        this.doubleMapView = doubleMapView;
    }

    public void showMenu() {
        stage.setScene(menu.getScene());
        this.currentView = menu;
    }

    public void showSingleMapApp(SimulationSettings settings) {
        stage.setScene(singleMapView.getScene());
        this.currentView = singleMapView;
    }

    public void showDoubleMapApp(SimulationSettings settings) {
        stage.setScene(doubleMapView.getScene());
        this.currentView = doubleMapView;
    }

    public MenuView getMenu() {
        return menu;
    }

    public SingleMapAppView getSingleMapView() {
        return singleMapView;
    }

    public DoubleMapAppView getDoubleMapView() {
        return doubleMapView;
    }

    public static ViewManager load(Stage stage, SimulationSettings settings) throws IOException {
        var loader = new FXMLLoader(ViewManager.class.getResource("/menu.fxml"));
        loader.load();
        var menu = loader.<MenuView>getController();
        menu.setDefault(settings);

        loader = new FXMLLoader(ViewManager.class.getResource("/single-map-app.fxml"));
        loader.load();
        var singleMapApp = loader.<SingleMapAppView>getController();

        loader = new FXMLLoader(ViewManager.class.getResource("/double-map-app.fxml"));
        loader.load();
        var doubleMapApp = loader.<DoubleMapAppView>getController();

        var manager = new ViewManager(stage, menu, singleMapApp, doubleMapApp);

        stage.setMaximized(true);
        stage.show();

        return manager;
    }
}
