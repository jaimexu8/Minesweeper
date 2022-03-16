class Tile {
   /** Whether or not the tile is a mine */
   private boolean isMine;
   /** Cover status of the tile */
   private String status;
   /** X Position of tile */
   private int xPos;
   /** Y Position of tile */
   private int yPos;
   /** Size of tile */
   private int size;
   /** Row of which tile is located in 2D array */
   private int row;
   /** Column of which tile is located in 2D array */
   private int col;
   
   public Tile(boolean isMine, int xPos, int yPos, int size, int row, int col) {
      this.isMine = isMine;
      this.status = "covered"; //Status types: covered, revealed, flagged
      this.xPos = xPos;
      this.yPos = yPos;
      this.size = size;
      this.row = row;
      this.col = col;
   }
   /** Determines whether or not the tile was clicked
   * @param x X value of the click location
   * @param y Y value of the click location
   * @return Whether or not the tile was clicked*/
   public boolean isClicked(int x, int y) {
      return (x >= xPos && x <= xPos + size) && (yPos >= y && yPos <= y + size);
   }
   /** Sets the cover status of the mine
   * @param mineStatus Cover status */
   public void setStatus(String mineStatus) {
      status = mineStatus;
   }
   /** Returns cover status of mine
   * @return Cover status of mine */
   public String getStatus() {
      return status;
   }
   /** Returns whether or not the tile is a mine
   * @return Mine or not a mine */
   public boolean getIsMine() {
      return isMine;
   }
   /** Sets the tile to mine or not mine
   * @param mine Whether or not it will become a mine */
   public void setIsMine(boolean mine) {
      isMine = mine;
   }
   /** Returns the row in which the tile is located
   * @return Row number */
   public int getRow() {
      return row;
   }
   /** Returns the column in which the tile is located
   * @return Column number */
   public int getCol() {
      return col;
   }
}