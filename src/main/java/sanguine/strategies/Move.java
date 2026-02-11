package sanguine.strategies;

/**
 * Represents a move in Sanguine, either a card play at (row, col) with a specific card
 * or a pass.
 */
public record Move(int row, int col, int cardIndex, boolean pass) {
  /**
   * * Constructs a Move.
   *
   * @param row     the row to play on
   * @param col     the column to play on
   * @param cardIndex the index of the card in the player's hand
   */
  public Move(int row, int col, int cardIndex) {
    this(row, col, cardIndex, false);
  }

  /**
   * Pass a move.
   *
   * @param pass to create a pass move
   */
  public Move(boolean pass) {
    this(-1, -1, -1, true);
  }

  /**
   * Determines if the move is a pass.
   *
   * @return true if this move is a pass
   */
  public boolean isPass() {
    return pass;
  }

  /**
   * gets the card index.
   *
   * @return the card index
   */
  public int getCardIndex() {
    return cardIndex;
  }

  /**
   * returns the row number.
   *
   *  @return the row number
   */
  public int getRow() {
    return row;
  }

  /**
   * returns the column number.
   *
   * @return the column number
   */
  public int getCol() {
    return col;
  }

  @Override
  public String toString() {
    return pass ? "PASS" : String.format("Play card %d at (%d,%d)", cardIndex, row, col);
  }
}