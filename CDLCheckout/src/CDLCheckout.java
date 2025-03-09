import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is a model of a checkout till.
 * 
 * This is my submission for the coding challenge set by CDL for the
 * Intermediate Software Engineer (Java) position in 2025.
 * 
 * 
 * @author Aqib A Shah
 * @version 1.0
 * @since 2025-03-07
 *
 */

public class CDLCheckout {

	private static Scanner userInput = new Scanner(System.in);
	private static HashMap<Character, Integer> priceList = new HashMap<>();
	private static HashMap<Character, HashMap<Integer, Integer>> specialPriceList = new HashMap<>();
	private static List<Character> itemList;

	public static void main(String[] args) {
		while (true) {
			String choice = getNextLine("Insert price list? 'y' for Yes");
			if (choice.equalsIgnoreCase("Y")) {
				createItemList();
				for (char item : itemList) {
					// insert unit price for each item
					int price = getNextInt("Insert the unit price in pence of item: " + item);
					priceList.put(item, price);
					// insert discount on this item
					int quantity = getNextInt("Insert the number of items for a discount on item: " + item
							+ " (enter 1 for no discount).");
					// one hash map per item of quantity as the key and the discounted
					// price as the value
					HashMap<Integer, Integer> discount = new HashMap<>();
					if (quantity != 1) {
						int specialPrice = getNextInt("Insert the special price for item: " + item);
						discount.put(quantity, specialPrice);
					} else {
						discount.put(1, 0);
					}
					// the special price list is a hash map of characters
					// and its discount hash map we defined
					specialPriceList.put(item, discount);
				}
			} else {
				// if no price list is given, the sample prices from the
				// task are used
				setDefaultValues();
			}

			createShoppingList();

		}

	}

	/**
	 * Method to accept a list of items from the user that need to be scanned. This
	 * prompts the user for a list of items until the input is valid.
	 */
	private static void createShoppingList() {
		List<Character> scannableItems = new ArrayList<>();
		do {
			final String shoppingList = getNextLine("Please scan your shopping, Press Q to finish shopping.");

			if (shoppingList.equalsIgnoreCase("Q")) {
				System.out.println("Thank you for shopping with us.");
				endShopping();
			}
			// filter out items that are not in the item list
			scannableItems = filterShoppingItems(shoppingList);
			if (scannableItems.isEmpty()) {
				System.out.println("No items were recognised in your shopping list. Please try again.");
				continue;
			}
		} while (scannableItems.isEmpty());

		System.out.println("Scanning the following items: " + scannableItems);
		scanShopping(scannableItems);

	}

	/**
	 * Method to accept a valid list of items from the user and set the itemList
	 * attribute when a valid input is provided.
	 */
	private static void createItemList() {
		List<Character> items = new ArrayList<>();
		do {
			// user inserts their custom item list
			String itemInput = getNextLine(
					"Please insert a list of your SKUs (Only alphabetic characters (A-Z, a-z) are allowed). Type 'Q' to quit.");
			if (itemInput.equalsIgnoreCase("Q")) {
				System.out.println("Thank you for shopping with us.");
				endShopping();
			}
			// item list is filtered if non alphabetic characters are used
			items = filterItemList(itemInput);
			if (items.isEmpty()) {
				System.out.println("No items were recognised in your item list. Please try again.");
			}
		} while (items.isEmpty());

		System.out.println("Item List: " + items);
		setItemList(items);

	}

	/**
	 * This method takes a list of shopping items and generates a running total of
	 * the customers shopping as items are being scanned. It prints the final total
	 * after all items have been scanned.
	 * 
	 * @param items            The list of filtered shopping items
	 * @param specialPriceList
	 * @param priceList
	 */
	private static void scanShopping(List<Character> items) {
		List<Character> scannedItems = new ArrayList<>();
		int totalCost = 0;
		for (int i = 0; i <= items.size(); i++) {
			// getting the sublist of scanned items to
			// generate a running total
			scannedItems = items.subList(0, i);
			HashMap<Character, Integer> itemCounts = getItemCount(scannedItems);
			totalCost = calculateTotalValue(itemCounts);
			if (i != items.size()) {
				displayCurrentPrice(totalCost, 'r');
			}
		}
		// display final total after all items
		// scanned
		displayCurrentPrice(totalCost, 'f');
	}

	/**
	 * This method is used to calculate the cost of the customer's shopping. It gets
	 * the frequency of each item of interest and calculates the value, applying
	 * discounts where possible.
	 * 
	 * @param itemCounts list containing the frequency of each valid item in the
	 *                   list
	 * @return The total value of the shopping after applying discounted prices
	 */
	public static int calculateTotalValue(HashMap<Character, Integer> itemCounts) {
		int totalValue = 0;
		int count, value;
		for (char item : itemList) {
			count = itemCounts.get(item);
			int specialPrice = specialPriceList.get(item).values().iterator().next();
			int quantity = specialPriceList.get(item).keySet().iterator().next();
			if (quantity != 1) {
				value = (specialPrice * (count / quantity)) + (priceList.get(item) * (count % quantity));
			} else {
				value = priceList.get(item) * count;
			}
			totalValue += value;

		}
		return totalValue;
	}

