package games.polarbearbytes.mobxp.gui.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import org.jetbrains.annotations.Nullable;

/**
 * Widget to list our custom mob entries
 */
public class MobXPListWidget extends AlwaysSelectedEntryListWidget<MobXPEntry> {
    private final Callback callback;

    public MobXPListWidget(int x, int y, int width, int height, int itemHeight, Callback selectCallback) {
        super(MinecraftClient.getInstance(), width, height, 0, itemHeight);
        this.callback = selectCallback;
        this.setPosition(x, y);
    }

    @Override
    protected int getScrollbarX() {
        return this.getRight() - 6;
    }

    @Override
    public void setSelected(@Nullable MobXPEntry entry) {
        super.setSelected(entry);
        this.callback.onValueChange(entry);
    }

    /**
     * Callback interface to allow for passing a callback to tbe called when an entry has been selected
     */
    @Environment(EnvType.CLIENT)
    public interface Callback {
        void onValueChange(MobXPEntry entry);
    }
}
