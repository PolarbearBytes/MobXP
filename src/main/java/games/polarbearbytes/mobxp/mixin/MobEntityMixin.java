package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.MobXP;
import games.polarbearbytes.mobxp.config.ConfigManager;
import games.polarbearbytes.mobxp.config.MobXPConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow
    protected int experiencePoints;
    @Final @Shadow
    private DefaultedList<ItemStack> armorItems;
    @Final @Shadow
    private DefaultedList<ItemStack> handItems;
    @Final @Shadow
    protected float[] armorDropChances;
    @Final @Shadow
    protected float[] handDropChances;
    @Shadow
    protected float bodyArmorDropChance;
    @Shadow
    private ItemStack bodyArmor;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "getXpToDrop()I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getExperienceToDropMixin(CallbackInfoReturnable<Integer> cir) {
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

            for(int j = 0; j < this.armorItems.size(); ++j) {
                if (!((ItemStack)this.armorItems.get(j)).isEmpty() && this.armorDropChances[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            for(int j = 0; j < this.handItems.size(); ++j) {
                if (!((ItemStack)this.handItems.get(j)).isEmpty() && this.handDropChances[j] <= 1.0F) {
                    i += 1 + this.random.nextInt(3);
                }
            }

            if (!this.bodyArmor.isEmpty() && this.bodyArmorDropChance <= 1.0F) {
                i += 1 + this.random.nextInt(3);
            }

            cir.setReturnValue(i);
        } else {
            cir.setReturnValue(xp);
        }
    }
}
