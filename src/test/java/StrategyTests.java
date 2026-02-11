import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import sanguine.model.BasicSanguine;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;
import sanguine.strategies.FillFirst;
import sanguine.strategies.MaximizeRowScore;
import sanguine.strategies.Move;
import sanguine.strategies.Strategy;

/**
 * Class for testing different strategies.
 */
public class StrategyTests {
  SanguineModel startOfGame;
  SanguineModel middleOfGame;
  SanguineModel endOfGame;
  SanguineModel maximizeRowScorePass;

  /**
   * Initializes all of the models for testing.
   */
  public void initModels() {
    this.startOfGame = new BasicSanguine(3, 5, "docs/config.deck", 3);
    this.middleOfGame = new BasicSanguine(3, 5, "docs/config.deck", 3);
    middleOfGame.playCard(0, 0, 0);
    middleOfGame.playCard(0, 0, 4);
    middleOfGame.playCard(0, 1, 0);

    this.endOfGame = new BasicSanguine(3, 5, "docs/config.deck", 3);
    while (!endOfGame.isGameOver()) {
      List<SanguineCard> hand = endOfGame.getHand(endOfGame.getCurrentPlayer());
      if (hand.isEmpty()) {
        endOfGame.pass();
        continue;
      }
      boolean played = false;
      for (int i = 0; i < hand.size() && !played; i++) {
        for (int r = 0; r < 3 && !played; r++) {
          for (int c = 0; c < 5 && !played; c++) {
            try {
              endOfGame.playCard(i, r, c);
              played = true;
            } catch (Exception e) {
              // try next cell
            }
          }
        }
      }
      if (!played) {
        endOfGame.pass();
      }
    }

    this.maximizeRowScorePass = new  BasicSanguine(3, 5, "docs/config.deck", 1);
    maximizeRowScorePass.pass();
    maximizeRowScorePass.playCard(0, 0, 4);
    maximizeRowScorePass.pass();
    maximizeRowScorePass.playCard(0, 1, 4);
    maximizeRowScorePass.pass();
    maximizeRowScorePass.playCard(0, 2, 4);
  }

  @Test
  public void testFillFirstChooseMove() {
    this.initModels();
    // testing that the first move is in row 0, col 0
    Strategy fillFirst = new FillFirst();
    assertEquals(new Move(0, 0, 0, false),
            fillFirst.chooseMove(this.startOfGame, Player.RED));

    // testing that the second move for blue is row 0, col 3
    assertEquals(new Move(0, 3, 0, false),
            fillFirst.chooseMove(this.middleOfGame, Player.BLUE));

    // testing that the strategy passes when the board is full
    assertEquals(new Move(true),
            fillFirst.chooseMove(this.endOfGame, Player.RED));
  }

  @Test
  public void testFillFirstMakeMove() {
    this.initModels();
    Strategy fillFirst = new FillFirst();
    fillFirst.makeMove(this.startOfGame, Player.RED);
    assertEquals(1, startOfGame.getScore(0, Player.RED));
    assertEquals("Security", startOfGame.getCell(0, 0).getCard().getName());

    fillFirst.makeMove(this.middleOfGame, Player.BLUE);
    assertEquals(2, this.middleOfGame.getScore(0, Player.BLUE));
    assertEquals("Security", this.middleOfGame.getCell(0, 3).getCard().getName());

    assertEquals(10, this.endOfGame.getTotalScore(Player.RED));
    fillFirst.makeMove(this.endOfGame, Player.RED);
    assertEquals(Player.BLUE, this.endOfGame.getCurrentPlayer());
    assertEquals(10, this.endOfGame.getTotalScore(Player.RED));
  }

  @Test
  public void testMaximizeRowScoreChooseMove() {
    this.initModels();
    //testing that the first move is row 0, col 0
    Strategy maximize = new MaximizeRowScore();
    assertEquals(new Move(0, 0, 0, false),
            maximize.chooseMove(this.startOfGame, Player.RED));

    //testing that the second move for blue is row 0, col 0
    assertEquals(new Move(0, 3, 0, false),
            maximize.chooseMove(this.middleOfGame, Player.BLUE));

    //testing that the strategy passes at the end of the game.
    assertEquals(new Move(true),
            maximize.chooseMove(this.endOfGame, Player.RED));

    //testing that the strategy passes if it cannot get a higher score than the other player
    assertEquals(new Move(0, 0, 0, true),
            maximize.chooseMove(this.maximizeRowScorePass, Player.RED));
  }

