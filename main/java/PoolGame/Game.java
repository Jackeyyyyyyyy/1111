package PoolGame;

import java.util.*;

import PoolGame.Builder.BallBuilderDirector;
import PoolGame.Config.BallConfig;
import PoolGame.Items.Ball;
import PoolGame.Items.PoolTable;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
/** The game class that runs the game */
public class Game {
    private PoolTable table;
    private boolean shownWonText = false;
    private final Text winText = new Text(50, 50, "Win and Bye");
    // Declare timer and score counter variables
    private int timerSeconds;
    private int score;
    private List<Observer> observers=new ArrayList<>();
    ScoreDisplay scoreDisplay;
    TimeDisplay timeDisplay;

    public CareTaker getCaretaker() {
        return caretaker;
    }

    public void setCaretaker(CareTaker caretaker) {
        this.caretaker = caretaker;
    }

    private CareTaker caretaker= new CareTaker();
    private boolean undoInProgress = false;
    // Create a Text object to display the timer
    private final Text timerText = new Text(50, 50, "Time: 0:00");

    public Text getTimerText() {
        return timerText;
    }

    public Text getScoreText() {
        return scoreText;
    }

    // Create a Text object to display the score
    private final Text scoreText = new Text(50, 75, "Score: 0");

    public LinkedList<GameState> getGameStates() {
        return gameStates;
    }

    public void setGameStates(LinkedList<GameState> gameStates) {
        this.gameStates = gameStates;
    }

    // Declare a stack to store the game states
    //private Stack<GameState> gameStates=new Stack<>();
    private LinkedList<GameState> gameStates = new LinkedList<>();

    /**
     * Initialise the game with the provided config
     * @param config The config parser to load the config from
     */
    public Game(ConfigReader config) {
        this.setup(config);
        timerSeconds = 0;
        score = 0;
        scoreDisplay = new ScoreDisplay();
        timeDisplay = new TimeDisplay();
        registerObserver(scoreDisplay);
        registerObserver(timeDisplay);
    }

    private void setup(ConfigReader config) {
        this.table = new PoolTable(config.getConfig().getTableConfig());
        List<BallConfig> ballsConf = config.getConfig().getBallsConfig().getBallConfigs();
        List<Ball> balls = new ArrayList<>();
        BallBuilderDirector builder = new BallBuilderDirector();
        builder.registerDefault();
        for (BallConfig ballConf: ballsConf) {
            Ball ball = builder.construct(ballConf);
            if (ball == null) {
                System.err.println("WARNING: Unknown ball, skipping...");
            } else {
                balls.add(ball);
            }
        }
        this.table.setupBalls(balls);
        this.winText.setVisible(false);
        this.winText.setX(table.getDimX() / 2);
        this.winText.setY(table.getDimY() / 2);
    }
    private void setup(List<Ball> balls) {
        this.table.setupBalls(balls);
        this.winText.setVisible(false);
        this.winText.setX(table.getDimX() / 2);
        this.winText.setY(table.getDimY() / 2);
    }
    /**
     * Get the window dimension in the x-axis
     * @return The x-axis size of the window dimension
     */
    public double getWindowDimX() {
        return this.table.getDimX();
    }

    /**
     * Get the window dimension in the y-axis
     * @return The y-axis size of the window dimension
     */
    public double getWindowDimY() {
        return this.table.getDimY();
    }

    /**
     * Get the pool table associated with the game
     * @return The pool table instance of the game
     */
    public PoolTable getPoolTable() {
        return this.table;
    }

    /** Add all drawable object to the JavaFX group
     * @param root The JavaFX `Group` instance
    */
    public void addDrawables(Group root) {
        ObservableList<Node> groupChildren = root.getChildren();
        table.addToGroup(groupChildren);
        groupChildren.add(this.winText);
        groupChildren.add(timerText);
        groupChildren.add(scoreText);
    }

    /** Reset the game */
    public void reset() {
        this.winText.setVisible(false);
        this.shownWonText = false;
        this.table.reset();
        this.timerSeconds=0;
        this.score=0;
    }
    public void removeBallsByColor(Color color) {
        caretaker.saveState(this);
        for (Ball ball : table.getBalls()) {
            if (ball.getColor().equals(color)) {
                ball.disable();
            }
        }
    }
    /** Code to execute every tick. */
    public void tick() {
        // Increment the timer
        if(!undoInProgress) {
            score = 0;
            this.setScore();
           // String scoreString = String.format("Score: %d", score);
            // Update the timer text
//            int minutes = timerSeconds / 60;
//            int seconds = timerSeconds % 60;
//            String timerString = String.format("Time: %d:%02d", minutes, seconds);
//            timerText.setText(timerString);

            // Check if a ball has fallen into a pocket
            List<Ball> ballsInPocket = table.getBallsInPocket();
            if (ballsInPocket.size() > 0)
                for (Ball ball : ballsInPocket) {
                    // Increment the score
                    score += ball.getValue();
                    this.setScore();
                    // Update the score text
//                   scoreString = String.format("Score: %d", score);
//                    scoreText.setText(scoreString);
                }
            else
//                scoreText.setText(scoreString);
            for (Ball ball : this.table.getBalls()) {
                if (ball.getBallType() == Ball.BallType.CUEBALL)
                    if (ball.isCueBallHit()) {
                        System.out.println("cue ball hit");
                        caretaker.saveState(this );
                        ball.setCueBallHit(false);
                        break;
                    }
            }
            if (table.hasWon() && !this.shownWonText) {
               // System.out.println(this.winText.getText());
                this.winText.setVisible(true);
                this.shownWonText = true;
            }
            else
                this.shownWonText = false;
            if(!winText.isVisible())
            { timerSeconds++; this.setTime();}
            table.checkPocket(this);
            table.handleCollision();
            this.table.applyFrictionToBalls();
            for (Ball ball : this.table.getBalls()) {
                ball.move();
            }
        }
        else
            return;
    }
    public GameState saveState() {
        // Create a new game state object with the current score, time, and ball positions
        System.out.println("Saved");
        List<Ball> ballsInTable = table.getBalls();
        List<Ball> ballsInGameState = new ArrayList<>();
        for (Ball ball : ballsInTable) {
            Ball copyOfBall = new Ball(ball);
            ballsInGameState.add(copyOfBall);
        }
//        gameStates.addFirst(new GameState(score, timerSeconds, ballsInGameState));
//        System.out.println(gameStates.peek().getBalls().get(0).getXPos());
//        System.out.println(gameStates.peek().getScore());
//        System.out.println(gameStates.peek().getBalls().get(1).isDisabled());
        return new GameState(score, timerSeconds, ballsInGameState);
    }

    // Add a method to undo the last game state change
    public void setGameState(GameState gameState) {

            GameState previousState = gameState;
            System.out.println(previousState.getBalls().get(0).getXPos());
            for (int i = 0; i < table.getBalls().size(); i++) {

            Ball ballInState = previousState.getBalls().get(i);
    table.getBalls().get(i).setXPos(ballInState.getXPos());
 table.getBalls().get(i).setYPos(ballInState.getYPos());
                table.getBalls().get(i).setXVel(0);
                table.getBalls().get(i).setYVel(0);
                table.getBalls().get(i).setDisabled(ballInState.isDisabled());


            timerSeconds = previousState.getTime();
            score = previousState.getScore();
            System.out.println(score);
        }
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return timerSeconds;
    }
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
    public void setScore() {

        notifyObservers();
    }
    public void setTime() {

        notifyObservers();
    }
}
