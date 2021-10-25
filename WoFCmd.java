/* WoFCmd.java
 * Shane Kenny, Isreal Perea, Kyle Holzshu
 * CSC381: Software Engineering
 * just runs the game in a text based format
 * establishing game logic to be implemented graphically in later sprints
 */

import java.util.Scanner;


public class WoFCmd {

	public static final int GAME_DURATION = 60 * 2; // two mins

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

		new TimerDemo(GAME_DURATION, players);

		boolean playing = true;
		int cp = 0;

		while (playing) {// while playing, cycle thru players

			cp %= 4;// makes sure index of player in players array stays in bounds

			if (!players[cp].hasTurn()) {// player is skipped

				System.out.println("Player " + (cp + 1) + " turn skipped.");
				players[cp].turnSkipped();
				cp++;

			} else {// player takes a turn

				System.out.println("Player " + (cp + 1) + "'s turn.");
				// put their options here
				// this is where they can choose to solve the puzzle
				System.out.println("Please select one of the following options:\n1. Spin Wheel\n2. Solve\n3. Buy a vowel");

				int option = selectOption(3);

				if (option == 3) {// player buys a vowel

					// have them buy a vowel
					System.out.println("Please select a vowel to buy: ");
					char vowel = getVowel();
					System.out.println("$500 subtracted from Player " + (cp + 1) + "'s balance.");
					players[cp].addBal(-500);
					System.out.println("Player " + (cp + 1) + "'s balance is now " + players[cp].getBal());
					int appears = guess(vowel);
					System.out.println("\n" + vowel + " appears " + appears + " times.");

				} else if (option == 2) {// player elects to solve

					System.out.println("Please enter your guess: ");
					String solution = sc.nextLine();
					if (check(solution)) { // player guesses correctly
						System.out.println("Correct!");
						round++;
						guesses = PUNC;
						System.out.println("New clue: ");
						guess(' ');
						System.out.println("");
					} else { // player guesses incorrectly

						System.out.println("Incorrect.");
						cp++;
					}

				} else if (option == 1) {// player spins wheel

					int guessVal = wheel.spinWheel();
					if (guessVal == wheel.B_RUPT) {

						System.out.println("Player " + (cp + 1) + " goes bankrupt.");
						players[cp++].bankrupt(); // player goes bankrupt

					} else if (guessVal == wheel.L_TURN) {

						System.out.println("Player " + (cp + 1) + " loses a turn.");
						players[cp++].skipTurn();// player loses a turn

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
						}// player guesses a letter
					}

				}
			}

		}
	}

	public static char getVowel() {
		sc = new Scanner(System.in);
		char vowel = sc.nextLine().charAt(0);
		vowel = ((vowel + "").toLowerCase()).charAt(0);
		String vowels = "aeiou";
		if (vowels.indexOf(vowel) != -1) {
			return vowels.charAt(vowels.indexOf(vowel));
		}
		System.out.println("Please select a valid vowel.");
		return getVowel();
	}

	public static int selectOption(int range) {
		sc = new Scanner(System.in);
		int result = -1;
		try {

			result = Integer.parseInt(sc.nextLine().charAt(0) + "");
			if (result > range || result < 1) {
				System.out.println("Please select an option from 1 to " + range + ".");
				return selectOption(range);
			}
			return result;

		} catch (Exception e) {

			System.out.println("Invalid input. Please try again.");
			return selectOption(range);

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