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
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    public MovementInput field_71158_b;
    @Shadow
    public float field_71154_f;
    @Shadow
    public float field_71155_g;
    @Shadow
    public float field_71163_h;
    @Shadow
    public float field_71164_i;

    @Shadow
    protected abstract boolean func_175160_A();

    @Overwrite
    public void func_71165_d(String string) {
        EventChat eventChat = new EventChat(string);
        EventBus.getInstance().call(eventChat);
        if (eventChat.isCancelled()) {
            return;
        }
        this.field_71174_a.func_147297_a(new C01PacketChatMessage(string));
    }

    @Inject(method="onUpdateWalkingPlayer", at={@At(value="HEAD")}, cancellable=true)
    private void onUpdateWalkingPlayerPre(CallbackInfo callbackInfo) {
        EventPreUpdate eventPreUpdate = new EventPreUpdate(this.field_70177_z, this.field_70125_A, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70122_E);
        EventBus.getInstance().call(eventPreUpdate);
        if (eventPreUpdate.isCancelled()) {
            EventBus.getInstance().call(new EventPostUpdate(this.field_70177_z, this.field_70125_A));
            callbackInfo.cancel();
        }
        this.cachedX = this.field_70165_t;
        this.cachedY = this.field_70163_u;
        this.cachedZ = this.field_70161_v;
        this.cachedRotationYaw = this.field_70177_z;
        this.cachedRotationPitch = this.field_70125_A;
        this.field_70165_t = eventPreUpdate.getX();
        this.field_70163_u = eventPreUpdate.getY();
        this.field_70161_v = eventPreUpdate.getZ();
        this.field_70177_z = eventPreUpdate.getYaw();
        this.field_70125_A = eventPreUpdate.getPitch();
    }

    @Inject(method="onUpdateWalkingPlayer", at={@At(value="RETURN")})
    private void onUpdateWalkingPlayerPost(CallbackInfo callbackInfo) {
        this.field_70165_t = this.cachedX;
        this.field_70163_u = this.cachedY;
        this.field_70161_v = this.cachedZ;
        this.field_70177_z = this.cachedRotationYaw;
        this.field_70125_A = this.cachedRotationPitch;
        EventBus.getInstance().call(new EventPostUpdate(this.field_70177_z, this.field_70125_A));
    }

    @Redirect(method="onLivingUpdate", at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"))
    public boolean isUsingItem(EntityPlayerSP entityPlayerSP) {
        return (!Client.instance.getModuleManager().getModuleByClass(NoSlowDown.class).isEnabled() || !entityPlayerSP.func_71039_bw()) && entityPlayerSP.func_71039_bw();
    }

    @Override
    @Overwrite
    public void func_70626_be() {
        super.func_70626_be();
        if (this.func_175160_A()) {
            this.field_70702_br = this.field_71158_b.field_78902_a;
            this.field_70701_bs = this.field_71158_b.field_78900_b;
            this.field_70703_bu = this.field_71158_b.field_78901_c;
            this.field_71163_h = this.field_71154_f;
            this.field_71164_i = this.field_71155_g;
            this.field_71155_g += (this.field_70125_A - this.field_71155_g) * 0.5f;
            this.field_71154_f += (this.field_70177_z - this.field_71154_f) * 0.5f;
            Client.instance.rotationPitchHead = this.field_70125_A;
        }
    }
}

