package sanguine.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads a deck configuration file and returns a list of SanguineCards.
 * The influence grid is stored in red's pov the board calls flipGrid for blue.
 */
public final class DeckReader {

  private DeckReader() {}

  /**
   * Reads a deck from the given file path.
   *
   * @param path path to the deck config file
   * @return list of cards in the order they appear in the file
   * @throws IllegalArgumentException if there's an issue with the file
   */
  public static List<SanguineCard> readDeck(String path) {
    List<SanguineCard> cards = new ArrayList<>();

    try (Scanner sc = new Scanner(new File(path))) {
      while (sc.hasNextLine()) {
        String header = sc.nextLine();
        if (header.trim().isEmpty()) {
          continue;
        }
        header = header.trim();

        String[] parts = header.split("\\s+");
        if (parts.length != 3) {
          throw new IllegalArgumentException("bad header line: " + header);
        }

        String name  = parts[0];
        int cost  = Integer.parseInt(parts[1]);
        int value = Integer.parseInt(parts[2]);

        boolean[][] influence = new boolean[5][5];
        for (int r = 0; r < 5; r++) {
          String row = sc.nextLine();
          if (row.length() != 5) {
            throw new IllegalArgumentException("influence row must be 5 chars: " + row);
          }
          for (int c = 0; c < 5; c++) {
            char ch = row.charAt(c);
            if (ch == 'C') {
              if (r != 2 || c != 2) {
                throw new IllegalArgumentException("'C' must be at center (2,2)");
              }
              influence[r][c] = false;
            } else if (ch == 'I') {
              influence[r][c] = true;
            } else if (ch != 'X') {
              throw new IllegalArgumentException("invalid char in grid: '" + ch + "'");
            }
          }
        }

        cards.add(new BasicSanguineCard(name, cost, value, influence));
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("deck file not found: " + path, e);
    }

    return cards;
  }
}