package ViewModel;

import Model.Character.CHARACTER_MOVEMENT;
import Model.IModel;
import javafx.scene.input.KeyCode;

import java.util.Hashtable;
import java.util.Set;

final class Input {

    // region Keyboard Input

    private static final Hashtable<KeyCode, CHARACTER_MOVEMENT> characterControls = initTable();

    private static Hashtable<KeyCode, Runnable> keyEvent = new Hashtable<>();


    private static Hashtable<KeyCode, CHARACTER_MOVEMENT> initTable() {
        Hashtable<KeyCode, CHARACTER_MOVEMENT> res = new Hashtable<>();
        res.put(KeyCode.UP, CHARACTER_MOVEMENT.UP);
        res.put(KeyCode.NUMPAD8, CHARACTER_MOVEMENT.UP);
        res.put(KeyCode.DOWN, CHARACTER_MOVEMENT.DOWN);
        res.put(KeyCode.NUMPAD2, CHARACTER_MOVEMENT.DOWN);
        res.put(KeyCode.RIGHT, CHARACTER_MOVEMENT.RIGHT);
        res.put(KeyCode.NUMPAD6, CHARACTER_MOVEMENT.RIGHT);
        res.put(KeyCode.LEFT, CHARACTER_MOVEMENT.LEFT);
        res.put(KeyCode.NUMPAD4, CHARACTER_MOVEMENT.LEFT);
        res.put(KeyCode.NUMPAD7, CHARACTER_MOVEMENT.LEFT_UP);
        res.put(KeyCode.NUMPAD9, CHARACTER_MOVEMENT.RIGHT_UP);
        res.put(KeyCode.NUMPAD1, CHARACTER_MOVEMENT.LEFT_DOWN);
        res.put(KeyCode.NUMPAD3, CHARACTER_MOVEMENT.RIGHT_DOWN);
        return res;
    }


    static boolean isValidCharacterInput(KeyCode code) {
        return characterControls.containsKey(code);
    }

    static void setKeyEvent(KeyCode code, Runnable event) {
        keyEvent.put(code, event);
    }

    static void initCharacterMovement(IModel model) {
        Set<KeyCode> keys = characterControls.keySet();
        for (KeyCode code : keys) {
            keyEvent.put(code, () -> model.moveCharacter(characterControls.get(code)));
        }
    }

    static void parseKeyboardInput(KeyCode code) {
        Runnable event = keyEvent.get(code);
        if (event != null) event.run();
    }

    static boolean isValidInput(KeyCode code) {
        return keyEvent.containsKey(code);
    }

    //endregion


    static CHARACTER_MOVEMENT parseMouseInput(double mX, double mY, double cX, double cY) {
        CHARACTER_MOVEMENT res = CHARACTER_MOVEMENT.IDLE;

        double vecX = mX - cX;
        double vecY = mY - cY;

        double alpha = Math.atan(vecY / vecX);

        alpha = alpha * (180 / Math.PI);

        if (alpha > 0 && vecX > 0 && vecY > 0) alpha = (90 - alpha) + 270;
        if (alpha > 0 && vecX < 0) alpha = (90 - alpha) + 90;
        if (alpha < 0 && vecX > 0) alpha = Math.abs(alpha);
        if (alpha < 0 && vecY > 0) alpha = Math.abs(alpha) + 180;

        if (alpha >= 360 - 22.5 || alpha <= 22.5) return CHARACTER_MOVEMENT.RIGHT;
        if (alpha >= 22.5 && alpha <= 45 + 22.5) return CHARACTER_MOVEMENT.RIGHT_UP;
        if (alpha >= 45 + 22.5 && alpha <= 90 + 22.5) return CHARACTER_MOVEMENT.UP;
        if (alpha >= 90 + 22.5 && alpha <= 180 - 22.5) return CHARACTER_MOVEMENT.LEFT_UP;
        if (alpha >= 180 - 22.5 && alpha <= 180 + 22.5) return CHARACTER_MOVEMENT.LEFT;
        if (alpha >= 180 + 22.5 && alpha <= 270 - 22.5) return CHARACTER_MOVEMENT.LEFT_DOWN;
        if (alpha >= 270 - 22.5 && alpha <= 270 + 22.5) return CHARACTER_MOVEMENT.DOWN;
        if (alpha >= 270 + 22.5 && alpha <= 360 - 22.5) return CHARACTER_MOVEMENT.RIGHT_DOWN;

        return res;
    }

    private Input() {}


}
