package sanguine.model;

/**
 * Represents a cell with a card placed on it.
 */
public class CardCell implements Cell {
  private final SanguineCard card;
  private final Player owner;

  /**
   * Creates a new card cell.
   *
   * @param card  the card placed
   * @param owner the player who owns this cell
   */
  public CardCell(SanguineCard card, Player owner) {
    if (card == null || owner == null) {
      throw new IllegalArgumentException("card and owner cannot be null");
    }
    this.card = card;
    this.owner = owner;
  }

  @Override
  public SanguineCard getCard() {
    return card;
  }

  @Override
  public Cell makeCopy() {
    return new CardCell(new BasicSanguineCard(this.card.getName(), this.card.getCost(),
            this.card.getValue(), this.card.getInfluence()), this.owner);
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public boolean canAddCard(SanguineCard card) {
    return false;
  }

  @Override
  public Cell addPawns(Player player) {
    return this;
  }

  @Override
  public String toString() {
    return owner == Player.RED ? "R" : "B";
  }

  @Override public int getPawnCount() {
    return 0;
  }

}