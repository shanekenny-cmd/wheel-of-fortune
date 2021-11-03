/* Clue.java
 * class that encapsulates the CLue itself, the previously guessed letters, and
 * all processing to determine if guesses are correct
 */


public class Clue {
	private static final String[] PHRASES = {	"all the world is a stage",
												"a bug's life",
												"all that jazz",
												"behind closed doors" };
	private static final String PUNC = " .,/\\+=@#$%^&*()-_!`~:;\'\"{}[]";
	public static final int REPEAT = -2, MISS = -1;

	private static int currentIndex;

	private String currentPhrase, guesses, displayPhrase;

	public Clue() {

		this.currentIndex = 0;// change this to be random index that doesn't repeat phrases
		this.currentPhrase = this.PHRASES[this.currentIndex];
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
		this.currentIndex = (this.currentIndex + 1) % this.PHRASES.length;
		this.currentPhrase = this.PHRASES[this.currentIndex];
	}

	public boolean solve(String solution) {
		if (solution.length() != this.PHRASES[currentIndex].length()) {
			return false;
		}
		for (int i = 0; i < solution.length(); i++) {
			if (solution.toCharArray()[i] != this.PHRASES[currentIndex].toCharArray()[i]) {
				return false;
			}
		}
		this.guesses += solution;
		return true;
	}

	public int check(char letterGuessed) {
		// takes a char
		// if it is not in guesses, but it is in currentPhrase, return # of appearances, update displayPhrase
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
			if (guesses.indexOf(s) == -1) {
				return false;
			}
		}
		return true;
	}

	public String getDisplayPhrase() {
		this.updateDisplayPhrase();
		return this.displayPhrase;
	}
}


