package View;

import Model.Game;
import Server.Configurations;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Configurations.DEBUG = false;

        //connect model to view model.
        Game game = new Game();
        ViewModel viewModel = new ViewModel(game);
        game.addObserver(viewModel);

        //load the launcher.
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/launcher.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Game");
        primaryStage.setScene(new Scene(root, 1200, 720));
        primaryStage.show();

        //connect the view model to the launcher.
        LauncherController launcherController = fxmlLoader.getController();
        launcherController.setViewModel(viewModel);
        viewModel.addObserver(launcherController);

        launcherController.setStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
