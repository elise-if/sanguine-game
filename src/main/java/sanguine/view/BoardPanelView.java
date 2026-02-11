package sanguine.view;

import sanguine.model.ReadOnlySanguineModel;

/**
 * Marker interface for the board panel in the visual view.
 */
public interface BoardPanelView {
  /**
   * Updates the board panel with the given model.
   *
   * @param model the current model of the game to update the board with.
   */
  public void updateModel(ReadOnlySanguineModel model);
}