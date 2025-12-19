package games.polarbearbytes.mobxp.gui.screens;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Interface class for allowing adding drawables and other widgets to screen instance from other "panel" widgets
 */
public abstract class WidgetReceiverScreen extends Screen {
    protected WidgetReceiverScreen(Text title) {
        super(title);
    }

    public <T extends Element & Drawable & Selectable> T add(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }
    public <T extends Drawable> T addDrawable(T drawable) {
        return super.addDrawable(drawable);
    }
}
