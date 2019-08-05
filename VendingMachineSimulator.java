/**
 * This program simulates a vending machine, and accepts formatted inventory
 * and currency data files
 * @author Alex Sandberg-Bernard
 *
 */

// import IO and Scanner
import java.io.*;
import java.util.Scanner;

public class VendingMachineSimulator
{
    /**
     * Main program method processes data files, accepts user console input, and
     * directs flow of program.
     * @param args -- two runtime parameters required: inventory file path,
     *             currency file path
     */
    public static void main(String[] args)
    {
        // ensure runtime parameters are supplied
        if (!(args.length==2))
        {
            System.out.println("Program requires two runtime parameters: " +
                    "<inventory file path>, <currency file path>");
            System.exit(1);
        }

        System.out.println("\nVending machine simulator\n");

        // load inventory file
        File inventoryFile = new File(args[0]);

        // read file and determine number of inventory entries by counting lines
        int inventoryEntries = 0;
        try (BufferedReader br = new BufferedReader(new FileReader
                (inventoryFile))) // use try/catch for IOExceptions
        {
            String line;
            while((line = br.readLine()) != null)
            {
                inventoryEntries++;
            }
        }
        catch (IOException exception)
        {
            System.out.println("Exception: " + exception);
        }

        // subtract 2 from inventoryEntries to account for headers
        inventoryEntries = inventoryEntries-2;

        // create new Inventory object using inventoryFile and set inventory
        Inventory inventory = new Inventory(inventoryFile, inventoryEntries);
        inventory.setInventory(inventoryFile, inventoryEntries);

        // load currency file
        File currencyFile = new File(args[1]);

        // count currency file entries
        int currencyEntries = 0;
        // read file and catch IOExceptions
        try (BufferedReader br = new BufferedReader(new FileReader
                (currencyFile)))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                currencyEntries++;
            }
        }
        catch (IOException exception)
        {
            System.out.println("Exception: " + exception);
        }

        // subtract 2 to account for headers
        currencyEntries = currencyEntries-2;

        // create new Currency object using currencyFile and set currency
        Currency currency = new Currency(currencyFile, currencyEntries);
        currency.setCurrency(currencyFile, currencyEntries);

        // display command menu
        commandMenu();

        // initialize new Scanner
        Scanner input = new Scanner(System.in);

        // variable for user command
        int commandChoice = -2;

        // use do-while to ensure proper input
        do
        {
            // prompt user to enter command
            System.out.print( "\nEnter Command:  " );
            commandChoice = input.nextInt();
            System.out.println();

            // print error if invalid entry
            if(!(commandChoice==0) && !(commandChoice==1)
                    && !(commandChoice==2) && !(commandChoice==3)
                    && !(commandChoice==-1))
            {
                System.out.println("Invalid entry.");
            }

            // use switch to direct program based on user input
            switch(commandChoice)
            {
                case 0: // display command menu
                {
                    commandMenu();
                    break;
                }
                case 1: // display inventory
                {
                    System.out.println("Inventory: ");
                    inventory.getInventory();
                    break;
                }
                case 2: // display currency
                {
                    System.out.println("Currency: ");
                    currency.getCurrency();
                    break;
                }
                case 3:  // purchase item
                {
                    purchase(inventory, currency, input, inventoryEntries,
                            currencyEntries);
                }
                case -1: // exit -- terminates do-while and ends main()
            }
        }
        while(!( commandChoice == -1 ));
    }

    /**
     * Displays formatted menu of commands for user input.
     */
    public static void commandMenu()
    {
        // print each command line by line
        System.out.println();
        System.out.println(String.format("%s%6s", "Show Commands:", "0"));
        System.out.println(String.format("%s%2s", "Display Inventory:", "1"));
        System.out.println(String.format("%s%3s", "Display Currency:", "2"));
        System.out.println(String.format("%s%6s", "Purchase Item:", "3"));
        System.out.println(String.format("%s%15s", "Exit:", "-1"));
        System.out.println();
    }

    /**
     * purchase() is called to purchase an item from the inventory. This method
     * utilizes the Inventory and Currency classes to ensure product is
     * available, ensure user funds are sufficient, ensure exact change is
     * available, provide change, and to update currency and inventory totals.
     * @param inventory -- inventory object must be created using data file
     * @param currency --  currency object must be created using data file
     * @param input -- Scanner object used to process user input from console
     * @param inventoryEntries -- integer value representing amount of items in
     *                         inventory from inventory data file
     * @param currencyEntries -- integer value representing amount of currency
     *                        types from currency data file
     */
    public static void purchase(Inventory inventory, Currency currency,
                                Scanner input, int inventoryEntries,
                                int currencyEntries)
    {
        // method variables
        int itemNum;
        int currencyNums;
        int currencyType;
        double currencyValue;
        double itemCost;
        boolean available;
        boolean funds;
        boolean change;

       // use do-while to ensure item availability
       do
       {
           //use do-while to ensure user input validity
           do
           {
               // prompt for item #
               System.out.print("Enter Item #:  \n");
               itemNum = input.nextInt();

               // display message for invalid entry
               if(!(itemNum>0) || !(itemNum<=inventoryEntries))
               {
                   System.out.println("Invalid entry.\n");
               }
           }
           while (!(itemNum>0) || !(itemNum<=inventoryEntries));

           // display item information
           inventory.getItem(itemNum);

           // obtain item cost
           itemCost = inventory.getItemCost(itemNum);

           // prompt if unavailable
           available = inventory.availability(itemNum);
           if(!available)
           {
               System.out.println("\nItem unavailable!");
               System.out.println("Please choose a different item.\n");
           }
       }
       while (!available);

       // use do-while to ensure funds are sufficient and change is available
       do
       {   // prompt user for currency information
           // validate user entry
           do
           {   System.out.print("\nHow many currency items?:  ");
               currencyNums = input.nextInt();

               // must be greater than 0
               if(!(currencyNums>0))
               {
                   System.out.println("\nPlease enter an amount greater " +
                           "than 0.");
               }

           }
           while(!(currencyNums>0));

           // display current currency information
           System.out.println("\nAvailable currency:");
           currency.getCurrency();

           // prompt for currency type
           // validate user entry. must be between 1 and <currencyEntries>
           do
           {
               System.out.println("\nEnter Currency #:  ");
               currencyType = input.nextInt();

               // print error message if invalid entry
               if((currencyType<=0) || (currencyType>currencyEntries))
               {
                   System.out.println("\nInvalid entry.\n");
               }
           }
           while((currencyType<=0) || (currencyType>currencyEntries));

           // total input currency amount
           currencyValue = currency.getAmount(currencyType, currencyNums);

           // display error if funds inadequate
           funds = inventory.funds(currencyValue, itemNum);
           if(!funds)
           {
               System.out.println("\nInsufficient funds. Come back with more " +
                       "money!\n");
           }

           // check to ensure machine can make change
           change = currency.checkChange(currencyValue, itemCost);
           // display error if exact change is unavailable
           if(!change)
           {
               System.out.println("\nInsufficient currency in machine for " +
                       "exact change. Sorry!");
               System.out.println("Please use a smaller currency type.\n");
           }
       }
       while ((!change) || (!funds) );

        // print message if purchase successful
        System.out.println("\nItem purchased!\n");

        // get adequate change and print results
        currency.getChange(currencyType, currencyNums, currencyValue, itemCost);

        // update inventory
        inventory.quantities[itemNum-1]--;
    }
}
