import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * A class representing a WheelOfFortune object
 *
 */
public class WheelOfFortune
{
    private Clue clue;
    private Scanner in;
    private Player[] players;
    private int cp;
    private Wheel wheel;

    // here we need a clue, 
    
    public WheelOfFortune() throws FileNotFoundException {
        clue = new Clue();
        players = new Player[4];
        for (Player p: players) {
            p = new Player();
        }
        cp = 0;
        wheel = new Wheel();
    }

    public Clue getClue() {
        return this.clue;
    }

    public Wheel getWheel() {
        return this.wheel;
    }

    public void nextPlayer() {
        cp++;
    }
    public int numPlayers() {
        return this.players.length;
    }

    public boolean submitSolve(String s) {
        // check to see if s is the correct string
        if (this.clue.solve(s)) {
            this.clue.newRound();
            return true;
        }
        return false;
    }

    public String getNext() {
        this.clue.newRound();
        return this.clue.getPhrase();
    }

    public boolean isEmpty() {
        // if 
        return false;
    }
}
