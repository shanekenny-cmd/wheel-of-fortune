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

	private String currentPhrase, guesses, displayPhrase;

	public Clue() {

		this.currentPhrase = this.PHRASES[0];// change this to be random index that doesn't repeat phrases
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

	public String getDisplayPhrase() {
		this.updateDisplayPhrase();
		return this.displayPhrase;
	}
}


