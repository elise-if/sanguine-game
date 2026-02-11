import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.model.BasicSanguine;
import sanguine.model.DeckReader;
import sanguine.model.Player;
import sanguine.model.SanguineBoard;
import sanguine.model.SanguineCard;

/**
 * Tests for BasicSanguine using deck files.
 */
public class ModelTests {

  private BasicSanguine game;
  private SanguineBoard board;

  private static final String CONFIG_DECK = "docs/config.deck";
  private static final String EXAMPLE_DECK = "docs/example.deck";

  /**
   * Sets up a board with the config deck.
   */
  @Before
  public void setUp() {
    game = new BasicSanguine(3, 5, CONFIG_DECK, 3);
    board = game.getBoard();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorHandSizeNegative() {
    new BasicSanguine(3, 5, CONFIG_DECK, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorDeckTooSmall() {
    new BasicSanguine(10, 10, CONFIG_DECK, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorHandSizeTooLarge() {
    new BasicSanguine(3, 5, CONFIG_DECK, 6);
  }

  @Test
  public void testInitialState() {
    this.setUp();
    assertEquals(Player.RED, game.getCurrentPlayer());
    assertFalse(game.isGameOver());
    assertEquals(3, game.getHand(Player.RED).size());
    assertEquals(3, game.getHand(Player.BLUE).size());
    assertEquals(0, game.getScore(0, Player.RED));
    assertEquals(0, game.getScore(0, Player.BLUE));
  }

  @Test
  public void testDeckHasEnoughCards() {
    List<SanguineCard> master = DeckReader.readDeck(CONFIG_DECK);
    assertTrue(master.size() >= 3 * 5);
  }

  @Test
  public void testPlayCardValid() {
    SanguineCard card = game.getHand(Player.RED).get(0);
    game.playCard(0, 0, 0);

    assertEquals(1, game.getScore(0, Player.RED));
    assertEquals(3, game.getHand(Player.RED).size());
    assertEquals(Player.BLUE, game.getCurrentPlayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardInvalidIndex() {
    game.playCard(10, 0, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardOnOpponentPawn() {
    game.playCard(0, 0, 4);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardNotEnoughPawns() {
    BasicSanguine temp = new BasicSanguine(3, 5, "docs/config.deck", 5);
    temp.playCard(4, 0, 0);
  }

  @Test
  public void testPassOnePlayer() {
    game.pass();
    assertEquals(Player.BLUE, game.getCurrentPlayer());
    assertFalse(game.isGameOver());
  }

  @Test
  public void testPassTwoPlayersEndsGame() {
    game.pass();
    game.pass();
    assertTrue(game.isGameOver());
  }

  @Test
  public void testPassResetsAfterPlay() {
    game.pass();
    game.playCard(0, 0, 4);
    game.pass();
    assertFalse(game.isGameOver());
  }

  @Test
  public void testDrawOnPlay() {
    int oldSize = game.getHand(Player.RED).size();
    game.playCard(0, 0, 0);
    assertEquals(oldSize, game.getHand(Player.RED).size());
  }

  @Test
  public void testDrawOnPass() {
    int oldSize = game.getHand(Player.BLUE).size();
    game.pass();
    assertEquals(oldSize, game.getHand(Player.BLUE).size());
  }

  @Test
  public void testNoDrawWhenHandFull() {
    BasicSanguine temp = new BasicSanguine(3, 5, CONFIG_DECK, 5);
    temp.playCard(0, 0, 0);
    assertEquals(5, temp.getHand(Player.RED).size());
  }

  @Test
  public void testNoDrawWhenDeckEmpty() {
    BasicSanguine temp = new BasicSanguine(3, 5, CONFIG_DECK, 1);
    for (int i = 0; i < 20; i++) {
      try {
        temp.playCard(0, 0, 0);
      } catch (Exception e) {
        break;
      }
    }
    int size = temp.getHand(Player.RED).size();
    try {
      temp.playCard(0, 0, 0);
    } catch (Exception e) {
      // expected
    }
    assertEquals(size, temp.getHand(Player.RED).size());
  }

  @Test
  public void testGetHandReturnsCorrectList() {
    List<SanguineCard> hand = game.getHand(Player.RED);
    assertEquals(3, hand.size());
    assertEquals(hand, game.getHand(Player.RED));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetHandNullPlayer() {
    game.getHand(null);
  }

  @Test
  public void testExampleDeckLoads() {
    BasicSanguine game2 = new BasicSanguine(3, 5, EXAMPLE_DECK, 3);
    assertEquals(3, game2.getHand(Player.RED).size());
    assertEquals(3, game2.getHand(Player.BLUE).size());
  }

  @Test
  public void testExampleDeckHasHighValueCards() {
    BasicSanguine game2 = new BasicSanguine(3, 5, EXAMPLE_DECK, 5);

    List<SanguineCard> master = DeckReader.readDeck(EXAMPLE_DECK);
    boolean hasBig = master.stream()
        .anyMatch(c -> c.getName().equals("Big") && c.getValue() == 5);

    assertTrue("example.deck should contain Big card with value 5", hasBig);
  }

  @Test
  public void testFullGameEndsCorrectly() {
    while (!game.isGameOver()) {
      List<SanguineCard> hand = game.getHand(game.getCurrentPlayer());
      if (hand.isEmpty()) {
        game.pass();
        continue;
      }
      boolean played = false;
      for (int i = 0; i < hand.size() && !played; i++) {
        for (int r = 0; r < 3 && !played; r++) {
          for (int c = 0; c < 5 && !played; c++) {
            try {
              game.playCard(i, r, c);
              played = true;
            } catch (Exception e) {
              // try next cell
            }
          }
        }
      }
      if (!played) {
        game.pass();
      }
    }
    assertTrue(game.isGameOver());
  }


  // PART 2 READONLY METHODS:
  @Test
  public void testGetBoardHeight() {
    assertEquals(3, game.getBoardHeight());
  }

  @Test
  public void testGetBoardWidth() {
    assertEquals(5, game.getBoardWidth());
  }

  @Test
  public void testGetCell() {
    assertEquals("1", game.getCell(0, 0).toString());
    assertEquals(Player.RED, game.getCell(0, 0).getOwner());
    assertEquals(1, game.getCell(0, 0).getPawnCount());
    assertEquals("_", game.getCell(0, 1).toString());
    assertEquals(0, game.getCell(0, 1).getPawnCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellInvalidPosition() {
    game.getCell(-1, 0);
    game.getCell(0, -1);
    game.getCell(4, 6);
  }

  @Test
  public void testGetHand() {
    assertEquals(3, game.getHand(Player.RED).size());
    assertEquals("Security", game.getHand(Player.RED).get(0).getName());
    assertEquals("Levrikon", game.getHand(Player.RED).get(2).getName());
    assertEquals(3, game.getHand(Player.BLUE).size());
    assertEquals("Security", game.getHand(Player.BLUE).get(0).getName());
  }

  @Test
  public void testGetOwner() {
    assertEquals(Player.RED, game.getOwner(0, 0));
    assertEquals(Player.RED, game.getOwner(1, 0));
    assertEquals(Player.BLUE, game.getOwner(0, 4));
    assertNull(game.getOwner(0, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOwnerInvalidPosition() {
    game.getOwner(-1, 0);
    game.getOwner(0, -1);
    game.getOwner(4, 0);
  }

  @Test
  public void testCanPlayCard() {
    assertTrue(game.canPlayCard(0, 0, 0));
    assertFalse(game.canPlayCard(0, 0, 1));
    assertFalse(game.canPlayCard(0, -1, -1));
    game.pass();
    assertTrue(game.canPlayCard(0, 0, 4));
    assertFalse(game.canPlayCard(0, 0, 0));
    assertFalse(game.canPlayCard(-1, 0, 4));
  }

  @Test
  public void testGetTotalScore() {
    assertEquals(0, game.getTotalScore(Player.RED));
    assertEquals(0, game.getTotalScore(Player.BLUE));
    game.playCard(0, 0, 0);
    assertEquals(1, game.getTotalScore(Player.RED));
    assertEquals(0, game.getTotalScore(Player.BLUE));
    game.playCard(0, 0, 4);
    assertEquals(1, game.getTotalScore(Player.RED));
    assertEquals(1, game.getTotalScore(Player.BLUE));
  }

  @Test
  public void testGetWinner() {
    assertNull(game.getWinner());
    game.playCard(0, 0, 0);
    assertNull(game.getWinner());
    while (!game.isGameOver()) {
      List<SanguineCard> hand = game.getHand(game.getCurrentPlayer());
      if (hand.isEmpty()) {
        game.pass();
        continue;
      }
      boolean played = false;
      for (int i = 0; i < hand.size() && !played; i++) {
        for (int r = 0; r < 3 && !played; r++) {
          for (int c = 0; c < 5 && !played; c++) {
            try {
              game.playCard(i, r, c);
              played = true;
            } catch (Exception e) {
              // try next cell
            }
          }
        }
      }
      if (!played) {
        game.pass();
      }
    }
    assertEquals(Player.BLUE, game.getWinner());
  }

  @Test
  public void testGetCurrentPlayer() {
    assertEquals(Player.RED, game.getCurrentPlayer());
    game.playCard(0, 0, 0);
    assertEquals(Player.BLUE, game.getCurrentPlayer());
    game.pass();
    assertEquals(Player.RED, game.getCurrentPlayer());
  }
}