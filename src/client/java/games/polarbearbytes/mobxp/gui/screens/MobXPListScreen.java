package games.polarbearbytes.mobxp.gui.screens;

import games.polarbearbytes.mobxp.MobXPClient;
import games.polarbearbytes.mobxp.gui.widgets.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import games.polarbearbytes.mobxp.data.MobXPData;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Screen for modifying mob xp
 */
public class MobXPListScreen extends WidgetReceiverScreen {
    private List<MobXPEntry> entryCache;
    private SearchableMobXPListWidget listWidget;
    private MobXPDetailsPanel detailsPanel;

    private static final int LIST_WIDTH = 160;
    private static final int MARGIN = 10;
    private static final int ITEMS_VISIBLE = 4;
    private static final int BUTTON_HEIGHT = 20;

    private ButtonWidget applyButton;
    private ButtonWidget saveButton;

    public MobXPListScreen() {
        super(Text.literal("Mob XP Editor"));
    }

    /**
     * Called when mob is selected from {@link #listWidget}
     * @param entry the mob entry that was selected
     */
    public void onMobSelected(MobXPEntry entry){
        if(entry == null){
            detailsPanel.setDetails(null);
            applyButton.active = false;
            saveButton.active = false;
            return;
        }
        applyButton.active = true;
        saveButton.active = true;
        detailsPanel.setDetails(entry.getData());
    }

    @Override
    protected void init(){
        listWidget = new SearchableMobXPListWidget(this, 0, 0,LIST_WIDTH, height, ITEMS_VISIBLE, this::onMobSelected);
        detailsPanel = new MobXPDetailsPanel(this,LIST_WIDTH,0,width - LIST_WIDTH, height);

        int bottomY = height;
        int startX = LIST_WIDTH + MARGIN;

        int buttonWidth = (width - LIST_WIDTH - MARGIN * 4) / 3;
        int buttonY = bottomY - BUTTON_HEIGHT - MARGIN;
        applyButton = ButtonWidget.builder(Text.literal("Apply"), b -> applyChanges())
                .position(startX, buttonY)
                .size(buttonWidth, 20)
                .build();
        saveButton = ButtonWidget.builder(Text.literal("Save & Close"), b -> saveAndClose())
                .position(startX + buttonWidth + MARGIN, buttonY )
                .size(buttonWidth, 20)
                .build();

        applyButton.active = false;
        saveButton.active = false;

        addDrawableChild(applyButton);
        addDrawableChild(saveButton);
        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), b -> close())
                .position(startX + buttonWidth * 2 + MARGIN * 2, buttonY)
                .size(buttonWidth, 20)
                .build());

        if(entryCache != null){
            listWidget.setEntries(entryCache);
        }
    }

    /**
     * Sort and create entries from passed dataList, and set the entries to the list widget
     * @param dataList
     */
    public void updateList(HashMap<String, MobXPData> dataList){
        List<MobXPEntry> list = dataList.values().stream()
                .sorted(Comparator.comparing(MobXPData::id))
                .map(data->new MobXPEntry(data, LIST_WIDTH, height / ITEMS_VISIBLE)).toList();
        entryCache = list;
        listWidget.setEntries(list);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    /**
     * Saves the changes of the currently selected mob to the respective places, and sends packet to server for updating on server side
     */
    private void applyChanges(){
        MobXPEntry entry = listWidget.getSelected();
        if(entry == null) return;

        MobXPData newData = detailsPanel.getDetails();
        entry.setData(newData);
        MobXPClient.updateData(newData);
    }

    /**
     * Apply the changes and close screen
     */
    private void saveAndClose(){
        applyChanges();
        close();
    }
}
