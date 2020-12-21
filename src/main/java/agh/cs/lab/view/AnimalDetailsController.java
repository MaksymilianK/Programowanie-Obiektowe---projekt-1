package agh.cs.lab.view;

import agh.cs.lab.element.animal.Gene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class AnimalDetailsController implements Controller {

    @FXML
    private Text gene;

    @FXML
    private TextField epoch;

    @FXML
    private Button startButton;

    @FXML
    private Text children;

    @FXML
    private Text descendants;

    @FXML
    private Text deathEpoch;

    @FXML
    private Button interruptButton;

    private int getEpoch() {
        int epoch = Integer.parseInt(this.epoch.getText());
        if (epoch < 1) {
            throw new NumberFormatException("An epoch cannot be a negative number");
        }
        return epoch;
    }

    public void onStart(Consumer<Integer> onStart) {
        startButton.setOnAction(event -> {
            try {
                onStart.accept(getEpoch());
            } catch (NumberFormatException e) {
                epoch.setText("Niepoprawna epoka");
            }
        });
    }

    public void setRunning(boolean isRunning) {
        if (epoch.getText().equals("")) {
            startButton.setDisable(isRunning);
        }
        interruptButton.setDisable(isRunning);
    }

    public void onInterrupt(Runnable onInterrupt) {
        this.interruptButton.setOnAction(event -> onInterrupt.run());
    }

    public void notifySelected(Gene gene) {
        this.gene.setText(gene.toString());
        epoch.setDisable(false);
        startButton.setDisable(false);
        interruptButton.setDisable(false);

        deathEpoch.setText("Zyje");
    }

    public void notifyTrackingStarted() {
        epoch.setDisable(true);
        startButton.setDisable(true);
        interruptButton.setDisable(false);

        children.setText("-");
        descendants.setText("-");
    }

    public void notifyDead(int epochDied) {
        deathEpoch.setText(Integer.toString(epochDied));
    }

    public void notifyTrackingInterrupted() {
        reset();
    }

    public void notifyTrackingFinished(int children, int descendants) {
        this.children.setText(Integer.toString(children));
        this.descendants.setText(Integer.toString(descendants));
        epoch.setText("");
        epoch.setDisable(false);
        startButton.setDisable(false);
    }

    @Override
    public void init() {
        reset();
    }

    @Override
    public void reset() {
        epoch.setDisable(true);
        startButton.setDisable(true);
        interruptButton.setDisable(true);

        gene.setText("-");
        epoch.setText("");
        children.setText("-");
        descendants.setText("-");
        deathEpoch.setText("-");
    }
}
