package sanguine.strategies;

import sanguine.controller.SanguineController;

/**
 * Represents a player that notifies listeners when they make a move (playCard or pass).
 */
public interface PublisherPlayerAction extends Strategy {

  /**
   * Adds the given controller as a listener to this publisher.
   *
   * @param controller controller to be notified when a move is made.
   */
  public void addListener(SanguineController controller);
}
