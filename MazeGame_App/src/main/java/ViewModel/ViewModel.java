package ViewModel;

import Model.Character;
import Model.Character.CHARACTER_MOVEMENT;
import Model.Game;
import Model.IModel;
import View.DisplayableMaze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

    private IModel model;

    public ViewModel(IModel model) {
        this.model = model;
        Input.initCharacterMovement(model);
        Input.setKeyEvent(KeyCode.ESCAPE, this::togglePause);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Game.GAMESTATE || arg instanceof DisplayableMaze || arg instanceof CHARACTER_MOVEMENT) {
            setChanged();
            notifyObservers(arg);
        }
    }

    public void startGame(Character.CHARACTER_TYPE type, int width, int height) {
        this.model.start(type, width, height);
    }

    public void keyPressed(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        Input.parseKeyboardInput(code);
    }

    public void keyReleased(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (Input.isValidCharacterInput(code)) this.model.moveCharacter(CHARACTER_MOVEMENT.IDLE);
    }

    public void mousePressed(MouseEvent mouseEvent, double characterX, double characterY) {
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY))
            this.model.moveCharacter(Input.parseMouseInput(mouseEvent.getSceneX(), mouseEvent.getSceneY(), characterX, characterY));
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) model.moveCharacter(CHARACTER_MOVEMENT.IDLE);
    }

    public double getCharacterX() { return model.getCharacterX(); }

    public double getCharacterY() { return model.getCharacterY(); }

    public Position getCharacterPosition() { return model.getCharacterPosition(); }

    public int getCharacterFrame() { return model.getCharacterFrame(); }

    public void togglePause() {
        this.model.togglePause();
    }

    public void closeGame() {
        this.model.close();
    }

    public void exitGame() {
        this.model.exit();
    }

    public void saveGame(String saveName) { this.model.saveGame(saveName); }

    public void loadGame(String save) { this.model.loadGame(save); }
}
