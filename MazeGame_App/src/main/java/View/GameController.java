package View;

import Model.Game.GAMESTATE;
import Model.GamePersistenceManager;
import Server.Configurations;
import ViewModel.ViewModel;
import algorithms.mazeGenerators.Position;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static Model.GamePersistenceManager.LOG;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class GameController extends AnimationTimer implements Observer, IView {

    private Stage gameStage;
    private ViewModel viewModel;

    private ResourceManager resourceManager = ResourceManager.getInstance();

    private  DisplayableMaze maze;

    private double r_CharacterX, r_CharacterY;
    private Position characterPosition;
    private int characterFrame;
    private boolean isShowingMiniMap;
    private boolean isFinished;
    private double miniMapZoom;
    private MediaPlayer backgroundMediaPlayer;
    private MediaPlayer openingMediaPlayer;
    private MediaPlayer mediaPlayer;
    private String saveName;


    @FXML private Canvas gameCanvas;
    @FXML private StackPane leftMenu;

    @Override
    public void setStage(Stage gameStage) {
        this.gameStage = gameStage;
        this.gameStage.setTitle("Maze Game");
        this.gameCanvas.widthProperty().bind(gameStage.widthProperty());
        this.gameCanvas.heightProperty().bind(gameStage.heightProperty());
        this.gameStage.setOnCloseRequest((event) -> onClickClose());
        this.miniMapZoom = 1;
        this.isFinished = false;
        this.gameStage.getIcons().add(resourceManager.getIcon());

        if (Configurations.DEBUG) {
            mediaPlayer = new MediaPlayer(this.resourceManager.getClip(ResourceManager.WELCOME_CLIP));
            mediaPlayer.setAutoPlay(false);
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer = new MediaPlayer(this.resourceManager.getClip(ResourceManager.BACKGROUND_CLIP));
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.2);
                mediaPlayer.setAutoPlay(false);
                mediaPlayer.play();
            });
            if (!saveName.isEmpty()) {
                mediaPlayer.getOnEndOfMedia().run();
            }
            else mediaPlayer.play();
        }
        else {
            if (saveName.isEmpty()) {
                openingMediaPlayer = new MediaPlayer(this.resourceManager.getClip(ResourceManager.WELCOME_CLIP));
                openingMediaPlayer.setAutoPlay(false);
                openingMediaPlayer.play();
            }

            backgroundMediaPlayer = new MediaPlayer(this.resourceManager.getClip(ResourceManager.BACKGROUND_CLIP));
            backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMediaPlayer.setVolume(0.2);
            backgroundMediaPlayer.setAutoPlay(false);
            backgroundMediaPlayer.play();
        }

    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    //region Event Listeners

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.S) this.maze.toggleSolution();
        if (keyEvent.getCode() == KeyCode.CONTROL) toggleMiniMap();
        viewModel.keyPressed(keyEvent);
    }

    @FXML
    public void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.CONTROL) toggleMiniMap();
        viewModel.keyReleased(keyEvent);
    }

    @FXML
    public void onScroll(ScrollEvent scrollEvent) {
        if (!scrollEvent.isControlDown()) return;
        double zoomOnWheel = 0.05;
        if(scrollEvent.getDeltaY() > 0 && this.miniMapZoom + zoomOnWheel <= 10)
            this.miniMapZoom += zoomOnWheel;
        if(scrollEvent.getDeltaY() < 0 && this.miniMapZoom - zoomOnWheel >= 1)
            this.miniMapZoom -= zoomOnWheel;
    }

    @FXML
    public void onMousePressed(MouseEvent mouseEvent) {
        this.viewModel.mousePressed(mouseEvent, this.gameCanvas.getWidth() / 2, this.gameCanvas.getHeight() / 2);
    }

    @FXML
    public void onMouseReleased(MouseEvent mouseEvent) {
        this.viewModel.mouseReleased(mouseEvent);
    }

    @FXML
    public void onMouseDragged(MouseEvent mouseEvent) {
        this.viewModel.mousePressed(mouseEvent, this.gameCanvas.getWidth() / 2, this.gameCanvas.getHeight() / 2);
    }

    @FXML
    public void onClickContinue() {
        this.viewModel.togglePause();
    }

    @FXML
    public void onClickSaveGame() {
        TextInputDialog saveNameDialog = new TextInputDialog(saveName);
        saveNameDialog.setTitle("Choose save name");
        saveNameDialog.setHeaderText(null);
        saveNameDialog.setContentText(null);

        Optional<String> result = saveNameDialog.showAndWait();

        result.ifPresent(saveName -> {
            if(!saveName.matches("^[a-zA-Z0-9_-]+$")) {
                Alert badName = new Alert(Alert.AlertType.ERROR);
                badName.setTitle("Name Error");
                badName.setHeaderText(null);
                badName.setContentText("Please enter a save name composed of letters and numbers.");
                badName.showAndWait();
                return;
            }

            if (GamePersistenceManager.getSaveList().contains(saveName)) {
                Alert confirmOverride = new Alert(Alert.AlertType.CONFIRMATION);
                confirmOverride.setTitle("Override Save");
                confirmOverride.setHeaderText(null);
                confirmOverride.setContentText("Are you sure you want to override the save - " + saveName);
                confirmOverride.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

                Optional<ButtonType> confirm = confirmOverride.showAndWait();
                if (confirm.get() == ButtonType.YES) this.viewModel.saveGame(saveName);
            }
            else this.viewModel.saveGame(saveName);
        });


    }

    @FXML
    public void onClickClose() {
        this.viewModel.closeGame();
        this.gameStage.close();
        this.stop();
    }

    //endregion

    //region State Update

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GAMESTATE) {
            GAMESTATE newState = (GAMESTATE) arg;
            if (newState == GAMESTATE.RUNNING) updateRunning();
            else if (newState == GAMESTATE.PAUSED) updatePaused();
            else if (newState == GAMESTATE.CLOSED) updateClosed();
            else if (newState == GAMESTATE.SOLVED) updateSolved();
        }
        else if(arg instanceof DisplayableMaze) startup((DisplayableMaze) arg);
        else updateCharacter();
    }

    private void updateRunning() {
        this.leftMenu.setVisible(false);
        this.start();
        this.gameCanvas.requestFocus();
        if (Configurations.DEBUG) {
            mediaPlayer.play();
        }
        else {
            backgroundMediaPlayer.play();
            if (saveName.isEmpty()) openingMediaPlayer.play();
        }

        this.gameStage.setResizable(true);
    }

    private void updatePaused() {
        this.leftMenu.setVisible(true);
        this.stop();
        this.gameStage.setTitle("Maze Game");
        if (Configurations.DEBUG) {
            mediaPlayer.pause();
        }
        else {
            backgroundMediaPlayer.pause();
            openingMediaPlayer.pause();
        }
        this.gameStage.setResizable(false);
    }

    private void updateClosed() {
        if (Configurations.DEBUG) {
            mediaPlayer.stop();
        }
        else {
            backgroundMediaPlayer.stop();
            openingMediaPlayer.stop();
        }
        mediaPlayer.stop();
        this.stop();
        this.viewModel.deleteObserver(this);
    }

    private void updateSolved() {
        this.isFinished = true;
        if (Configurations.DEBUG) {
            mediaPlayer.stop();
        }
        else {
            backgroundMediaPlayer.stop();
            openingMediaPlayer.stop();
        }
        mediaPlayer.stop();
        this.viewModel.deleteObserver(this);
    }

    private void startup(DisplayableMaze maze) {
        this.start();
        this.maze = maze;
        this.characterPosition = maze.getStartPosition();
        this.r_CharacterX = (this.characterPosition.getColumnIndex() * 32) + 16;
        this.r_CharacterY = (this.characterPosition.getRowIndex() * 32) + 16;
    }

    private void updateCharacter() {
        this.r_CharacterY = viewModel.getCharacterY();
        this.r_CharacterX = viewModel.getCharacterX();
        this.characterPosition = viewModel.getCharacterPosition();
        this.characterFrame = viewModel.getCharacterFrame();
    }

    private void finishGame() {
        this.stop();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game_over.fxml"));
            Pane root = fxmlLoader.load();

            Stage stage = new Stage();

            stage.setScene(new Scene(root, 600, 300));

            GameOverController newController = fxmlLoader.getController();
            newController.setStage(stage);
            stage.show();
            stage.requestFocus();

        } catch (IOException e) {
           LOG.error("Error loading end game window: \n", e);
        }
        this.gameStage.close();
    }

    //endregion

    //region Graphics

    @Override
    public void handle(long now) {

        GraphicsContext graphics = gameCanvas.getGraphicsContext2D();

        double centerX = this.gameCanvas.getWidth() / 2;
        double centerY = this.gameCanvas.getHeight() / 2;

        int tileOffsetX = (int)r_CharacterX % 32;
        int tileOffsetY = (int)r_CharacterY % 32;

        int tileCountX = (int)Math.ceil(centerX / 32) + 1;
        int tileCountY = (int)Math.ceil(centerY / 32) + 1;

        int mazeTileOffsetX = characterPosition.getColumnIndex() - tileCountX;
        int mazeTileOffsetY = characterPosition.getRowIndex() - tileCountY;

        graphics.setFill(Color.valueOf("#030830"));
        graphics.fillRect(0,0, gameCanvas.getWidth(), gameCanvas.getHeight());

        for (int y = 0; y < tileCountY*2; y++) {
            for (int x = 0; x < tileCountX*2; x++) {
                if ( mazeTileOffsetX + x >= 0 && mazeTileOffsetY + y >= 0 &&
                        mazeTileOffsetX + x < maze.getWidth() && mazeTileOffsetY + y < maze.getHeight()) {
                    double xPos = ((centerX - tileOffsetX) - tileCountX * 32) + x * 32;
                    double yPos = ((centerY - tileOffsetY) - tileCountY * 32) + y * 32;
                    graphics.drawImage(resourceManager.getTile(maze.getTile(mazeTileOffsetX + x, mazeTileOffsetY + y))
                            , xPos, yPos, 32, 32);
                }
            }
        }

        graphics.drawImage(resourceManager.getCharacterFrame(characterFrame),centerX - 16, centerY - 16, 32, 32);

        if (this.isFinished) finishGame();

        if (this.isShowingMiniMap) showMiniMap(graphics);

        showFPS(now);
    }

    private void toggleMiniMap() {
        this.isShowingMiniMap = !this.isShowingMiniMap;
    }

    private void showMiniMap(GraphicsContext graphics) {
        //mini map constants.
        double width = gameCanvas.getWidth() - 100;
        double height = gameCanvas.getHeight() - 100;
        double startX = 50;
        double startY = 50;

        //tile width and height int without scaling.
        double oTileWidth = width / this.maze.getWidth();
        double oTileHeight = height / this.maze.getHeight();

        //scaled tile width.
        double tileWidth = width / this.maze.getWidth() * miniMapZoom;
        //transform to fit width of mini map.
        int tilesInX = (int)Math.ceil(width / tileWidth);
        if (tilesInX > this.maze.getWidth()) tilesInX = this.maze.getWidth();
        tileWidth = width / tilesInX;

        //scaled tile height.
        double tileHeight = height / this.maze.getHeight() * miniMapZoom;
        //transform to fit width of mini map.
        int tilesInY = (int)(height / tileHeight);
        if (tilesInY > this.maze.getHeight()) tilesInY = this.maze.getHeight();
        tileHeight = height / tilesInY;

        /*
        position of the character using the unscaled tile size.
        this will allow us to scale the mini map while focusing on the character,
        meaning the character should not move its position on the screen
        while the rest of the mini map scales around him.
         */
        double characterX = startX + this.characterPosition.getColumnIndex() * oTileWidth;
        double characterY = startY + this.characterPosition.getRowIndex() * oTileHeight;

        //distances between the character and the mini map borders in terms of pixels.
        double right = (width + startX) - characterX;
        double up = characterY - startY;

        //distances in terms of tiles.
        int rightTiles = (int)Math.ceil(right / tileWidth);

        int leftTiles = tilesInX - rightTiles;
        if (leftTiles > characterPosition.getColumnIndex()) leftTiles = characterPosition.getRowIndex();

        int upTiles = (int)Math.ceil(up / tileHeight);
        if (upTiles >= tilesInY) upTiles = tilesInY - 1;
        if (upTiles <= 0) upTiles = 1;

        if (upTiles > characterPosition.getRowIndex()) upTiles = characterPosition.getRowIndex();
        int downTiles = tilesInY - upTiles;

        //the first tile that should be drawn on the mini map (top left).
        int startTileX = characterPosition.getColumnIndex() - leftTiles;
        if (startTileX < 0) startTileX = 0;
        int startTileY = characterPosition.getRowIndex() - upTiles;
        if (startTileY < 0) startTileY = 0;

        //the last tile that should be drawn on the mini map (bottom right).
        int endTileX = characterPosition.getColumnIndex() + rightTiles;
        if (endTileX > maze.getWidth()) endTileX = maze.getWidth();
        int endTileY = characterPosition.getRowIndex() + downTiles;
        if (endTileY > maze.getHeight()) endTileY = maze.getHeight();

        graphics.setFill(DisplayableMaze.COLOR_FLOOR);
        graphics.fillRect(startX, startY, width, height);

        for (int y = startTileY; y < endTileY; y++) {
            for (int x = startTileX; x < endTileX; x++) {
                if (this.characterPosition.equals(new Position(y, x))) graphics.setFill(DisplayableMaze.COLOR_PLAYER);
                else graphics.setFill(this.maze.getMiniMapTileColor(x, y));
                graphics.fillRect(startX + (x - startTileX)*tileWidth, startY + (y - startTileY)*tileHeight, tileWidth, tileHeight);
            }
        }
    }

    //region FPS Counter

    private long lastUpdate = 0;
    private long delta = 0;
    private int frames = 0;

    private void showFPS(long time) {
        if (delta < (1000000000))  {
            frames++;
            delta += time - lastUpdate;
            lastUpdate = time;
        }
        else  {
            this.gameStage.setTitle("MazeGame | " + frames + " FPS");
            delta = 0;
            frames = 0;
        }
    }

    //endregion

    //endregion
}
