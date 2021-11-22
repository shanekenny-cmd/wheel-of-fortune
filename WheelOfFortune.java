import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.*;
import java.util.*;

/**
 * A class representing a WheelOfFortune object
 *
 */
public class WheelOfFortune
{
    private Clue clue;
    private Scanner in;
    private Player[] players;
    private int cp;
    private Wheel wheel;
    private ArrayList<Player> standings;
    
    public WheelOfFortune() throws FileNotFoundException {
        clue = new Clue();
        players = new Player[4];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player();
        }
        cp = 0;
        wheel = new Wheel();
    }

    public void reset() throws FileNotFoundException {
        clue = new Clue();
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].setBank(0);
            this.players[i].setBal(0);
        }
        this.cp = 0;
        this.wheel = new Wheel();
    }

    public Clue getClue() {
        return this.clue;
    }

    public Wheel getWheel() {
        return this.wheel;
    }

    public void nextPlayer() {
        this.cp++;
        this.cp %= this.players.length;
    }
    public int getCP() {
        return this.cp;
    }
    public void setCurrentPlayer(int index) {
        this.cp = index % this.players.length;
    }
    public boolean setName(int player, String name) {
        return this.players[player % this.players.length].setName(name);
    }
    public String getName(int player) {
        return this.players[player].getName();
    }
    public boolean setID(int player, String id) {
        return this.players[player].setID(id.toCharArray());
    }
    public int getPlayerByName(String name) {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    public int numPlayers() {
        return this.players.length;
    }
    public boolean checkNames() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getName().equals("")) {
                return false;
            }
        }
        return true;
    }
    public boolean checkIDS(String id) {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getID().equals(id)) {
                return true;
            }
        }
        return false;
    }
    public void bankrupt() {
        this.players[this.cp].setBal(0);
        this.cp++;
        this.cp %= this.players.length;
    }
    public String[] getPlayerString() {
        String[] toString = new String[this.players.length];
        for (int i = 0; i < this.players.length; i++) {
            toString[i] = "Name: " + this.players[i].getName() + 
                            "\nID: " + this.players[i].getID() + 
                            ":\nBalance: " + this.players[i].getBal() + 
                            "\nBank: " + this.players[i].getBank();
        }
        return toString;
    }
    public void playerWinTossUp(int round) {
        int winnings = 1000;
        if (round != 0) {
            winnings += 1000;
        }
        this.players[this.cp].addBal(winnings);
    }

    public boolean buyVowel(char c) {
        this.players[this.cp].addBal(-500);
        return (this.clue.check(c) > 0);
    }

    public boolean playerGuess(char c, int value) {
        int hits = this.clue.check(c);// this returns -1 if its a miss
        if (hits < 0) {
            return false;
        }
        this.players[this.cp].addBal(hits * value);
        return true;
        //System.out.println((hits * value) + " added.");
    }

    public boolean submitSolve(String s) {
        // check to see if s is the correct string
        return this.clue.solve(s);
    }

    public String getNext(int round) {
        this.bankRound();
        this.clue.newRound();
        this.wheel.newWheel(round);
        return this.clue.getPhrase();
    }
    public void bankRound() {
        this.players[this.cp].addBank(this.players[this.cp].getBal());
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].setBal(0);
        }
    }

    public int getWinner() {
        int winner = 0;
        for (int i = 1; i < this.players.length; i++) {
            if (this.players[i].getBank() > this.players[winner].getBank()) {
                winner = i;
            }
        }
        return winner;
    }

    private final String FILE_PATH = "data_file.csv";
    public void updateStandings() {
        // store player name, score
        standings = new ArrayList<Player>();
        try {
            standings = retrieveStandings();
            new FileWriter(FILE_PATH, false).close();
            FileWriter writer = new FileWriter(FILE_PATH, false);
            for (int i = 0; i < standings.size() && i < 5; i++) {
                //System.out.println("Player " + i + ", " + standings.get(i).getBank() + "\n");
                writer.write("Player " + standings.get(i).getID() + ", " + standings.get(i).getBank() + ", " + standings.get(i).getName() + "\n");
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Player> retrieveStandings() {
        standings = new ArrayList<Player>();
        try {
            FileReader reader = new FileReader(new File(FILE_PATH));
            BufferedReader bR = new BufferedReader(reader);

            String line;

            while ((line = bR.readLine()) != null) {
                Player p = new Player();
                String id = line.substring(line.indexOf(" ") + 1, line.indexOf(" ") + 6);
                p.setID(id.toCharArray());
                if (line.length() > 0) {
                    line = line.substring(line.indexOf(",")  + 2);
                    p.setBank(Integer.parseInt(line.substring(0, line.indexOf(","))));
                    standings.add(p);
                }
                line = line.substring(line.indexOf(",") + 2);
                p.setName(line);
            }
            reader.close();
            for (int i = 0; i < players.length; i++) {
                boolean add = true;
                for (Player p: standings) {
                    if (p.getID().equals(players[i].getID())) {
                        add = false;
                    }
                }
                if (add) {
                    standings.add(players[i]);
                }
            }
            // cleanseStandings();
            Collections.sort(standings, new Comparator<Player>(){
                public int compare(Player o1, Player o2){
                    return o2.getBank() - o1.getBank();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return standings;
    }
    // private void cleanseStandings() {
    //     HashMap<char[], Player> map = new HashMap<char[], Player>();
    //     for (int i = 0; i < this.standings.size(); i++) {
    //         System.out.println(this.standings.get(i).getID());
    //         System.out.println(!map.containsKey(this.standings.get(i).getID().toCharArray()));
    //         if (!map.containsKey(this.standings.get(i).getID().toCharArray())) {
    //             map.put(this.standings.get(i).getID().toCharArray(), this.standings.get(i));
    //         } else {
    //             if (map.get(this.standings.get(i).getID().toCharArray()).getBank() < this.standings.get(i).getBank()) {
    //                 map.get(this.standings.get(i).getID().toCharArray()).setBank(this.standings.get(i).getBank());
    //             }
    //             standings.remove(this.standings.get(i));
    //         }
    //     }
    //     for (Player p: standings) {
    //         System.out.println(p.getID() + " " + p.getName() + " " + p.getBank());
    //     }
    //     System.out.println();
    // }
}
