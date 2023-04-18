/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import net.minecraft.block.BlockAir;
import net.minecraft.potion.Potion;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class OldAnimations
extends Module {
    public Option<Boolean> oldRod = new Option<Boolean>("Rod", true);
    public Option<Boolean> oldBow = new Option<Boolean>("Bow", true);
    public Option<Boolean> oldEating = new Option<Boolean>("Eating", true);
    public Option<Boolean> oldModel = new Option<Boolean>("Model", true);
    public Option<Boolean> punching = new Option<Boolean>("Punching", true);
    public Option<Boolean> oldBlockhitting = new Option<Boolean>("BlockHit", true);
    public Numbers<Double> handX = new Numbers<Double>("HandX", 0.0, -1.0, 1.0, 0.1);
    public Numbers<Double> handY = new Numbers<Double>("HandY", 0.0, -1.0, 1.0, 0.1);
    public Numbers<Double> handZ = new Numbers<Double>("HandZ", 0.0, -1.0, 1.0, 0.1);

    public OldAnimations() {
        super("OldAnimations", new String[]{"alc", "asc", "lobby"}, ModuleType.Others);
        this.addValues(this.oldBlockhitting, this.punching, this.oldModel, this.oldEating, this.oldBow, this.oldRod, this.handX, this.handY, this.handZ);
        this.setModInfo("1.7 Animations");
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.mc.objectMouseOver == null) {
            return;
        }
        if (this.mc.objectMouseOver.getBlockPos() == null) {
            return;
        }
        if ((this.mc.thePlayer.isBlocking() || this.mc.thePlayer.isUsingItem()) && !(this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockAir)) {
            int n;
            int n2 = this.mc.thePlayer.isPotionActive(Potion.digSpeed) ? 6 - (1 + this.mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1 : (n = this.mc.thePlayer.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.mc.thePlayer.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
            if (this.mc.gameSettings.keyBindAttack.isKeyDown() && (!this.mc.thePlayer.isSwingInProgress || this.mc.thePlayer.swingProgressInt >= n / 2 || this.mc.thePlayer.swingProgressInt < 0)) {
                this.mc.thePlayer.swingProgressInt = -1;
                this.mc.thePlayer.isSwingInProgress = true;
            }
        }
    }
}

