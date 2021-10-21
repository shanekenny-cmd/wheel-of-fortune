/* WoFCmd.java
 * Shane Kenny, Isreal Perea, Kyle Holzshu
 * CSC381: Software Engineering
 * just runs the game in a text based format
 * establishing game logic to be implemented graphically in later sprints
 */

import java.util.Scanner;


public class WoFCmd {

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

			cp %= 4;// makes sure index of player in players array stays in bounds

			if (!players[cp].hasTurn()) {// checks to see if the player has a turn

				System.out.println("Player " + (cp + 1) + " turn skipped.");
				players[cp].turnSkipped();
				cp++;

			} else {// if they do

				System.out.println("Player " + (cp + 1) + "'s turn.");
				// put their options here
				// this is where they can choose to solve the puzzle
				int option = selectOption();

				if (option == 2) {// they elected to solve

					System.out.println("Please enter your guess: ");
					String solution = sc.nextLine();
					if (check(solution)) {
						System.out.println("Correct!");
						round++;
						guesses = PUNC;
					} else {

						System.out.println("Incorrect.");
						cp++;
					}

				} else if (option == 1) {// they elected to spin the wheel

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
	}

	public static int selectOption() {
		System.out.println("Please select one of the following options:\n1. Spin Wheel\n2. Solve");
		sc = new Scanner(System.in);
		int result = Integer.parseInt(sc.nextLine().charAt(0) + "");
		return result;
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

	public static boolean check(String guess) {
		System.out.println(" -" + guess + "- ");
		System.out.println(" -" + PHRASES[round] + "- ");
		if (guess.length() != PHRASES[round].length()) {
			return false;
		}
		for (int i = 0; i < guess.length(); i++) {
			if ((guess.toCharArray())[i] != (PHRASES[round].toCharArray())[i]) {
				return false;
			}
		}
		return true;
	}
}