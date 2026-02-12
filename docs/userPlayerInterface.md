The user-player interface, when implemented, could interact with the model in the following way:

- In order to decide which move to make, the user-player can access information about the model
using the public methods such as getBoard() and getScore(). 
- They can then interact with the model by calling playCard() or pass()
- The user-player would then be integrated into the controller, prompting them to 
make a move when it's their turn, transmitting the turn to the model, and passing control to the next player.
- The textual view could also adapt to show the board for each player, and prompt them when it's their turn.