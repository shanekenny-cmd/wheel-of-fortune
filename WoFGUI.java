import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.io.IOException;


public class WoFGUI extends JFrame {

	/**
	 * The main method to create run our Wheel of fortune GUI. Very compact, 
	 * 	so it doesn't need its own class.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
        WoFGUI wof = new WoFGUI();
        wof.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wof.setSize(1000, 1000);
        wof.setLocationRelativeTo(null);
        wof.setVisible(true);
    }
	
	
	
	
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**************************************************************************************************************/

	
	
		
	/**
	 * 
	 * An ActionListener implementation for buttons representing alphabet characters.
	 * When one of these buttons is clicked, it will check if the movie title contains it. If it does,
	 * 	all occurrences of that letter will be displayed on the screen. If not, another body part
	 * 	of Hang Man will appear.
	 *
	 */
	private class CharacterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			char label = ((JButton) (e.getSource())).getText().charAt(0);
    		
			// identify the button
			int index = (int) (label) - ((int) 'A');
			buttons[index].setEnabled(false); // disable this character's button for further use

            if (buyingVowel) {
                if (!wf.buyVowel(label)) {
                    wf.nextPlayer();
                }
                buyingVowel = false;
            } else {
                if (!wf.playerGuess(label, spinValue)) {
                    wf.nextPlayer();
                }
            }

