package sanguine.view;

import sanguine.model.ReadOnlySanguineModel;

/**
 * Marker interface for the hand panel in the visual view.
 */
public interface HandPanelView {
  /**
   * Updates the hand panel with the current state of the model.
   *
   * @param model the current state of the model to update the hand panel to.
   */
  public void updateModel(ReadOnlySanguineModel model);
}