  @Test
  public void testMaximizeRowScoreMakeMove() {
    this.initModels();

    Strategy maximize = new MaximizeRowScore();
    maximize.makeMove(this.startOfGame, Player.RED);
    assertEquals(1, startOfGame.getScore(0, Player.RED));
    assertEquals("Security", startOfGame.getCell(0, 0).getCard().getName());

    maximize.makeMove(this.middleOfGame, Player.BLUE);
    assertEquals(2, this.middleOfGame.getScore(0, Player.BLUE));
    assertEquals("Security", this.middleOfGame.getCell(0, 3).getCard().getName());

    assertEquals(10, this.endOfGame.getTotalScore(Player.RED));
    maximize.makeMove(this.endOfGame, Player.RED);
    assertEquals(Player.BLUE, this.endOfGame.getCurrentPlayer());
    assertEquals(10, this.endOfGame.getTotalScore(Player.RED));

    assertEquals(0, this.maximizeRowScorePass.getTotalScore(Player.RED));
    maximize.makeMove(this.maximizeRowScorePass, Player.RED);
    assertEquals(Player.BLUE, this.maximizeRowScorePass.getCurrentPlayer());
    assertEquals(0, this.maximizeRowScorePass.getTotalScore(Player.RED));
  }

  @Test
  public void testCellChecks() {
    // checking that the first strategy stops checking after the first possible location
    Appendable log1 = new StringBuilder();
    SanguineModel mock1 = new CellsInspectedMock(log1);
    new FillFirst().chooseMove(mock1, Player.RED);
    String expected = "Checked card at idx: 0, row: 0, col: 0\n";
    assertEquals(expected, log1.toString());

    //checking that maximize row score checks all rows before passing
    Appendable log2 = new StringBuilder();
    SanguineModel mock2 = new NoMovesMock(log2);
    new MaximizeRowScore().chooseMove(mock2, Player.RED);
    String expected2 = "Checked card at idx: 0, row: 0, col: 0\n"
            + "Checked card at idx: 0, row: 0, col: 1\n"
            + "Checked card at idx: 0, row: 1, col: 0\n"
            + "Checked card at idx: 0, row: 1, col: 1\n"
            + "Checked card at idx: 0, row: 2, col: 0\n"
            + "Checked card at idx: 0, row: 2, col: 1\n";
    assertEquals(expected2, log2.toString());
  }

  @Test
  public void testStrategyTwoOneValidMove() {
    SanguineModel mock = new OneValidMoveMock();
    Move move = new MaximizeRowScore().chooseMove(mock, Player.RED);
    assertEquals(new Move(1, 0, 0), move);
  }

  @Test
  public void testingTranscripts() {
    Appendable log1 = new StringBuilder();
    SanguineModel mock = new MaxCardsCheckedMock(log1);
    Strategy fillFirst = new FillFirst();
    fillFirst.makeMove(mock,  Player.RED);
    String transcript1 = "Checked card at row: 0, col: 0\n"
        + "Checked card at row: 0, col: 1\n"
        + "Checked card at row: 0, col: 2\n"
        + "Checked card at row: 0, col: 3\n"
        + "Checked card at row: 0, col: 4\n"
        + "Checked card at row: 1, col: 0\n"
        + "Checked card at row: 1, col: 1\n"
        + "Checked card at row: 1, col: 2\n"
        + "Checked card at row: 1, col: 3\n"
        + "Checked card at row: 1, col: 4\n"
        + "Checked card at row: 2, col: 0\n"
        + "Checked card at row: 2, col: 1\n"
        + "Checked card at row: 2, col: 2\n"
        + "Checked card at row: 2, col: 3\n"
        + "Checked card at row: 2, col: 4\n";
    assertEquals(transcript1, log1.toString());

    Appendable log2 = new StringBuilder();
    SanguineModel mock2 = new MaxCardsCheckedMock(log2);
    Strategy maximize = new MaximizeRowScore();
    maximize.makeMove(mock2,  Player.RED);
    String transcript2 = "Checked card at row: 0, col: 0\n"
        + "Checked card at row: 0, col: 1\n"
        + "Checked card at row: 0, col: 2\n"
        + "Checked card at row: 0, col: 3\n"
        + "Checked card at row: 0, col: 4\n"
        + "Checked card at row: 1, col: 0\n"
        + "Checked card at row: 1, col: 1\n"
        + "Checked card at row: 1, col: 2\n"
        + "Checked card at row: 1, col: 3\n"
        + "Checked card at row: 1, col: 4\n"
        + "Checked card at row: 2, col: 0\n"
        + "Checked card at row: 2, col: 1\n"
        + "Checked card at row: 2, col: 2\n"
        + "Checked card at row: 2, col: 3\n"
        + "Checked card at row: 2, col: 4\n";
    assertEquals(transcript2, log2.toString());
  }
}
