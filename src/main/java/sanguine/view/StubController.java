package sanguine.view;

import java.awt.event.KeyEvent;

/**
 * NOTE: Not used anymore.
 * A stub controller to get the user actions and print descriptive messages
 * to the console.
 */
public class StubController {

  /**
   * Prints the card's index and current player when the player clicks a card in their hand.
   *
   * @param cardIndex the index of the selected card in the hand (-1 if nothing)
   * @param player the player whose card was clicked ("RED" or "BLUE")
   */
  public void cardSelected(int cardIndex, String player) {
    System.out.println("Card selected — Index: " + cardIndex + ", Player: " + player);
  }

  /**
   * Prints coordinates of the clicked cell when the player clicks a cell on the game board.
   *
   * @param row the row index of the clicked cell
   * @param col the column index of the clicked cell
   */
  public void cellSelected(int row, int col) {
    System.out.println("Cell selected: (" + row + ", " + col + ")");
  }

  /**
   * Prints confirmation when the player presses the Enter key to confirm a move.
   */
  public void confirmMove() {
    System.out.println("CONFIRM MOVE (Enter key)");
  }

  /**
   * Prints a pass message when the player presses the Space key to pass their turn.
   */
  public void passTurn() {
    System.out.println("PASS TURN (Space pressed)");
  }
}