	/**
	 * Method to count the occurrences of valid items.
	 * 
	 * @param scannedItems list of characters that are valid
	 * @return a map of items and their frequency counts
	 */
	private static HashMap<Character, Integer> getItemCount(List<Character> scannedItems) {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		for (char item : itemList) {
			itemCountMap.put(item, Collections.frequency(scannedItems, item));
		}
		return itemCountMap;
	}

	/**
	 * This method prepares the shopping list for further processing by removing
	 * items that are not in the item list.
	 * 
	 * @param shoppingList The list of items to filter
	 * @return A list of valid shopping items
	 */
	public static List<Character> filterShoppingItems(String shoppingList) {
		String validChars = itemList.toString();
		char[] items = shoppingList.toUpperCase().toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (int i = 0; i < items.length; i++) {
			if (validChars.indexOf(items[i]) > -1) {
				list.add(items[i]);
			}
		}

		return list;

	}

	/**
	 * A method to filter out invalid characters from the item list.
	 * 
	 * @param itemInput list of items to process
	 * @return a list of valid items
	 */
	public static List<Character> filterItemList(String itemInput) {
		String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] items = itemInput.toUpperCase().toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (int i = 0; i < items.length; i++) {
			if (validChars.indexOf(items[i]) > -1 && list.indexOf(items[i]) == -1) {
				list.add(items[i]);
			}
		}
		return list;
	}

	/**
	 * A method to display the current value of the scanned items in pounds
	 * 
	 * @param totalCost   The total cost of the scanned items
	 * @param displayType A character that determines whether the running total or
	 *                    final total is displayed
	 * 
	 * @return void. This method prints the total value of the scanned items
	 */
	private static void displayCurrentPrice(int totalCost, char displayType) {
		String valueInPounds = String.format("%.2f", (double) totalCost / 100);
		String totalType = "";
		if (displayType == 'r') {
			totalType = "Running";
		} else if (displayType == 'f') {
			totalType = "Final";
		}
		System.out.println(String.format("%s Total: \u00A3%s", totalType, valueInPounds));

	}

	/**
	 * A method to set default values for the price, item and special price lists.
	 */
	public static void setDefaultValues() {
		System.out.println("No pricing information was provided. Using default values.");
		String defaultItems = "ABCD";
		Map<Character, Integer> defaultPrice = Map.ofEntries(Map.entry('A', 50), Map.entry('B', 30), Map.entry('C', 20),
				Map.entry('D', 15));
		HashMap<Character, HashMap<Integer, Integer>> specialPrices = new HashMap<>();
		specialPrices.put('A', new HashMap<>(Map.ofEntries(Map.entry(3, 130))));
		specialPrices.put('B', new HashMap<>(Map.ofEntries(Map.entry(2, 45))));
		specialPrices.put('C', new HashMap<>(Map.ofEntries(Map.entry(1, 0))));
		specialPrices.put('D', new HashMap<>(Map.ofEntries(Map.entry(1, 0))));

		setItemList(filterItemList(defaultItems));
		setPriceList(new HashMap<>(defaultPrice));
		setSpecialPriceList(specialPrices);
		System.out.println("Items:\n A B C D\nUnit Price(pence):\n 50 30 20 15\nSpecial Price:\n 3 for 130 2 for 45");

	}

	/**
	 * A method to set the private attribute itemList
	 * 
	 * @param items The list of items for which we want to set item list to
	 */
	public static void setItemList(List<Character> items) {
		itemList = items;

	}

	/**
	 * A method to set the private attribute priceList
	 * 
	 * @param prices The map of characters to prices for which we want to set price
	 *               list to
	 */
	public static void setPriceList(HashMap<Character, Integer> prices) {
		priceList = prices;

	}

	/**
	 * A method to set the private attribute specialPriceList
	 *
	 * @param specialPrices The map of special prices for which we want to set
	 *                      special price list to
	 */
	public static void setSpecialPriceList(HashMap<Character, HashMap<Integer, Integer>> specialPrices) {
		specialPriceList = specialPrices;

	}

	/**
	 * A method to get user input for the price and discount of items
	 * 
	 * @param userPrompt message used to prompt the user for an input
	 * @return The integer given by the user
	 */
	private static int getNextInt(String userPrompt) {
		if (!userPrompt.isEmpty()) {
			System.out.println(userPrompt);
		}

		int nextInt = -1;
		boolean validInput = false;

		while (!validInput) {
			try {
				nextInt = userInput.nextInt();
				userInput.nextLine();
				validInput = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter a valid integer.");
				userInput.nextLine();
			}
		}

		return nextInt;
	}

	/**
	 * Method to prompt the user for input when character input is required
	 * 
	 * @param userPrompt The message used to prompt the user for a response
	 * @return The line provided by the user
	 */
	private static String getNextLine(String userPrompt) {
		if (!userPrompt.isEmpty()) {
			System.out.println(userPrompt);
		}

		String nextLine = "";

		while (true) {
			nextLine = userInput.nextLine();
			if (nextLine.trim().isEmpty()) {
				System.out.println("No response detected. Please try again.");
			} else {
				return nextLine;
			}
		}
	}

	/**
	 * Method to end the shopping session, closing the scanner and ending the
	 * program.
	 */
	private static void endShopping() {
		userInput.close();
		System.exit(0);
	}

}
