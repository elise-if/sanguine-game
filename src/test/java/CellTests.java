import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sanguine.model.BasicSanguineCard;
import sanguine.model.CardCell;
import sanguine.model.Cell;
import sanguine.model.EmptyCell;
import sanguine.model.PawnsCell;
import sanguine.model.Player;
import sanguine.model.SanguineCard;

/**
 * Tests for EmptyCell, PawnsCell, and CardCell.
 */
public class CellTests {
  SanguineCard securityCard;
  SanguineCard levrikonCard;

  /**
   * Creates a security card with a cost of 1, and a levrikon card with a cost of 2.
   */
  public void initData() {
    boolean[][] securityGrid = new boolean[][]{{false, false, false, false, false},
                                                  {false, false, true, false, false},
                                                  {false, true, false, true, false},
                                                  {false, false, true, false, false},
                                                  {false, false, false, false, false}};
    this.securityCard = new BasicSanguineCard("Security", 1, 1, securityGrid);
    boolean[][] levrikonGrid = new boolean[][]{{false, false, false, false, false},
                                                  {false, false, false, false, false},
                                                  {false, false, false, true, false},
                                                  {false, false, true, false, false},
                                                  {false, false, false, false, false}};
    this.levrikonCard = new BasicSanguineCard("Levrikon", 2, 1, levrikonGrid);

  }

  // EMPTY CELL TESTS

  @Test
  public void testCanAddCardEmptyCell() {
    this.initData();
    Cell emptyCell = new EmptyCell();
    assertFalse(emptyCell.canAddCard(securityCard));
    assertFalse(emptyCell.canAddCard(levrikonCard));
  }

  @Test
  public void testAddPawnEmptyCell() {
    this.initData();
    Cell emptyCell = new EmptyCell();
    assertEquals("_", emptyCell.toString());
    emptyCell = emptyCell.addPawns(Player.RED);
    assertEquals("1", emptyCell.toString());
  }

  @Test
  public void testToStringEmptyCell() {
    this.initData();
    Cell emptyCell = new EmptyCell();
    assertEquals("_",  emptyCell.toString());
  }

  // PAWN CELL TESTS
  @Test
  public void testcanAddCardPawnsCell() {
    this.initData();
    Cell pawnsCell1 = new PawnsCell(Player.BLUE, 1);
    assertTrue(pawnsCell1.canAddCard(securityCard));
    assertFalse(pawnsCell1.canAddCard(levrikonCard));
    Cell pawnsCell2 = new PawnsCell(Player.BLUE, 2);
    assertTrue(pawnsCell2.canAddCard(securityCard));
    assertTrue(pawnsCell2.canAddCard(levrikonCard));
  }

  @Test
  public void testAddPawnPawnsCell() {
    this.initData();
    Cell pawnsCell = new PawnsCell(Player.BLUE, 1);
    assertEquals("1", pawnsCell.toString());
    pawnsCell = pawnsCell.addPawns(Player.BLUE);
    assertEquals("2", pawnsCell.toString());
  }

  @Test
  public void testToStringPawnsCell() {
    this.initData();
    Cell pawnsCell = new PawnsCell(Player.BLUE, 2);
    assertEquals("2", pawnsCell.toString());
  }

  // CARD CELL TESTS
  @Test
  public void testcanAddCardCardCell() {
    this.initData();
    Cell cardCell = new CardCell(securityCard, Player.RED);
    assertFalse(cardCell.canAddCard(securityCard));
    assertFalse(cardCell.canAddCard(levrikonCard));
  }

  @Test
  public void testAddPawnCardCell() {
    this.initData();
    Cell cardCell = new CardCell(securityCard, Player.BLUE);
    assertEquals("B", cardCell.toString());
    cardCell = cardCell.addPawns(Player.BLUE);
    assertEquals("B",  cardCell.toString());
  }

  @Test
  public void testToStringCardCell() {
    this.initData();
    Cell cardCell = new CardCell(securityCard, Player.BLUE);
    assertEquals("B", cardCell.toString());
    Cell cardCell2 = new CardCell(levrikonCard, Player.RED);
    assertEquals("R", cardCell2.toString());
  }

  @Test
  public void testMakeCopy() {
    this.initData();
    // empty cell copy:
    Cell emptyCell = new EmptyCell();
    Cell copyCell = emptyCell.makeCopy();
    assertEquals("_", copyCell.toString());
    // changes to copy don't affect original:
    copyCell.addPawns(Player.BLUE);
    assertEquals("_", emptyCell.toString());

    // pawn cell copy:
    Cell pawnCell = new  PawnsCell(Player.RED, 2);
    Cell copyPawnCell = pawnCell.makeCopy();
    assertEquals(2, copyPawnCell.getPawnCount());
    assertEquals("2", copyPawnCell.toString());
    // changes to copy don't affect original:
    copyPawnCell.addPawns(Player.BLUE);
    assertEquals(2, pawnCell.getPawnCount());

    // card cell copy:
    Cell cardCell = new CardCell(securityCard, Player.RED);
    Cell copyCard = cardCell.makeCopy();
    assertEquals("R",  copyCard.toString());
    copyCard.addPawns(Player.BLUE);
    assertEquals("R", cardCell.toString());
  }
}