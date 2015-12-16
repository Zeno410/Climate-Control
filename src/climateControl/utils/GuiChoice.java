
package climateControl.utils;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;

/**
 *
 * @author Zeno410
 */
public class GuiChoice extends GuiScreen {    /** The text shown for the first button in GuiYesNo */
    protected String confirmButtonText;
    /** The text shown for the second button in GuiYesNo */
    protected String cancelButtonText;
    protected ArrayList<String> text;
    private int countDown;

    public GuiChoice(ArrayList<String> text) {
        this.text = text;
        this.confirmButtonText = I18n.format("Run Anyway", new Object[0]);
        this.cancelButtonText = I18n.format("menu.quit", new Object[0]);
    }

    @Override
   protected void actionPerformed(GuiButton par1GuiButton)
   {
     if (par1GuiButton.id == 0)
       Minecraft.getMinecraft().setIngameFocus();
     else
       Minecraft.getMinecraft().stopIntegratedServer();
   }


    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

   public void drawScreen(int par1, int par2, float par3)
   {
       this.drawDefaultBackground();
     int y = 20;
     for (String msg : this.text) {
       this.drawCenteredString(this.fontRendererObj, msg, this.width / 2, y, 16777215);
       y += 10;
     }
     super.drawScreen(par2, par1, par3);
   }
       /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
    }



    public void func_146350_a(int p_146350_1_)
    {
        this.countDown = p_146350_1_;
        GuiButton guibutton;

        for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = false)
        {
            guibutton = (GuiButton)iterator.next();
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen(){
        super.updateScreen();
        GuiButton guibutton;
        if (--countDown==0){
        for (Iterator iterator = this.buttonList.iterator(); iterator.hasNext(); guibutton.enabled = true)
            {
                guibutton = (GuiButton)iterator.next();
            }
        }

    }
}