            displayedText = wf.getClue().getDisplayPhrase();
            spun = false;
            spin.setEnabled(true);
            buyVowel.setEnabled(true);
            solutionField.setEnabled(true);
			repaint();
		}
	}

    /**
     * 
     * An ActionListener implementation for the PlayAgain button. Button only enabled after the player either wins
     *  or loses a round. When pressed, everything resets with a new movie title.
     *
     *
     */
    private class SolutionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String sol = solutionField.getText();
            if (wf.submitSolve(sol)) {
                // round is over
                // bank currentPlayers balance
                getNewClue();
                initDisplayedText();

                for (JButton b: buttons) {
                    b.setEnabled(true);
                }
            } else {
                wf.nextPlayer();
            }
            solutionField.setText("");
            repaint();
        }
        
    }

    /**
     * 
     * An ActionListener implementation for the Buzzer buttons for the tossup rounds. only enabled when
     * it is a tossup round. when one is pressed everything is disabled until the player that buzzed in
     * enters their solution.
     * 
     */
    private class BuzzerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // "Player x"
            //  01234567
            int label = Integer.parseInt(((JButton) (e.getSource())).getText().charAt(7) + "");
            wf.setCurrentPlayer(label);

            for (JButton b: buttons) {
                b.setEnabled(false);
            }

            for (JButton b: buzzers) {
                b.setEnabled(false);
            }



            repaint();
        }
        
    }

    /**
     * 
     * An ActionListener implementation for the spin button. This spins the wheel and returns a random value on it
     * this disables the 
     *
     */
    private class SpinListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // disable solve and buy vowel buttons
            solutionField.setEnabled(false);
            buyVowel.setEnabled(false);
            spin.setEnabled(false);
            spinValue = wf.getWheel().spinWheel();
            if (spinValue == 2) {
                // bankrupt current player
                wf.bankrupt();
                displayedText += " Player " + (wf.getCP() + 1) + " goes bankrupt.";
            } else if (spinValue == 1) {
                // current player loses turn
                displayedText += " Player " + (wf.getCP() + 1) + " loses a turn.";
                wf.nextPlayer();
            } else {
                spun = true;
                displayedText += " value: " + spinValue;
            }
            repaint();
        }
        
    }
    private class BuyVowelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // disable solve and buy vowel buttons
            solutionField.setEnabled(false);
            buyVowel.setEnabled(false);
            spin.setEnabled(false);
            buyingVowel = true;
            repaint();
        }
        
    }
    
		
	/**
	 * 
	 * An ActionListener implementation for the PlayAgain button. Button only enabled after the player either wins
	 * 	or loses a round. When pressed, everything resets with a new movie title.
	 *
	 *
	 */
	private class PlayAgainListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wf.isEmpty()) {
				displayedText = "We are all out of movie titles. Sorry!";
			} else {
    			getNewClue();
    			initDisplayedText();

    			for (JButton b: buttons) {
    				b.setEnabled(true);
    			}
			}
			spin.setEnabled(true);
            buyVowel.setEnabled(true);
            solutionField.setEnabled(true);
			playAgain.setEnabled(false);
			repaint();
		}
		
	}
	
	
	
	
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**************************************************************************************************************/

	
	
	
	private static final long serialVersionUID = 1L;

    private WheelOfFortune wf; 
    private int incorrectGuessCount;
    private String[] dashes;
    
    private String actualText;
    private String displayedText;
    private String playerInfo;
    private String currentPlayer;

    private CharacterListener charListener;
    private Container container;
    private JButton[] buttons = new JButton[26];
    private JButton playAgain;
    private JButton spin;
    private JButton buyVowel;
    private JButton[] buzzers;
    private JTextField solutionField;

    // game state variables
    private int roundNumber;
    private boolean buyingVowel;
    private boolean tossUpRound;
    private boolean spun;
    private int spinValue;

    
    
    
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**************************************************************************************************************/
	/**************************************************************************************************************/

    
    
    /**
     * 
     * @throws IOException HangmanMaker constructor uses a scanner to read a file, which will throw the exception
     * 		if file not present.
     * 
     */
    public WoFGUI() throws IOException {
        super("Wheel Of Fortune");
        wf = new WheelOfFortune();
        getNewClue();

        buyingVowel = false;
        tossUpRound = false;
        spun = false;
        spinValue = 0;

        initDisplayedText();
        // initPlayerInfo();
        setLayout(new FlowLayout());
        initButtons();
        initTextField();
        setVisible(true);
    }
    
    /**
     * Initiates or re-initiates a few fields
     */
    private void getNewClue() {
    	actualText = wf.getNext();       
        dashes = new String[actualText.length()];
        incorrectGuessCount = 0;
        displayedText = "";
    }
    
    /**
     * Initializes the displayedText variable, which is then displayed on the GUI.
     */
    private void initDisplayedText() {
        displayedText = wf.getClue().getDisplayPhrase();
        // for (int i = 0; i < actualText.length(); i++) {
        //     if (actualText.charAt(i) == ' ') {
        //     	dashes[i] = "      ";
        //     }
        //     else {
        //     	dashes[i] = "_____ ";
        //     }
            
        //     displayedText += dashes[i];
        // }  
    }

    // /**
    //  * Initializes the playerInfo variable, which is displayed on the GUI
    //  */
    // private void initPlayerInfo() {
    //     playerInfo = wf.getPlayerString();
    // }
    
    /**
     * Initializes all the buttons of the GUI, references the private classes written at the top.
     */
    private void initButtons() {
    	container = getContentPane();
    	charListener = new CharacterListener();

        for (int i = 0; i < 26; i++) {
        	// Generate every character in the alphabet and use as labels for buttons
        	char alpha = (char) ((int) 'A' + i);
        	buttons[i] = new JButton(String.valueOf(alpha));
        	container.add(buttons[i]);
        	buttons[i].addActionListener(charListener);
            buttons[i].setEnabled(false);
        }
        buzzers = new JButton[wf.numPlayers()];
        for (int i = 0; i < buzzers.length; i++) {
            buzzers[i] = new JButton("Player " + (i + 1));
            container.add(buzzers[i]);
            buzzers[i].addActionListener(new BuzzerListener());
            buzzers[i].setEnabled(false);
        }
        spin = new JButton("Spin");
        container.add(spin);
        spin.addActionListener(new SpinListener());
        spin.setEnabled(true);

        buyVowel = new JButton("Buy Vowel");
        container.add(buyVowel);
        buyVowel.addActionListener(new BuyVowelListener());
        buyVowel.setEnabled(true);

        playAgain = new JButton("Play Again?"); 
        container.add(playAgain); 
        playAgain.addActionListener(new PlayAgainListener());
        playAgain.setEnabled(false);
    }

    /**
     * Initializes the text fild of the GUI, references the private classes written at the top.
     */
    private void initTextField() {
        container = getContentPane();
        solutionField = new JTextField(20);
        container.add(solutionField);
        solutionField.addActionListener(new SolutionListener());
        solutionField.setEnabled(true);
    }

    /**
     * Overrides the super class's paint method. 
     * repaint() will call this method without recreating the pop up window. 
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawString(displayedText, 250, 175);

        currentPlayer = "Player " + (wf.getCP() + 1) + "'s turn.";
        g2.drawString(currentPlayer, 250, 225);

        playerInfo = wf.getPlayerString();
        g2.drawString(playerInfo, 250, 300);

        g2.drawString(roundNumber + "", 250, 325);

        for (JButton b: buttons) {
            if (wf.getClue().getGuesses().indexOf(b.getText().charAt(0)) == -1 && spun) {
                b.setEnabled(true);
            } else {
                b.setEnabled(false);
            }
        }

        if (buyingVowel) {
            for (JButton b: buttons) {
                // if it is a vowel, and it hasn't been guessed, enable it
                displayedText += b.getText().charAt(0) + " ";
                if ("AEIOU".indexOf(b.getText().charAt(0)) != -1 && wf.getClue().getGuesses().indexOf(b.getText().charAt(0)) == -1) {
                    b.setEnabled(true);
                }
            }
        }

        if (tossUpRound) {
            for (JButton b: buzzers) {
                b.setEnabled(true);
            }
        }

        if (isWinner()) {
            for (JButton b: buttons) {
                b.setEnabled(false);
            }
            spin.setEnabled(false);
            buyVowel.setEnabled(false);
            solutionField.setEnabled(false);
            Ellipse2D.Double leftEye = new Ellipse2D.Double(390, 265, 5, 5);
            g2.draw(leftEye);
            Ellipse2D.Double rightEye = new Ellipse2D.Double(408, 265, 5, 5);
            g2.draw(rightEye);
            Arc2D.Double mouth = new Arc2D.Double(390, 284, 23, 12, 0, -180, Arc2D.OPEN);
            g2.draw(mouth);
            roundNumber++;
            if (roundNumber > 4) {
                g2.drawString("Game Over.", 250, 400);
            } else {
                playAgain.setEnabled(true);
            }
        }
    }

    /**
     * 
     * @return true if the players have completed the clue
     */
    private boolean isWinner() {
       	return wf.getClue().completed();
    }
}