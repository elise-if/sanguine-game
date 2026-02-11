package sanguine;

import java.util.List;
import sanguine.model.BasicSanguine;
import sanguine.model.SanguineCard;
import sanguine.view.SanguineTextualView;

/**
 * Represents a playable game of sanguine.model.Sanguine.
 */

public class Sanguine {
  /**
   * Automatically plays a game of sanguine.model.Sanguine with the decks in the given path.
   *
   * @param args main arguments, first argument should be the path to the
   *             config file.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("Must specify config file path");
    }
    BasicSanguine game = new BasicSanguine(3, 5, args[0], 5);
    SanguineTextualView textualView = new SanguineTextualView(game);
    while (!game.isGameOver()) {
      System.out.println(textualView.toString());
      List<SanguineCard> hand = game.getHand(game.getCurrentPlayer());
      if (hand.isEmpty()) {
        game.pass();
        continue;
      }
      boolean played = false;
      for (int i = 0; i < hand.size() && !played; i++) {
        for (int r = 0; r < 3 && !played; r++) {
          for (int c = 0; c < 5 && !played; c++) {
            try {
              game.playCard(i, r, c);
              played = true;
            } catch (Exception e) {
              continue;
            }
          }
        }
      }
      if (!played) {
        game.pass();
      }
      System.out.println("\n");
    }
    System.out.println("Game over! Final game state: ");
    System.out.println(textualView.toString());
  }
}
