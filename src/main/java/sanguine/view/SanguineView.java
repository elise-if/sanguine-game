package sanguine.view;

/**
 * Renders a model for a sanguine.model.Sanguine game.
 */
public interface SanguineView {

  /**
   * Returns a textual representation of the current game board and row scores.
   *
   * @return the textual view as a string, with one line per row
   */
  String toString();
}