package henrys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static henrys.StockItem.ItemName.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemDiscountTest {

  ArrayList<StockItem> stockItemsDB;
  RegisterCalculator registerCalculator;
  ItemDiscount itemDiscount;
  ArrayList<StockItem> purchasedItems;

  @BeforeEach
  void setUp() {
    stockItemsDB = new StockItemRepository().findAll();
    registerCalculator = new RegisterCalculator();
    itemDiscount = new ItemDiscount();
    purchasedItems = new ArrayList<>();
  }

  @Test
  void threeSoup_twoLoavesBread_getOneloafOfBread_halfOff_purchasedDateOutOfRangeOfDiscount() {
    addToPurchasedItems(SOUP, 3);
    addToPurchasedItems(BREAD, 2);
    Double baseTotalPrice = 3.55;
    LocalDate dateTwoDaysAgo = LocalDate.now().minusDays(2);

    Double discountedPrice = itemDiscount.twoSoupGetOneLoafBreadHalfOff(purchasedItems, baseTotalPrice, dateTwoDaysAgo);

    assertEquals(baseTotalPrice, discountedPrice);
  }

  @Test
  void threeSoup_twoLoavesBread_purchasedDateToday() {
    addToPurchasedItems(SOUP, 3);
    addToPurchasedItems(BREAD, 2);
    Double baseTotalPrice = 3.55;
    LocalDate dateTwoDaysAgo = LocalDate.now();
    Double expectdDiscountedPrice = 3.15;

    Double discountedPrice = itemDiscount.twoSoupGetOneLoafBreadHalfOff(purchasedItems, baseTotalPrice, dateTwoDaysAgo);

    assertEquals(expectdDiscountedPrice, discountedPrice);
  }

  @Test
  void sixApples_and_oneBottleOfMilk_purchasedThreeDaysFromToday() {
    addToPurchasedItems(APPLE, 6);
    addToPurchasedItems(MILK, 1);
    Double baseTotalPrice = 1.84;
    LocalDate dateToday = LocalDate.now().plusDays(5);

    Double discountedPrice = itemDiscount.twoSoupGetOneLoafBreadHalfOff(purchasedItems, baseTotalPrice, dateToday);

    assertEquals(baseTotalPrice, discountedPrice);
  }

  @Test
  void apples_purchased_today() {
    addToPurchasedItems(APPLE, 3);
    LocalDate dateToday = LocalDate.now();
    Double baseTotalPrice = .30;
    Double expectedDiscountedPrice = .30;

    Double discountedPrice = itemDiscount.applyAppleTenPercentDiscount(purchasedItems, baseTotalPrice, dateToday);

    assertEquals(expectedDiscountedPrice, discountedPrice);
  }

  @Test
  void apples_purchased_threeDaysFromToday() {
    addToPurchasedItems(APPLE, 3);
    LocalDate dateThreeDaysFromToday = LocalDate.now().plusDays(3);
    Double baseTotalPrice = .30;
    Double expectedDiscountedPrice = .27;

    Double discountedPrice = itemDiscount.applyAppleTenPercentDiscount(purchasedItems, baseTotalPrice, dateThreeDaysFromToday);

    assertEquals(expectedDiscountedPrice, discountedPrice);
  }

  private void addToPurchasedItems(StockItem.ItemName itemName, Integer howManyToAdd) {
    StockItem item = stockItemsDB.get(itemName.getValue());
    for (int i = 0; i < howManyToAdd; i++) {
      purchasedItems.add(item);
    }
  }
}
