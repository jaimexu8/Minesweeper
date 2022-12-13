import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Jaime Xu
 * @version 12/12/22
 */
class Minesweeper extends JComponent implements Runnable {
   /** 2D Array of Tile objects */
   private static Tile[][] TILES;
   /** 2D Array representing the location of each mine */
   private static boolean[][] MINES;
   /** Playing field size (SIZE x SIZE TILES) */
   private static int SIZE = 9;
   /** Tile pixels */
   private static int TILE_PIXELS = 16; //Pixel size of sprites is 16
   /** Dimension size of field in pixels */
   private static int FIELD_PIXELS;
   /** Number of mines on the field */
   private static int N_MINES;
   /** Number of tiles left to clear */
   private static int TILES_LEFT;
   /** Position game starts at on the screen */
   private static int GAME_POS = 250;
   /** Whether or not the first tile has been cleared yet */
   private static boolean FIRST_CLEAR = true;
   /** Shows whether or not the game is still running */
   private static boolean GAME_OVER = false;
   /** User selected theme */
   private static String THEME = "Classic";
   /** Array list of current themes */
   private static String THEMES[] = {"Classic", "Mao"};
   /** User selected difficulty */
   private static String DIFFICULTY = "Beginner";
   /** Array list of current difficulties */
   private static String DIFFICULTIES[] = {"Beginner", "Intermediate", "Expert"};
   /** Combo box of themes */
   private JComboBox themesCB;
   /** Combo box of difficulties */
   private JComboBox difficultiesCB;
   /** Displays game win */
   private JLabel GAME_WIN;
   /** Displays game lost */
   private JLabel GAME_LOSE;

   private JFrame frame;
   private JPanel panel;

