/**
 * Inventory utilizes a formatted inventory data file to maintain current
 * inventory information and facilitate vending machine purchases for a vending
 * machine simulator.
 * @author Alex Sandberg-Bernard
 */

// import IO
import java.io.*;

public class Inventory
{
    // class variables
    private File inventoryFile;
    private int inventoryEntries;
    private String header;

    // class inventory arrays
    private String[] productNames;
    private double[] prices;
    private String[] containers;
    int[] quantities;

    /**
     * Default class constructor to create Inventory objects.
     * @param inventoryFile -- user-supplied formatted inventory data file
     * @param inventoryEntries -- integer specifying the amount of entries in
     *                         inventory data file
     */
    public Inventory(File inventoryFile, int inventoryEntries)
    {
        // set class variables
        this.inventoryFile = inventoryFile;
        this.inventoryEntries = inventoryEntries;
    }

    /**
     * Setter method loads class arrays with data from inventory data file.
     * @param inventoryFile -- user-supplied formatted inventory data file
     * @param inventoryEntries -- integer specifying the amount of entries in
     *                         inventory data file
     */
    public void setInventory(File inventoryFile, int inventoryEntries)
    {
        // read inventory data file
        // use try/catch to catch IO exceptions
        try (BufferedReader br = new BufferedReader(new FileReader
                (inventoryFile)))
        {
            // read header to String
            header = br.readLine();

            // eat blank line
            br.readLine();

            // create inventory arrays of size inventoryEntries
            productNames = new String[inventoryEntries];
            prices = new double[inventoryEntries];
            containers = new String[inventoryEntries];
            quantities = new int[inventoryEntries];

            // fill arrays by substringing inventory data file
            for(int index = 0; index<inventoryEntries; index++)
            {
                String line;
                line = br.readLine();
                productNames[index] = line.substring(0,18).
                        replaceAll(" ", "");
                prices[index] = Double.valueOf(line.substring(32,38).
                        replaceAll(" ", ""));
                containers[index] = line.substring(47,54).
                        replaceAll(" ", "");
                quantities[index] = Integer.valueOf(line.substring(65,69).
                        replaceAll(" ", ""));
            }
        }
        catch(IOException exception)
        {
            System.out.println("Exception: " + exception);
        }
    }

    /**
     * Getter method prints formatted current inventory information to console.
     */
    public void getInventory()
    {
        // print formatted current inventory information
        System.out.println();
        System.out.println(String.format("%5s%16s", "Item #", header));
        for(int index = 0; index<inventoryEntries; index++)
        {
            System.out.print(String.format("%5s%s", (index+1), "."));
            System.out.println(String.format("%18s%20.2f%16s%15d",
                    productNames[index], prices[index], containers[index],
                    quantities[index]));
        }
    }

    /**
     * getItem() gets and prints information about any specified item in
     * inventory.
     * @param itemNum -- integer value corresponding to the reference number
     *                for the specified item, as displayed on console by
     *                getInventory()
     */
    public void getItem(int itemNum)
    {
        // print formatted current item information
        System.out.println("\nItem #: " + itemNum);
        System.out.println("Name: " + productNames[itemNum-1] + "    "
        + "Price: " + prices[itemNum-1] + "    " + "Container: " +
                containers[itemNum-1] + "    " + "Quantity: " +
                quantities[itemNum-1] + " in stock");

    }

    /**
     * Returns the cost of any specified item.
     * @param itemNum -- integer value corresponding to the reference number
     *                for the specified item, as displayed on console by
     *                getInventory()
     * @return double value representing cost of specified item
     */
    public double getItemCost(int itemNum)
    {
        return prices[itemNum-1];
    }

    /**
     * Checks inventory to ensure requested item is available.
     * @param itemNum -- integer value corresponding to the reference number
     *                for the specified item, as displayed on console by
     *                getInventory()
     * @return boolean value of true if item is available, or false if item is
     * unavailable
     */
    public boolean availability(int itemNum)
    {
        // check quantities array for item availability
        if( !(quantities[itemNum-1]==0) )
        {
            return true;
        }
        else return false;
    }

    /**
     * Checks price of desired item to ensure that user has input sufficient
     * currency needed to purchase item.
     * @param currencyValue -- double representing value of currency supplied
     *                      by user for purchase
     * @param itemNum -- integer value corresponding to the reference number
     *                for the specified item, as displayed on console by
     *                getInventory()
     * @return boolean true if currency supplied by user is sufficient to
     * purchase desired item, or false if currency is insufficient
     */
    public boolean funds(double currencyValue, int itemNum)
    {
        // check prices array for item price
        if(currencyValue < prices[itemNum-1])
        {
            return false;
        }
        else return true;
    }

}
