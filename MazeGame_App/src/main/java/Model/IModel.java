package Model;

import Model.Character.CHARACTER_MOVEMENT;
import Model.Character.CHARACTER_TYPE;
import algorithms.mazeGenerators.Position;

public interface IModel {

    void moveCharacter(CHARACTER_MOVEMENT movement);

    double getCharacterX();

    double getCharacterY();

    Position getCharacterPosition();

    int getCharacterFrame();

    void start(CHARACTER_TYPE type, int width, int height);

    void togglePause();

    void close();

    void exit();

    void saveGame(String saveName);

    void loadGame(String save);
}
