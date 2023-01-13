package PoolGame;

public class TimeDisplay implements Observer{
    private int timerSeconds;

    public void update(Game subject) {
        if (subject instanceof Game) {
            Game game = (Game) subject;
            timerSeconds = game.getTime();
            displayTime(subject);
        }
    }

    public void displayTime(Game subject) {
        int minutes = timerSeconds / 60;
        int seconds = timerSeconds % 60;
        String timeString = String.format("Time: %d:%02d", minutes, seconds);;
       subject.getTimerText().setText(timeString);
    }

}
