/* Wheel.java
 * Shane Kenny, Israel Perea, Kyle Holzshu
 * CSC381: Software Engineering
 */

// constructor - sets default values

// newWheel - randomizes the wheel

// spinWheel - returns a value from the wheel

/*
 24 spaces, dollar amounts ranging from $500 to $900, 
 plus a top dollar value: $2,500 in round 1, $3,500 in rounds 2 and 3, 
 and $5,000 for round 4 and any subsequent rounds. 
 The wheel also features two Bankrupt wedges and one Lose a Turn
*/

 // refreshing the wheel - 
	 // fill with normal values around the lturn, brupt, lturn thing.
	 // pick a value between 0 and 21, add lturnright mod 24 to get index that isnt one of the three

import java.util.Random;

public class Wheel {

	public final int L_TURN = 1, B_RUPT = 2;
	private final int[] T_DOLL = {2500, 3500, 3500, 5000};
	private final int[] N_VALS = {500, 600, 700, 800, 900};
	private Random random;
	private int[] wheel;

	public Wheel () {

		this.random = new Random();
		this.wheel = new int[24];
		this.newWheel(1);

	}

	public void newWheel(int round) {

		int lTurnIndex = this.random.nextInt(22) + 1; // gives index from 1-23 inclusive
		wheel[lTurnIndex] = L_TURN;
		wheel[lTurnIndex - 1] = B_RUPT;
		wheel[lTurnIndex + 1] = B_RUPT;
		for (int i = 0; i < lTurnIndex - 1; i++) {
			this.wheel[i] = this.N_VALS[this.random.nextInt(this.N_VALS.length)];
		}
		for (int i = lTurnIndex + 2; i < this.wheel.length; i++) {
			this.wheel[i] = this.N_VALS[this.random.nextInt(this.N_VALS.length)];
		}
		int topIndex = (this.random.nextInt(22) + lTurnIndex + 2) % this.wheel.length;
		if ((round - 1) > 3) {
			this.wheel[topIndex] = this.T_DOLL[3];
		} else {
			this.wheel[topIndex] = this.T_DOLL[round - 1];
		}
	}

	public int spinWheel() {
		return this.wheel[this.random.nextInt(24)];
	}
}