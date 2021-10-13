/* GameFrame.java
 * Shane Kenny, Israel Perea, Kyle Holzshu
 * CSC381: Software Engineering
 * This is the Game Frame class - opens and formats the window of the game
 */

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	GameFrame() {

		this.add(new GamePanel());
		this.setTitle("Wheel Of Fortune");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);

	}

}