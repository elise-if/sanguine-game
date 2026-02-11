package sanguine.model;

import java.util.ArrayList;
import java.util.List;
import sanguine.controller.GameStatusListener;
import sanguine.controller.PublisherSanguineModel;

/**
 * Represents a basic game of sanguine.model.Sanguine.
 */
public class BasicSanguine implements PublisherSanguineModel, ReadOnlySanguineModel {
  private final SanguineBoard board;
  private final List<SanguineCard> redDeck;
  private final List<SanguineCard> blueDeck;
  private final List<SanguineCard> redHand;
  private final List<SanguineCard> blueHand;
  private Player currentPlayer;
  private int consecutivePasses;
  private final int handSize;
  private final List<GameStatusListener> statusListeners = new ArrayList<>();

  /**
   * Initializes a new game of basic sanguine.model.Sanguine.
   *
   * @param rows      board height (>0)
   * @param cols      board width (odd, >1)
   * @param deckFile  path to a deck configuration
   * @param handSize  ≤ deck size/3
   */
  //INVARIANT: number of columns is odd. This is a logical statement, an instantaneous state,
  // upheld by the constructor (the BasicSanguineBoard constructor throws an exception if the
  // columns aren't odd), and maintained by the methods (no method mutates the number of columns)
  public BasicSanguine(int rows, int cols, String deckFile, int handSize) {
    if (handSize < 0) {
      throw new IllegalArgumentException("handSize negative");
    }
    this.board = new BasicSanguineBoard(rows, cols);

    List<SanguineCard> master = DeckReader.readDeck(deckFile);
    if (master.size() < rows * cols) {
      throw new IllegalArgumentException("deck too small for board");
    }
    if (handSize > master.size() / 3) {
      throw new IllegalArgumentException("hand size too large");
    }

    this.redDeck = new ArrayList<>(master);
    this.blueDeck = new ArrayList<>(master);
    this.handSize = handSize;
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();

    for (int i = 0; i < handSize; i++) {
      redHand.add(redDeck.remove(0));
      blueHand.add(blueDeck.remove(0));
    }

    this.currentPlayer = Player.RED;
    this.consecutivePasses = 0;
  }

  @Override
  public void playCard(int cardIndex, int row, int col) {
    List<SanguineCard> hand = currentPlayer == Player.RED ? redHand : blueHand;
    if (cardIndex < 0 || cardIndex >= hand.size()) {
      throw new IllegalArgumentException("invalid card index");
    }

    SanguineCard card = hand.get(cardIndex);
    board.playCard(card, currentPlayer, row, col);

    hand.remove(cardIndex);
    drawIfPossible();
    consecutivePasses = 0;
    switchTurn();

    if (isGameOver()) {
      notifyGameOver();
    }
  }

  @Override
  public void pass() {
    consecutivePasses++;
    drawIfPossible();
    switchTurn();

    if (isGameOver()) {
      notifyGameOver();
    }
  }

  /**
   * Switches the turn of the player from red to blue or blue to red.
   */
  private void switchTurn() {
    currentPlayer = currentPlayer.opponent();
    notifyTurnChanged();
  }

  /**
   * Draw from the deck to add to the hand if it is valid.
   */
  private void drawIfPossible() {
    List<SanguineCard> deck = currentPlayer == Player.RED ? redDeck : blueDeck;
    List<SanguineCard> hand = currentPlayer == Player.RED ? redHand : blueHand;
    if (!deck.isEmpty() && hand.size() < 5) {
      hand.add(deck.remove(0));
    }
  }

  @Override
  public SanguineBoard getBoard() {
    return new BasicSanguineBoard(this.board);
  }

  @Override
  public int getScore(int row, Player player) {
    return board.getScore(row, player);
  }

  @Override
  public boolean isGameOver() {
    return consecutivePasses >= 2;
  }

  @Override
  public int getBoardHeight() {
    return this.board.getHeight();
  }

  @Override
  public int getBoardWidth() {
    return this.board.getWidth();
  }

  @Override
  public Cell getCell(int row, int col) {
    if (row < 0 || col < 0 || row >= this.board.getHeight() || col >= this.board.getWidth()) {
      throw new IllegalArgumentException("invalid row or col");
    }
    List<List<Cell>> cells = this.board.getBoardCells();
    return cells.get(row).get(col).makeCopy();
  }

  @Override
  public Player getOwner(int row, int col) {
    if (row < 0 || col < 0 || row >= this.board.getHeight() || col >= this.board.getWidth()) {
      throw new IllegalArgumentException("invalid row or col");
    }
    return this.board.getBoardCells().get(row).get(col).getOwner();
  }

  @Override
  public boolean canPlayCard(int cardIdx, int row, int col) {
    if (row < 0 || col < 0 || row >= this.board.getHeight() || col >= this.board.getWidth()
        || cardIdx < 0) {
      return false;
    }
    Cell cell = this.board.getBoardCells().get(row).get(col);
    if (this.currentPlayer == Player.RED) {
      if (cardIdx >= this.redHand.size()) {
        return false;
      }
      return cell.canAddCard(redHand.get(cardIdx)) && cell.getOwner() == Player.RED;
    } else {
      if (cardIdx >= this.blueHand.size()) {
        return false;
      }
      return cell.canAddCard(blueHand.get(cardIdx)) && cell.getOwner() == Player.BLUE;  // ← FIXED
    }
  }

  @Override
  public int getTotalScore(Player player) {
    int score = 0;
    for (int row  = 0; row < this.board.getHeight(); row++) {
      score +=  getScore(row, player);
    }
    return score;
  }

  @Override
  public Player getWinner() {
    int blueScore = this.getTotalScore(Player.BLUE);
    int redScore = this.getTotalScore(Player.RED);
    if (!this.isGameOver() || blueScore == redScore) {
      return null;
    } else if (blueScore > redScore) {
      return Player.BLUE;
    } else {
      return Player.RED;
    }
  }

  /**
   * Gets which Player's turn it is.
   *
   * @return the current Player's turn.
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public List<SanguineCard> getHand(Player p) {
    if (p == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    return new ArrayList<>(p == Player.RED ? redHand : blueHand);
  }

  @Override
  public void addGameStatusListener(GameStatusListener listener) {
    statusListeners.add(listener);
  }

  @Override
  public void startGame() {
    notifyTurnChanged();
  }

  private void notifyTurnChanged() {
    for (GameStatusListener l : statusListeners) {
      l.turnChanged(currentPlayer);
    }
  }

  private void notifyGameOver() {
    Player winner = getWinner();
    int redScore = getTotalScore(Player.RED);
    int blueScore = getTotalScore(Player.BLUE);
    for (GameStatusListener l : statusListeners) {
      l.gameOver(winner, redScore, blueScore);
    }
  }
}
