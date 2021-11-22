/* Clue.java
 * class that encapsulates the CLue itself, the previously guessed letters, and
 * all processing to determine if guesses are correct
 */

import java.util.*;
import java.io.*;

public class Clue {
	private static final String PUNC = " .,/\\+=@#$%^&*()-_!`~:;\'\"{}[]";
	public static final int REPEAT = -2, MISS = -1;

	private int currentIndex;

	private String currentPhrase, currentCategory, guesses, displayPhrase;
	private ArrayList<String[]> data;
	private String[][] phrases;

	private Scanner in;

	public Clue() throws FileNotFoundException {

		try {
        	in = new Scanner(new FileReader("lotsOfClues.csv"));
        } catch (FileNotFoundException e) {
        	in = new Scanner(new FileReader("../lotsOfClues.csv"));
        }

        data = new ArrayList<String[]>();

        while (in.hasNextLine()) {
			String[] line = new String[2];
			String rawLine = in.nextLine();
			line[0] = rawLine.substring(1, rawLine.substring(1).indexOf("\"") + 1).toLowerCase();
			rawLine = rawLine.substring(rawLine.indexOf(","));
			line[1] = rawLine.substring(2, rawLine.substring(3).indexOf("\"") + 3);
        	if (!line[0].equals("puzzle")) {
        		data.add(line);
        	}
        } 
    	
    	Collections.shuffle(data);
    	phrases = new String[data.size()][2];
    	for (int i = 0; i < data.size(); i++) {
    		phrases[i][0] = data.get(i)[0];
    		phrases[i][1] = data.get(i)[1];
    	}

		this.currentIndex = 0;// change this to be random index that doesn't repeat phrases
		this.currentPhrase = this.phrases[this.currentIndex][0];
		this.guesses = this.PUNC;	
		this.updateDisplayPhrase();

	}

	private void updateDisplayPhrase() {
		this.displayPhrase = "";
		for (char s: this.currentPhrase.toCharArray()) {
			if (this.guesses.indexOf(s) != -1) {
				this.displayPhrase += s;
			} else {
				this.displayPhrase += "*";
			}
		}
	}

	public void newRound() {
		this.guesses = this.PUNC;
		this.currentIndex = (this.currentIndex + 1) % this.phrases.length;
		this.currentPhrase = this.phrases[this.currentIndex][0];
		this.currentCategory = this.phrases[this.currentIndex][1];
	}

	public boolean solve(String solution) {
		if (solution.length() != this.phrases[currentIndex][0].length()) {
			return false;
		}
		for (int i = 0; i < solution.length(); i++) {
			if (Character.toLowerCase(solution.toCharArray()[i]) != Character.toLowerCase(this.phrases[currentIndex][0].toCharArray()[i])) {
				return false;
			}
		}
		this.guesses += solution;
		//System.out.println(this.guesses);
		return true;
	}

	public int check(char letterGuessed) {
		// takes a char
		// if it is not in guesses, but it is in currentPhrase, return # of appearances, update displayPhrase
		letterGuessed = Character.toLowerCase(letterGuessed);
		if (this.guesses.indexOf(letterGuessed) == -1) {
			this.guesses += letterGuessed;
			if (this.currentPhrase.indexOf(letterGuessed) == -1) {
				return this.MISS;
			}
			int hits = 0;
			for (char s: this.currentPhrase.toCharArray()) {
				if (letterGuessed == s) {
					hits++;
				}
			}

			return hits;
		}
		return this.REPEAT;
	}

	public boolean completed() {
		for (char s: this.currentPhrase.toCharArray()) {
			if (this.guesses.indexOf(Character.toLowerCase(s)) == -1) {
				return false;
			}
		}
		//System.out.println("true");
		return true;
	}

	public String getCategory() {
		return this.currentCategory;
	}

	public String getGuesses() {
		return this.guesses;
	}

	public String getDisplayPhrase() {
		this.updateDisplayPhrase();
		return this.displayPhrase;
	}

	public String getPhrase() {
		return this.currentPhrase;
	}
}


