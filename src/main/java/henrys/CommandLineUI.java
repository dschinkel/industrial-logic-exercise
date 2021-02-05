package henrys;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandLineUI implements RegisterUI {
  private final PrintStream outputStream;
  private final InputStream inputStream;
  private Scanner scanner;
  Map<Integer, Integer> enteredItems = new HashMap<Integer, Integer>();

  public CommandLineUI(InputStream input, OutputStream output) {
    this.inputStream = input;
    this.outputStream = new PrintStream(output);
    this.scanner = new Scanner(inputStream);
  }

  public CommandLineUI() {
    this.inputStream = System.in;
    this.outputStream = System.out;
    this.scanner = new Scanner(inputStream);
  }

  public void displayStartMessage() {
    outputStream.println("READY FOR CHECKOUT!");
  }

  public void displayItemsForSelection(Map<Integer, String> stockItems) {
    StringBuilder message = new StringBuilder("Items in Stock: ");
    stockItems.forEach((itemIndex, itemName) ->
      message.append(String.format("%d:%s ", itemIndex, itemName))
    );
    outputStream.println(message.toString());
  }

  @Override
  public Map<Integer, Integer> inputAllItems() {
    String itemId = promptForInventoryItemSelection();
    String itemQty = promptForInventoryItemQuantity();
    String done = promptforDone();

    if(done.equals("d")) {
      return enteredItems;
    }

    enteredItems.put(Integer.parseInt(itemId), Integer.parseInt(itemQty));
    inputAllItems();
    return null;
  }

  @Override
  public String promptforDone() {
    promptForDoneMessage();
    return getUserInput();
  }

  @Override
  public void displayTotalPrice(String price) {
    String message = String.format("Total Price is: %s", price);
    outputStream.println(message.toString());
  }

  @Override
  public void promptForDoneMessage() {
    outputStream.println("Press d for done or c to continue:");
  }

  @Override
  public String promptForInventoryItemSelection() {
    promptForInventoryItem();
    return getUserInput();
  }

  private void promptForInventoryItem() {
    outputStream.println("Please Specify an Item by Number:");
  }

  @Override
  public String promptForInventoryItemQuantity() {
    promptForInventoryItemQuantityMessage();
    return getUserInput();
  }

  @Override
  public void promptForInventoryItemQuantityMessage() {
    outputStream.println("Please Specify a Qty (e.g. 1, 2, 3, etc.) for this item:");
  }

  private String getUserInput() {
    String input = "";
    while (scanner.hasNextLine()) {
      input = scanner.nextLine();
      break;
    }
    return input;
  }
}
