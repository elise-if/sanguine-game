package sanguine.view;

/**
 * Interface for the visual view of sanguine.model.Sanguine.
 */
public interface VisualSanguineView {
  /**
   * Makes the game visible on the screen.
   */
  void makeVisible();

  /**
   * Refreshes the display by updating the turn, row scores, and re-doing the board.
   */
  void refresh();
}