package games.polarbearbytes.mobxp.gui.screens;

import games.polarbearbytes.mobxp.MobXPClient;
import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import java.util.HashMap;

/**
 * Screen for modifying mob xp
 */
public class MobXPListScreen extends Screen {
    private MobXPListWidget listWidget = null;
    private MobXPDetailsPanel detailsPanel;

    private static final float LIST_WIDTH_PERCENTAGE = 0.40F;
    private static final int MARGIN = 8;
    private static final int ITEMS_VISIBLE = 4;
    private static final int FIELD_HEIGHT = 20;

    private TextFieldWidget searchField;

    public MobXPListScreen() {
        super(Text.literal("Mob XP Editor"));
    }

    /**
     * Called when mob is selected from {@link #listWidget}
     * @param entry the mob entry that was selected
     */
    public void onMobSelected(MobXPEntry entry){
        if(entry == null) {
            detailsPanel.setDetails(null);
            return;
        }
        detailsPanel.setDetails(entry.getData());
    }

    @Override
    protected void init(){
        String lastSearch = "";
        if(this.searchField != null){
            lastSearch = this.searchField.getText();
        }
        int listWidth = (int) (LIST_WIDTH_PERCENTAGE * width) - MARGIN * 2;

        Text labelText = Text.literal("Search:");
        int labelWidth = textRenderer.getWidth(labelText) + MARGIN;
        int fieldWidth = listWidth - labelWidth;

        int layoutX = MARGIN;
        int layoutY = MARGIN;

        TextWidget searchLabelWidget = new TextWidget(layoutX, layoutY, labelWidth, FIELD_HEIGHT, labelText, textRenderer);
        searchField = new TextFieldWidget(textRenderer, layoutX + labelWidth, layoutY, fieldWidth, FIELD_HEIGHT, Text.literal(lastSearch));
        searchField.setText(lastSearch);
        layoutY += FIELD_HEIGHT + MARGIN;
        int detailsWidth = width - listWidth - MARGIN * 3;
        int detailsHeight = height - FIELD_HEIGHT - MARGIN * 3;

        listWidget = new MobXPListWidget(this, layoutX, layoutY, listWidth, height - FIELD_HEIGHT - MARGIN * 3, ITEMS_VISIBLE, this::onMobSelected, listWidget);
        detailsPanel = new MobXPDetailsPanel(this, layoutX + listWidth + MARGIN, MARGIN, detailsWidth, detailsHeight);

        searchField.setChangedListener(listWidget::filter);

        int buttonWidth = width - listWidth - MARGIN * 3;
        int buttonY = height - FIELD_HEIGHT - MARGIN;

        ButtonWidget cancelButton = ButtonWidget.builder(Text.literal("Cancel"), b -> close())
                .position(listWidth + MARGIN * 2, buttonY)
                .size(buttonWidth, FIELD_HEIGHT)
                .build();

        listWidget.init();

        addDrawableChild(searchLabelWidget);
        addDrawableChild(searchField);

        addDrawableChild(cancelButton);

        addDrawableChild(listWidget);
    }

    public String getSearch(){
        return searchField.getText();
    }

    /**
     * Update the mob list with data from server
     * @param dataList hash map of mob xp details keyed on minecraft id
     */
    public void updateList(HashMap<String, MobXPData> dataList){
        listWidget.update(dataList);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    public <T extends Drawable & Element & Selectable> void add(T drawableElement){
        this.addDrawableChild(drawableElement);
    }

    public <T extends Drawable> T addDrawable(T drawable){
        return super.addDrawable(drawable);
    }


    /**
     * Saves the changes of the currently selected mob to the respective places, and sends packet to server for updating on server side
     */
    public void applyChanges(MobXPData data){
        MobXPEntry entry = listWidget.getSelectedOrNull();
        if(entry == null) return;
        entry.setData(data);
        MobXPClient.updateData(data);
    }

    /**
     * Apply the changes and close screen
     */
    public void saveAndClose(MobXPData data){
        applyChanges(data);
        close();
    }
}
