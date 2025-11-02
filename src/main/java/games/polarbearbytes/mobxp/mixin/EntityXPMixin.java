package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.ConfigManager;
import games.polarbearbytes.mobxp.config.MobXPConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.WaterAnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class,AnimalEntity.class, WaterAnimalEntity.class})
public abstract class EntityXPMixin extends Entity {

    protected EntityXPMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "getExperienceToDrop(Lnet/minecraft/server/world/ServerWorld;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getExperienceToDropMixin(ServerWorld world, CallbackInfoReturnable<Integer> cir) {
        //TODO Check for Chicken Jockey
        MobXPConfig.MobXPData data = ConfigManager.getConfig().xp.get(this.getSavedEntityId());
        if(data == null){
            MobXP.LOGGER.info("{} not found in MobXPData list",this.getSavedEntityId());
            return;
        }
        if(data.enabled()){
            int xp = data.random() ? this.random.nextInt(data.experiencePoints()) : data.experiencePoints();
            cir.setReturnValue(xp);
        }
    }
}
