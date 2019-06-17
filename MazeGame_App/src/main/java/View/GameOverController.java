package View;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class GameOverController implements IView {

    private Stage stage;
    private MediaPlayer mediaPlayer;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setFullScreen(false);
        this.stage.setMaxHeight(300);
        this.stage.setMaxWidth(600);
        this.stage.setResizable(false);
        this.stage.setTitle("Maze Solved");
        stage.getIcons().add(ResourceManager.getInstance().getIcon());
        stage.setOnCloseRequest(event -> this.onClickFinish());

        this.mediaPlayer = new MediaPlayer(ResourceManager.getInstance().getClip(ResourceManager.FINISH_CLIP));
        mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
        mediaPlayer.setVolume(0.2);
        mediaPlayer.play();
    }

    public void onClickFinish() {
        this.mediaPlayer.stop();
        this.stage.close();
    }
}
