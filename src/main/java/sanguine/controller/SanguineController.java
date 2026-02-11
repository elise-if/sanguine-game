package sanguine.controller;

import javax.swing.JOptionPane;
import sanguine.model.Player;
import sanguine.strategies.ActionPublisher;
import sanguine.view.SanguineVisualView;

/**
 * Controller for a player in Sanguine.
 * Manages the view, the model, and the player object as well as listens to both GUI events
 * and model status changes.
 */
public class SanguineController implements PlayerActionListener, GameStatusListener {

  private final PublisherSanguineModel model;
  private final SanguineVisualView view;
  private final PlayablePlayer player;
  private final Player myPlayer;

  private int selectedCardIndex = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private boolean isMyTurn = false;

  /**
   * Creates a controller for one player.
   *
   * @param model     the shared game model
   * @param view      the visual view dedicated to this player
   * @param player    the player object (human or AI)
   * @param myPlayer  which color this controller represents
   */
  public SanguineController(PublisherSanguineModel model,
                            SanguineVisualView view,
                            PlayablePlayer player,
                            Player myPlayer) {
    this.model = model;
    this.view = view;
    this.player = player;
    this.myPlayer = myPlayer;

    view.addPlayerActionListener(this);
    model.addGameStatusListener(this);

    if (player instanceof ActionPublisher) {
      ((ActionPublisher) player).setActionListener(this);
    }
    updateTurnIndicator();
  }

  @Override
  public void turnChanged(Player currentPlayer) {
    boolean wasMyTurn = isMyTurn;
    isMyTurn = currentPlayer == myPlayer;

    updateTurnIndicator();
    clearSelection();
    view.refresh();

    if (isMyTurn && player instanceof AiPlayer) {
      javax.swing.SwingUtilities.invokeLater(() -> {
        player.chooseMove(model, myPlayer);
      });
    }
  }

  @Override
  public void cardSelected(int cardIndex, Player player) {
    if (!isMyTurn || player != myPlayer) {
      return;
    }
    this.selectedCardIndex = cardIndex;
  }

  @Override
  public void cellSelected(int row, int col) {
    if (!isMyTurn) {
      return;
    }
    this.selectedRow = row;
    this.selectedCol = col;
  }

  @Override
  public void confirmMove() {
    if (!isMyTurn) {
      return;
    }

    if (selectedCardIndex == -1 || selectedRow == -1 || selectedCol == -1) {
      JOptionPane.showMessageDialog(view,
          "Please select a card and a board cell first.", "Incomplete Move",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      model.playCard(selectedCardIndex, selectedRow, selectedCol);
      clearSelection();
    } catch (IllegalArgumentException | IllegalStateException ex) {
      JOptionPane.showMessageDialog(view,
          "Invalid move: " + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  @Override
  public void passTurn() {
    if (!isMyTurn) {
      return;
    }
    model.pass();
    clearSelection();
  }

  private void clearSelection() {
    selectedCardIndex = -1;
    selectedRow = -1;
    selectedCol = -1;
  }

  private void updateTurnIndicator() {
    String status = isMyTurn ? " — Your turn" : " — Waiting";
    view.setTitle("Sanguine — " + myPlayer + status);
  }

  @Override
  public void gameOver(Player winner, int redScore, int blueScore) {
    String message;
    if (winner == null) {
      message = "It's a tie!\nFinal score: Red " + redScore + " – Blue " + blueScore;
    } else {
      message = winner + " wins!\nFinal score: Red " + redScore + " – Blue " + blueScore;
    }
    JOptionPane.showMessageDialog(view, message, "Game Over",
        JOptionPane.INFORMATION_MESSAGE);
  }
}