   private class MouseListener extends MouseAdapter {
      private int x;
      private int y;
      @Override
      public void mouseClicked(MouseEvent e) {
         x = e.getX();
         y = e.getY();
         System.out.println("User clicked: " + x + ", " + y);
         for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
               if (e.getButton() == MouseEvent.BUTTON1 && GAME_OVER == false) {
                  if (TILES[i][j].isClicked(x, y) && TILES[i][j].getStatus().equals("covered")) {
                     clearTile(TILES[i][j]);
                  }
               } else if (e.getButton() == MouseEvent.BUTTON3 && GAME_OVER == false) {
                  if (TILES[i][j].isClicked(x, y)) {
                     flagTile(TILES[i][j]);
                  }
               }
            }
         }
      }
   }

   public static void main(String[] args) { SwingUtilities.invokeLater(new Minesweeper()); }
   
   public Minesweeper() {
      frame = new JFrame("Minesweeper");
      frame.setSize(1000, 500);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.setLocationRelativeTo(null);
      panel = new JPanel();
      frame.add(panel);
   }

   public void run() {
      panel.addMouseListener(new MouseListener());
      initCB();
      Music m = new Music(THEME);
      m.playMusic();
      chooseGame("beginner");
      GAME_WIN = new JLabel("Game Won!");
      GAME_WIN.setFont(new Font("Verdana", 1, 20));
      GAME_WIN.setForeground(Color.WHITE);
      GAME_LOSE = new JLabel("Game Lost!");
      GAME_LOSE.setFont(new Font("Verdana", 1, 20));
      GAME_LOSE.setForeground(Color.WHITE);
      add(GAME_WIN);
      add(GAME_LOSE);
      GAME_WIN.setVisible(false);
      GAME_LOSE.setVisible(false);
      newGame();
   }
   
   /** Initializes combo boxes */
   public void initCB() {
      themesCB = new JComboBox(THEMES);
      difficultiesCB = new JComboBox(DIFFICULTIES);
      panel.add(themesCB);
      panel.add(difficultiesCB);
      themesCB.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String selectedTheme = (String) themesCB.getSelectedItem();
            THEME = selectedTheme;
            repaint();
         }
      });
      difficultiesCB.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            String selectedDiff = (String) difficultiesCB.getSelectedItem();
            if (selectedDiff.equals("Beginner")) chooseGame("beginner");
            if (selectedDiff.equals("Intermediate")) chooseGame("intermediate");
            if (selectedDiff.equals("Expert")) chooseGame("expert");
            newGame();
         }
      });
   }
   
   /** Draws the components onto the panel 
   * @param g Graphics object */
   public void paintComponent(Graphics g)  {
      super.paintComponent(g);
      BufferedImage bg = load("./Themes/" + THEME + "/Background.png");
      g.drawImage(bg, 0, 0, null);
      drawMap(g);
   }
   
   /** Paints each tile
   * @param g Graphics object */
   private static void drawMap(Graphics g) {
      for (int i = 0; i < SIZE; i++) {
         for (int j = 0; j < SIZE; j++) {
            String fileLoc = "./Themes/" + THEME + "/";
            String tileType = "block.png";
            if (TILES[i][j].getStatus().equals("covered"))
               tileType = "block.png";
            if (TILES[i][j].getStatus().equals("flagged"))
               tileType = "flagged.png";
            if (TILES[i][j].getStatus().equals("revealed")) {
               int nNeighbors = findNeighbors(i, j);
               if (nNeighbors == 0)
                  tileType = "empty.png";
               if (nNeighbors == 1)
                  tileType = "one.png";
               if (nNeighbors == 2)
                  tileType = "two.png";
               if (nNeighbors == 3)
                  tileType = "three.png";
               if (nNeighbors == 4)
                  tileType = "four.png";
               if (nNeighbors == 5)
                  tileType = "five.png";
               if (nNeighbors == 6)
                  tileType = "six.png";
               if (nNeighbors == 7)
                  tileType = "seven.png";
               if (nNeighbors == 8)
                  tileType = "eight.png";
            }
            BufferedImage tile = load(fileLoc + tileType);
            g.drawImage(tile, (GAME_POS - TILE_PIXELS * SIZE / 2) + i * TILE_PIXELS, (GAME_POS - TILE_PIXELS * SIZE / 2) + j * TILE_PIXELS, TILE_PIXELS, TILE_PIXELS, null);
         }
      }
   }
   
   /** Buffers the image
   * @param path Path to read file */
   public static BufferedImage load(String path){
      BufferedImage img = null;
      try {
         img = ImageIO.read(new File(path));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return img;
   }
   
   /** Chooses the field size */
   public static void chooseGame(String diff) {
      if (diff.equals("beginner")) {
         SIZE = 9;
         N_MINES = 10;
      }
      if (diff.equals("intermediate")) {
         SIZE = 16;
         N_MINES = 40;
      }      
      if (diff.equals("expert")) {
         SIZE = 22;
         N_MINES = 99;
      }
      TILES_LEFT = SIZE * SIZE - N_MINES;
   }
   
   /** Creates a new game */
   public void newGame() {
      GAME_OVER = false;
      GAME_WIN.setVisible(false);
      GAME_LOSE.setVisible(false);
      FIRST_CLEAR = true;
      FIELD_PIXELS = SIZE * TILE_PIXELS;
      TILES = new Tile[SIZE][SIZE];
      MINES = new boolean[SIZE][SIZE];
      MINES = generateMines(N_MINES);
      
      for (int i = 0; i < SIZE; i++) {
         for (int j = 0; j < SIZE; j++) {
            //Finding tile cords
            int xPos = (GAME_POS - TILE_PIXELS * SIZE / 2) + i * TILE_PIXELS; //X coordinate of tile
            int yPos = (GAME_POS - TILE_PIXELS * SIZE / 2) + j * TILE_PIXELS; //Y coordinate of tile
            Tile tile = new Tile(MINES[i][j], xPos, yPos + TILE_PIXELS, TILE_PIXELS, i, j);
            TILES[i][j] = tile;
         }
      }
      repaint();
   }
   
   /** Generates the mine field
   * @param size Number of size on the field
   * @param size Number of columns on the field
   * @param N_MINES Number of mines on the field
   * @return Returns a 2D array of booleans determining where the mines are located */
   public static boolean[][] generateMines(int N_MINES) {
      Random r = new Random();
      boolean[][] mines = new boolean[SIZE][SIZE];
      for (int i = 0; i < N_MINES; i++) {
         int mine = r.nextInt(SIZE * SIZE);
         int row = mine / SIZE;
         int col;
         if (mine % SIZE == 0) {
            col = SIZE - 1;
         } else {
            col = mine % SIZE - 1;
         }
         if (mines[row][col]) { //if it is already a mine: restart iteration
            i--;
         }
         mines[row][col] = true;
      }
      return mines;
   }
   
   /** Clears a tile when clicked
   * @param tile The tile clicked */
   public void clearTile(Tile tile) {
      int nNeighbors = findNeighbors(tile.getRow(), tile.getCol());
      if (tile.getIsMine()) {
         if (FIRST_CLEAR) { //Will not end game but will relocate first mine
            FIRST_CLEAR = false;
            relocateMine(TILES[tile.getRow()][tile.getCol()]);
            clearTile(tile);
         } else {
            gameLost();
            System.out.println("Clicked on mine");
         }
      } else {
         System.out.println("Tile cleared at: Row - " + tile.getRow() + ", Col - " + tile.getCol());
         tile.setStatus("revealed");
         FIRST_CLEAR = false;
         TILES_LEFT--;
         if (TILES_LEFT <= 0)
            gameWin();
         if (nNeighbors == 0) { //Will multiclear (recursive)
            int row = tile.getRow();
            int col = tile.getCol();
            if (row > 1) {
               if (col > 1 && TILES[row - 1][col - 1].getStatus().equals("covered"))
                  clearTile(TILES[row - 1][col - 1]);
               if (col < SIZE - 2 && TILES[row - 1][col + 1].getStatus().equals("covered"))
                  clearTile(TILES[row - 1][col + 1]);
               if (TILES[row - 1][col].getStatus().equals("covered"))
                  clearTile(TILES[row - 1][col]);
            }
            if (row < SIZE - 2) {
               if (col > 1 && TILES[row + 1][col - 1].getStatus().equals("covered"))
                  clearTile(TILES[row + 1][col - 1]);
               if (col < SIZE - 2 && TILES[row + 1][col + 1].getStatus().equals("covered"))
                  clearTile(TILES[row + 1][col + 1]);
               if (TILES[row + 1][col].getStatus().equals("covered"))
                  clearTile(TILES[row + 1][col]);
            }
            if (col > 1 && TILES[row][col - 1].getStatus().equals("covered"))
               clearTile(TILES[row][col - 1]);
            if (col < SIZE - 2 && TILES[row][col + 1].getStatus().equals("covered"))
               clearTile(TILES[row][col + 1]);
         }
      }
      repaint();
   }
   
   /** Flags the tile or unflags if already flagged
   * @param tile The tile to be flagged or unflagged */
   public void flagTile(Tile tile) {
      if (tile.getStatus().equals("covered"))
         tile.setStatus("flagged");
      else if (tile.getStatus().equals("flagged"))
         tile.setStatus("covered");
      repaint();
   }
   
   /** Relocates a mine 
   * @param tile The tile that should be relocated */
   public static void relocateMine(Tile tile) {
      tile.setIsMine(false);
      Random r = new Random();
      int row = r.nextInt(SIZE);
      int col = r.nextInt(SIZE);
      if (TILES[row][col].getIsMine() == false) { //Tile is already mine
         TILES[row][col].setIsMine(true);
      } else {
         relocateMine(tile);
      }
   }
   
   /** Called when the game has been won */
   public void gameWin() {
      System.out.println("User won");
      GAME_OVER = true;
      GAME_WIN.setVisible(true);
   }
   
   /** Called when the game has been lost */
   public void gameLost() {
      System.out.println("User lost");
      GAME_OVER = true;
      GAME_LOSE.setVisible(true);
   }
   
   /** A helper method to find the number of neighboring mines a tile has
   * @param row The current row
   * @param col The current column
   * @return The number of neighboring mines */
   public static int findNeighbors(int row, int col) {
      // TODO: change to switch statement
      int neighbors = 0;
      if (row > 0) {
         if (col > 0) {
            if (TILES[row - 1][col - 1].getIsMine())
               neighbors++;
         }
         if (col < SIZE - 1) {
            if (TILES[row - 1][col + 1].getIsMine())
               neighbors++;
         }
         if (TILES[row - 1][col].getIsMine())
            neighbors++;
      }
      if (row < SIZE - 1) {
         if (col > 0) {
            if (TILES[row + 1][col - 1].getIsMine())
               neighbors++;
         }
         if (col < SIZE - 1) {
            if (TILES[row + 1][col + 1].getIsMine())
               neighbors++;
         if (TILES[row + 1][col].getIsMine())
            neighbors++;
      }
      if (col > 0) {
         if (TILES[row][col - 1].getIsMine())
            neighbors++;
      }
      if (col < SIZE - 1)
         if (TILES[row][col + 1].getIsMine())
            neighbors++;
      }
      return neighbors;
   }
}