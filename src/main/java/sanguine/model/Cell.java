package sanguine.model;

/**
 * Represents a single cell on a board of sanguine.model.Sanguine.
 */
public interface Cell {
  /**
   * Returns whether the card can be placed on this cell.
   *
   * @param card the card to be placed on this cell.
   * @return whether the card can be placed on this cell.
   */
  public boolean canAddCard(SanguineCard card);

  /**
   * If possible, adds a pawn to this cell for the given player and returns the cell.
   *
   * @param player the player the pawns represent.
   * @return the new cell with the pawns added.
   */
  public Cell addPawns(Player player);

  /**
   * Returns the string representation for this cell.
   *
   * @return the string representation of this cell.
   */
  public String toString();

  /**
   * Gets the owner of the cell.
   *
   * @return the Owner of the cell
   */
  public Player getOwner();

  /**
   * Gets the number of pawns for the cell.
   *
   * @return the number of pawns on the cell
   */
  public int getPawnCount();

  /**
   * The card on the cell.
   *
   * @return the SanguineCard on the cell currently.
   */
  public SanguineCard getCard();

  /**
   * Returns a copy of this cell.
   *
   * @return a copy of this cell.
   */
  public Cell makeCopy();
}
