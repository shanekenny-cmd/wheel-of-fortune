import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo {
  Toolkit toolkit;

  Timer timer;

  public TimerDemo(int seconds, Player[] players) {
    toolkit = Toolkit.getDefaultToolkit();
    timer = new Timer();
    timer.schedule(new RemindTask(players), seconds * 1000);
  }

  class RemindTask extends TimerTask {
    Player[] players;
    RemindTask(Player[] players) {
      this.players = players;
    }
    public void run() {
      // here is the task to be completed when the timer is up
      System.out.println("Time's up!");
      int winner = 0;
      System.out.println(winner + ": " + this.players[winner].getBank());
      for (int i = 1; i < this.players.length; i++) {
        System.out.println((i + 1) + ": " + this.players[i].getBank());
        if (this.players[i].getBank() > this.players[winner].getBank()) {
          winner = i;
        }
      }
      System.out.println("Player " + (winner + 1) + " is the winner.");
      //toolkit.beep();
      System.exit(0); 
    }
  }
}  