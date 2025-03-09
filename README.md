# CDL Checkout System

## Description
This project is a model of a checkout system that handles custom prcing schemes that can be set by the user in the console. The code in this repository is part of a coding challenge set by CDL Software for the Intermediate Software Engineer (Java) position in 2025. 

My model achieves the following objectives:
* Allow the user to insert their own, custom pricing scheme which includes a list of items, their unit prices and discounts. This pricing scheme is used by the system to value combinations of items. A default pricing scheme is used and displayed to the user in the case that one is not provided. 
* Prompts the user to insert their shopping list, which is filtered according to which items are part of the item list. An empty list is not accepted. 
* Scans each item individually, displaying the running total of scanned items throughout the program, applying discounts where possible. After all items are scanned, the final total is displayed by the user. 
* Validate user inputs and handle errors where necessary throughout the program.
* Allow the user to use the checkout system multiple times.
* Unit tested public methods to validate the system against a range of inputs (JUnit5)

### Running tests

1. Open the project in an IDE of your choice. **Eclipse** was used to develop the app, so it is recommended.
2. **Configure Build Path** - To configure the build path in eclipse, right click on the project -> Build Path -> Configure Build Path -> Libraries.
3. **Add Libraries** - Add your JDK and JUnit5 to the Modulepath. Apply and close.
4. **Running tests** - Right click on CDLCheckoutTest.java -> Run As JUnit test

### Running Application
It is assumed that Java is installed and is accessible from the terminal. Check if java is installed through 
```
java -version
```
This code was developed using JDK 23.0.2.

To compile, run:
```
javac CDLCheckout.java
```
To run:
```
java CDLCheckout
```

**Pricing Scheme**

Follow the prompts in the terminal. If you would like to insert your own pricing scheme, you will be asked to input information in the following order: 

1. **List of Items** - Insert a list of your Stock Keeping Units, which is made up of alphabetic characters. Other characters will not be recognised and filtered out and an empty list will not be accepted. 
2. **Insert Unit Price** - As part of the custom pricing scheme, you will need to insert a unit price in pence for each recognised item in your list
3. **Insert Discount** - A discount is modelled after the following structure ```<quantity> for <discounted price>```. In this model, you will need to enter the quantity and the discounted price separately. First, you insert the number of items for which a discount can be applied, then the price at which this number of items can be purchased at. If you do not want a discount, enter 1 when prompted.  

You will need to perform steps 2 and 3 for each item in your list.

After the pricing scheme has been created, custom or otherwise, it is used by the system to calculate the value of items. If you did not insert your own pricing scheme, a default one is used, which is displayed to the user when it is applied. 

**Shopping List**

Once the pricing scheme has been applied, the user is prompted for a list of items. The user's input is filtered against the list of valid items, which is checked to ensure it is not empty. The user will not be able to pass in an empty list when prompted for their list of shopping items. 

When a valid shopping list is provided, each item in the list is scanned and used to generate and display a running total to the user as it is happening. When the total of a list of items is calculated, any potential discounts are applied on a combination of items. After every item has been scanned, the system displays the final price and allows the user to scan the next batch of items.

