package sanguine.controller;

import sanguine.model.SanguineModel;

/**
 * Represents a sanguine model that notifies subscribers when the player changes.
 */
public interface PublisherSanguineModel extends SanguineModel {
  /**
   * Lets listener receive game state changes from the model.
   *
   * @param listener the listener
   * @throws NullPointerException if listener is null
   */
  void addGameStatusListener(GameStatusListener listener);

  /**
   * Tells the player when it's their move.
   */
  void startGame();
}
