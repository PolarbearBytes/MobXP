package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.ConfigManager;
import games.polarbearbytes.mobxp.data.MobXPData;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * General mixin to modify mob classes to allow for custom xp, called if these classes overrode the getExperienceToDrop() method
 * The other mobs will be caught by the MobEntityMixin
 */
@Mixin({AnimalEntity.class, WaterAnimalEntity.class, WaterCreatureEntity.class, PiglinEntity.class, HoglinEntity.class, ChickenEntity.class, ZombieEntity.class})
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
        String entityId = this.getSavedEntityId();
        if(entityId == null) return;

        MobXPData data = ConfigManager.getConfig().get(entityId);
        if(data == null){
            MobXP.LOGGER.error("{} not found in MobXPData list", entityId);
            return;
        }

        //Custom xp not enabled so no need to continue
        if(!data.enabled()) return;

        Integer xp = null;
        EntityType<?> type = this.getType();

        if(type == EntityType.CHICKEN){
            ChickenEntity chicken = (ChickenEntity) (Object) this;

            if(data.experiencePoints() == null && data.secondaryExperiencePoints() == null && data.babyExperiencePoints() == null) {
                //Enabled but all the xp fields are set to default
                return;
            } else if( chicken.hasJockey() ) {
                //Chicken has a rider
                xp = data.secondaryExperiencePoints();
            } else if( chicken.isBaby() ){
                //Chicken is a baby
                xp = data.useAdultXPForBaby() ? data.experiencePoints() : data.babyExperiencePoints();
            } else {
                //Regular adult chicken
                xp = data.experiencePoints();
            }
        } else if(type == EntityType.ZOMBIE && !((ZombieEntity) (Object) this).isBaby()) {
            //If it is a zombie but not a baby we let it drop through to the MobEntityMixin
            return;
        } else {
            //All other mobs
            LivingEntity entity = (LivingEntity) (Object) this;
            Integer babyXP = data.babyExperiencePoints();
            Integer adultXP = data.experiencePoints();

            xp = entity.isBaby() ? (data.useAdultXPForBaby() ? adultXP : babyXP) : adultXP;
        }
        //Custom xp was enabled but left as default
        if(xp == null) return;

        if(data.random()){
            xp = data.random() ? this.random.nextInt(xp) : xp;
        }
        cir.setReturnValue(xp);
    }


}
