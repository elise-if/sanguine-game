import sanguine.model.SanguineCard;

/**
 * Make a basic mock card for testing.
 */
public class MockCard implements SanguineCard {
  private final String name;
  private final int cost;
  private final int value;

  /**
   * Initializes the mock card.
   *
   * @param name the name of the card
   * @param cost the cost of the card
   * @param value the value of the card
   */
  public MockCard(String name, int cost, int value) {
    this.name = name;
    this.cost = cost;
    this.value = value;
  }

  @Override public boolean checkInfluence(int cardX, int cardY, int checkX, int checkY) {
    return false;
  }

  @Override public int getCost() {
    return cost;
  }

  @Override public int getValue() {
    return value;
  }

  @Override public String getName() {
    return name;
  }

  @Override public void flipGrid() {}

  @Override public boolean[][] getInfluence() {
    return new boolean[5][5];
  }
}