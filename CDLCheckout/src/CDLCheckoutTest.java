import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CDLCheckoutTest {

	@BeforeEach
	public void setupTests() {
		CDLCheckout.setDefaultValues();
	}

	@Test
	void filterShoppingItems_lower() {
		String shopping = "AaBCD";
		List<Character> expectedResult = List.of('A', 'A', 'B', 'C', 'D');
		List<Character> result = CDLCheckout.filterShoppingItems(shopping);
		assertEquals(expectedResult, result);
	}

	@Test
	void filterShoppingItems_number() {
		String shopping = "A1BCD";
		List<Character> expectedResult = List.of('A', 'B', 'C', 'D');
		List<Character> result = CDLCheckout.filterShoppingItems(shopping);
		assertEquals(expectedResult, result);
	}

	@Test
	void filterShoppingItems_special() {
		String shopping = "A@B'C;D£!";
		List<Character> expectedResult = List.of('A', 'B', 'C', 'D');
		List<Character> result = CDLCheckout.filterShoppingItems(shopping);
		assertEquals(expectedResult, result);
	}

	@Test
	void filterItemList_unique() {
		String items = "A@B'C;D£!FELHJDSRERETHSFSTHAFV";
		List<Character> expectedResult = List.of('A', 'B', 'C', 'D', 'F', 'E', 'L', 'H', 'J', 'S', 'R', 'T', 'V');
		List<Character> result = CDLCheckout.filterItemList(items);
		assertEquals(expectedResult, result);
	}

	@Test
	void calculateTotalValue_singleton() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('A', 1);
		itemCountMap.put('B', 0);
		itemCountMap.put('C', 0);
		itemCountMap.put('D', 0);
		int expectedValue = 50;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

	@Test
	void calculateTotalValue_threeA_discount() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('A', 3);
		itemCountMap.put('B', 0);
		itemCountMap.put('C', 0);
		itemCountMap.put('D', 0);
		int expectedValue = 130;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

	@Test
	void calculateTotalValue_twoB_discount() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('A', 0);
		itemCountMap.put('B', 2);
		itemCountMap.put('C', 0);
		itemCountMap.put('D', 0);
		int expectedValue = 45;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

	@Test
	void calculateTotalValue_multiple_discounts() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('A', 3);
		itemCountMap.put('B', 2);
		itemCountMap.put('C', 0);
		itemCountMap.put('D', 0);
		int expectedValue = 175;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

	@Test
	void calculateTotalValue_multiple_items() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('A', 4);
		itemCountMap.put('B', 3);
		itemCountMap.put('C', 1);
		itemCountMap.put('D', 1);
		int expectedValue = 290;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

	@Test
	void calculateTotalValue_large() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('A', 100);
		itemCountMap.put('B', 100);
		itemCountMap.put('C', 100);
		itemCountMap.put('D', 100);
		int expectedValue = 10090;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

	@Test
	void calculateTotalValue_custom() {
		HashMap<Character, Integer> itemCountMap = new HashMap<>();
		itemCountMap.put('X', 10);
		itemCountMap.put('Y', 10);
		itemCountMap.put('S', 10);
		Map<Character, Integer> defaultPrice = Map.ofEntries(Map.entry('X', 50), Map.entry('Y', 30),
				Map.entry('S', 20));

		HashMap<Character, HashMap<Integer, Integer>> specialPrices = new HashMap<>();
		specialPrices.put('X', new HashMap<>(Map.ofEntries(Map.entry(2, 70))));
		specialPrices.put('Y', new HashMap<>(Map.ofEntries(Map.entry(5, 35))));
		specialPrices.put('S', new HashMap<>(Map.ofEntries(Map.entry(1, 0))));

		CDLCheckout.setItemList(CDLCheckout.filterItemList("XYS"));
		CDLCheckout.setPriceList(new HashMap<>(defaultPrice));
		CDLCheckout.setSpecialPriceList(specialPrices);

		int expectedValue = 620;
		int result = CDLCheckout.calculateTotalValue(itemCountMap);
		assertEquals(expectedValue, result);
	}

}
