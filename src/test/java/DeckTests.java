import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sanguine.model.BasicSanguineCard;
import sanguine.model.BasicSanguineDeck;
import sanguine.model.Player;
import sanguine.model.SanguineDeck;

/**
 * testing for BasicSanguineDeck.
 */
public class DeckTests {
  @Test
  public void testValidDeckConstructorRedPlayer() {
    SanguineDeck deck = new BasicSanguineDeck("docs/config.deck", Player.RED);
    assertEquals("Security", deck.getCard(0).getName());
    assertEquals(1, deck.getCard(0).getValue());
    assertEquals(1, deck.getCard(0).getCost());
    assertEquals("Levrikon", deck.getCard(2).getName());
    assertEquals(2, deck.getCard(2).getValue());
    assertEquals(1, deck.getCard(2).getCost());
    assertFalse(deck.getCard(2).getInfluence()[2][1]);
    assertFalse(deck.getCard(2).getInfluence()[2][2]);
    assertTrue(deck.getCard(2).getInfluence()[2][3]);
  }

  @Test
  // checking that when the player is blue, the influence grid is flipped
  public void testValidDeckConstructorBluePlayer() {
    SanguineDeck deck = new  BasicSanguineDeck("docs/config.deck", Player.BLUE);
    assertEquals("Levrikon", deck.getCard(2).getName());
    assertEquals(2, deck.getCard(2).getValue());
    assertEquals(1, deck.getCard(2).getCost());
    assertTrue(deck.getCard(2).getInfluence()[2][1]);
    assertFalse(deck.getCard(2).getInfluence()[2][2]);
    assertFalse(deck.getCard(2).getInfluence()[2][3]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDeckConstructor() {
    SanguineDeck deck = new BasicSanguineDeck("config.deck", Player.BLUE);
  }

  @Test
  public void testGetCard() {
    SanguineDeck deck = new BasicSanguineDeck("docs/config.deck", Player.RED);
    boolean[][] securityGrid = new boolean[][]{{false, false, false, false, false},
                                                  {false, false, true, false, false},
                                                  {false, true, false, true, false},
                                                  {false, false, true, false, false},
                                                  {false, false, false, false, false}};
    assertEquals(new BasicSanguineCard("Security", 1, 1, securityGrid), deck.getCard(0));
    boolean[][] levrikonGrid = new boolean[][]{{false, false, false, false, false},
                                                  {false, false, false, false, false},
                                                  {false, false, false, true, false},
                                                  {false, false, true, false, false},
                                                  {false, false, false, false, false}};
    assertEquals(new BasicSanguineCard("Levrikon", 1, 2, levrikonGrid), deck.getCard(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardNegativeIndex() {
    SanguineDeck deck = new BasicSanguineDeck("docs/config.deck", Player.RED);
    deck.getCard(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardInvalidIndex() {
    SanguineDeck deck = new BasicSanguineDeck("docs/config.deck", Player.RED);
    deck.getCard(16);
  }
}
