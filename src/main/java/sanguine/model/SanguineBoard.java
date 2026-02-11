package sanguine.model;

import java.util.List;

/**
 * Represents a board in a sanguine.model.Sanguine game.
 */
public interface SanguineBoard {
  /**
   * Plays the given card at the given row and column for the player.
   *
   * @param card card to be played.
   * @param player player playing the card.
   * @param row row to place the card.
   * @param col column to place the card.
   */
  public void playCard(SanguineCard card, Player player, int row, int col);

  /**
   * Returns the score for the given player and row.
   *
   * @param row row to get the score for.
   * @param player player to get the score for.
   * @return the score of the player in that row.
   */
  public int getScore(int row, Player player);

  /**
   * Returns the width of the board.
   *
   * @return the width of the board.
   */
  public int getWidth();

  /**
   * Returns the height of the board.
   *
   * @return the height of the board.
   */
  public int getHeight();

  /**
   * Returns the list of cells that make up the board.
   *
   * @return the list of cells that make up the board.
   */
  public List<List<Cell>> getBoardCells();

  /**
   * Recalculates the scores in each row.
   */
  public void recalcAllScores();
}
