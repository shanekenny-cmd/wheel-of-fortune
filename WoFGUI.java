import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;



public class WoFGUI extends JFrame {

    /**
     * The main method to create run our Wheel of fortune GUI. Very compact, 
     *  so it doesn't need its own class.
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
     * When one of these buttons is clicked, it will check if the clue contains it. If it does,
     *  all occurrences of that letter will be displayed on the screen.
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
                    gameMessages = label + " does not appear in the clue.";
                    wf.nextPlayer();
                } else {
                    gameMessages = label + " appears in the clue.";
                }
                buyingVowel = false;
            } else {
                if (!wf.playerGuess(label, spinValue)) {
                    gameMessages = "Incorrect!";
                    wf.nextPlayer();
                } else {
                    gameMessages = "Correct!";
                }
            }
            if (isCompleted()) {
                gameMessages = "Correct! " + wf.getName(wf.getCP()) + " wins the round.";
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
                if (tossUpRound) {
                    wf.playerWinTossUp(roundNumber);
                    tossUpRound = false;
                    tossUps++;
                }
                solutionField.setText("");
                displayedText = wf.getClue().getDisplayPhrase();
                gameMessages = "Correct! " + wf.getName(wf.getCP()) + " wins the round.";
                repaint();
            } else {
                gameMessages = "Inorrect!";
                if (tossUpRound) {
                    timer = new Timer();
                    //timer.schedule(new TossUpTick(), 0);
                    tot = new TossUpTick();
                    tot.start();
                    ticking = true;
                }
                solutionField.setText("");// here we need to make it so that if its a toss-up, 
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
            ticking = false;
            tot.interrupt();
            tot = null;
            int label = wf.getPlayerByName(((JButton) (e.getSource())).getText());//Integer.parseInt(((JButton) (e.getSource())).getText().charAt(7) + "") - 1;
            buzzAbl[label] = false;
            wf.setCurrentPlayer(label);

            for (JButton b: buttons) {
                b.setEnabled(false);
            }

            for (JButton b: buzzers) {
                b.setEnabled(false);
            }
            spin.setEnabled(false);
            buyVowel.setEnabled(false);
            solutionField.setEnabled(true);

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
            gameMessages = "";
            if (spinValue == 2) {
                // bankrupt current player
                wheelMessages += wf.getName(wf.getCP()) + " goes bankrupt.";
                wf.bankrupt();
            } else if (spinValue == 1) {
                // current player loses turn
                wheelMessages += wf.getName(wf.getCP()) + " loses a turn.";
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
            gameMessages = "";
            wheelMessages = "";
            getNewClue();
            initDisplayedText();
            for (int i = 0; i < buzzAbl.length; i++) {
                buzzAbl[i] = true;
            }
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
     *  or loses a round. When pressed, everything resets with a new movie title.
     *
     *
     */
    private class PlayAgainListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Random random = new Random();
            String fileName = "advert_" + random.nextInt(9) + ".jpeg";
            //System.out.println(fileName);
            adImage = new ImageIcon(fileName).getImage();
            //wf = null;
            timer = null;
            // try {
            //     wf = new WheelOfFortune();
            // } catch (Exception ex) {
            //     ex.printStackTrace();
            // }
            try {
                wf.reset();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            getNewClue();

            gameMessages = "";
            wheelMessages = "";
            for (int i = 0; i < buzzAbl.length; i++) {
                buzzAbl[i] = true;
            }

            tot = null;
            buyingVowel = false;
            tossUpRound = true;
            ticking = true;
            spun = false;
            spinValue = 0;
            roundNumber = 0;
            tossUpRound = true;
            for (JButton b: buzzers) {
                b.setEnabled(true);
            }
            playAgain.setEnabled(false);
            spin.setEnabled(false);
            buyVowel.setEnabled(false);
            displayedText = wf.getClue().getDisplayPhrase();

            repaint();
        }
        
    }

    /**
     * 
     * An ActionListener implementation for the name text fields.
     *
     *
     */
    private class NameFieldListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = ((JTextField)(e.getSource())).getText();
            ((JTextField)(e.getSource())).setText("");
            boolean isID = false;
            ArrayList<Player> standings = wf.retrieveStandings();
            for (Player p: standings) {
                if (p.getID().equals(name)) {
                    isID = true;
                    if (!wf.checkIDS(name)) {
                        for (int i = 0; i < nameFields.length; i++) {
                            if (e.getSource().equals(nameFields[i])) {
                                wf.setName(i, p.getName());
                                wf.setID(i, name);
                                ((JTextField)(e.getSource())).setVisible(false);
                                getContentPane().remove(((JTextField)(e.getSource())));
                                getContentPane().revalidate();
                                buzzers[i].setText(wf.getName(i));
                            }
                        }
                    }
                }

            }
            //check name here
            // set the correct player's name to be this
            if (!isID) {
                for (int i = 0; i < nameFields.length; i++) {
                    if (e.getSource().equals(nameFields[i])) {
                        if (wf.setName(i, name)) {
                            ((JTextField)(e.getSource())).setVisible(false);
                            getContentPane().remove(((JTextField)(e.getSource())));
                            getContentPane().revalidate();
                            buzzers[i].setText(wf.getName(i));
                        }
                    }
                }
            }
            if (wf.checkNames()) {
                namesInputed = true;
                tossUpRound = true;
                ticking = true;
            }
            repaint();
        }
        
    }

    /**
     * 
     * An ActionListener implementation for the Quit button. system.exit(0)
     *
     *
     */
    private class QuitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
        
    }

    /** 
     * 
     * A class that extends TimerTask in order to tick for the tossuprounds
     */
    private class TossUpTick extends Thread {

        public void run() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                tot = null;
            }
            for (int i = 97; i < 123 && ticking; i++) {
                if (wf.getClue().check(Character.toLowerCase((char)i)) > 0) {
                    displayedText = wf.getClue().getDisplayPhrase();
                    repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        tot = null;
                    }
                }
            }
            for (JButton b: buzzers) {
                b.setEnabled(false);
            }
            // here the clue has been completed
            if (ticking) {
                tossUpRound = false;
                for (int i = 0; i < buzzAbl.length; i++){
                    buzzAbl[i] = true;
                }
            }
            ticking = false;
            tot = null;
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
    private String gameMessages;
    private String[] playerInfo;// change this to be an array
    private String currentPlayer;
    private String[] standings;

    private CharacterListener charListener;
    private Container container;
    private JButton[] buttons = new JButton[26];
    private JButton playAgain;
    private JButton quitButton;
    private JButton spin;
    private JButton buyVowel;
    private JButton nextRound;
    private JButton[] buzzers;
    private boolean[] buzzAbl;
    private JTextField solutionField;
    private Image adImage;
    private BufferedImage bufImg;
    private JTextField[] nameFields;
    private NameFieldListener nfl;

    // game state variables
    private int roundNumber;
    private boolean buyingVowel;
    private boolean tossUpRound;
    private boolean ticking;
    private boolean spun;
    private int spinValue;
    private Timer timer;
    private int tossUps;
    private TossUpTick tot;
    private boolean namesInputed;

    
    
    
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

        // add randomization
        Random random = new Random();
        String fileName = "advert_" + random.nextInt(8) + ".jpeg";
        adImage = new ImageIcon(fileName).getImage();

        wheelMessages = "";
        gameMessages = "";
        tossUps = 0;
        buyingVowel = false;
        tossUpRound = false;
        ticking = false;
        spun = false;
        spinValue = 0;
        roundNumber = 0;
        namesInputed = false;

        getNewClue();
        initDisplayedText();
        // initPlayerInfo();
        setLayout(new FlowLayout());
        initButtons();
        initTextField();
        initNameFields();
        setVisible(true);
    }
    
    /**
     * Initiates or re-initiates a few fields
     */
    private void getNewClue() {
        actualText = wf.getNext(roundNumber + 1);
        displayedText = "";
        if ((roundNumber == 1 || roundNumber == 4) && namesInputed) {
            tossUpRound = true;
            ticking = true;
        }
    }
    
    /**
     * Initializes the displayedText variable, which is then displayed on the GUI.
     */
    private void initDisplayedText() {
        displayedText = wf.getClue().getDisplayPhrase();
    }

    private void initNameFields() {
        container = getContentPane();
        nfl = new NameFieldListener();
        nameFields = new JTextField[wf.numPlayers()];
        for (int i = 0; i < nameFields.length; i++) {
            nameFields[i] = new JTextField(20);
            container.add(nameFields[i]);
            nameFields[i].addActionListener(nfl);
            nameFields[i].setEnabled(true);
            nameFields[i].setVisible(true);
            nameFields[i].setBackground(Color.black);
            nameFields[i].setForeground(Color.green);
            nameFields[i].setFont(new Font("Courier New", Font.PLAIN, 14));
        }
    }

    
    /**
     * Initializes all the buttons of the GUI, references the private classes written at the top.
     */
    private void initButtons() {
        container = getContentPane();
        charListener = new CharacterListener();

        container.setBackground(Color.black);

        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 26; i++) {
            // Generate every character in the alphabet and use as labels for buttons
            char alpha = (char) ((int) 'A' + i);
            buttons[i] = new JButton(String.valueOf(alpha));
            container.add(buttons[i]);
            buttons[i].addActionListener(charListener);
            buttons[i].setEnabled(false);
            buttons[i].setFocusable(false);
            buttons[i].setBackground(Color.black);
            buttons[i].setForeground(Color.green);
            buttons[i].setFont(new Font("Courier New", Font.PLAIN, 14));
        }
        buzzers = new JButton[wf.numPlayers()];
        buzzAbl = new boolean[buzzers.length];
        for (int i = 0; i < buzzers.length; i++) {
            buzzers[i] = new JButton(wf.getName(i));//("Player " + (i + 1));
            container.add(buzzers[i]);
            buzzers[i].addActionListener(new BuzzerListener());
            buzzers[i].setEnabled(false);
            buzzers[i].setBackground(Color.black);
            buzzers[i].setForeground(Color.green);
            buzzers[i].setFont(new Font("Courier New", Font.PLAIN, 14));
            buzzers[i].setPreferredSize(new Dimension(100, 25));
            buzzAbl[i] = true;
        }
        spin = new JButton("Spin");
        container.add(spin);
        spin.addActionListener(new SpinListener());
        spin.setEnabled(false);
        spin.setBackground(Color.black);
        spin.setForeground(Color.green);
        spin.setFont(new Font("Courier New", Font.PLAIN, 14));

        buyVowel = new JButton("Buy Vowel");
        container.add(buyVowel);
        buyVowel.addActionListener(new BuyVowelListener());
        buyVowel.setEnabled(false);
        buyVowel.setBackground(Color.black);
        buyVowel.setForeground(Color.green);
        buyVowel.setFont(new Font("Courier New", Font.PLAIN, 14));

        // add next round button here - enabled if the round is over, blocking
        nextRound = new JButton("Next Round");
        container.add(nextRound);
        nextRound.addActionListener(new NextRoundListener());
        nextRound.setEnabled(false);
        nextRound.setBackground(Color.black);
        nextRound.setForeground(Color.green);
        nextRound.setFont(new Font("Courier New", Font.PLAIN, 14));

        playAgain = new JButton("Play Again");// change to reset the whole game, only enabled when game over
        container.add(playAgain); 
        playAgain.addActionListener(new PlayAgainListener());
        playAgain.setEnabled(false);
        playAgain.setBackground(Color.black);
        playAgain.setForeground(Color.green);
        playAgain.setFont(new Font("Courier New", Font.PLAIN, 14));

        // add quit button here - enable when game over, just System.exit(0) if clicked
        quitButton = new JButton("Quit");
        container.add(quitButton);
        quitButton.addActionListener(new QuitButtonListener());
        quitButton.setEnabled(true);
        quitButton.setBackground(Color.black);
        quitButton.setForeground(Color.green);
        quitButton.setFont(new Font("Courier New", Font.PLAIN, 14));

    }

    /**
     * Initializes the text fild of the GUI, references the private classes written at the top.
     */
    private void initTextField() {
        container = getContentPane();
        solutionField = new JTextField(20);
        container.add(solutionField);
        solutionField.addActionListener(new SolutionListener());
        solutionField.setEnabled(false);
        solutionField.setBackground(Color.black);
        solutionField.setForeground(Color.green);
        solutionField.setFont(new Font("Courier New", Font.PLAIN, 14));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private void buttonAbility() {
        for (JButton b: buttons) {
            if (wf.getClue().getGuesses().indexOf(Character.toLowerCase(b.getText().charAt(0))) == -1 && spun) {
                b.setEnabled(true);
            } else {
                b.setEnabled(false);
            }
        }
    }

    private void buyVowelAbility() {
        if (buyingVowel) {
            for (JButton b: buttons) {
                if ("aeiou".indexOf(Character.toLowerCase(b.getText().charAt(0))) != -1 && wf.getClue().getGuesses().indexOf(Character.toLowerCase(b.getText().charAt(0))) == -1) {
                    b.setEnabled(true);
                }
            }
        }
    }

    private void getStandings() {
        ArrayList<Player> data = wf.retrieveStandings();
        standings = new String[data.size()];
        for (int i = 0; i < standings.length; i++) {
            standings[i] =  "Name: " + data.get(i).getName() +
                            "\nScore: " + data.get(i).getBank();
        }
    }

    /**
     * Overrides the super class's paint method. 
     * repaint() will call this method without recreating the pop up window. 
     */
    public void paint(Graphics g) {
        super.paint(g);

        bufImg = new BufferedImage(adImage.getWidth(null), adImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufImg.createGraphics();
        g2.drawImage(adImage, 0, 0, null);
        try {
            bufImg = resizeImage(bufImg, 200, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.dispose();

        g2 = (Graphics2D) g;

        g2.setFont(new Font("Courier New", Font.PLAIN, 16));
        g2.setColor(Color.green);

        g2.drawImage(bufImg, null, 600, 175);

        g2.drawString(gameMessages, 100, 125);

        g2.drawString("Category: " + wf.getClue().getCategory(), 100, 150);
        
        g2.drawString(displayedText, 100, 175);

        g2.drawString(wheelMessages, 100, 200);

        currentPlayer = wf.getName(wf.getCP()) + "'s turn.";
        g2.drawString(currentPlayer, 100, 225);

        playerInfo = wf.getPlayerString();
        for (int i = 0; i < playerInfo.length; i++) {
            g2.drawString(playerInfo[i], 100, 250 + (i * 25));
        }

        g2.drawString("Round: " + (roundNumber + 1), 100, 250 + (playerInfo.length * 25));

        getStandings();
        g2.drawString("All time standings:", 100, 425);
        for (int i = 0; i < standings.length && i < 5; i++) {
            g2.drawString(standings[i], 100, 450 + (i * 25));
        }

        nextRound.setEnabled(false);

        buttonAbility();

        buyVowelAbility();


        if (tossUpRound && namesInputed) {
            if (tot == null) {
                for (boolean b: buzzAbl) {
                    b = true;
                }
                timer = new Timer();
                tot = new TossUpTick();
                tot.start();
            }
        }
        if (ticking && namesInputed) { 
            for (int i = 0; i < buzzers.length; i++) {
                if (buzzAbl[i]) {
                    buzzers[i].setEnabled(true);
                }
            }
        }

        if (isCompleted()) {
            for (JButton b: buttons) {
                b.setEnabled(false);
            }
            for (JButton b: buzzers) {
                b.setEnabled(false);
            }
            spin.setEnabled(false);
            buyVowel.setEnabled(false);
            solutionField.setEnabled(false);
            nextRound.setEnabled(true);
            Ellipse2D.Double leftEye = new Ellipse2D.Double(100, 600, 5, 5);
            g2.draw(leftEye);
            Ellipse2D.Double rightEye = new Ellipse2D.Double(118, 600, 5, 5);
            g2.draw(rightEye);
            Arc2D.Double mouth = new Arc2D.Double(100, 619, 23, 12, 0, -180, Arc2D.OPEN);
            g2.draw(mouth);
            if (roundNumber < 6) {
                roundNumber++;
            }
        }

        if (roundNumber >= 6) {
            g2.drawString("Game Over.", 100, 250 + ((playerInfo.length + 1) * 25));
            wf.bankRound();
            g2.drawString(wf.getName(wf.getWinner()) + " is the winner.", 100, 250 + ((playerInfo.length + 2) * 25));
            // call method to wtite game data to file in wf
            // ask playAgain
            wf.updateStandings();
            playAgain.setEnabled(true);
            nextRound.setEnabled(false);
        }
        if ((roundNumber == 0 || roundNumber == 1 || roundNumber == 4) && (!tossUpRound)) {
            tossUpRound = true;
            ticking = true;
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