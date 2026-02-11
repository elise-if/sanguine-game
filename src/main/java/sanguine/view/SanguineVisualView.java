package sanguine.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import sanguine.controller.PlayerActionListener;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;

/**
 * A complete visual representation of the Sanguine game for one player.
 * Shows the board, hand, turn indicator, and row scores.
 * Publishes player actions (card/cell selection, confirm, pass) via {@link PlayerActionListener}.
 */
public class SanguineVisualView extends JFrame implements VisualSanguineView {

  private ReadOnlySanguineModel model;
  private final Player playerPerspective;

  private final BoardPanel boardPanel;
  private final HandPanel handPanel;
  private final JLabel turnLabel;
  private final JPanel scorePanel;

  private final List<PlayerActionListener> actionListeners = new ArrayList<>();

  /**
   * Constructs a view dedicated to one player's perspective.
   *
   * @param model            the read-only game model
   * @param playerPerspective the player this window represents (Red or Blue)
   */
  public SanguineVisualView(ReadOnlySanguineModel model, Player playerPerspective) {
    super("Sanguine — " + playerPerspective);
    this.model = model;
    this.playerPerspective = playerPerspective;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    this.boardPanel = new BoardPanel(model);
    this.handPanel = new HandPanel(model);
    this.turnLabel = new JLabel("", SwingConstants.CENTER);
    this.turnLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
    this.scorePanel = new JPanel(new GridLayout(0, 1));

    add(boardPanel, BorderLayout.CENTER);
    add(handPanel, BorderLayout.SOUTH);
    add(turnLabel, BorderLayout.NORTH);
    add(scorePanel, BorderLayout.EAST);

    setupKeyBindings();

    pack();
    setMinimumSize(new Dimension(1200, 900));
    setLocationRelativeTo(null);
    updateAll();
  }

  /**
   * Registers a listener (typically controller) to receive player input events.
   */
  public void addPlayerActionListener(PlayerActionListener listener) {
    actionListeners.add(listener);
    boardPanel.addPlayerActionListener(listener);
    handPanel.addPlayerActionListener(listener);
  }

  /** Sets up Enter and Space key bindings for confirm/pass when it is this player's turn. */
  private void setupKeyBindings() {
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (!model.getCurrentPlayer().equals(playerPerspective)) {
          return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          fireConfirmMove();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          firePassTurn();
        }
      }
    });
    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
  }

  /** Notifies all listeners that the player confirms their move. */
  private void fireConfirmMove() {
    for (PlayerActionListener l : actionListeners) {
      l.confirmMove();
    }
  }

  /** Notifies all listeners that the player passes their turn. */
  private void firePassTurn() {
    for (PlayerActionListener l : actionListeners) {
      l.passTurn();
    }
  }

  @Override
  public void makeVisible() {
    setVisible(true);
    requestFocusInWindow();
  }

  @Override
  public void refresh() {
    updateAll();
    boardPanel.repaint();
    handPanel.repaint();
  }

  /**
   * Fully updates the view from the current model state.
   */
  public void update(ReadOnlySanguineModel model) {
    this.model = model;
    updateAll();
    boardPanel.updateModel(model);
    handPanel.updateModel(model);
    repaint();
  }

  /**
   * Updates turn label, score panel, and window title from the current model state.
   */
  private void updateAll() {
    updateTurnLabel();
    updateScores();
    updateTitle();
  }

  /** Updates the large "X's Turn" label at the top with correct color. */
  private void updateTurnLabel() {
    Player current = model.getCurrentPlayer();
    turnLabel.setText(current + "'s Turn");
    turnLabel.setForeground(current == Player.RED ? new Color(200, 40, 40)
        : new Color(40, 40, 200));
  }

  /** Updates the window title to show whether it is currently this player's turn. */
  private void updateTitle() {
    boolean myTurn = model.getCurrentPlayer() == playerPerspective;
    setTitle("Sanguine — " + playerPerspective + (myTurn ? " (YOUR TURN)" : " (Waiting)"));
  }

  /** Rebuilds the right-hand score panel with up-to-date per-row scores. */
  private void updateScores() {
    scorePanel.removeAll();
    for (int r = 0; r < model.getBoardHeight(); r++) {
      JLabel label = new JLabel(
          String.format("Row %d:  Red %2d  |  Blue %2d",
              r, model.getScore(r, Player.RED), model.getScore(r, Player.BLUE)),
          SwingConstants.CENTER);
      label.setFont(new Font("Monospaced", Font.BOLD, 15));
      scorePanel.add(label);
    }
    scorePanel.revalidate();
    scorePanel.repaint();
  }

  /**
   * Draws a card with consistent appearance.
   *
   * @param g2d    the graphics context
   * @param card   the card to draw
   * @param owner  the player owning the card
   * @param x      top-left x coordinate
   * @param y      top-left y coordinate
   * @param w      width of the card rectangle
   * @param h      height of the card rectangle
   * @param inHand if true, the 5×5 influence grid is drawn
   */
  public static void drawCardStatic(Graphics2D g2d, SanguineCard card, Player owner,
                                    int x, int y, int w, int h, boolean inHand) {
    g2d.setColor(owner == Player.RED ? new Color(255, 230, 230) :
        new Color(230, 230, 255));
    g2d.fillRoundRect(x, y, w, h, 25, 25);
    g2d.setColor(Color.BLACK);
    g2d.drawRoundRect(x, y, w, h, 25, 25);

    g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
    g2d.drawString(card.getName(), x + 12, y + 30);
    g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
    g2d.drawString("Cost: " + card.getCost(), x + 12, y + 52);
    g2d.drawString("Value: " + card.getValue(), x + 12, y + 72);

    if (inHand) {
      boolean[][] inf = card.getInfluence();
      if (owner == Player.BLUE) {
        inf = mirrorInfluence(inf);
      }

      int gridX = x + (w - 50) / 2;
      int gridY = y + 100;
      for (int r = 0; r < 5; r++) {
        for (int c = 0; c < 5; c++) {
          int cx = gridX + c * 10;
          int cy = gridY + r * 10;
          g2d.setColor(inf[r][c] ? Color.CYAN : (r == 2 && c == 2) ? Color.ORANGE :
              Color.LIGHT_GRAY);
          g2d.fillRect(cx, cy, 10, 10);
          g2d.setColor(Color.GRAY);
          g2d.drawRect(cx, cy, 10, 10);
        }
      }
    }
  }

  /**
   * Returns a horizontally mirrored copy of a 5×5 influence grid.
   * Used when rendering Blue player's cards so their influence pattern faces the correct direction.
   */
  private static boolean[][] mirrorInfluence(boolean[][] grid) {
    boolean[][] mirrored = new boolean[5][5];
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        mirrored[r][c] = grid[r][4 - c];
      }
    }
    return mirrored;
  }
}