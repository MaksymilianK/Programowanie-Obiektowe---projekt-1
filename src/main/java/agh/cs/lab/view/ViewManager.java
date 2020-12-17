package agh.cs.lab.view;

import agh.cs.lab.engine.SimulationSettings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ViewManager {

    private final Stage stage;
    private final MenuController menuController;
    private final SingleMapAppController singleMapController;
    private final DoubleMapAppController doubleMapController;

    private ViewManager(Stage stage, MenuController menuController, SingleMapAppController singleMapController,
                        DoubleMapAppController doubleMapController) {
        this.stage = stage;
        this.menuController = menuController;
        this.singleMapController = singleMapController;
        this.doubleMapController = doubleMapController;
    }

    public void showMenu() {
        stage.setScene(menuController.getScene());
    }

    public void showSingleMapApp() {
        stage.setScene(singleMapController.getScene());
    }

    public void showDoubleMapApp() {
        stage.setScene(doubleMapController.getScene());
    }

    public MenuController getMenu() {
        return menuController;
    }

    public SingleMapAppController getSingleMapController() {
        return singleMapController;
    }

    public DoubleMapAppController getDoubleMapController() {
        return doubleMapController;
    }

    public void show() {
        stage.show();
    }

    public static ViewManager load(Stage stage, SimulationSettings settings) throws IOException {
        var loader = new FXMLLoader(ViewManager.class.getResource("/menu.fxml"));
        loader.load();
        var menu = loader.<MenuController>getController();
        menu.setDefault(settings);
        menu.init();

        loader = new FXMLLoader(ViewManager.class.getResource("/single-map-app.fxml"));
        loader.load();
        var singleMapApp = loader.<SingleMapAppController>getController();
        singleMapApp.init();

        loader = new FXMLLoader(ViewManager.class.getResource("/double-map-app.fxml"));
        loader.load();
        var doubleMapApp = loader.<DoubleMapAppController>getController();
        doubleMapApp.init();

        var manager = new ViewManager(stage, menu, singleMapApp, doubleMapApp);

        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(false);

        return manager;
    }
}
