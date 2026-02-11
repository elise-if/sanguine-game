package sanguine.model;

import java.util.List;

/**
 * Represents a sanguine.model.Sanguine model that cannot mutate the game state.
 */
public interface ReadOnlySanguineModel {
  /**
   * Returns a copy of the current board.
   *
   * @return the current board.
   */
  public SanguineBoard getBoard();

  /**
   * Returns the score for the given player in the given row.
   *
   * @param row the row to get the score from.
   * @param player the player to get the score from.
   * @return the score for the given player and row.
   */
  public int getScore(int row, Player player);

  /**
   * Returns whether this game is over (after two consecutive
   * passes).
   *
   * @return whether this game is over.
   */
  public boolean isGameOver();

  /**
   * Returns the height of the board.
   *
   * @return the height of the board.
   */
  public int getBoardHeight();

  /**
   * Returns the width of the board.
   *
   * @return the width of the board.
   */
  public int getBoardWidth();

  /**
   * Returns a copy of the cell at the given row and column.
   *
   * @param row row to find the cell at.
   * @param col column to find the cell at.
   * @return a copy of the cell at that position.
   */
  public Cell getCell(int row, int col);

  /**
   * Returns a copy of the list of cards in the given player's hand.
   *
   * @param player the player to get the hand from.
   * @return a copy of their list of cards.
   */
  public List<SanguineCard> getHand(Player player);

  /**
   * Returns the owner of the cell at the given position.
   *
   * @param row the row of the cell.
   * @param col the column of the cell.
   * @return the owner of the cell at the given position.
   */
  public Player getOwner(int row, int col);

  /**
   * Returns whether it is possible for the player to play the given card at the
   * given position.
   *
   * @param cardIdx index of the card in the current player's hand.
   * @param row the row of the cell to play on.
   * @param col the column of the cell to play on.
   * @return whether the card can be played on the given cell position.
   */
  public boolean canPlayCard(int cardIdx, int row, int col);

  /**
   * Returns the score for the given player across all rows.
   *
   * @param player the player to check the score of.
   * @return the score for the player over all rows.
   */
  public int getTotalScore(Player player);

  /**
   * Returns the winner if the game is won, null if not over or tied.
   *
   * @return the winner of the game.
   */
  public Player getWinner();

  /**
   * Returns the current player whose turn it is.
   *
   * @return the current player
   */
  public Player getCurrentPlayer();
}
