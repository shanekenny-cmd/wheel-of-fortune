/* Player.java
 * Shane Kenny, Israel Perea, Kyle Holzshu
 * CSC381: Software Engineering
 */

// private fields: boolean skipTurn, int balance, int banked

// constructor - default vals

// setters and getters - on/off, and check for bool. add, set, get for bal and bank.
import java.util.Random;

public class Player {

	private int bal;
	private int bank;
	private char[] id;// length 5, base64, 1,073,741,828 possible id's
	private boolean loseTurn;
	private char[] base;
	private Random random;
	private String name;

	Player() {
		this.bal = 0;
		this.bank = 0;
		this.loseTurn = false;
		this.fillBase();
		this.newID();
		this.name = "";
	}

	private void fillBase(){
		base = new char[64];
		int j = 0;
		for (int i = 48; i < 58; i++) {
			base[j++] = (char)i;
		}
		for (int i = 65; i < 91; i++) {
			base[j++] = (char)i;
		}
		for (int i = 97; i < 123; i++) {
			base[j++] = (char)i;
		}
		base[j++] = (char)45;
		base[j] = (char)95;
	}

	public String newID() {
		this.random = new Random();
		id = new char[5];
		for (int i = 0; i < id.length; i++) {
			id[i] = base[random.nextInt(64)];
		}
		return new String(id);
	}
	public String getID() {
		return new String(id);
	}
	public boolean setID(char[] id) {
		if (id.length != 5) {
			// throw an exception
			//System.out.println("INVALID PLAYER ID");
			return false;
		} else {
			for (char c: id) {
				if ((new String(this.base)).indexOf(c) < 0) {
					// throw exeption
					//System.out.println("INVALID PLAYER ID");
					return false;
					//break;
				}
			}
			this.id = id;
		}
		return true;
	}

	public String getName() {
		return this.name;
	}
	public boolean setName(String name) {
		if (name.length() < 8) {
			this.name = name;
			return true;
		}
		return false;
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