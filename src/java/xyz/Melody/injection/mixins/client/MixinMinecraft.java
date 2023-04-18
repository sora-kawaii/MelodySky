/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.injection.mixins.entity.PlayerControllerAccessor;
import xyz.Melody.module.balance.NoHitDelay;
import xyz.Melody.module.modules.others.OldAnimations;

@SideOnly(value=Side.CLIENT)
@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Shadow
    public GuiScreen field_71462_r;
    @Shadow
    private static final Logger field_147123_G = LogManager.getLogger();
    @Shadow
    private int field_71429_W;
    private long lastFrame = this.getTime();

    public long getTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    @Inject(method="clickMouse", at={@At(value="HEAD")}, cancellable=true)
    private void onLeftClick(CallbackInfo callbackInfo) {
        if (Client.instance.getModuleManager().getModuleByClass(NoHitDelay.class).isEnabled()) {
            this.field_71429_W = 0;
        }
    }

    @Inject(method="rightClickMouse", at={@At(value="HEAD")})
    public void rightClickMouse(CallbackInfo callbackInfo) {
        OldAnimations oldAnimations = (OldAnimations)Client.instance.getModuleManager().getModuleByClass(OldAnimations.class);
        if (oldAnimations.isEnabled() && ((Boolean)oldAnimations.punching.getValue()).booleanValue() && ((PlayerControllerAccessor)((Object)Minecraft.func_71410_x().field_71442_b)).isHittingBlock() && Minecraft.func_71410_x().field_71439_g.func_70694_bm() != null && (Minecraft.func_71410_x().field_71439_g.func_70694_bm().func_77975_n() != EnumAction.NONE || Minecraft.func_71410_x().field_71439_g.func_70694_bm().func_77973_b() instanceof ItemBlock)) {
            Minecraft.func_71410_x().field_71442_b.func_78767_c();
        }
    }

    @Inject(method="startGame", at={@At(value="HEAD")})
    private void run(CallbackInfo callbackInfo) {
        Client.preCharset();
    }

    @Inject(method="startGame", at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift=At.Shift.BEFORE)})
    private void startGame(CallbackInfo callbackInfo) {
        Client.instance.start();
    }

    @Inject(method="runTick", at={@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift=At.Shift.AFTER)})
    private void onKey(CallbackInfo callbackInfo) {
        if (Keyboard.getEventKeyState() && this.field_71462_r == null) {
            EventBus.getInstance().call(new EventKey(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
        }
    }

    @Inject(method="shutdown", at={@At(value="HEAD")})
    private void onShutdown(CallbackInfo callbackInfo) {
        Client.instance.stop();
    }

    @Inject(method="displayCrashReport", at={@At(value="HEAD")})
    public void displayCrashReport(CrashReport crashReport, CallbackInfo callbackInfo) {
        Client.instance.stop();
    }

    @Inject(method="runTick", at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;joinPlayerCounter:I", shift=At.Shift.BEFORE)})
    private void onTick(CallbackInfo callbackInfo) {
        EventTick eventTick = new EventTick();
        EventBus.getInstance().call(eventTick);
    }
}

