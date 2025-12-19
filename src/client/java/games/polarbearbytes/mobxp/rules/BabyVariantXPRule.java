package games.polarbearbytes.mobxp.rules;

import games.polarbearbytes.mobxp.data.MobXPData;
import games.polarbearbytes.mobxp.gui.widgets.EditorContext;
import games.polarbearbytes.mobxp.utils.Utils;

/**
 * Rule for mobs that can have baby versions like animals, zombies, etc.
 */
public class BabyVariantXPRule extends GeneralXPRule {
    @Override
    public void onSelect(EditorContext ctx, MobXPData data) {
        super.onSelect(ctx, data);
        ctx.babyXP.setText(valueOrDefault(data.babyExperiencePoints()));
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
                null,
                Utils.tryParse(ctx.babyXP.getText(), (Integer) null),
                ctx.enabledCheckbox.isChecked(),
                ctx.randomCheckbox.isChecked(),
                ctx.useAdultXPCheckbox.isChecked()
        );
    }
}
