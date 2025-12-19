package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.utils.Utils;

/**
 * Rule for mobs that need all the details, like chickens as they could have separate xp for babies, jockeyed, and adult versions
 */
public class AllXPRule extends GeneralXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.secondaryXP.setText(valueOrDefault(data.secondaryExperiencePoints()));
        ctx.babyXP.setText(valueOrDefault(data.babyExperiencePoints()));
        ctx.showSecondary(true);
        ctx.showBaby(true);
    }

    /**
     * Builds the {@link MobXPData} record to include the secondary xp amount in the record
     * @param ctx {@link EditorContext} that controls the detail controls
     * @param id  Minecraft entity id
     * @return {@link MobXPData} record of mob xp details
     */
    @Override
    public MobXPData buildData(EditorContext ctx, String id) {
        return new MobXPData(
                id,
                Utils.tryParse(ctx.primaryXP.getText(), (Integer) null),
                Utils.tryParse(ctx.secondaryXP.getText(), (Integer) null),
                Utils.tryParse(ctx.babyXP.getText(), (Integer) null),
                ctx.enabledCheckbox.isChecked(),
                ctx.randomCheckbox.isChecked(),
                ctx.useAdultXPCheckbox.isChecked()
        );
    }
}