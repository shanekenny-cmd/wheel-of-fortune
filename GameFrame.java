/* GameFrame.java
 */

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GameFrame extends JFrame {

	static final int SCREEN_WIDTH = 1024, SCREEN_HEIGHT = 480;

	Clue clue;
	Player[] players;
	Wheel wheel;
	int round, cp;
	char letterGuessed;
	boolean playing;

	JFrame frame = new JFrame();
	JTextField textField = new JTextField();
	JTextArea textArea = new JTextArea();
	JButton[] btnLetters = new JButton[26];

	JLabel timeLabel = new JLabel();
	JLabel timeLeft = new JLabel();
	
	public GameFrame() {

		frame.setTitle("Wheel Of Fortune");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(SCREEN_WIDTH, 600);
		frame.setLocationRelativeTo(null);

		frame.getContentPane().setBackground(Color.black);
		frame.setLayout(null);

		textField.setFont(new Font("Courier New", Font.PLAIN, 16));
		textField.setBounds(0, 0, SCREEN_WIDTH, textField.getFont().getSize());
		textField.setBackground(Color.black	);
		textField.setForeground(Color.green);
		textField.setBorder(BorderFactory.createBevelBorder(2));
		textField.setHorizontalAlignment(JTextField.LEFT);
		textField.setEditable(false);
		textField.setText("Welcome to Wheel Of Fortune");

		clue = new Clue();

		textArea.setFont(new Font("Courier New", Font.PLAIN, 16));
		textArea.setBounds(0, textField.getFont().getSize(), SCREEN_WIDTH, textArea.getFont().getSize());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(Color.black	);
		textArea.setForeground(Color.green);
		textArea.setBorder(BorderFactory.createBevelBorder(2));
		textArea.setEditable(false);
		textArea.setText(clue.getDisplayPhrase());

		FontMetrics metrics = getFontMetrics(new Font("Courier New", Font.PLAIN, 16));

		int width = metrics.stringWidth("A") * 2;
		int height = (new Font("Courier New", Font.PLAIN, 16)).getSize() * 2;

		int w = (SCREEN_WIDTH / 2) - (2 * 15) - metrics.stringWidth("A");

		for (int i = 0; i < btnLetters.length; i++) {
			btnLetters[i] = new JButton();
			btnLetters[i].setBounds(i * width, 450, width, height);
			btnLetters[i].setFocusable(false);
			btnLetters[i].addActionListener(new CharacterListener());
			btnLetters[i].setBackground(Color.black);
			btnLetters[i].setForeground(Color.green);
			btnLetters[i].setFont(new Font("Courier New", Font.PLAIN, 16));
			//btnLetters[i].setBorder(new LineBorder(Color.green));
			btnLetters[i].setText((char)(i + 65) + "");
		}

		try {
		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
		    e.printStackTrace();
		}

		frame.add(textField);
		frame.add(textArea);
		for (JButton btn: btnLetters) {
			frame.add(btn);
		}
		frame.setVisible(true);
	}

	public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

    }

	private class CharacterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			char label = ((JButton) (e.getSource())).getText().charAt(0);
    		
			// identify the button
			int index = (int) (label) - ((int) 'A');
			btnLetters[index].setEnabled(false); // disable this character's button for further use

			int result = clue.check(label);
			
			if (result == Clue.REPEAT) { // character already guessed

			} else if (result == Clue.MISS) { // character not in clue

			} else { // character has certain number of hits(stored in result)

				if (result == 0) {

					//System.out.println("\nIncorrect guess.\nPlayer " + (cp + 1) + " turn is over.");
					cp++;

				} else {

				}// player guesses a letter
			}
			repaint();		}
	}
}