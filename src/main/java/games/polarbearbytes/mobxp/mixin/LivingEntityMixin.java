package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.MobXPStateManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

/**
 * Mixin to make it so shouldDropExperience() return true if it did normally or if the custom xp was enabled
 */
@Mixin({LivingEntity.class, TadpoleEntity.class})
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "shouldDropExperience()Z",
            at = @At("TAIL"),
            cancellable = true
    )
    public void enableXPDropForBaby(CallbackInfoReturnable<Boolean> cir) {
        MobXPStateManager state = MobXPStateManager.get(Objects.requireNonNull(this.getEntityWorld().getServer()));
        MobXPData data = state.getMobData(getSavedEntityId());
        if(data == null){
            MobXP.LOGGER.error("{} not found in MobXPData list", getSavedEntityId());
            return;
        }

        cir.setReturnValue( cir.getReturnValue() || ( data.enabled() && (data.babyXP() > -1 || data.usePrimaryXPForBaby()) ) );
    }
}
