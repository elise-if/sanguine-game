package sanguine.model;

/**
 * Represents a cell with nothing on it.
 */
public class EmptyCell implements Cell {
  /**
   * Creates a new empty cell.
   */
  public EmptyCell() {}

  @Override
  public boolean canAddCard(SanguineCard card) {
    return false;
  }

  @Override
  public Cell addPawns(Player player) {
    return new PawnsCell(player, 1);
  }

  @Override
  public String toString() {
    return "_";
  }

  @Override public Player getOwner() {
    return null;
  }

  @Override public int getPawnCount() {
    return 0;
  }

  @Override public SanguineCard getCard() {
    return null;
  }

  @Override
  public Cell makeCopy() {
    return new EmptyCell();
  }
}
