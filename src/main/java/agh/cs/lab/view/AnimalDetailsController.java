package agh.cs.lab.view;

import agh.cs.lab.element.animal.Gene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.function.Consumer;

public class AnimalDetailsController implements Controller {

    @FXML
    private Text gene;

    @FXML
    private TextField epoch;

    @FXML
    private Button checkButton;

    @FXML
    private Text children;

    @FXML
    private Text descendants;

    @FXML
    private Text deathEpoch;

    @FXML
    private Button finishButton;

    public void onCheck(Consumer<Integer> onCheck) {
        checkButton.setOnAction(event -> {
            try {
                int epoch = Integer.parseInt(this.epoch.getText());
                if (epoch < 0) {
                    this.epoch.setText("!");
                } else {
                    onCheck.accept(epoch);
                }
            } catch (NumberFormatException e) {
                epoch.setText("!");
            }
        });
    }

    public void onFinish(Runnable onFinish) {
        finishButton.setOnAction(event -> onFinish.run());
    }

    public void makeActive(Gene gene) {
        this.gene.setText(gene.toString());

        checkButton.setDisable(false);
        finishButton.setDisable(false);
    }

    public void update(int children, int descendants) {
        if (children == -1) {
            this.children.setText("-");
        } else {
            this.children.setText(Integer.toString(children));
        }

        if (descendants == -1) {
            this.descendants.setText("-");
        } else {
            this.descendants.setText(Integer.toString(descendants));
        }
    }

    public void onAnimalDeath(int deathEpoch) {
        this.deathEpoch.setText(Integer.toString(deathEpoch));
    }

    @Override
    public void init() {
        reset();
    }

    @Override
    public void reset() {
        checkButton.setDisable(true);
        finishButton.setDisable(true);

        gene.setText("-");
        epoch.setText("-");
        children.setText("-");
        descendants.setText("-");
        deathEpoch.setText("-");
    }
}
