package games.polarbearbytes.mobxp.gui.widgets;

import games.polarbearbytes.mobxp.gui.screens.WidgetReceiverScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Custom widget to display a list widget and a search field to filter entries
 */
public class SearchableMobXPListWidget implements Drawable {
    private String previousFilter = "";
    private final MobXPListWidget mobXPListWidget;

    private List<MobXPEntry> masterList = Lists.newArrayList();
    private final HashMap<String, List<MobXPEntry>> filteredLists = new HashMap<>();

    private final int x;
    private final int y;
    private final int width;

    private static final int SEARCH_HEIGHT = 20;
    private static final int PADDING = 2;
    private static final int MARGIN = 5;
    private static final int SEARCH_BORDER = 1;

    public SearchableMobXPListWidget(WidgetReceiverScreen parent, int x, int y, int width, int height, int itemsVisible, MobXPListWidget.Callback selectCallback){
        this.x = x;
        this.y = y;
        this.width = width;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int itemHeight = (height - SEARCH_HEIGHT) / itemsVisible;

        Text labelText = Text.literal("Search:");
        int textWidth = textRenderer.getWidth(labelText);

        int labelWidth = textWidth + PADDING * 2;
        TextWidget searchLabelWidget = new TextWidget(x + PADDING, y + PADDING, labelWidth, SEARCH_HEIGHT, labelText, textRenderer);

        int fieldX = labelWidth + MARGIN;
        int fieldWidth = width - labelWidth - MARGIN;
        TextFieldWidget searchField = new TextFieldWidget(textRenderer, fieldX, y, fieldWidth, SEARCH_HEIGHT, Text.literal(""));

        searchField.setChangedListener(this::filter);

        mobXPListWidget = new MobXPListWidget(x, y + SEARCH_HEIGHT + SEARCH_BORDER * 2,width, height - (SEARCH_HEIGHT + SEARCH_BORDER*2), itemHeight, selectCallback);

        parent.addDrawable(this);
        parent.add(mobXPListWidget);
        parent.add(searchLabelWidget);
        parent.add(searchField);
    }

    @Nullable
    public MobXPEntry getSelected(){
        return mobXPListWidget.getSelectedOrNull();
    }

    /**
     * Search field changes calls this to filter the entries of the list, caches filtered lists to be more efficient (though it probably is a negligible amount in this instance)
     * @param filterString string to filter entries by
     */
    public void filter(String filterString){
        if(filterString.equals(previousFilter)) return;
        previousFilter = filterString;
        mobXPListWidget.replaceEntries(
            filteredLists.computeIfAbsent(filterString,(filter )->{
                return masterList.stream().filter(entry-> {
                    return entry.matches(filter);
                }).sorted(Comparator.comparing( MobXPEntry::getTitle )).toList();
            })
        );
        mobXPListWidget.setSelected(null);
        mobXPListWidget.setScrollY(0);
    }

    public void setEntries(List<MobXPEntry> entries) {
        masterList = entries;
        filteredLists.clear();
        mobXPListWidget.replaceEntries(entries);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(0, 0, x+width, y+SEARCH_HEIGHT+SEARCH_BORDER*4,0XFF000000);
    }
}
