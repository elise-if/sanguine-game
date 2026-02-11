package sanguine;

import sanguine.controller.AiPlayer;
import sanguine.controller.HumanPlayer;
import sanguine.controller.PlayablePlayer;
import sanguine.controller.PublisherSanguineModel;
import sanguine.controller.SanguineController;
import sanguine.model.BasicSanguine;
import sanguine.model.Player;
import static sanguine.model.Player.RED;
import sanguine.strategies.FillFirst;
import sanguine.strategies.MaximizeRowScore;
import sanguine.strategies.Strategy;
import sanguine.view.SanguineVisualView;

/**
 * Main entry point for the Sanguine game.
 * Configures and starts a game with two players (human or AI) based on command-line arguments.
 */
public final class SanguineGame {
  /**
   * Makes a game of Sanguine with two players based on
   * command-line arguments. Creates the shared model, two visual views, two dedicated controllers.
   * Supports any combination of human and AI opponents.
   *
   * @param args command-line arguments: rows, cols, red deck path, blue deck path,
   *             red player type, blue player type
   */
  public static void main(String[] args) {
    if (args.length != 6) {
      System.out.println("java sanguine.SanguineGame rows cols redDeck"
          + " blueDeck redPlayer bluePlayer");
      System.out.println("Player types: human, fillfirst, maxrow");
      System.out.println(args.length);
      return;
    }


    int rows = Integer.parseInt(args[0]);
    int cols = Integer.parseInt(args[1]);
    String redDeckPath = args[2];
    String blueDeckPath = args[3];
    String redPlayerType = args[4].toLowerCase();
    String bluePlayerType = args[5].toLowerCase();

    PublisherSanguineModel model = new BasicSanguine(rows, cols, redDeckPath, 5);

    SanguineVisualView redView = new SanguineVisualView(model, RED);
    SanguineVisualView blueView = new SanguineVisualView(model, Player.BLUE);

    PlayablePlayer redPlayer = createPlayer(redPlayerType);
    PlayablePlayer bluePlayer = createPlayer(bluePlayerType);

    SanguineController redController = new SanguineController(model, redView, redPlayer, RED);
    SanguineController blueController = new SanguineController(model, blueView,
        bluePlayer, Player.BLUE);

    if (redPlayer instanceof AiPlayer aiPlayer) {
          aiPlayer.setActionListener(redController);
    }
    if (bluePlayer instanceof AiPlayer aiPlayer) {
          aiPlayer.setActionListener(blueController);
    }

    redView.makeVisible();
    blueView.makeVisible();

    model.startGame();
  }

  /**
   * Factory method to create the correct player type.
   */
  private static PlayablePlayer createPlayer(String type) {
    switch (type) {
      case "human" -> {
          return new HumanPlayer();
          }
      case "fillfirst" -> {
          return new AiPlayer(new FillFirstAdapter());
          }
      case "maxrow" -> {
          return new AiPlayer(new MaximizeRowScoreAdapter());
          }
      default -> throw new IllegalArgumentException("Unknown player type: " + type
          + ". Use: human, fillfirst, maxrow");
    }
  }

  private static class FillFirstAdapter implements Strategy {
    private final FillFirst delegate = new FillFirst();

    @Override
    public sanguine.strategies.Move chooseMove(
        sanguine.model.ReadOnlySanguineModel model, Player player) {
      return delegate.chooseMove(model, player);
    }
  }

  private static class MaximizeRowScoreAdapter implements Strategy {
    private final MaximizeRowScore delegate = new MaximizeRowScore();

    @Override
    public sanguine.strategies.Move chooseMove(
        sanguine.model.ReadOnlySanguineModel model, Player player) {
      return delegate.chooseMove(model, player);
    }
  }
}