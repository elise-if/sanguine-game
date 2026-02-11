package sanguine.model;

/**
 * Represents possible players in a game of sanguine.model.Sanguine.
 */
public enum Player {
  RED, BLUE;

  /** Helper to return the opponent of the current player. */
  public Player opponent() {
    return this == RED ? BLUE : RED;
  }
}
