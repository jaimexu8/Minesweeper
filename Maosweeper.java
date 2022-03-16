import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Maosweeper extends JFrame {

   public static void main(String[] args) throws FileNotFoundException {
      new Maosweeper();
   }
   
   public Maosweeper() throws FileNotFoundException {
      add(new Board());
      setSize(1000, 500);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
      setResizable(false);
      setLocationRelativeTo(null);
      setLayout(null);
   }
}