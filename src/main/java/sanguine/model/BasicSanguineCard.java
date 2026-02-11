package sanguine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a sanguine.model.Sanguine card with a cost, value, and
 * grid of influence.
 */
public class BasicSanguineCard implements SanguineCard {
  int cost;
  int value;
  String name;
  List<List<Boolean>> influenceGrid;

  /**
   * Creates a card from parsed data (for DeckReader).
   */
  public BasicSanguineCard(String name, int cost, int value, boolean[][] influence) {
    this.name = name;
    if (cost < 0) {
      throw new IllegalArgumentException("cost cannot be negative");
    }
    this.cost = cost;
    if (value < 0) {
      throw new IllegalArgumentException("value cannot be negative");
    }
    this.value = value;

    if (influence.length != 5) {
      throw new IllegalArgumentException("influence must be 5x5");
    }
    this.influenceGrid = new ArrayList<>(5);
    for (int r = 0; r < 5; r++) {
      if (influence[r].length != 5) {
        throw new IllegalArgumentException("influence must be 5x5");
      }
      List<Boolean> row = new ArrayList<>(5);
      for (int c = 0; c < 5; c++) {
        row.add(influence[r][c]);
      }
      this.influenceGrid.add(row);
    }
  }

  @Override
  public boolean checkInfluence(int cardX, int cardY, int checkX, int checkY) {
    int diffX = checkX - cardX;
    int diffY = checkY - cardY;
    if (diffX <= -3 || diffX >= 3 || diffY <= -3 || diffY >= 3 || cardX < 0 || checkX < 0
            || checkY < 0 || cardY < 0 || cardY >= 5 || checkY >= 5 || cardX >= 5 || checkX >= 5) {
      return false;
    }
    return influenceGrid.get(2 + diffY).get(2 + diffX);
  }

  @Override
  public int getCost() {
    return this.cost;
  }

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void flipGrid() {
    List<List<Boolean>> newInfluenceGrid = new ArrayList<>();
    for (int row = 0; row < 5; row++) {
      newInfluenceGrid.add(new ArrayList<>());
      for (int col = 4; col >= 0; col--) {
        boolean block = influenceGrid.get(row).get(col);
        newInfluenceGrid.get(row).add(block);
      }
    }
    this.influenceGrid = newInfluenceGrid;
  }

  @Override
  public boolean[][] getInfluence() {
    boolean[][] grid = new boolean[5][5];
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        grid[r][c] = influenceGrid.get(r).get(c);
      }
    }
    return grid;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BasicSanguineCard)) {
      return false;
    } else {
      BasicSanguineCard card = (BasicSanguineCard) o;
      for (int r = 0; r < 5; r++) {
        for (int c = 0; c < 5; c++) {
          if (!card.influenceGrid.get(r).get(c).equals(influenceGrid.get(r).get(c))) {
            return false;
          }
        }
      }
      return card.getName().equals(this.name) && card.getCost() == this.cost
              && card.getValue() == this.value;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, cost, value);
  }
}
