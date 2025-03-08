import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;
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

	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		// continue to scan shopping until the user is done
		while (true) {
			System.out.println("Please scan your shopping. Type 'Q' to quit.");
			final String shoppingList = userInput.nextLine();

			if (shoppingList.equalsIgnoreCase("Q")) {
				System.out.println("Thank you for shopping with us.");
				break;
			}
			// filter out invalid characters from the string
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
	 */
	private static void scanShopping(List<Character> items) {
		List<Character> scannedItems = new ArrayList<>();
		int totalCost = 0;
		for (int i = 0; i <= items.size(); i++) {
			scannedItems = items.subList(0, i);
			totalCost = calculateTotalValue(scannedItems);
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
	 * @param scannedItems list of items for which we want the total value
	 * @return the total value of the list of items
	 */
	public static int calculateTotalValue(List<Character> scannedItems) {
		if (scannedItems.isEmpty()) {
			return 0;
		}
		final HashMap<Character, Integer> itemCounts = getItemCount(scannedItems);
		int totalValue = applyDiscounts(itemCounts);
		return totalValue;

	}

	/**
	 * 
	 * @param itemCounts list containing the frequency of each valid item in the
	 *                   list
	 * @return The total value of the shopping after applying discounted prices
	 */
	private static int applyDiscounts(HashMap<Character, Integer> itemCounts) {
		final Map<Character, Integer> priceList = Map.ofEntries(entry('A', 50), entry('B', 30), entry('C', 20),
				entry('D', 15));
		final Map<Character, Integer> specialPriceList = Map.ofEntries(entry('A', 130), entry('B', 45));
		int countA = itemCounts.get('A');
		int countB = itemCounts.get('B');
		int countC = itemCounts.get('C');
		int countD = itemCounts.get('D');
		int valueA = specialPriceList.get('A') * (countA / 3) + priceList.get('A') * (countA % 3);
		int valueB = specialPriceList.get('B') * (countB / 2) + priceList.get('B') * (countB % 2);
		int valueC = priceList.get('C') * countC;
		int valueD = priceList.get('D') * countD;
		return valueA + valueB + valueC + valueD;
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
		// list of valid characters
		String validChars = "ABCD";
		// convert string to upper case, then an array
		char[] items = shoppingList.toUpperCase().toCharArray();
		// arrayList to store valid characters
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

}
