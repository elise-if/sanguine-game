![sanguineGameplay](https://github.com/user-attachments/assets/e176c17b-2855-44d5-8b45-9b93ea841b89)

## Rules of Sanguine:
The goal of this game, inspired by the game Queen's Blood in Final Fantasy VII, is to earn a higher score than your oponent. You gain points by placing your cards on the board, where each card has a value that contributes to your score -- but you can only place cards on grid spaces that you occupy, and have enough pawns for the cost of the card (ex. if the card has cost 2, the space must display at least "2"). Each card has its own influence, which places pawns around the card. 
<br>
<img height="200" alt="Sanguine card" src="https://github.com/user-attachments/assets/24dcd511-f804-48ee-86be-eff73f57f1c0" />
For example, this card must be placed on a cell with 2 pawns, and adds 3 to your score. The blue squares show where more pawns will be placed.
The game continues until either the whole grid is filled, or both players pass consecutively. 

## How to play: 
First, download the files and locate SanguineGame.jar -- you will need at least Java 21 to run this jar.
To run the game, run java -jar SanguineGame.jar with the following arguments:
<ol>
  <li>Number of rows -- standard row size is 3</li>
  <li>Number of columns -- standard column size is 5</li>
  <li>Path to the red player's deck -- you may use the provided deck in "docs/big.deck" or "docs/config.deck", or create your own deck following their format!</li>
  <li>Path to the blue player's deck -- same as above, this allows both players to use their own decks.</li>
  <li>Red player type -- either "human", "fillfirst" or "maxrow". The first allows you to play normally, but the second and third are robot opponents that automatically choose moves based on different strategies.</li>
  <li>Blue player type -- same as above</li>
</ol>
Once the game has started, red and blue players will take turns playing. When it's your turn, select a grid space and a card, and hit enter -- if you want to skip, just hit space!

## Quick start:
If you don't want to mess around with the arguments, here's the command to play a standard 2-player game: 
- java -jar SanguineGame.jar 3 5 "docs/big.deck" "docs/big.deck" "human" "human"

and a single-player game against an automated opponent:
- java -jar SanguineGame.jar 3 5 "docs/big.deck" "docs/big.deck" "human" "maxrow"
