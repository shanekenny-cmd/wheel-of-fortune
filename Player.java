/* Player.java
 */

 
public class Player
{
    private double banked;                                                                     
    private double balance;                                                             
    private boolean skipTurn;                                                   
     

    // no-argument constructor
    public Player ()
    {


    }

    // initialization constructor
    public Player (double bK, double b, boolean sT)
    {
        banked = bK;
        balance = b;
		skipTurn = sT;

    }

    
    // mutator method for banked
    public void setBanked (double bK)
    {
        banked = bK;

    }

    // mutator method for balance
    public void setBalance (double b)
    {
        balance = b;

    }

    // mutator method for skip turn
    public void setSkipT (boolean sT)
    {
        skipTurn = sT;

    }

    // accessor method for banked
    public int getBanked()
    {
        return banked;

    }

    // accessor method for balance
    public double getBalance()
    {
        return balance;
    }

    // accessor method for skip turn
    public double getSkipTurn()
    {
        return annualInterestRate;

    }
