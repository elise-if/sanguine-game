package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.strategies.ActionPublisher;
import sanguine.strategies.Move;
import sanguine.strategies.Strategy;

/**
 * Represents the controller for an AI player in Sanguine.
 * The AI player uses a strategy to automatically determine its next move.
 */
public class AiPlayer implements PlayablePlayer, ActionPublisher {

  private final Strategy strategy;
  private PlayerActionListener publisher;

  /**
   * Constructs an AI player with the given strategy.
   *
   * @param strategy the strategy used to choose moves; must not be null
   * @throws IllegalArgumentException if strategy is null
   */
  public AiPlayer(Strategy strategy) {
    if (strategy == null) {
      throw new IllegalArgumentException("Strategy cannot be null");
    }
    this.strategy = strategy;
  }

  @Override
  public void setActionListener(PlayerActionListener listener) {
    this.publisher = listener;
  }

  @Override
  public Move chooseMove(ReadOnlySanguineModel model, Player player) {
    if (publisher == null) {
      throw new IllegalStateException("AIPlayer: setActionListener() was never called");
    }

    Move move = strategy.chooseMove(model, player);

    if (move.isPass()) {
      publisher.passTurn();
    } else {
      publisher.cardSelected(move.getCardIndex(), player);
      publisher.cellSelected(move.getRow(), move.getCol());
      publisher.confirmMove();
    }
    return move;
  }
}