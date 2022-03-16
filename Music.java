import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Music {
   
   private String theme;
   public Music(String theme) {
      this.theme = theme;
   }
   
   public void playMusic() {
      try {
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:/Users/jaime/Documents/JGrasp/Maosweeper/Themes/Mao").getAbsoluteFile());
         Clip clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         clip.start();  
      } catch (Exception e) {
         System.out.println("File not found");
      }
   }
}