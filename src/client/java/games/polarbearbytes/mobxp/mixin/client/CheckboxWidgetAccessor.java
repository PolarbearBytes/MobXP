package games.polarbearbytes.mobxp.mixin.client;

import net.minecraft.client.gui.widget.CheckboxWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor mixin to make it so we can programmatically change a checkboxes checked state (CheckboxWidget doesn't seem to have an appropriate method for this)
 */
@Mixin(CheckboxWidget.class)
public interface CheckboxWidgetAccessor {
    @Accessor("checked")
    void mobxp$setChecked(boolean checked);
}