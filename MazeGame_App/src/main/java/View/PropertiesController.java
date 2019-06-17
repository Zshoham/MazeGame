package View;

import Server.Configurations;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static Model.GamePersistenceManager.LOG;

public class PropertiesController implements IView {

    private Stage stage;
    private EventHandler<WindowEvent> closeHandler;

    @FXML public CheckBox deleteCache;
    @FXML public ChoiceBox<String> mazeGenerator;
    @FXML public ChoiceBox<String> mazeSolver;
    @FXML public Slider threadCount;
    @FXML public Text threadCountValue;


    private boolean currentDeleteCache;
    private String currentMazeGenerator;
    private String currentMazeSolver;
    private int currentThreadCount;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setMinWidth(600);
        stage.setMinHeight(300);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> this.closeHandler.handle(event));
        stage.getIcons().add(ResourceManager.getInstance().getIcon());

        this.currentDeleteCache = Configurations.shouldDeleteCache();
        this.deleteCache.setSelected(currentDeleteCache);

        mazeGenerator.getItems().addAll(Configurations.getMazeGenerators());
        this.currentMazeGenerator = Configurations.getMazeGenerator();
        mazeGenerator.setValue(currentMazeGenerator);

        mazeSolver.getItems().addAll(Configurations.getMazeSolvers());
        this.currentMazeSolver = Configurations.getMazeSolver();
        mazeSolver.setValue(currentMazeSolver);

        this.currentThreadCount = Configurations.getThreadCount();
        threadCountValue.setText(String.valueOf(currentThreadCount));
        threadCount.setMin(0);
        threadCount.setMax(Runtime.getRuntime().availableProcessors() * 10);
        threadCount.setValue(currentThreadCount);
        threadCount.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            threadCount.setValue(newValue.intValue());
            threadCountValue.setText(String.valueOf(newValue.intValue()));
        });

    }

    public void onClickSave() {
        boolean newDeleteCache = deleteCache.isSelected();
        if (currentDeleteCache != newDeleteCache) {
            if (newDeleteCache) Configurations.setProperty("Delete_Cache_On_Close", "T");
            else Configurations.setProperty("Delete_Cache_On_Close", "F");
            LOG.info("changed the property 'Delete_Cache_On_Close' to " + newDeleteCache);
        }

        String newMazeGenerator = mazeGenerator.getValue();
        if (!currentMazeGenerator.equals(newMazeGenerator)) {
            Configurations.setProperty("Maze_Generation_Algorithm", newMazeGenerator);
            LOG.info("changed the property 'Maze_Generation_Algorithm' to " + newMazeGenerator);
        }

        String newMazeSolver = mazeSolver.getValue();
        if (!currentMazeSolver.equals(newMazeSolver)) {
            Configurations.setProperty("Maze_Solver_Algorithm", newMazeSolver);
            LOG.info("changed the property 'Maze_Solver_Algorithm' to " + newMazeSolver);
        }

        int newThreadCount = Integer.valueOf(threadCountValue.getText());
        if (currentThreadCount != newThreadCount) {
            Configurations.setProperty("Server_Thread_Count", threadCountValue.getText());
            LOG.info("changed the property 'Server_Thread_Count' to " + newThreadCount);
        }

        this.closeHandler.handle(new WindowEvent(this.stage.getOwner(), WindowEvent.WINDOW_CLOSE_REQUEST));
        this.stage.close();
    }

    public void setCloseEvent(EventHandler<WindowEvent> handler) {
        this.closeHandler = handler;
    }
}
