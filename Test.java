/* Test.java
 */

import java.util.Scanner;


public class Test {
	public static final String[] PHRASES = {	"all the world is a stage",
												"a bug's life",
												"all that jazz",
												"behind closed doors" };
	public static final String PUNC = " .,/\\+=@#$%^&*()-_!`~:;\'\"{}[]";
	public static String guesses = " .,/\\+=@#$%^&*()-_!`~:;\'\"{}[]";
	public static Scanner sc;
	public static int round = 0;
	public static void main(String [] args) {
		sc = new Scanner(System.in);
		Wheel wheel = new Wheel();
		Player[] players = new Player[4];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player();
		}
		boolean playing = true;
		int cp = 0;
		while (playing) {
			cp %= 4;
			if (!players[cp].hasTurn()) {
				System.out.println("Player " + (cp + 1) + " turn skipped.");
				players[cp].turnSkipped();
				cp++;
			} else {
				int guessVal = wheel.spinWheel();
				if (guessVal == wheel.B_RUPT) {
					System.out.println("Player " + (cp + 1) + " goes bankrupt.");
					players[cp++].bankrupt();
				} else if (guessVal == wheel.L_TURN) {
					System.out.println("Player " + (cp + 1) + " loses a turn.");
					players[cp++].skipTurn();
				} else {
					// heres where they guess
					// for now lets just add to their balance and move on.
					System.out.println("Your spin is of value: " + guessVal + ".\nPlease enter a character guess and press enter. ");
					char letterGuessed = sc.nextLine().charAt(0);
					while (guesses.indexOf(letterGuessed) != -1) {
						System.out.println("Invalid guess. Please try again.");
						letterGuessed = sc.nextLine().charAt(0);
					}
					int hits = guess(letterGuessed);
					if (hits == 0) {
						System.out.println("\nIncorrect guess.\nPlayer " + (cp + 1) + " turn is over.");
						cp++;
					} else {
						System.out.println("\nPlayer " + (cp + 1) + " receives " + (guessVal * hits) + ".\nbalance is now " + ((guessVal * hits) + players[cp].getBal()) + ".");
						players[cp].addBal(guessVal * hits);
						if (players[cp].getBal() > 10000) {
							System.out.println("Player " + (cp + 1) + " is the winner!");
							playing = false;
						} else if (completed(PHRASES[round])) {
							System.out.println("Player " + (cp + 1) + " wins the round.");
							players[cp].addBank(players[cp].getBal());
							for (Player player: players) {
								player.setBal(0);
							}
							guesses = PUNC;

							round++;
							round %= PHRASES.length;
						}
					}
				}
			}

		}
	}

	public static boolean completed(String secret) {
		for (char s: secret.toCharArray()) {
			if (guesses.indexOf(s) == -1) {
				return false;
			}
		}
		return true;
	}

	public static int guess(char letter) {
		// change this to ignore the case of the letter
		guesses += letter;
		int hits = 0;
		for (char secretLetter: PHRASES[round].toCharArray()) {
			if (guesses.indexOf(secretLetter) == -1) {
				System.out.print('*');
			} else {
				System.out.print(secretLetter);
				if (secretLetter == letter) {
					hits++;
				}
			}
		}
		return hits;
	}
}