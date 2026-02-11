import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sanguine.model.BasicSanguineCard;
import sanguine.model.SanguineCard;

/**
 * Testing BasicSanguineCard.
 */
public class CardTests {
  boolean[][] securityGrid;
  boolean[][] levrikonGrid;

  /**
   * Creates new basic security and levrikon cards.
   */
  public void initData() {
    this.securityGrid = new boolean[][]{{false, false, false, false, false},
                                        {false, false, true, false, false},
                                        {false, true, false, true, false},
                                        {false, false, true, false, false},
                                        {false, false, false, false, false}};
    this.levrikonGrid = new boolean[][]{{false, false, false, false, false},
                                        {false, false, false, false, false},
                                        {false, false, false, true, false},
                                        {false, false, true, false, false},
                                        {false, false, false, false, false}};
  }

  @Test
  public void testValidCardConstructor() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 1, 1, securityGrid);
    assertEquals("Security", card.getName());
    assertEquals(1, card.getCost());
    assertEquals(1, card.getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardConstructorNegativeCost() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", -1, 1, securityGrid);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardConstructorNegativeValue() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 1, -1, securityGrid);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardConstructorInvalidRowLength() {
    boolean[][] securityGrid = new boolean[][]{{false, false, false, false, false},
                                                  {false, false, true, false, false},
                                                  {false, true, false, true, false},
                                                  {false, false, true, false, false},
                                                  {false, false, false, false}};
    SanguineCard card = new BasicSanguineCard("Security", 1, 1, securityGrid);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardConstructorInvalidNumRows() {
    boolean[][] securityGrid = new boolean[][]{{false, false, false, false, false},
                                                  {false, false, true, false, false},
                                                  {false, true, false, true, false},
                                                  {false, false, true, false, false}};
    SanguineCard card = new BasicSanguineCard("Security", 1, 1, securityGrid);
  }

  @Test
  public void testCheckInfluence() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 1, 1, securityGrid);
    assertTrue(card.checkInfluence(0, 0, 1, 0));
    assertFalse(card.checkInfluence(0, 0, 1, 1));
    assertFalse(card.checkInfluence(0, 0, -1, 0));
    assertFalse(card.checkInfluence(4, 4, 5, 4));
  }

  @Test
  public void testGetCost() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 10, 1, securityGrid);
    assertEquals(10, card.getCost());
    SanguineCard card2 = new BasicSanguineCard("Levrikon", 2, 1, levrikonGrid);
    assertEquals(2, card2.getCost());
  }

  @Test
  public void testGetValue() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 1, 10, securityGrid);
    assertEquals(10, card.getValue());
    SanguineCard card2 = new BasicSanguineCard("Levrikon", 1, 2, levrikonGrid);
    assertEquals(2, card2.getValue());
  }

  @Test
  public void testGetName() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 10, 1, securityGrid);
    assertEquals("Security", card.getName());
    SanguineCard card2 = new BasicSanguineCard("Levrikon", 2, 1, levrikonGrid);
    assertEquals("Levrikon",  card2.getName());
  }

  @Test
  public void testFlipGrid() {
    this.initData();
    boolean[][] flippedGrid = new boolean[][]{{false, false, false, false, false},
                                              {false, false, false, false, false},
                                              {false, true, false, false, false},
                                              {false, false, true, false, false},
                                              {false, false, false, false, false}};
    SanguineCard card = new BasicSanguineCard("Levrikon", 2, 1, levrikonGrid);
    assertEquals(levrikonGrid, card.getInfluence());
    card.flipGrid();
    assertEquals(flippedGrid, card.getInfluence());
  }

  @Test
  public void testGetInfluence() {
    this.initData();
    SanguineCard card = new BasicSanguineCard("Security", 1, 1, securityGrid);
    assertEquals(securityGrid, card.getInfluence());
  }

  @Test
  public void testEquals() {
    this.initData();
    SanguineCard card1 = new BasicSanguineCard("Security", 1, 1, securityGrid);
    boolean[][] securityGrid2 = new boolean[][]{{false, false, false, false, false},
                                                {false, false, true, false, false},
                                                {false, true, false, true, false},
                                                {false, false, true, false, false},
                                                {false, false, false, false, false}};
    SanguineCard card2 = new BasicSanguineCard("Security", 1, 1, securityGrid2);
    assertTrue(card1.equals(card2));
    boolean[][] slightlyDifferentGrid = new boolean[][]{{false, false, false, false, false},
                                                        {false, false, true, false, false},
                                                        {false, true, false, true, false},
                                                        {false, false, true, false, false},
                                                        {true, false, false, false, false}};
    SanguineCard card3 = new BasicSanguineCard("Security", 1, 1, slightlyDifferentGrid);
    assertFalse(card1.equals(card3));
    SanguineCard card4 = new BasicSanguineCard("Levrikon", 1, 1, levrikonGrid);
    assertFalse(card1.equals(card4));
  }
}
