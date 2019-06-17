package View;

import Model.Character.CHARACTER_TYPE;
import Model.Game.GAMESTATE;
import Model.GamePersistenceManager;
import ViewModel.ViewModel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import static Model.GamePersistenceManager.LOG;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class LauncherController implements Observer, IView, EventHandler<WindowEvent> {

    private static final Paint WHITE = Color.valueOf("0xffffff");
    private static final Paint YELLOW = Color.valueOf("0xf4f43f");

    private ResourceManager resourceManager = ResourceManager.getInstance();;

    @FXML public TextField widthField;
    @FXML public TextField heightField;
    @FXML private Text text_guy;
    @FXML private Text text_meir;
    @FXML private Text text_ariel;
    @FXML private Text text_elia;
    @FXML private Text text_kobe;
    @FXML private Text text_yuval;
    @FXML private Pane mainPane;

    private CHARACTER_TYPE selected = CHARACTER_TYPE.UNSELECTED;
    private String width = "";
    private String height = "";

    private ViewModel viewModel;

    public void setViewModel(ViewModel viewModel) { this.viewModel = viewModel; }

    public void setStage(Stage stage) {
        stage.setMinWidth(1280 / 1.19);
        stage.setMinHeight(720 / 1.2);
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.getIcons().add(resourceManager.getIcon());

        stage.setOnCloseRequest(this);
    }

    //region Character Select

    public void onClickGuy() {
        text_guy.setFill(YELLOW);
        this.selected = CHARACTER_TYPE.GUY;
        updateCharacterSelect();
    }

    public void onClickMeir() {
        text_meir.setFill(YELLOW);
        this.selected = CHARACTER_TYPE.MEIR;
        updateCharacterSelect();
    }

    public void onClickAriel() {
        text_ariel.setFill(YELLOW);
        this.selected = CHARACTER_TYPE.ARIEL;
        updateCharacterSelect();
    }

    public void onClickElia() {
        text_elia.setFill(YELLOW);
        this.selected = CHARACTER_TYPE.ELIA;
        updateCharacterSelect();
    }

    public void onClickKobe() {
        text_kobe.setFill(YELLOW);
        this.selected = CHARACTER_TYPE.KOBE;
        updateCharacterSelect();
    }

    public void onClickYuval() {
        text_yuval.setFill(YELLOW);
        this.selected = CHARACTER_TYPE.YUVAL;
        updateCharacterSelect();
    }

    private void updateCharacterSelect() {
        if (selected != CHARACTER_TYPE.GUY) text_guy.setFill(WHITE);
        if (selected != CHARACTER_TYPE.MEIR) text_meir.setFill(WHITE);
        if (selected != CHARACTER_TYPE.ARIEL) text_ariel.setFill(WHITE);
        if (selected != CHARACTER_TYPE.ELIA) text_elia.setFill(WHITE);
        if (selected != CHARACTER_TYPE.KOBE) text_kobe.setFill(WHITE);
        if (selected != CHARACTER_TYPE.YUVAL) text_yuval.setFill(WHITE);

    }

    //endregion

    @FXML
    public void onHeightUpdated() {
        if (!heightField.getText().matches("\\d*"))  {
            heightField.setText(height);
            heightField.end();
        }
        else height = heightField.getText();
    }

    @FXML
    public void onWidthUpdated() {
        if (!widthField.getText().matches("\\d*")) {
            widthField.setText(width);
            widthField.end();
        }
        else width = widthField.getText();
    }

    @FXML
    public void onClickStartGame() {
        if (!validateForm()) return;
        openGame("");
        LOG.info("Started a game");
    }

    private void openGame(String saveName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Pane root = fxmlLoader.load();
            GameController controller = fxmlLoader.getController();
            controller.setViewModel(this.viewModel);
            if (saveName.isEmpty()) {
                viewModel.startGame(selected, Integer.parseInt(this.widthField.getText()), Integer.parseInt(this.heightField.getText()));
                resourceManager.loadCharacter(selected);
            }
            else controller.setSaveName(saveName);


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            root.requestFocus();
            mainPane.setDisable(true);

            controller.setStage(stage);

        } catch(Exception e) {
            LOG.error("Error creating game window: \n", e);
        }
    }

    private boolean validateForm(){
        String errors = "";
        if(selected == CHARACTER_TYPE.UNSELECTED)
            errors += "Please select a character!\n";

        if(heightField.getText().length() == 0)
            errors += "Please enter desired height for the maze!\n";
        else if (Integer.parseInt(heightField.getText()) < 3)
            errors += "Minimum maze height is 3.\n";

        if(widthField.getText().length() == 0)
            errors += "Please enter desired width for the maze!\n";
        else if (Integer.parseInt(widthField.getText()) < 3)
            errors += "Minimum maze width is 3.\n";

        if(!errors.equals("")) {
            Alert error = new Alert(Alert.AlertType.WARNING);
            error.setTitle("Form Error");
            error.setHeaderText("Invalid Input");
            error.setContentText(errors);
            error.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            error.show();
        }
        return errors.equals("");
    }

    @FXML
    public void onClickLoadGame() {
        ArrayList<String> saves = GamePersistenceManager.getSaveList();
        if (saves.isEmpty()) {
            Alert noSaves = new Alert(AlertType.WARNING);
            noSaves.setTitle("Cant Find Saves");
            noSaves.setHeaderText(null);
            noSaves.setContentText("Cannot find saved games to load.");
            noSaves.showAndWait();
            return;
        }
        ChoiceDialog<String> chooseSave = new ChoiceDialog<>(saves.get(0), saves);
        chooseSave.setTitle("Load Save");
        chooseSave.setHeaderText(null);
        chooseSave.setContentText("Choose the save you wish to load");

        Optional<String> result = chooseSave.showAndWait();
        result.ifPresent(save -> {
            openGame(save);
            viewModel.loadGame(save);
        });

    }

    @FXML
    public void onClickProperties() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/properties.fxml"));
            Pane root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.requestFocus();

            PropertiesController controller = fxmlLoader.getController();
            controller.setStage(stage);
            controller.setCloseEvent(event -> this.mainPane.setDisable(false));
            this.mainPane.setDisable(true);

        } catch (IOException e) {
            LOG.error("Error loading properties window: ", e);
        }
    }

    @FXML
    public void onClickExit() {
        this.viewModel.exitGame();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void onClickHelp() {
        Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
        helpDialog.setTitle("Tutorial");
        helpDialog.setHeaderText(null);
        helpDialog.setContentText(resourceManager.getHelpText());
        helpDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        helpDialog.getDialogPane().setMinWidth(800);
        helpDialog.show();
    }

    @FXML
    public void onClickAbout() {
        Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
        aboutDialog.setTitle("About");
        aboutDialog.setHeaderText(null);
        aboutDialog.setContentText(resourceManager.getAboutText());
        aboutDialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        aboutDialog.getDialogPane().setMinWidth(800);
        aboutDialog.show();
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GAMESTATE) {
            GAMESTATE newState = (GAMESTATE) arg;
            if (newState == GAMESTATE.CLOSED || newState == GAMESTATE.SOLVED) {
                this.mainPane.setDisable(false);
                LOG.info("Game Ended");
            }
        }
    }

    @Override
    public void handle(WindowEvent event) {
        if (!this.mainPane.isDisabled()) {
            this.onClickExit();


        }
        else event.consume();
    }
}
