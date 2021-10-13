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

	public final int B_RUPT = 1, L_TURN = 2;
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

		int ruptIndex = this.random.nextInt(22) + 1; // gives index from 1-23 inclusive
		wheel[ruptIndex] = B_RUPT;
		wheel[ruptIndex - 1] = L_TURN;
		wheel[ruptIndex + 1] = L_TURN;
		for (int i = 0; i < ruptIndex - 1; i++) {
			this.wheel[i] = this.N_VALS[this.random.nextInt(this.N_VALS.length)];
		}
		for (int i = ruptIndex + 2; i < this.wheel.length; i++) {
			this.wheel[i] = this.N_VALS[this.random.nextInt(this.N_VALS.length)];
		}
		int topIndex = (this.random.nextInt(22) + ruptIndex + 2) % this.wheel.length;
		this.wheel[topIndex] = this.T_DOLL[round - 1];

	}

	public int spinWheel() {
		return this.wheel[this.random.nextInt(24)];
	}
}