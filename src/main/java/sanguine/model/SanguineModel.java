package sanguine.model;

/**
 * Represents a game of sanguine.model.Sanguine.
 */
public interface SanguineModel extends ReadOnlySanguineModel {
  /**
   * Plays the card at the given index for the current player,
   * at the given row and column on the board.
   *
   * @param cardIndex index of the card in the current player's deck.
   * @param row row on the board to place the card.
   * @param col column on the board to place the card.
   * @throws IllegalArgumentException if the row or column isn't
   valid, or the move isn't valid.
   */
  public void playCard(int cardIndex, int row, int col);

  /**
   * Passes the turn for the current player, ending the game
   * if this is the second consecutive pass.
   */
  public void pass();
}