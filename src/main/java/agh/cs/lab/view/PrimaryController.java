package agh.cs.lab.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;

public abstract class PrimaryController implements Controller {

    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    protected void setScene(Parent parent) {
        this.scene = new Scene(parent);
    }
}
