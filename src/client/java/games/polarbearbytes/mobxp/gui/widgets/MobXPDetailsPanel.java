package games.polarbearbytes.mobxp.gui.widgets;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.screens.MobXPListScreen;
import games.polarbearbytes.mobxp.rules.XPRule;
import games.polarbearbytes.mobxp.rules.XPRules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * "Panel" widget for displaying the editor controls for mob xp details
 */
public class MobXPDetailsPanel implements Drawable {
    private boolean visible = true;
    private String title;
    private String id;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private final TextRenderer textRenderer;

    private final EditorContext editorContext;
    private XPRule activeRule;

    private static final int MARGIN = 5;
    private static final int MARGIN_LEFT = 20;
    private static final int MARGIN_RIGHT = 20;
    private static final int FIELD_HEIGHT = 20;
    private static final int LINE_HEIGHT = FIELD_HEIGHT + MARGIN;

    private final MobXPListScreen parent;

    public MobXPDetailsPanel(MobXPListScreen parent, int x, int y, int width, int height){
        this.parent = parent;
        textRenderer = MinecraftClient.getInstance().textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        int placementX = x + MARGIN_LEFT;
        int placementY = y + 20;

        Text xpLabelText = Text.of("XP: ");
        int xpLabelWidth = textRenderer.getWidth(xpLabelText);

        int fieldX = placementX + xpLabelWidth + MARGIN;
        int fieldWidth = (width - (MARGIN_LEFT + MARGIN_RIGHT) - (xpLabelWidth + MARGIN) - MARGIN) / 2;

        TextWidget primaryXPLabel = new TextWidget(fieldX, placementY, fieldWidth, FIELD_HEIGHT, Text.of(""), textRenderer);
        TextWidget secondaryXPLabel = new TextWidget(fieldX + fieldWidth + MARGIN, placementY, fieldWidth, FIELD_HEIGHT, Text.of(""), textRenderer);

        placementY += LINE_HEIGHT;

        TextWidget xpLabel = new TextWidget(placementX, placementY, xpLabelWidth, FIELD_HEIGHT, xpLabelText, textRenderer);

        TextFieldWidget primaryXPField = new TextFieldWidget(textRenderer, fieldX, placementY, fieldWidth, FIELD_HEIGHT, Text.literal("Value"));
        TextFieldWidget secondaryXPField = new TextFieldWidget(textRenderer, fieldX + fieldWidth + MARGIN, placementY, fieldWidth, FIELD_HEIGHT, Text.literal("Value"));

        placementY += LINE_HEIGHT;

        CheckboxWidget enabledCheckbox = CheckboxWidget.builder(Text.literal("Enabled"), textRenderer)
                .pos(placementX, placementY)
                .build();

        placementY += LINE_HEIGHT;

        CheckboxWidget randomCheckbox = CheckboxWidget.builder(Text.literal("Random"), textRenderer)
                .pos(placementX, placementY)
                .build();

        placementY += LINE_HEIGHT;

        Text babyLabelText = Text.of("When Baby: ");
        int babyLabelWidth = textRenderer.getWidth(babyLabelText);
        fieldX = placementX + babyLabelWidth + MARGIN;

        TextWidget babyLabel = new TextWidget(placementX, placementY, babyLabelWidth, FIELD_HEIGHT, babyLabelText, textRenderer);
        TextFieldWidget babyXPField = new TextFieldWidget(textRenderer, fieldX, placementY, fieldWidth, FIELD_HEIGHT, Text.literal("Value"));

        placementY += LINE_HEIGHT;

        CheckboxWidget usePrimaryXPForBaby = CheckboxWidget.builder(Text.literal("Same As Adult"), textRenderer)
                .pos(fieldX, placementY)
                .build();

        int startX = x + MARGIN;

        int buttonWidth = (width - MARGIN * 3) / 2;
        int buttonY = height - FIELD_HEIGHT;

        ButtonWidget applyButton = ButtonWidget.builder(Text.literal("Apply"), b -> applyChanges())
                .position(startX, buttonY)
                .size(buttonWidth, FIELD_HEIGHT)
                .build();
        ButtonWidget saveButton = ButtonWidget.builder(Text.literal("Save & Close"), b -> saveAndClose())
                .position(startX + buttonWidth + MARGIN, buttonY)
                .size(buttonWidth, FIELD_HEIGHT)
                .build();

        parent.addDrawable(this);
        parent.add(xpLabel);
        parent.add(primaryXPField);
        parent.add(secondaryXPField);

        parent.add(primaryXPLabel);
        parent.add(secondaryXPLabel);

        parent.add(enabledCheckbox);
        parent.add(randomCheckbox);

        parent.add(babyLabel);
        parent.add(babyXPField);
        parent.add(usePrimaryXPForBaby);

        parent.add(applyButton);
        parent.add(saveButton);

        editorContext = new EditorContext(
                primaryXPField,
                primaryXPLabel,
                secondaryXPField,
                secondaryXPLabel,
                enabledCheckbox,
                randomCheckbox,
                xpLabel,
                babyLabel,
                babyXPField,
                usePrimaryXPForBaby,
                applyButton,
                saveButton
        );

        editorContext.hideAll();
    }

    /**
     * Sets the detail controls to the data in {@code details}, gets XPRule based on EntityType and displays the controls appropriately
     * @param details The mob's xp details (xp amount, enabled, use random amount of xp)
     */
    public void setDetails(@Nullable MobXPData details){
        if(details == null){
            id = "";
            title = "";
            activeRule = null;
            editorContext.hideAll();
            visible = false;
            return;
        }
        visible = true;
        id = details.id();
        title = details.getName();
        activeRule = XPRules.forEntity(id);
        activeRule.onSelect(editorContext, details);
    }

    /**
     * Builds the MobXPData record from the detail controls based on XPRule
     * @return {@link MobXPData}
     */
    public MobXPData getDetails(){
        return activeRule.buildData(editorContext, id);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if(!visible) return;
        context.fill(x,y, x+width, y+height, 0x99000000);
        context.fill(x,y, x+width, y+24, 0xFF0a0a0a);

        context.drawCenteredTextWithShadow(textRenderer, title, x + (width / 2), y+8, 0xFFFFFFFF);
    }

    /**
     * Saves the changes of the currently selected mob
     */
    private void applyChanges(){
        this.parent.applyChanges(getDetails());
    }

    /**
     * Apply the changes and close screen
     */
    private void saveAndClose(){
        this.parent.saveAndClose(getDetails());
    }
}