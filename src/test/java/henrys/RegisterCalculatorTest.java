package henrys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static henrys.StockItem.ItemName.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterCalculatorTest {
  RegisterCalculator registerCalculator;
  ArrayList<StockItem> purchasedItems;
  ArrayList<StockItem> stockItemsDB;

  @BeforeEach
  void setUp() {
    registerCalculator = new RegisterCalculator();
    purchasedItems = new ArrayList<>();
    stockItemsDB = new StockItemRepository().findAll();
  }

  @Test
  void calculates_totalPrice_purchasedItems_soup() {
    addToPurchasedItems(SOUP, 1);
    Double expectedTotalPrice = .65;
    LocalDate dateToday = LocalDate.now();

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(expectedTotalPrice, total);
  }

  @Test
  void calculates_totalPrice_purchasedItems_bread() {
    addToPurchasedItems(BREAD, 1);
    Double expectedTotalPrice = .80;
    LocalDate dateToday = LocalDate.now();

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(expectedTotalPrice, total);
  }

  @Test
  void calculates_totalPrice_purchasedItems_oneMilk() {
    addToPurchasedItems(MILK, 1);
    Double expectedTotalPrice = 1.30;
    LocalDate dateToday = LocalDate.now();

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(expectedTotalPrice, total);
  }

  @Test
  void calculates_totalPrice_purchasedItems_oneApple_discounted() {
    addToPurchasedItems(APPLE, 1);
    Double expectedTotalPrice = .09;
    LocalDate dateToday = LocalDate.now().plusDays(3);

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(expectedTotalPrice, total);
  }

  @Test
  void calculates_totalPrice_multiplePurchasedItems() {
    addToPurchasedItems(SOUP, 1);
    addToPurchasedItems(MILK, 1);
    Double expectedTotalPrice = 1.95;
    LocalDate dateToday = LocalDate.now();

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(expectedTotalPrice, total);
  }

  @Test
  void calculates_totalPrice_withDiscount_forApples() {
    addToPurchasedItems(APPLE, 3);
    Double expectedTotalPrice = .27;
    LocalDate dateToday = LocalDate.now().plusDays(3);

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);
    assertEquals(expectedTotalPrice, total);
  }

  @Test
  void sixApples_and_oneBottleOfMilk_purchasedToday() {
    addToPurchasedItems(APPLE, 6);
    addToPurchasedItems(MILK, 1);
    Double baseTotalPrice = 1.90;
    LocalDate dateToday = LocalDate.now();

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(baseTotalPrice, total);
  }

  @Test
  void sixApples_and_oneBottleOfMilk_purchasedFiveDaysFromToday() {
    addToPurchasedItems(APPLE, 6);
    addToPurchasedItems(MILK, 1);
    Double baseTotalPrice = 1.84;
    LocalDate dateToday = LocalDate.now().plusDays(5);

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(baseTotalPrice, total);
  }

  @Test
  void threeApples_twoSoup_oneBread_purchasedFiveDaysFromToday() {
    addToPurchasedItems(APPLE, 3);
    addToPurchasedItems(SOUP, 2);
    addToPurchasedItems(BREAD, 1);
    Double baseTotalPrice = 1.97;
    LocalDate dateToday = LocalDate.now().plusDays(5);

    Double total = registerCalculator.tallyTotalForPurchasedStockItems(purchasedItems, dateToday);

    assertEquals(baseTotalPrice, total);
  }

  private StockItem createStockItem(Integer itemId, Integer quantity) {
    StockItem stockItem = new StockItem();
    stockItem.setItemId(itemId);
    stockItem.setPricePerUnit(stockItemsDB.get(itemId).getItemPricePerUnit());
    return stockItem;
  }

  private void addToPurchasedItems(StockItem.ItemName itemName, Integer howManyToAdd) {
    StockItem stockItem = createStockItem(itemName.getValue(),1);
    for (int i = 0; i < howManyToAdd; i++) {
      purchasedItems.add(stockItem);
    }
  }

}
