package sanguine.model;

/**
 * Represents a card in a sanguine.model.Sanguine game.
 */
public interface SanguineCard {

  /**
   * Returns whether this card in the given position has influence on the given
   * coordinates.
   *
   * @param cardX x coordinate of this card.
   * @param cardY y coordinate of this card.
   * @param checkX x coordinate to check for influence.
   * @param checkY y coordinate to check for influence.
   * @return whether this card in the given position influences the coordinates.
   */
  public boolean checkInfluence(int cardX, int cardY, int checkX, int checkY);

  /**
   * Returns the score for this card.
   *
   * @return the score for this card.
   */
  public int getCost();

  /**
   * Returns the value of this card.
   *
   * @return the value of this card.
   */
  public int getValue();

  /**
   * Returns the name of this card.
   *
   * @return the name of this card.
   */
  public String getName();

  /**
   * Flips the influence grid of this card (used for Blue players).
   */
  public void flipGrid();

  /**
   * Returns a 5×5 boolean grid of influence (true means it affects the cell).
   */
  boolean[][] getInfluence();
}
