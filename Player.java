/* Player.java
 * Shane Kenny, Israel Perea, Kyle Holzshu
 * CSC381: Software Engineering
 */

// private fields: boolean skipTurn, int balance, int banked

// constructor - default vals

// setters and getters - on/off, and check for bool. add, set, get for bal and bank.


public class Player {

	private int bal;
	private int bank;
	private boolean loseTurn;

	Player() {
		this.bal = 0;
		this.bank = 0;
		this.loseTurn = false;
	}

	public int getBal() {
		return this.bal;
	}
	public void setBal(int newBal) {
		this.bal = newBal;
	}
	public void addBal(int dep) {
		this.bal += dep;
	}

	public int getBank() {
		return this.bank;
	}
	public void setBank(int newBank) {
		this.bank = newBank;
	}
	public void addBank(int dep) {
		this.bank += dep;
	}

	public boolean hasTurn() {
		return !(this.loseTurn);
	}
	public void skipTurn() {
		loseTurn = true;
	}
	public void turnSkipped() {
		loseTurn = false;
	}

	public void bankrupt() {
		this.setBal(0);
	}

}