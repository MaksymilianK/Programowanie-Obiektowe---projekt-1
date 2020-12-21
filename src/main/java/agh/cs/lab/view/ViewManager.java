package agh.cs.lab.view;

import agh.cs.lab.engine.SimulationSettings;
import agh.cs.lab.shared.Trio;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {

    private final Stage stage;
    private final MenuController menuController;
    private final SingleMapAppController singleMapController;
    private final DoubleMapAppController doubleMapController;

    private PrimaryController currentView;

    private ViewManager(Stage stage, MenuController menuController, SingleMapAppController singleMapController,
                        DoubleMapAppController doubleMapController) {
        this.stage = stage;
        this.menuController = menuController;
        this.singleMapController = singleMapController;
        this.doubleMapController = doubleMapController;
    }

    public void showMenu() {
        showScene(menuController);
    }

    public void showSingleMapApp() {
        showScene(singleMapController);
    }

    public void showDoubleMapApp() {
        showScene(doubleMapController);
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

    private void showScene(PrimaryController controller) {
        currentView.reset();
        stage.setScene(controller.getScene());
        currentView = controller;
    }

    public static ViewManager load(Stage stage, SimulationSettings settings) throws IOException {
        var controllers = loadFiles(settings);
        var manager = new ViewManager(stage, controllers.first, controllers.second, controllers.third);
        initStage(stage);

        stage.setScene(controllers.first.getScene());
        manager.currentView = controllers.first;

        return manager;
    }

    private static Trio<MenuController, SingleMapAppController, DoubleMapAppController>
            loadFiles(SimulationSettings settings) throws IOException {
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

        return new Trio<>(menu, singleMapApp, doubleMapApp);
    }

    private static void initStage(Stage stage) {
        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(false);
        stage.show();
    }
}
