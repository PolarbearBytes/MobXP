package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.ConfigManager;
import games.polarbearbytes.mobxp.config.MobXPConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentDropChances;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow
    protected int experiencePoints;
    @Shadow
    private EquipmentDropChances equipmentDropChances;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "getExperienceToDrop(Lnet/minecraft/server/world/ServerWorld;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getExperienceToDropMixin(ServerWorld world, CallbackInfoReturnable<Integer> cir) {
        //TODO check for baby zombie
        //TODO check for baby zoglin

        MobXPConfig.MobXPData data = ConfigManager.getConfig().xp.get(this.getSavedEntityId());
        if(data == null){
            MobXP.LOGGER.info("{} not found in MobXPData list",this.getSavedEntityId());
        }

        int xp = this.experiencePoints;
        if(data!=null && data.enabled()){
            xp = data.random() ? this.random.nextInt( data.experiencePoints() ) : data.experiencePoints();
        }

        if (xp > 0) {
            int i = xp;
            for(EquipmentSlot equipmentSlot : EquipmentSlot.VALUES) {
                if (equipmentSlot.increasesDroppedExperience()) {
                    ItemStack itemStack = this.getEquippedStack(equipmentSlot);
                    if (!itemStack.isEmpty() && this.equipmentDropChances.get(equipmentSlot) <= 1.0F) {
                        i += 1 + this.random.nextInt(3);
                    }
                }
            }
            cir.setReturnValue(i);
        } else {
            cir.setReturnValue(xp);
        }
    }
}
