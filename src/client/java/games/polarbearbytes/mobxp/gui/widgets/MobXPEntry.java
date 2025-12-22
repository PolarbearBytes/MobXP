package games.polarbearbytes.mobxp.gui.widgets;

import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget.Entry;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Custom {@link #Entry} class for our {@link EntryListWidget}, display mob entity model, name, xp amount, and options
 */
public class MobXPEntry extends Entry<MobXPEntry> {
    private MobXPData data;
    private final String title;
    private MobWidget mobWidget;
    private static final int MARGIN = 5;

    public MobXPEntry(MobXPData data) {
        this.data = data;
        this.title = data.getName();
    }

    @Override
    public int getHeight(){
        return super.getHeight() + MARGIN * 2;
    }

    @Override
    public void setHeight(int height){
        super.setHeight(height);
        this.mobWidget = new MobWidget(Registries.ENTITY_TYPE.get(Identifier.of(data.id())), getContentX(), getContentY(), getContentHeight(), getContentHeight());
    }

    @Override
    public void setWidth(int width){
        super.setWidth(width);
        this.mobWidget = new MobWidget(Registries.ENTITY_TYPE.get(Identifier.of(data.id())), getContentX(), getContentY(), getContentHeight(), getContentHeight());
    }

    /**
     * Method to detect if this entry matches the passed string, based on the minecraft ID and readable name
     * @param match string to match against
     * @return {@code boolean} true if matched, false otherwise
     */
    public boolean matches(String match){
        return data.id().contains(match.toLowerCase()) || title.toLowerCase().contains(match.toLowerCase());
    }

    public String getTitle(){
        return title;
    }

    public MobXPData getData(){
        return data;
    }
    public void setData(MobXPData data){
        this.data = data;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        this.mobWidget.render(context,mouseX,mouseY,deltaTicks);

        int labelHeight = textRenderer.fontHeight + 2;

        int textX = getContentHeight() + MARGIN * 2;
        int textY = getContentY() + MARGIN;

        context.drawText(textRenderer, data.getName(), textX,  textY, 0xFFFFFFFF, false);
        String xp = String.valueOf(data.primaryXP());
        xp = xp.equals("-1") ? "default" : xp;

        context.drawText(textRenderer, "XP: " + xp, textX, textY+labelHeight, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Enabled: " + data.enabled(), textX, textY+labelHeight*2, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Random XP: " + data.random(), textX, textY+labelHeight*3, 0xFFFFFFFF, false);
    }

    @Override
    public Text getNarration() {
        return Text.of( data.getName() );
    }
}