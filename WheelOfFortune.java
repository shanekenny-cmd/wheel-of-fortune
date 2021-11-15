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
    
    public WheelOfFortune() throws FileNotFoundException {
        clue = new Clue();
        players = new Player[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
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
        this.cp++;
        this.cp %= this.players.length;
    }
    public int getCP() {
        return this.cp;
    }
    public void setCurrentPlayer(int index) {
        this.cp = index % this.players.length;
    }
    public int numPlayers() {
        return this.players.length;
    }
    public void bankrupt() {
        this.players[this.cp].setBal(0);
        this.cp++;
        this.cp %= this.players.length;
    }
    public String getPlayerString() {
        String toString = "";
        for (int i = 0; i < this.players.length; i++) {
            toString += "Player " + (i + 1) + 
                        ":\nBalance: " + this.players[i].getBal() + 
                        "\nBank: " + this.players[i].getBank() + "     ";
        }
        return toString;
    }

    public boolean buyVowel(char c) {
        this.players[this.cp].addBal(-500);
        return (this.clue.check(c) > 0);
    }

    public boolean playerGuess(char c, int value) {
        int hits = this.clue.check(c);// this returns -1 if its a miss
        if (hits < 0) {
            return false;
        }
        this.players[this.cp].addBal(hits * value);
        return true;
        //System.out.println((hits * value) + " added.");
    }

    public boolean submitSolve(String s) {
        // check to see if s is the correct string
        return this.clue.solve(s);
    }

    public String getNext() {
        this.players[this.cp].addBank(this.players[this.cp].getBal());
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].setBal(0);
        }
        this.clue.newRound();
        return this.clue.getPhrase();
    }

    public boolean isEmpty() {
        // if 
        return false;
    }
}
