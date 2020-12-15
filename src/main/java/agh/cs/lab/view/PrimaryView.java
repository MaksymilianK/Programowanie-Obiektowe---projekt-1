package agh.cs.lab.view;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class PrimaryView implements View {

    private Scene scene;

    protected void setScene(Parent parent) {
        this.scene = new Scene(parent);
    }

    public Scene getScene() {
        return scene;
    }
}
