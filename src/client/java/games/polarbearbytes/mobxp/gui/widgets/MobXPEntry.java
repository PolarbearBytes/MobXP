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
import org.joml.Matrix3x2fStack;

/**
 * Custom {@link #Entry} class for our {@link EntryListWidget}, display mob entity model, name, xp amount, and options
 */
public class MobXPEntry extends Entry<MobXPEntry> {
    private MobXPData data;
    private final String title;
    private final MobWidget mobWidget;
    private static final int MARGIN = 5;
    private static final float TEXT_SCALE = 0.90f;

    public MobXPEntry(MobXPData data, int width, int height) {
        this.data = data;
        this.title = data.getName();
        setWidth(width);
        setHeight(height);

        this.mobWidget = new MobWidget(Registries.ENTITY_TYPE.get(Identifier.of(data.id())), 0, 0, height-2, height-2);
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
        mobWidget.x = 1;
        mobWidget.y = getY()+1;

        context.enableScissor(getX(),getY(), getX()+getWidth(),getY()+getHeight());

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        context.fill(getX(),getY(),getX()+getWidth(),getY()+getHeight(),0xBB000000);
        this.mobWidget.render(context,mouseX,mouseY,deltaTicks);


        Matrix3x2fStack stack = context.getMatrices();
        stack.pushMatrix();
        stack.scale(TEXT_SCALE);

        int scaledMargin = (int) (MARGIN / TEXT_SCALE);

        int labelHeight = (int) (textRenderer.fontHeight / TEXT_SCALE) + 2;

        int textStart = (int) (getHeight() / TEXT_SCALE + scaledMargin);
        int textY = (int) (getY() / TEXT_SCALE + scaledMargin);

        context.drawText(textRenderer, data.getName(), textStart,  textY, 0xFFFFFFFF, false);
        String xp = String.valueOf(data.experiencePoints());
        xp = xp.equals("null") ? "default" : xp;

        context.drawText(textRenderer, "XP: " + xp, textStart, textY+labelHeight, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Enabled: " + data.enabled(), textStart, textY+labelHeight*2, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Random XP: " + data.random(), textStart, textY+labelHeight*3, 0xFFFFFFFF, false);

        stack.popMatrix();
        context.disableScissor();
    }

    @Override
    public Text getNarration() {
        return Text.of("");
    }
}