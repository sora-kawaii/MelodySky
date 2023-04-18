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
        if (this.mc.field_71476_x == null) {
            return;
        }
        if (this.mc.field_71476_x.func_178782_a() == null) {
            return;
        }
        if ((this.mc.field_71439_g.func_70632_aY() || this.mc.field_71439_g.func_71039_bw()) && !(this.mc.field_71441_e.func_180495_p(this.mc.field_71476_x.func_178782_a()).func_177230_c() instanceof BlockAir)) {
            int n;
            int n2 = this.mc.field_71439_g.func_70644_a(Potion.field_76422_e) ? 6 - (1 + this.mc.field_71439_g.func_70660_b(Potion.field_76422_e).func_76458_c()) * 1 : (n = this.mc.field_71439_g.func_70644_a(Potion.field_76419_f) ? 6 + (1 + this.mc.field_71439_g.func_70660_b(Potion.field_76419_f).func_76458_c()) * 2 : 6);
            if (this.mc.field_71474_y.field_74312_F.func_151470_d() && (!this.mc.field_71439_g.field_82175_bq || this.mc.field_71439_g.field_110158_av >= n / 2 || this.mc.field_71439_g.field_110158_av < 0)) {
                this.mc.field_71439_g.field_110158_av = -1;
                this.mc.field_71439_g.field_82175_bq = true;
            }
        }
    }
}

