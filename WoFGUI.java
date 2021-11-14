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

            wf.getClue().check(label);

            displayedText = wf.getClue().getDisplayPhrase();

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
            }
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
    private class BuzzerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            char label = ((JButton) (e.getSource())).getText().charAt(0);
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

    private CharacterListener charListener;
    private Container container;
    private JButton[] buttons = new JButton[26];
    private JButton playAgain;
    private JButton solve;
    private JButton spin;
    private JButton buyVowel;
    private JButton[] buzzers;
    private JTextField solutionField;

    private boolean buyingVowel;
    private boolean tossUpRound;

    
    
    
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
        super("Hangman: Movie Titles");
        wf = new WheelOfFortune();
        getNewClue();

        buyingVowel = false;
        tossUpRound = false;

        initDisplayedText();
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
        }
        buzzers = new JButton[wf.numPlayers()];
        for (int i = 0; i < buzzers.length; i++) {
            buzzers[i] = new JButton("Player " + (i + 1));
            container.add(buzzers[i]);
            buzzers[i].addActionListener(new BuzzerListener());
            buzzers[i].setEnabled(false);
        }
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

        if (buyingVowel) {
            for (JButton b: buttons) {
                if ("aeioou".indexOf(b.getText().charAt(0)) == -1) {
                    b.setEnabled(false);
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
            Ellipse2D.Double leftEye = new Ellipse2D.Double(390, 265, 5, 5);
            g2.draw(leftEye);
            Ellipse2D.Double rightEye = new Ellipse2D.Double(408, 265, 5, 5);
            g2.draw(rightEye);
            Arc2D.Double mouth = new Arc2D.Double(390, 284, 23, 12, 0, -180, Arc2D.OPEN);
            g2.draw(mouth);
            playAgain.setEnabled(true);
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