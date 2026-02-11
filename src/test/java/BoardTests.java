import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import sanguine.model.BasicSanguineBoard;
import sanguine.model.BasicSanguineCard;
import sanguine.model.Player;
import sanguine.model.SanguineBoard;
import sanguine.model.SanguineCard;


/**
 * Tests for BasicSanguineBoard class.
 */
public class BoardTests {

  private SanguineCard securityCard;   // cost 1, value 2
  private SanguineCard levrikonCard;   // cost 2, value 3
  private SanguineBoard board3x5;
  private SanguineBoard board1x3;

  /**
   * Set up security card, Levrikon card, and board for tests.
   */
  @Before
  public void setUp() {
    boolean[][] securityGrid = {
        {false, false, false, false, false},
        {false, false, true,  false, false},
        {false, true,  false, true,  false},
        {false, false, true,  false, false},
        {false, false, false, false, false}
    };
    securityCard = new BasicSanguineCard("Security", 1, 2, securityGrid);

    boolean[][] levrikonGrid = {
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, false, true,  false},
        {false, false, true,  false, false},
        {false, false, false, false, false}
    };
    levrikonCard = new BasicSanguineCard("Levrikon", 2, 3, levrikonGrid);

    board3x5 = new BasicSanguineBoard(3, 5);
    board1x3 = new BasicSanguineBoard(1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidRows() {
    new BasicSanguineBoard(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidColsEven() {
    new BasicSanguineBoard(3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidColsTooSmall() {
    new BasicSanguineBoard(3, 1);
  }

  @Test
  public void testConstructorValidDimensions() {
    SanguineBoard board = new BasicSanguineBoard(2, 7);
    assertEquals(2, board.getHeight());
    assertEquals(7, board.getWidth());
  }

  @Test
  public void testInitialPawnsPlaced() {
    String expected =
        "0 1___1 0\n"
            + "0 1___1 0\n"
            + "0 1___1 0";
    assertEquals(expected, board3x5.toString());
  }

  @Test
  public void testInitialScoresAllZeroExceptPawns() {
    for (int r = 0; r < 3; r++) {
      assertEquals(0, board3x5.getScore(r, Player.RED));
      assertEquals(0, board3x5.getScore(r, Player.BLUE));
    }
  }

  @Test
  public void testPlayCardOnOwnPawnCost1() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    String expected =
        "2 R1__1 0\n"
            + "0 2___1 0\n"
            + "0 1___1 0";
    assertEquals(expected, board3x5.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardOnOwnPawnCost2Requires2Pawns() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    board3x5.playCard(levrikonCard, Player.RED, 0, 1);
  }

  @Test
  public void testInfluenceAddsPawnToEmpty() {
    board3x5.playCard(securityCard, Player.RED, 1, 0);
    String expected =
        "0 2___1 0\n"
            + "2 R1__1 0\n"
            + "0 2___1 0";
    assertEquals(expected, board3x5.toString());
  }

  @Test
  public void testInfluenceConvertsOpponentPawn() {
    board3x5.playCard(securityCard, Player.BLUE, 0, 4);
    String expected =
        "0 1__1B 2\n"
            + "0 1___2 0\n"
            + "0 1___1 0";
    assertEquals(expected, board3x5.toString());
  }

  @Test
  public void testInfluenceDoesNotAffectCard() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    board3x5.playCard(securityCard, Player.BLUE, 0, 4);

    String expected =
        "2 R1_1B 2\n"
            + "0 2___2 0\n"
            + "0 1___1 0";
    assertEquals(expected, board3x5.toString());
  }

  @Test
  public void testInfluenceOffBoardIgnored() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    String expected =
        "2 R1__1 0\n"
            + "0 2___1 0\n"
            + "0 1___1 0";
    assertEquals(expected, board3x5.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testBluePlayerInfluenceMirrored() {
    SanguineCard blueLevrikon = new BasicSanguineCard("Levrikon", 2, 3,
        levrikonCard.getInfluence());
    blueLevrikon.flipGrid();
    board3x5.playCard(blueLevrikon, Player.BLUE, 0, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardInvalidRow() {
    board3x5.playCard(securityCard, Player.RED, -1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardInvalidCol() {
    board3x5.playCard(securityCard, Player.RED, 0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardOutOfBounds() {
    board3x5.playCard(securityCard, Player.RED, 3, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardOnEmptyCell() {
    board3x5.playCard(securityCard, Player.RED, 0, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardOnOpponentPawn() {
    board3x5.playCard(securityCard, Player.RED, 0, 4);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardNotEnoughPawns() {
    board3x5.playCard(levrikonCard, Player.RED, 0, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardOnExistingCard() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    board3x5.playCard(securityCard, Player.RED, 0, 0);
  }

  @Test
  public void testScoreAfterCardPlacement() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    assertEquals(2, board3x5.getScore(0, Player.RED));
    assertEquals(0, board3x5.getScore(0, Player.BLUE));
  }

  @Test
  public void testScoreMultipleCardsSameRow() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    board3x5.playCard(securityCard, Player.BLUE, 0, 4);
    assertEquals(2, board3x5.getScore(0, Player.RED));
    assertEquals(2, board3x5.getScore(0, Player.BLUE));
  }

  @Test
  public void testScorePawnsDoNotCount() {
    board3x5.playCard(securityCard, Player.RED, 1, 0);
    assertEquals(0, board3x5.getScore(0, Player.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetScoreInvalidRow() {
    board3x5.getScore(-1, Player.RED);
  }

  @Test
  public void testToString1x3Board() {
    String expected = "0 1_1 0";
    assertEquals(expected, board1x3.toString());
  }

  @Test
  public void testToStringAfterComplexPlay() {
    board3x5.playCard(securityCard, Player.BLUE, 0, 4);
    board3x5.playCard(securityCard, Player.BLUE, 2, 4);

    board3x5.playCard(securityCard, Player.RED, 1, 0);
    board3x5.playCard(levrikonCard, Player.BLUE, 1, 4);

    String expected =
        "0 2__1B 2\n"
            + "2 R1_1B 3\n"
            + "0 2__1B 2";
    assertEquals(expected, board3x5.toString());
  }

  @Test
  public void testMaxPawns3() {
    board3x5.playCard(securityCard, Player.RED, 0, 0);
    board3x5.playCard(securityCard, Player.RED, 0, 1);
    board3x5.playCard(securityCard, Player.RED, 0, 2);
    String row0 = board3x5.toString().split("\n")[0];
    assertFalse(row0.contains("3"));
  }

  @Test
  public void testGetWidthAndHeight() {
    assertEquals(5, board3x5.getWidth());
    assertEquals(3, board3x5.getHeight());
    assertEquals(3, board1x3.getWidth());
    assertEquals(1, board1x3.getHeight());
  }
}