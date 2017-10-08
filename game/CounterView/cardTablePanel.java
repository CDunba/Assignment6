package game.CounterView;

import javax.swing.JPanel;
import game.CounterController.cardGameAppController;

public class cardTablePanel extends JPanel
{
   private cardGameAppController baseController;
   
   public cardTablePanel(cardGameAppController baseController)
   {
      this.baseController = baseController;
      
   }
}
