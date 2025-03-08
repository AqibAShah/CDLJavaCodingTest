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
	
	private static HashMap<Character, Integer> priceList = new HashMap<>();
	private static HashMap<Character, HashMap<Integer, Integer>> specialPriceList = new HashMap<>();
	private static List<Character> itemList;
	
	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		while (true) {
			System.out.println("Insert price list? 'y' for Yes");
			String choice = userInput.nextLine();
			if (choice.equalsIgnoreCase("Y"))
			{
				System.out.println("Please insert a list of your SKUs (e.g. ABCD for items A-D). Type 'Q' to quit.");
				String itemInput = userInput.nextLine();
				if (itemInput.equalsIgnoreCase("Q")) {
					System.out.println("Thank you for shopping with us.");
					break;
				}
				itemList = filterItemList(itemInput);
				for (char item: itemList)
				{
					System.out.println("Insert the unit price in pence of item: " + item);
					int price = userInput.nextInt();
					priceList.put(item, price);
					System.out.println("Insert the number of items you would like to do a discount on for item: " + item);
					System.out.println("If you do not want to do a discount, enter 1");
					int quantity = userInput.nextInt();
					HashMap<Integer, Integer> discount = new HashMap<>();
					if (quantity != 1) {
						System.out.println("Insert the special price for item: " + item);
						int specialPrice = userInput.nextInt();
						discount.put(quantity, specialPrice);
					}
					else
					{
						discount.put(1,0);
					}
					specialPriceList.put(item, discount);
				}			
			}
			else {
				System.out.println("Using default prices");
				setDefaultValues();
				
			}
			System.out.println("Please scan your shopping. Press Q to finish shopping.");
			final String shoppingList = userInput.nextLine();

			if (shoppingList.equalsIgnoreCase("Q")) {
				System.out.println("Thank you for shopping with us.");
				break;
			}
			List<Character> items = filterShoppingItems(shoppingList);
			System.out.println("Scanning the following items: " + items);
			scanShopping(items);
		}

		userInput.close();

	}

	/**
	 * This method takes a list of shopping items and generates a running total of
	 * the customers shopping as items are being scanned. It prints the final total
	 * after all items have been scanned.
	 * 
	 * @param items The list of filtered shopping items
	 * @param specialPriceList 
	 * @param priceList 
	 */
	private static void scanShopping(List<Character> items) {
		List<Character> scannedItems = new ArrayList<>();
		int totalCost = 0;
		for (int i = 0; i <= items.size(); i++) {
			scannedItems = items.subList(0, i);
			HashMap<Character, Integer> itemCounts = getItemCount(scannedItems);
			totalCost = calculateTotalValue(itemCounts);
			if (i != items.size()) {
				displayCurrentPrice(totalCost, 'r');
			}
		}
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
			}
			else {
				value = priceList.get(item) * count;
			}
			totalValue += value;

		}
		return totalValue;
	}

	/**
	 * 
	 * @param scannedItems list of characters that are valid
	 * @return a map of items and their frequency counts
	 */
	private static HashMap<Character, Integer> getItemCount(List<Character> scannedItems) {
		List<Character> validItems = List.of('A', 'B', 'C', 'D');
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		for (char item : validItems) {
			itemCountMap.put(item, Collections.frequency(scannedItems, item));
		}
		return itemCountMap;
	}

	/**
	 * prepares the shopping list for further processing by removing items that are
	 * not A,B,C or D
	 * 
	 * @param shoppingList The list of items to filter
	 * @return A list of valid shopping items
	 */
	public static List<Character> filterShoppingItems(String shoppingList) {
		String validChars = "ABCD";
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
	
	public static void setDefaultValues() {
		String defaultItems = "ABCD";
		itemList = filterItemList(defaultItems);
		Map<Character, Integer> defaultPrice = Map.ofEntries(
				Map.entry('A', 50), Map.entry('B', 30),
				Map.entry('C', 20), Map.entry('D', 15));
		priceList = new HashMap<>(defaultPrice);
		specialPriceList = new HashMap<>();
		specialPriceList.put('A',  new HashMap<>(Map.ofEntries(Map.entry(3, 130))));
		specialPriceList.put('B',  new HashMap<>(Map.ofEntries(Map.entry(2, 45))));
		specialPriceList.put('C',  new HashMap<>(Map.ofEntries(Map.entry(1, 0))));
		specialPriceList.put('D',  new HashMap<>(Map.ofEntries(Map.entry(1, 0))));
		
		
	}

}
