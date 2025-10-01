package games.polarbearbytes.mobxp.mixin;

import games.polarbearbytes.mobxp.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public class EnderDragonMixin extends MobEntity {
	@Shadow
	private EnderDragonFight fight;
	@Shadow
	public int ticksSinceDeath;
	@Final @Shadow
	private EnderDragonPart[] parts;

	protected EnderDragonMixin(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
    }

	@Inject(at = @At("HEAD"), method = "updatePostDeath()V", cancellable = true)
	private void updatePostDeathMixin(CallbackInfo ci) {
		if (this.fight != null) {
			this.fight.updateFight((EnderDragonEntity) (Object) this);
		}

		++this.ticksSinceDeath;
		if (this.ticksSinceDeath >= 180 && this.ticksSinceDeath <= 200) {
			float f = (this.random.nextFloat() - 0.5F) * 8.0F;
			float g = (this.random.nextFloat() - 0.5F) * 4.0F;
			float h = (this.random.nextFloat() - 0.5F) * 8.0F;
			this.getEntityWorld().addParticleClient(ParticleTypes.EXPLOSION_EMITTER, this.getX() + (double)f, this.getY() + (double)2.0F + (double)g, this.getZ() + h, 0.0F, 0.0F, 0.0F);
		}

		int i = ConfigManager.getConfig().dragonXP;
		if (this.fight != null && !this.fight.hasPreviouslyKilled()) {
			i = ConfigManager.getConfig().firstDragonXP;
		}

		World var10 = this.getEntityWorld();
		if (var10 instanceof ServerWorld serverWorld) {
			if (this.ticksSinceDeath > 150 && this.ticksSinceDeath % 5 == 0 && serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
				ExperienceOrbEntity.spawn(serverWorld, this.getEntityPos(), MathHelper.floor((float)i * 0.08F));
			}

			if (this.ticksSinceDeath == 1 && !this.isSilent()) {
				serverWorld.syncGlobalEvent(1028, this.getBlockPos(), 0);
			}
		}

		Vec3d vec3d = new Vec3d(0.0F, 0.1F, 0.0F);
		this.move(MovementType.SELF, vec3d);

		for(EnderDragonPart enderDragonPart : this.parts) {
			enderDragonPart.resetPosition();
			enderDragonPart.setPosition(enderDragonPart.getEntityPos().add(vec3d));
		}

		if (this.ticksSinceDeath == 200) {
			World var13 = this.getEntityWorld();
			if (var13 instanceof ServerWorld serverWorld2) {
                if (serverWorld2.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
					ExperienceOrbEntity.spawn(serverWorld2, this.getEntityPos(), MathHelper.floor((float)i * 0.2F));
				}

				if (this.fight != null) {
					this.fight.dragonKilled((EnderDragonEntity) (Object)this);
				}

				this.remove(Entity.RemovalReason.KILLED);
				this.emitGameEvent(GameEvent.ENTITY_DIE);
			}
		}

		ci.cancel();
	}
}