## Overview: 
This codebase provides the user the ability to play a game of sanguine.Sanguine. 
Currently, this can be done by providing a config file for the decks and watching the game
play out automatically, or by creating a model and playing move-by-move.

## Quick start:
- Run the main() method in src/main/sanguine.Sanguine, and input a path to a config file.
Our provided files are located at the following paths: "docs/config.deck", "docs/example.deck"
- Create a new BasicSanguine(int rows, int cols, String deckFile, int handSize), and take turns playing
with playCard(int cardIndex, int row, int col) or pass(). Visualize the game at any step
by making a new SanguineTextualView(SanguineModel model), and calling toString().

## Key components:
### src/main/java/model:

- **SanguineModel interface**: Represents a playable game of sanguine.Sanguine with its own
board, and decks and hands for each player. Methods: playCard(int cardIndex, int row, int col) to play a 
card at the given index in the current player's deck and given position. pass() to pass current player's turn,
getBoard(), getScore(), and isGameOver().
  - BasicSanguine
- **SanguineBoard interface**: Represents a board in the sanguine.Sanguine game, which is a grid of cells.
Methods: playCard(SanguineCard card, Player player, int row, int col) to play a card for the given player at the 
given position. getScore(int row, Player player) to get the score for the given player at the given row. getWidth()
and getHeight() to get board width and height, and getBoardCells() to get the list of columns of Cells.
  - BasicSanguineBoard
- **SanguineCard interface**: Represents a single sanguine.Sanguine card, with its own cost, value, name, and influence grid. 
Methods: checkInfluence(int cardX, int cardY, int checkX, int checkY) to return whether the card at the given
coordinates has influence over the "check" coordinates. flipGrid() flips the influence grid vertically, which is used
for blue players. getCost(), getValue(), getName(), and getInfluence() report card information.
  - BasicSanguineCard
- **SanguineDeck interface**: Represents a deck of cards for a player in sanguine.Sanguine. Method: getCard(int index), returns
the card at the given index in the hand, starting at 0.
  - BasicSanguineDeck
- **Cell interface**: Represents a single cell in the board's grid, which can be empty, pawns, or a card. Methods: 
canAddCard(SanguineCard card) returns whether the card can be placed on the given cell. addPawns(Player player)
returns the cell after attempting to add a pawn for the given player. getOwner() reports the player that owns the cell,
if applicable. getPawnCount() returns the number of pawns on the cell, if there are any, 
and getCard() returns the card on the cell, if there is one.
  - EmptyCell: An empty grid space. A card can never be added to this cell, but pawns can be added. The toString() 
  is "_", pawn count is 0, and owner and card are null.
  - PawnsCell: Represents a cell with a number of pawns for one player. A card can only be added if there are enough
  pawns, addPawns changes the pawns to the given player and adds pawns up to 3 on a single cell. toString() is the number
  of pawns on the cell, and the card is null.
  - CardCell: Represents a cell with a card for a single player. canAddCard is always false, numPawns is 0, no pawns
  can be added, and the toString() is the owner of the card, either "B" or "R"
- **DeckReader**: A helper class that parses through a configuration file. Has one static method readDeck(String path), 
which reads a deck configuration file given a path and returns the list of cards found in the file.
- **Player enum**: represents a player, either Blue or Red.

### src/main/java/view:

- **SanguineView interface**: Visually renders a sanguine.Sanguine game.
  - SanguineTextualView: takes in a model, and renders the board with toString()

### src/main/java/sanguine.Sanguine:
Contains a main() method, which receives input for the path to the 
deck configuration file. This creates a basic sanguine game with 3 rows, 5 columns, 
and 5 cards in each hand. It then plays out the game automatically, and outputs
the results after each move.

### src/main/java/view/SanguineVisualView:
The visual view allows the player to select a card by pressing enter and pass their turn by pressing 
the space bar.


## Changes for Part 2: 
The following methods were missing from part 1, and were added to the ReadOnlySanguineModel interface:
- getBoardHeight(): returns the number of rows in the board
- getBoardWidth(): returns the number of columns in the board
- getCell(int row, int col): returns a copy of the cell at the given position
- getHand(Player p): returs the list of cards in the hand of the given player
- getOwner(int row, int col): returns the Player that owns the cell at the given position
- canPlayCard(int cardIdx, int row, int col): returns whether the current player can play the card at the given
index in their hand at the given position on the board
- getTotalScore(Player player): returns the total score of the given player
- getWinner(): returns the winner of the game if it is over, or null if it is not over or is tied

The following method was added to the Cell interface:
- makeCopy(): creates a copy of this cell, used in the ReadOnlyInterface

## Changes for Part 3:
Most of the new interfaces and classes for implementing the controller can be found in the controller package 
in the sanguine package:
- SanguineController class: represents a controller for a single player
- PublisherSanguineModel interface: represents a model that can have several controller listeners; the BasicSanguine model
now implements this interface
- PlayerActionListener interface: listener interface for player input actions originating from the GUI
- PlayablePlayer interface: represents a player in the game
- AiPlayer, HumanPlayer: classes that implement PlayablePlayer
- GameStatusListener interface: listener interface for game state changes made by the model.

Two interfaces were added to the strategies package:
- ActionPublisher: allows an AIPlayer to publish actions to a PlayerActionListener
- PublisherPlayerAction: allows strategies to notify listeners when they make a move.

Finally, SanguineGame was modified to use the new controller, and BasicSanguine and the strategies were modified to 
notify the controller of moves being made.