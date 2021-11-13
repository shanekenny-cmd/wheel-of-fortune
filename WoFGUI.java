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
			
			if (actualText.indexOf(label) == -1) { // character not in the movie title
				incorrectGuessCount++;
			} else {
				for (int i = 0; i < actualText.length(); i++) {
					if (actualText.charAt(i) == label) {
						displayedText = "";
			   
						if (i == actualText.length() - 1) {
							dashes[i] = "  " + label;
						} else {
							dashes[i] = "  " + label + "  ";
						}
						
						for (String s : dashes) {
							displayedText += s;
						}
					}
				}
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
    private class SolutionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String sol = solutionField.getText();
            wf.submitSolve(sol);
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
    			getMovieTitle();
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
    private JTextField solutionField;

    
    
    
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
        getMovieTitle();

        initDisplayedText();
        setLayout(new FlowLayout());
        initButtons();
        initTextField();
        setVisible(true);
    }
    
    /**
     * Initiates or re-initiates a few fields
     */
    private void getMovieTitle() {
    	actualText = wf.getNext();       
        dashes = new String[actualText.length()];
        incorrectGuessCount = 0;
        displayedText = "";
    }
    
    /**
     * Initializes the displayedText variable, which is then displayed on the GUI.
     */
    private void initDisplayedText() {
        for (int i = 0; i < actualText.length(); i++) {
            if (actualText.charAt(i) == ' ') {
            	dashes[i] = "      ";
            }
            else {
            	dashes[i] = "_____ ";
            }
            
            displayedText += dashes[i];
        }  
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
        
        // GALLOWS
        Line2D.Double post = new Line2D.Double(200, 600, 200, 200); //post
        g2.draw(post);
        Line2D.Double top = new Line2D.Double(200, 200, 400, 200); //top
        g2.draw(top);
        Line2D.Double rope = new Line2D.Double(400, 200, 400, 250); //rope
        g2.draw(rope);
        Line2D.Double bottom = new Line2D.Double(100, 600, 300, 600);
        g2.draw(bottom);
        
        if (incorrectGuessCount >= 1) { //HEAD
            Ellipse2D.Double head = new Ellipse2D.Double(370, 250, 60, 60);
            g2.draw(head);
        }

        if (incorrectGuessCount >= 2) { //BODY
            Line2D.Double body = new Line2D.Double(400, 310, 400, 420);
            g2.draw(body);
        }

        if (incorrectGuessCount >= 3) { // LEFT ARM
            Line2D.Double leftArm = new Line2D.Double(400, 360, 330, 360);
            g2.draw(leftArm);
        }

        if (incorrectGuessCount >= 4) { // RIGHT ARM
            Line2D.Double rightArm = new Line2D.Double(470, 360, 400, 360);
            g2.draw(rightArm);
        }

        if (incorrectGuessCount >= 5) { // RIGHT LEG
            Line2D.Double rightLeg = new Line2D.Double(400, 420, 455, 475);
            g2.draw(rightLeg);
        }

        if (incorrectGuessCount >= 6) { // LEFT LEG
            Line2D.Double leftLeg = new Line2D.Double(400, 420, 345, 475);
            g2.draw(leftLeg);
        }

        if (incorrectGuessCount >= 7) {
            for (JButton b: buttons) {
                b.setEnabled(false);
            }
            
            //EYE12
            Line2D.Double eye11 = new Line2D.Double(385, 265, 390, 275);
            g2.draw(eye11);
            //EYE12
            Line2D.Double eye12 = new Line2D.Double(390, 265, 385, 275);
            g2.draw(eye12);
            //EYE21
            Line2D.Double eye21 = new Line2D.Double(410, 265, 415, 275);
            g2.draw(eye21);
            //EYE22
            Line2D.Double eye22 = new Line2D.Double(415, 265, 410, 275);
            g2.draw(eye22);
            //MOUTH
            Line2D.Double mouth = new Line2D.Double(390, 290, 410, 290);
            g2.draw(mouth);
            Arc2D.Double mouth1 = new Arc2D.Double(400, 285, 8, 12, 0, -180, Arc2D.OPEN);
            g2.draw(mouth1);
            
            g2.drawString(actualText, 450, 200);
            playAgain.setEnabled(true);
        }

        if (isWinner()) {
            for (JButton b: buttons) {
                b.setEnabled(false);
            }

            if (incorrectGuessCount >= 1) {
                Ellipse2D.Double leftEye = new Ellipse2D.Double(390, 265, 5, 5);
                g2.draw(leftEye);
                Ellipse2D.Double rightEye = new Ellipse2D.Double(408, 265, 5, 5);
                g2.draw(rightEye);
                Arc2D.Double mouth = new Arc2D.Double(390, 284, 23, 12, 0, -180, Arc2D.OPEN);
                g2.draw(mouth);
                playAgain.setEnabled(true);
            }
        }
    }

    /**
     * 
     * @return true if the player's guess matches the movie title.
     * 
     * Checked by removing whitespace from each string and checking their equality.
     */
    private boolean isWinner() {
        String s = displayedText.replaceAll("\\s+", "");
        String t = actualText.replaceAll("\\s+", "");
       	return s.equals(t);
    }
}