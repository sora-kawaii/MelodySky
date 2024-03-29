/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.entity;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.Player.EventPostUpdate;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.misc.EventChat;
import xyz.Melody.injection.mixins.entity.MixinEntityPlayer;
import xyz.Melody.module.balance.NoSlowDown;

@SideOnly(value=Side.CLIENT)
@Mixin(value={EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP
extends MixinEntityPlayer {
    private double cachedX;
    private double cachedY;
    private double cachedZ;
    private float cachedRotationPitch;
    private float cachedRotationYaw;
    @Shadow
    @Final
    public NetHandlerPlayClient sendQueue;
    @Shadow
    public MovementInput movementInput;
    @Shadow
    public float renderArmYaw;
    @Shadow
    public float renderArmPitch;
    @Shadow
    public float prevRenderArmYaw;
    @Shadow
    public float prevRenderArmPitch;

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    @Overwrite
    public void sendChatMessage(String message) {
        EventChat event = new EventChat(message);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            return;
        }
        this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
    }

    @Inject(method="onUpdateWalkingPlayer", at={@At(value="HEAD")}, cancellable=true)
    private void onUpdateWalkingPlayerPre(CallbackInfo ci) {
        EventPreUpdate event = new EventPreUpdate(this.rotationYaw, this.rotationPitch, this.posX, this.posY, this.posZ, this.onGround);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            EventBus.getInstance().call(new EventPostUpdate(this.rotationYaw, this.rotationPitch));
            ci.cancel();
        }
        this.cachedX = this.posX;
        this.cachedY = this.posY;
        this.cachedZ = this.posZ;
        this.cachedRotationYaw = this.rotationYaw;
        this.cachedRotationPitch = this.rotationPitch;
        this.posX = event.getX();
        this.posY = event.getY();
        this.posZ = event.getZ();
        this.rotationYaw = event.getYaw();
        this.rotationPitch = event.getPitch();
    }

    @Inject(method="onUpdateWalkingPlayer", at={@At(value="RETURN")})
    private void onUpdateWalkingPlayerPost(CallbackInfo ci) {
        this.posX = this.cachedX;
        this.posY = this.cachedY;
        this.posZ = this.cachedZ;
        this.rotationYaw = this.cachedRotationYaw;
        this.rotationPitch = this.cachedRotationPitch;
        EventBus.getInstance().call(new EventPostUpdate(this.rotationYaw, this.rotationPitch));
    }

    @Redirect(method="onLivingUpdate", at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"))
    public boolean isUsingItem(EntityPlayerSP instance) {
        return (!Client.instance.getModuleManager().getModuleByClass(NoSlowDown.class).isEnabled() || !instance.isUsingItem()) && instance.isUsingItem();
    }

    @Override
    @Overwrite
    public void updateEntityActionState() {
        super.updateEntityActionState();
        if (this.isCurrentViewEntity()) {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch += (this.rotationPitch - this.renderArmPitch) * 0.5f;
            this.renderArmYaw += (this.rotationYaw - this.renderArmYaw) * 0.5f;
            Client.instance.rotationPitchHead = this.rotationPitch;
        }
    }
}

