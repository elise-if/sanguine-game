package sanguine.model;

/**
 * Represents a deck of cards in a sanguine.model.Sanguine game.
 */
public interface SanguineDeck {
  /**
   * Returns the card at the given index in the deck.
   *
   * @param index the index to get the card from.
   * @return the card at the index.
   */
  public SanguineCard getCard(int index);
}
