package game.CounterController;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import game.CounterView.*;
import game.CounterController.CardTable;

public class timer extends Thread
{
	static Counter gameCounter = new Counter();
	static final String START_TIMER = "Start Timer", STOP_TIMER = "Stop Timer", RESET_TIMER = "Reset Timer";
    static JLabel timerText = new JLabel("");
    static JButton startTimerButton = new JButton(START_TIMER);
    static Counter currentCounter = new Counter();
    private boolean doProcess = true;
    public static final int PAUSE = 1000; 
    static JLabel[] humanLabels = new JLabel[7];
    static timer gameTimer = new timer();  
    
    public timer()
    {
       doProcess = true;
    }
      
    public void run()
    {
       while (doProcess)
       {      
          currentCounter.increment();
           System.out.println(currentCounter.toString());
           timerText.setText( currentCounter.toString());
           this.repainttimer();
           doNothing(PAUSE);
       }
       System.out.println(currentCounter.toString());  
    }
      
    public void repainttimer()
    {
        timerText.repaint();
    }
      
      public void reset()
      {
         doProcess = true;
      }
      
      public void kill()
      {
         doProcess = false;
         this.repainttimer();
      }
      
      public  void doNothing(int milliseconds)
      {
         try
         {
         Thread.sleep(milliseconds);
         }
         catch(InterruptedException e)
         {
            System.out.println("Unexpected error");
            System.exit(0);
         }
      }
  
   public static class Counter
   {
      public static int secs;
      public static int mins;
      public static int hrs;
      
      public Counter()
      {
         resetCounter();
      }
  
      public static int getHrs()
      {
         return hrs;
      }

      public int getSecs()
      {
         return secs;
      }
      
      public int getMins()
      {
         return mins;
      }
      
      public void resetCounter()
      {
         secs = 0;
         mins = 0;
         hrs = 0;
      }
      public void increment()
      {
         if (++secs > 59)
         {
            ++mins;
            secs = 0;
         }
         
         if (mins > 59)
         {
            ++hrs;
            mins = 0;
         }   
      }
   
      public String toString()
      {
      String rtnHrs = String.format("%02d", hrs);
      String rtnMins = String.format("%02d", mins);
      String rtnSecs = String.format("%02d", secs);
      
      return (rtnHrs + ":" + rtnMins + ":" + rtnSecs);
      }
   }
} 
   
class ButtonListener implements ActionListener
{
   public void actionPerformed(ActionEvent e)
   {
      JButton source = (JButton)e.getSource();
      System.out.println(source.getActionCommand());

      if ( source.getActionCommand() == timer.START_TIMER)
      {

        timer.currentCounter.resetCounter();
        
        timer.gameTimer = new timer();
        timer.gameTimer.start();
        source.setText( timer.RESET_TIMER);

      }
      else
      {
         timer.gameTimer.kill();
         timer.currentCounter.resetCounter(); 
         
         try
         {
            timer.gameTimer.join();
         } catch (InterruptedException e1)
         {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         } finally
         {
            
            if ( source.getActionCommand() == timer.RESET_TIMER)
            {               
               timer.gameTimer =  new timer();
               timer.gameTimer.start();
               source.setText( timer.RESET_TIMER);

            }
            else
            {
               timer.startTimerButton.setText(timer.START_TIMER);
               timer.gameTimer.repainttimer();
            }
         }
      }
   }
}

 

