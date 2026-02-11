package sanguine.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import sanguine.controller.PlayerActionListener;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;

/**
 * Displays the current player's hand as a horizontal row of clickable cards.
 * Notifies registered {@link PlayerActionListener}s when a card is selected or deselected.
 */
public class HandPanel extends JPanel {

  private ReadOnlySanguineModel model;
  private final List<PlayerActionListener> actionListeners;

  private int selectedCardIndex = -1;

  private static final int CARD_W = 150;
  private static final int CARD_H = 220;
  private static final int CARD_SPACING = 30;

  /**
   * Constructs a hand panel that displays cards and publishes selection events.
   *
   * @param model the read-only model providing game state
   */
  public HandPanel(ReadOnlySanguineModel model) {
    this.model = model;
    this.actionListeners = new ArrayList<>();
    setBackground(new Color(240, 240, 245));
    addMouseListener(new HandClickListener());
  }

  /**
   * Adds a listener to be notified of card selection actions.
   *
   * @param listener the listener to add
   */
  public void addPlayerActionListener(PlayerActionListener listener) {
    actionListeners.add(listener);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Player currentPlayer = model.getCurrentPlayer();
    List<SanguineCard> hand = model.getHand(currentPlayer);

    if (hand.isEmpty()) {
      g2d.setColor(Color.GRAY);
      g2d.setFont(getFont().deriveFont(18f));
      g2d.drawString("No cards in hand", 30, 60);
      return;
    }

    int totalWidth = hand.size() * CARD_W + Math.max(0, hand.size() - 1) * CARD_SPACING;
    int startX = Math.max(20, (getWidth() - totalWidth) / 2);
    int y = 30;

    for (int i = 0; i < hand.size(); i++) {
      int x = startX + i * (CARD_W + CARD_SPACING);

      if (i == selectedCardIndex) {
        g2d.setColor(new Color(100, 255, 100, 100));
        g2d.fillRoundRect(x - 15, y - 15, CARD_W + 30, CARD_H + 30,
            35, 35);
      }

      SanguineVisualView.drawCardStatic(g2d, hand.get(i), currentPlayer, x, y, CARD_W, CARD_H,
          true);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1400, 280);
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(800, 250);
  }

  /**
   * Updates the model reference and repaints the hand.
   *
   * @param model the new read-only model
   */
  public void updateModel(ReadOnlySanguineModel model) {
    this.model = model;
    repaint();
  }

  /**
   * Notifies all registered listeners that a card has been selected or deselected.
   *
   * @param cardIndex the index of the selected card, or -1 if deselected
   */
  private void fireCardSelected(int cardIndex) {
    Player currentPlayer = model.getCurrentPlayer();
    for (PlayerActionListener listener : actionListeners) {
      listener.cardSelected(cardIndex, currentPlayer);
    }
    selectedCardIndex = cardIndex;
    repaint();
  }

  /**
   * Mouse listener responsible for detecting card clicks and toggling selection.
   */
  private class HandClickListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      List<SanguineCard> hand = model.getHand(model.getCurrentPlayer());
      if (hand.isEmpty()) {
        return;
      }

      int totalWidth = hand.size() * CARD_W + Math.max(0, hand.size() - 1) * CARD_SPACING;
      int startX = Math.max(20, (getWidth() - totalWidth) / 2);

      int mx = e.getX();

      for (int i = 0; i < hand.size(); i++) {
        int left = startX + i * (CARD_W + CARD_SPACING);
        int right = left + CARD_W;

        if (mx >= left && mx < right) {
          int newSelection = (selectedCardIndex == i) ? -1 : i;
          fireCardSelected(newSelection);
          return;
        }
      }
    }
  }
}