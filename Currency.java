/**
 * Currency utilizes a formatted currency data file to maintain current
 * currency information and facilitate vending machine purchases for a vending
 * machine simulator.
 * @author Alex Sandberg-Bernard
 */

// import IO
import java.io.*;

public class Currency
{
    // class variables
    private File currencyFile;
    private int currencyEntries;
    private String header;
    private double changeAmount = 0;

    // declare currency arrays
    private String[] currency;
    private String[] currencyName;
    private String[] currencyType;
    private double[] value;
    private int[] quantities;

    /**
     * Default constructor to create Currency objects.
     * @param currencyFile -- user-supplied formatted currency data file
     * @param currencyEntries -- integer specifying the amount of entries in
     *                        currency data file
     */
    public Currency(File currencyFile, int currencyEntries)
    {
        // set class variables
        this.currencyFile = currencyFile;
        this.currencyEntries = currencyEntries;
    }

    /**
     * Setter method loads class arrays with data from currency data file.
     * @param currencyFile -- user-supplied formatted currency data file
     * @param currencyEntries -- integer specifying the amount of entries in
     *                        currency data file
     */
    public void setCurrency(File currencyFile, int currencyEntries)
    {
        // read currency data file
        // use try/catch to catch IO exceptions
        try (BufferedReader br = new BufferedReader(new FileReader
                (currencyFile)))
        {
            // read header to String
            header = br.readLine();

            // eat blank line
            br.readLine();

            // create inventory arrays of size inventoryEntries
            currency = new String[currencyEntries];
            currencyName = new String[currencyEntries];
            currencyType = new String[currencyEntries];
            value = new double[currencyEntries];
            quantities = new int[currencyEntries];

            // fill arrays but substringing data file
            for (int index = 0; index<currencyEntries; index++)
            {
                String line;
                line = br.readLine();
                currency[index] = line.substring(3,10).
                        replaceAll(" ", "");
                currencyName[index] = line.substring(15,40).
                        replaceAll(" ", "");
                currencyType[index] = line.substring(47,60).
                        replaceAll(" ", "");
                value[index] = Double.valueOf(line.substring(67,75).
                        replaceAll(" ", ""));
                quantities[index] = Integer.valueOf(line.substring(85,90).
                        replaceAll(" ", ""));
            }
        }
        catch(IOException exception)
        {
            System.out.println("Exception: " + exception);
        }
    }

    /**
     * Getter method prints formatted current currency information to console.
     */
    public void getCurrency()
    {
        // print formatted currency information
        System.out.println();
        System.out.println(String.format("%5s%16s", "Currency #", header));
        for(int index = 0; index<currencyEntries; index++)
        {
            System.out.print(String.format("%9d%s",(index + 1),"." ));
            System.out.println(String.format("%10s%30s%20s%15.2f%15d",
                    currency[index], currencyName[index], currencyType[index],
                    value[index], quantities[index]));
        }
    }

    /**
     * getAmount() returns the value of a specified currency type and amount.
     * @param currencyType -- integer value corresponding to the reference
     *                     number for specified currency type, as displayed on
     *                     console by getCurrency()
     * @param currencyNums -- integer value specifying quantity of desired
     *                     currency type
     * @return double value of currency loaded by user
     */
    public double getAmount(int currencyType, int currencyNums)
    {
        // set index
        int index = currencyType-1;

        // return double value at index, multiplied by currencyNums
        return (currencyNums * value[index]);
    }

    /**
     * Calculates change following a purchase and returns currency to user in
     * the form of the largest value of currency currently available.
     * @param currencyType -- integer value corresponding to the reference
     *                     number for specified currency type, as displayed on
     *                     console by getCurrency()
     * @param currencyNums -- integer value specifying quantity of desired
     *                     currency type
     * @param currencyValue -- double representing value of currency loaded by
     *                      user for purchase
     * @param itemCost -- double representing cost of desired item
     */
    public void getChange(int currencyType, int currencyNums,
                          double currencyValue, double itemCost)
    {
        // calculate change amount
        changeAmount = currencyValue - itemCost;

        // set index
        int indexCur = currencyType-1;

        // update quantities
        quantities[indexCur] += currencyNums;

        // print change amount
        System.out.println("Change amount: " + currency[indexCur] + " " +
                changeAmount);

        // iterate through currency types to make change
        for(int index=0; index<currencyEntries; index++)
        {
            // variables for change
            int wholeAmount = 0;
            double remainder = 0;

            // use largest values possible first
            // skip if quantity=0
            if ((value[index] <= changeAmount) && !(quantities[index]==0))
            {

                // calculate largest amount of whole currency units possible
                wholeAmount = (int) (changeAmount/value[index]);

                // prevent quantity from going negative by setting wholeAmount
                // maximum to amount equal to amount currently stored in array
                if(wholeAmount>quantities[index])
                {
                    wholeAmount = quantities[index];
                }

                // calculate remainder
                remainder = changeAmount - (wholeAmount * value[index]);

                // update change amount
                changeAmount = remainder;

                // print change results
                System.out.println("Change: " + wholeAmount + " x " +
                        currencyName[index] + " (" + currency[index] + " " +
                        value[index] + ")");

                // update quantities array
                quantities[index] = quantities[index] - wholeAmount;

            }
        }
    }

    /**
     * checkChange() checks the current currency stored to ensure that there is
     * enough money to return exact change following a purchase by the user,
     * returning a boolean value of true only if exact change is possible.
     * @param currencyValue -- double representing value of currency loaded by
     *                      user for purchase
     * @param itemCost -- double representing cost of desired item
     * @return boolean value of true if exact change is possible, or false if
     * exact change is not possible.
     */
    public boolean checkChange(double currencyValue, double itemCost)
    {
        // variable for total money in machine
        double moneyTotal = 0;

        // calculate change amount
        changeAmount = currencyValue - itemCost;

        // calculate total amount of money in machine
        for(int index=0; index<currencyEntries; index++)
        {
            moneyTotal += (value[index] * quantities[index]);
        }

        // return false if there's not enough money in machine
        if(moneyTotal<changeAmount)
        {
            return false;
        }

        // check to make sure that exact change is possible
        int wholeAmount = 0;
        double remainder = 0;
        for(int index=0; index<currencyEntries; index++)
        {
            // use largest values possible first
            // skip if quantity=0
            if ((value[index] <= changeAmount) && !(quantities[index]==0))
            {
                // calculate largest amount of whole currency units possible
                wholeAmount = (int) (changeAmount/value[index]);

                // prevent quantity from going negative by setting wholeAmount
                // maximum to amount equal to amount currently stored in array
                if(wholeAmount>quantities[index])
                {
                    wholeAmount = quantities[index];
                }

                // calculate remainder
                remainder = changeAmount - (wholeAmount * value[index]);

                // update change amount
                changeAmount = remainder;
            }
        }

        // if remainder = 0, then exact change is possible and true is returned
        if(remainder==0)
        {
            return true;
        }
        else return false; // return false if exact change is not possible
    }
}
