package games.polarbearbytes.mobxp.gui.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

/**
 * class for applying actions like hiding, showing elements of details panel
 */
public final class EditorContext {
    public final TextFieldWidget primaryXP;
    public final TextFieldWidget secondaryXP;
    public final TextFieldWidget babyXP;
    public final TextWidget primaryLabel;
    public final TextWidget secondaryLabel;
    public final TextWidget xpLabel;
    public final TextWidget babyLabel;
    public final CheckboxWidget enabledCheckbox;
    public final CheckboxWidget randomCheckbox;
    public final CheckboxWidget usePrimaryXPForBaby;
    public final ButtonWidget applyButton;
    public final ButtonWidget saveCloseButton;

    public EditorContext(
            TextFieldWidget primaryXP,
            TextWidget primaryLabel,
            TextFieldWidget secondaryXP,
            TextWidget secondaryLabel,
            CheckboxWidget enabledCheckbox,
            CheckboxWidget randomCheckbox,
            TextWidget xpLabel,
            TextWidget babyLabel,
            TextFieldWidget babyXP,
            CheckboxWidget usePrimaryXPForBaby,
            ButtonWidget applyButton,
            ButtonWidget saveClostButton
    ) {
        this.primaryXP = primaryXP;
        this.primaryLabel = primaryLabel;
        this.secondaryXP = secondaryXP;
        this.secondaryLabel = secondaryLabel;
        this.enabledCheckbox = enabledCheckbox;
        this.randomCheckbox = randomCheckbox;
        this.xpLabel = xpLabel;
        this.babyLabel = babyLabel;
        this.babyXP = babyXP;
        this.usePrimaryXPForBaby = usePrimaryXPForBaby;
        this.applyButton = applyButton;
        this.saveCloseButton = saveClostButton;
    }

    /**
     * Hide all the controls, labels
     */
    public void hideAll(){
        xpLabel.visible = false;
        primaryXP.visible = false;
        primaryLabel.visible = false;
        secondaryXP.visible = false;
        secondaryLabel.visible = false;
        enabledCheckbox.visible = false;
        randomCheckbox.visible = false;
        babyLabel.visible = false;
        babyXP.visible = false;
        usePrimaryXPForBaby.visible = false;
        applyButton.visible = false;
        saveCloseButton.visible = false;
    }

    /**
     * Show the controls, labels for normal mob details
     */
    public void showNormal(){
        xpLabel.visible = true;
        primaryXP.visible = true;
        enabledCheckbox.visible = true;
        randomCheckbox.visible = true;
        applyButton.visible = true;
        saveCloseButton.visible = true;
    }

    /**
     * Show the secondary controls, labels for mobs with secondary xp (like ender dragon)
     */
    public void showSecondary(boolean show) {
        secondaryXP.visible = show;
        primaryLabel.visible = show;
        secondaryLabel.visible = show;
    }

    /**
     * Show the baby detail controls, labels for mobs with baby xp (like baby zombie, animals etc)
     */
    public void showBaby(boolean show) {
        babyLabel.visible = show;
        babyXP.visible = show;
        usePrimaryXPForBaby.visible = show;
    }

    public void setLabels(String primaryLabel, String secondaryLabel){
        this.primaryLabel.setMessage(Text.of(primaryLabel));
        this.secondaryLabel.setMessage(Text.of(secondaryLabel));
    }
}