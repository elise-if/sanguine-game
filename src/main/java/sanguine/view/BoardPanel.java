package sanguine.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import sanguine.controller.PlayerActionListener;
import sanguine.model.CardCell;
import sanguine.model.Cell;
import sanguine.model.PawnsCell;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Renders the game board grid with cards and pawns.
 * Allows cell selection via mouse click and notifies listeners of selection changes.
 */
public class BoardPanel extends JPanel {

  private ReadOnlySanguineModel model;
  private final List<PlayerActionListener> actionListeners;

  private int selectedRow = -1;
  private int selectedCol = -1;

  private static final int CELL_SIZE = 90;

  /**
   * Constructs a board panel that displays the game grid and publishes cell selection events.
   *
   * @param model the read-only model providing game state
   */
  public BoardPanel(ReadOnlySanguineModel model) {
    this.model = model;
    this.actionListeners = new ArrayList<>();
    setBackground(new Color(250, 248, 240));
    addMouseListener(new BoardClickListener());
  }

  /**
   * Adds a listener to receive cell selection notifications.
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

    int rows = model.getBoardHeight();
    int cols = model.getBoardWidth();
    int startX = (getWidth() - cols * CELL_SIZE) / 2;
    int startY = (getHeight() - rows * CELL_SIZE) / 2;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        int x = startX + c * CELL_SIZE;
        int y = startY + r * CELL_SIZE;

        if (r == selectedRow && c == selectedCol) {
          g2d.setColor(new Color(100, 200, 255, 100));
          g2d.fillRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10,
              20, 20);
        }

        Cell cell = model.getCell(r, c);
        Player owner = model.getOwner(r, c);

        if (cell instanceof CardCell cc) {
          SanguineVisualView.drawCardStatic(g2d, cc.getCard(), owner,
              x + 8, y + 8, CELL_SIZE - 16, CELL_SIZE - 16, false);
        } else if (cell instanceof PawnsCell pc) {
          g2d.setColor(owner == Player.RED ? new Color(220, 50, 50) :
              new Color(50, 50, 220));
          g2d.setFont(new Font("SansSerif", Font.BOLD, 48));
          String text = String.valueOf(pc.getPawnCount());
          FontMetrics fm = g2d.getFontMetrics();
          int tx = x + (CELL_SIZE - fm.stringWidth(text)) / 2;
          int ty = y + (CELL_SIZE + fm.getAscent()) / 2;
          g2d.drawString(text, tx, ty);
        }

        // Cell border
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(x, y, CELL_SIZE - 1, CELL_SIZE - 1,
            15, 15);
      }
    }
  }

  /**
   * Updates the model reference and repaints the board.
   *
   * @param model the new read-only model
   */
  public void updateModel(ReadOnlySanguineModel model) {
    this.model = model;
    repaint();
  }

  /**
   * Notifies all registered listeners that a cell has been selected or deselected.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   */
  private void fireCellSelected(int row, int col) {
    for (PlayerActionListener listener : actionListeners) {
      listener.cellSelected(row, col);
    }
    selectedRow = row;
    selectedCol = col;
    repaint();
  }

  /**
   * Deselects the currently selected cell and notifies listeners.
   */
  private void fireCellDeselected() {
    selectedRow = -1;
    selectedCol = -1;
    for (PlayerActionListener listener : actionListeners) {
      listener.cellSelected(-1, -1); // convention: -1 means deselected
    }
    repaint();
  }

  /**
   * Mouse listener that detects clicks within board cells and toggles selection.
   */
  private class BoardClickListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      int rows = model.getBoardHeight();
      int cols = model.getBoardWidth();
      int startX = (getWidth() - cols * CELL_SIZE) / 2;
      int startY = (getHeight() - rows * CELL_SIZE) / 2;

      int mx = e.getX();
      int my = e.getY();

      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          int x = startX + c * CELL_SIZE;
          int y = startY + r * CELL_SIZE;

          if (mx >= x && mx < x + CELL_SIZE && my >= y && my < y + CELL_SIZE) {
            if (selectedRow == r && selectedCol == c) {
              fireCellDeselected();
            } else {
              fireCellSelected(r, c);
            }
            return;
          }
        }
      }
    }
  }
}