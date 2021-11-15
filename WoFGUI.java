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
        wof.setSize(1088, 800);
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
                // // round is over
                // // bank currentPlayers balance
                // getNewClue();
                // initDisplayedText();
                // roundNumber++;

                // for (JButton b: buttons) {
                //     b.setEnabled(true);
                // }
                solutionField.setText("");
                repaint();
            } else {
                solutionField.setText("");
                wf.nextPlayer();
                repaint();
            }
            // solutionField.setText("");
            // repaint();
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
            spinValue = wf.getWheel().spinWheel();
            wheelMessages = "";
            if (spinValue == 2) {
                // bankrupt current player
                wheelMessages += "Player " + (wf.getCP() + 1) + " goes bankrupt.";
                wf.bankrupt();
            } else if (spinValue == 1) {
                // current player loses turn
                wheelMessages += "Player " + (wf.getCP() + 1) + " loses a turn.";
                wf.nextPlayer();
            } else {
                solutionField.setEnabled(false);
                buyVowel.setEnabled(false);
                spin.setEnabled(false);
                spun = true;
                wheelMessages += "Spin Value: " + spinValue + ".";
            }
            repaint();
        }
        
    }
    private class BuyVowelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // disable solve and buy vowel buttons
            // only allow contestants to buy a vowel if they have enough money(500)
            solutionField.setEnabled(false);
            buyVowel.setEnabled(false);
            spin.setEnabled(false);
            buyingVowel = true;
            repaint();
        }
        
    }

    /**
     * 
     * An ActionListener implementation for the nextRound button. 
     *
     *
     */
    private class NextRoundListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            getNewClue();
            initDisplayedText();

            for (JButton b: buttons) {
                b.setEnabled(true);
            }
            spin.setEnabled(true);
            buyVowel.setEnabled(true);
            solutionField.setEnabled(true);
            playAgain.setEnabled(false);
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
            try {
                wf = new WheelOfFortune();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            getNewClue();

            buyingVowel = false;
            tossUpRound = false;
            spun = false;
            spinValue = 0;
            roundNumber = 0;

            initDisplayedText();
            // initPlayerInfo();
            setLayout(new FlowLayout());
            initButtons();
            initTextField();
            setVisible(true);
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
    private class QuitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
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
    private String wheelMessages;
    private String playerInfo;
    private String currentPlayer;

    private CharacterListener charListener;
    private Container container;
    private JButton[] buttons = new JButton[26];
    private JButton playAgain;
    private JButton quitButton;
    private JButton spin;
    private JButton buyVowel;
    private JButton nextRound;
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
     * @throws IOException WheelOfFortune constructor uses Scanner which throws IOException FileNotFoundException
     * 
     */
    public WoFGUI() throws IOException {
        super("Wheel Of Fortune");
        wf = new WheelOfFortune();
        getNewClue();

        wheelMessages = "";

        buyingVowel = false;
        tossUpRound = false;
        spun = false;
        spinValue = 0;
        roundNumber = 0;

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

        // add next round button here - enabled if the round is over, blocking
        nextRound = new JButton("Next Round");
        container.add(nextRound);
        nextRound.addActionListener(new NextRoundListener());
        nextRound.setEnabled(false);

        playAgain = new JButton("Play Again");// change to reset the whole game, only enabled when game over
        container.add(playAgain); 
        playAgain.addActionListener(new PlayAgainListener());
        playAgain.setEnabled(false);

        // add quit button here - enable when game over, just System.exit(0) if clicked
        quitButton = new JButton("Quit");
        container.add(quitButton);
        quitButton.addActionListener(new QuitButtonListener());
        quitButton.setEnabled(true);

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
        
        g2.drawString(displayedText, 100, 175);

        g2.drawString(wheelMessages, 100, 200);

        currentPlayer = "Player " + (wf.getCP() + 1) + "'s turn.";
        g2.drawString(currentPlayer, 100, 225);

        playerInfo = wf.getPlayerString();
        g2.drawString(playerInfo, 100, 250);

        g2.drawString("Round: " + (roundNumber + 1), 100, 275);

        nextRound.setEnabled(false);

        for (JButton b: buttons) {
            if (wf.getClue().getGuesses().indexOf(Character.toLowerCase(b.getText().charAt(0))) == -1 && spun) {
                b.setEnabled(true);
            } else {
                b.setEnabled(false);
            }
        }

        if (buyingVowel) {
            for (JButton b: buttons) {
                // if it is a vowel, and it hasn't been guessed, enable it
                displayedText += b.getText().charAt(0) + " ";
                if ("aeiou".indexOf(Character.toLowerCase(b.getText().charAt(0))) != -1 && wf.getClue().getGuesses().indexOf(Character.toLowerCase(b.getText().charAt(0))) == -1) {
                    b.setEnabled(true);
                }
            }
        }

        if (tossUpRound) {
            for (JButton b: buzzers) {
                b.setEnabled(true);
            }
        }

        if (isCompleted()) {
            for (JButton b: buttons) {
                b.setEnabled(false);
            }
            spin.setEnabled(false);
            buyVowel.setEnabled(false);
            solutionField.setEnabled(false);
            nextRound.setEnabled(true);
            Ellipse2D.Double leftEye = new Ellipse2D.Double(100, 500, 5, 5);
            g2.draw(leftEye);
            Ellipse2D.Double rightEye = new Ellipse2D.Double(118, 500, 5, 5);
            g2.draw(rightEye);
            Arc2D.Double mouth = new Arc2D.Double(100, 519, 23, 12, 0, -180, Arc2D.OPEN);
            g2.draw(mouth);
            if (roundNumber < 4) {
                roundNumber++;
            }
        }

        if (roundNumber >= 4) {
            g2.drawString("Game Over.", 100, 300);
            // call method to wtite game data to file in wf
            // ask playAgain
        }
    }

    /**
     * 
     * @return true if the players have completed the clue
     */
    private boolean isCompleted() {
       	return wf.getClue().completed();
    }
}