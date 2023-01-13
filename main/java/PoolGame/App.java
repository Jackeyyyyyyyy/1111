/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package PoolGame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import PoolGame.Items.Pocket;
import PoolGame.Items.PoolTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.json.simple.parser.ParseException;

import PoolGame.ConfigReader.ConfigKeyMissingException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** The JavaFX application */
public class App extends Application {
    DifficultyState state;
    private final double FRAMETIME = 1.0 / 60.0;
    ConfigReader config;
    private ConfigReader loadConfig(List<String> args) {
        String configPath;
        boolean isResourcesDir = false;
		if (args.size() > 0) {
			configPath = args.get(0);
		} else {
			// configPath = "src/main/resources/config.json";
			configPath = "/config.json";
            isResourcesDir = true;
		}
		// parse the file:
        ConfigReader config = null;
        try {
            config = new ConfigReader(configPath, isResourcesDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (ConfigKeyMissingException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.printf("ERROR: %s\n", e.getMessage());
            System.exit(1);
        }
        return config;
    }

    @Override
    public void start(Stage stage) {

        Group root = new Group();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("PoolGame");
        ListView<String> menuList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList("Easy", "Medium", "Hard");
        menuList.setItems(items);

        // Set the menu list's action when an item is selected
        menuList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int level=1;
                String selectedItem = menuList.getSelectionModel().getSelectedItem();
                if (selectedItem.equals("Easy")) {
                   level = 1;
                } else if (selectedItem.equals("Medium")) {
                    level = 2;
                } else if (selectedItem.equals("Hard")) {
                    level = 3;
                }
                // Adjust game difficulty based on the chosen level
                adjustDifficulty(level);
                // Initialize the game
                initializeGame(stage, root);
            }
        });

//        ConfigReader config = loadConfig(getParameters().getRaw());
//        Game game = new Game(config);
//
//        Canvas canvas = new Canvas(game.getWindowDimX(), game.getWindowDimY());
//        stage.setWidth(game.getWindowDimX());
//        stage.setHeight(game.getWindowDimY() +
//                        Pocket.RADIUS +
//                        PoolTable.POCKET_OFFSET +
//                        4); // Magic number to get bottom to align
//        root.getChildren().add(canvas);
        root.getChildren().add(menuList);
        stage.show();

//       game.addDrawables(root);
//
//        Timeline timeline = new Timeline();
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        KeyFrame frame = new KeyFrame(Duration.seconds(FRAMETIME),
//        (actionEvent) -> {
//                game.tick();
//            });
//
//        timeline.getKeyFrames().add(frame);
//        timeline.play();
    }
    private void initializeGame(Stage stage, Group root) {
        config=loadConfig(Collections.singletonList(state.initializeGame(this)));
        Game game = new Game(config);

        Canvas canvas = new Canvas(game.getWindowDimX(), game.getWindowDimY());
        stage.setWidth(game.getWindowDimX());
        stage.setHeight(game.getWindowDimY() +
                Pocket.RADIUS +
                PoolTable.POCKET_OFFSET +
                4);  // Magic number to get bottom to align

        root.getChildren().add(canvas);
        //root.getChildren().add(cueStick);
        // GraphicsContext gc = canvas.getGraphicsContext2D();
        game.addDrawables(root);

        stage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R) {
                    game.removeBallsByColor(Color.RED);
                }
                if (event.getCode() == KeyCode.B) {
                    game.removeBallsByColor(Color.BLACK);
                }
                if (event.getCode() == KeyCode.N) {
                    game.removeBallsByColor(Color.BROWN);
                }
                if (event.getCode() == KeyCode.M) {
                    game.removeBallsByColor(Color.BLUE);
                }
                if (event.getCode() == KeyCode.Y) {
                    game.removeBallsByColor(Color.YELLOW);
                }
                if (event.getCode() == KeyCode.G) {
                    game.removeBallsByColor(Color.GREEN);
                }
                if (event.getCode() == KeyCode.P) {
                    game.removeBallsByColor(Color.PURPLE);
                }
                if (event.getCode() == KeyCode.O) {
                    game.removeBallsByColor(Color.ORANGE);
                }
                if (event.getCode() == KeyCode.Z) {
                    game.getCaretaker().undo(game);
                }
            }
        });

        //...
//        stage.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                game.cueStick.applyForce(forceX, forceY);
//            }
//        });
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.seconds(FRAMETIME),
                (actionEvent) -> {
                    game.tick();
                });
        timeline.getKeyFrames().add(frame);
        if (!game.getGameStates().isEmpty())
            System.out.println(game.getGameStates().peek().getBalls().get(0).getXPos());
        timeline.play();
    }
    private void adjustDifficulty(int level) {
        if(level == 1) {
            this.state = new EasyState();
        } else if(level == 2) {
            this.state = new NormalState();
        } else if(level == 3) {
            this.state = new HardState();
        }
    }
    public void setDifficulty(DifficultyState newState) {
        this.state = newState;
    }

    public int getDifficulty() {
        return state.getDifficulty();
    }
    /**
     * The entry point of the program
     * @param args CLI arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
