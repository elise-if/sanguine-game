package sanguine.model;

import java.util.List;

/**
 * Represents a deck of cards for a single player in
 * a basic sanguine.model.Sanguine game.
 */
public class BasicSanguineDeck implements SanguineDeck {
  List<SanguineCard> deck;

  /**
   * Creates a new deck with the given config file and player.
   *
   * @param path path to config file with the deck
   * @param player player the deck is for (if Blue, cards' grids will be flipped.)
   */
  public BasicSanguineDeck(String path, Player player) {
    this.deck = DeckReader.readDeck(path);
    if (player == Player.BLUE) {
      for (SanguineCard card : deck) {
        card.flipGrid();
      }
    }
  }

  @Override
  public SanguineCard getCard(int index) {
    if (index < 0 || index >= deck.size()) {
      throw new IllegalArgumentException("Invalid index");
    }
    return deck.get(index);
  }
}
