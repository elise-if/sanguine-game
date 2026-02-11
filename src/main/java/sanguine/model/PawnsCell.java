package sanguine.model;

/**
 * Represents a cell with at least one pawn on it.
 */
public class PawnsCell implements Cell {
  private final int numPawns;
  private final Player pawnColor;

  /**
   * Creates a new pawn cell.
   *
   * @param player   player owning the pawns
   * @param numPawns number of pawns (1–3)
   * @throws IllegalArgumentException if numPawns < 1 or > 3
   */
  public PawnsCell(Player player, int numPawns) {
    if (player == null) {
      throw new IllegalArgumentException();
    }
    if (numPawns < 1 || numPawns > 3) {
      throw new IllegalArgumentException();
    }
    this.pawnColor = player;
    this.numPawns = numPawns;
  }

  @Override
  public Player getOwner() {
    return pawnColor;
  }

  @Override
  public int getPawnCount() {
    return numPawns;
  }

  @Override
  public boolean canAddCard(SanguineCard card) {
    return numPawns >= card.getCost();
  }

  @Override
  public Cell addPawns(Player player) {
    if (player == pawnColor) {
      return numPawns < 3 ? new PawnsCell(player, numPawns + 1) : this;
    } else {
      return new PawnsCell(player, numPawns);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(numPawns);
  }

  @Override public SanguineCard getCard() {
    return null;
  }

  @Override
  public Cell makeCopy() {
    return new PawnsCell(pawnColor, numPawns);
  }
}