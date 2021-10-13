/* GamePanel.java
 * Shane Kenny, Israel Perea, Kyle Holzshu
 * CSC381: Software Engineering
 */

import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;

public class GamePanel extends JPanel /*implements ActionListener*/{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;

	GamePanel() {

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		startGame();

	}

	public void startGame() {
		// this is where the game logic will begin
	}

}