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

    // here we need a clue, 
    
    public WheelOfFortune() throws FileNotFoundException {
        clue = new Clue();
    }

    public void submitSolve(String s) {
        // check to see if s is the correct string
        if (this.clue.solve(s)) {

        }
    }

    public String getNext() {
        return this.clue.getDisplayPhrase();
    }

    public boolean isEmpty() {
        return false;
    }
}
