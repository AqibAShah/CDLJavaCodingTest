import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CDLCheckoutTest {

	@Test
	void filterShoppingItems_lower() {
		String shopping = "AaBCD";
		List<Character> expectedResult = List.of('A','A','B','C','D');
		List<Character> result = CDLCheckout.filterShoppingItems(shopping);
		assertEquals(result, expectedResult);
	}
	
	@Test
	void filterShoppingItems_number() {
		String shopping = "A1BCD";
		List<Character> expectedResult = List.of('A','B','C','D');
		List<Character> result = CDLCheckout.filterShoppingItems(shopping);
		assertEquals(result, expectedResult);
	}
	
	@Test
	void filterShoppingItems_special() {
		String shopping = "A@B'C;D£!";
		List<Character> expectedResult = List.of('A','B','C','D');
		List<Character> result = CDLCheckout.filterShoppingItems(shopping);
		assertEquals(result, expectedResult);
	}
	
	@Test
	void calculateTotalValue_singleton() {
		List<Character> shopping = List.of('A');
		int expectedValue = 50;
		int result = CDLCheckout.calculateTotalValue(shopping);
		assertEquals(result, expectedValue);
	}
	
	@Test
	void calculateTotalValue_threeA_discount() {
		List<Character> shopping = List.of('A','A','A');
		int expectedValue = 130;
		int result = CDLCheckout.calculateTotalValue(shopping);
		assertEquals(result, expectedValue);
	}
	
	@Test
	void calculateTotalValue_twoB_discount() {
		List<Character> shopping = List.of('B','B');
		int expectedValue = 45;
		int result = CDLCheckout.calculateTotalValue(shopping);
		assertEquals(result, expectedValue);
	}
	
	@Test
	void calculateTotalValue_multiple_discounts() {
		List<Character> shopping = List.of('B','A','B','A','A');
		int expectedValue = 175;
		int result = CDLCheckout.calculateTotalValue(shopping);
		assertEquals(result, expectedValue);
	}
	
	@Test
	void calculateTotalValue_multiple_items() {
		List<Character> shopping = List.of('B','A','B','A','A','C','A','B','D');
		int expectedValue = 290;
		int result = CDLCheckout.calculateTotalValue(shopping);
		assertEquals(result, expectedValue);
	}
	
	@Test
	void calculateTotalValue_large() {
		List<Character> shopping = new ArrayList<>();
		for (int i = 0; i < 100; i++)
		{
			shopping.add('A');
			shopping.add('B');
			shopping.add('C');
			shopping.add('D');
		}
		int expectedValue = 10090;
		int result = CDLCheckout.calculateTotalValue(shopping);
		assertEquals(result, expectedValue);
	}


}
