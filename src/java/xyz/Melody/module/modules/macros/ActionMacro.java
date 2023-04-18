/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class ActionMacro
extends Module {
    private BlockPos blockPos;
    private BlockPos lastBlockPos = null;
    private float currentDamage = 0.0f;
    private Option<Boolean> forward = new Option<Boolean>("W", false);
    private Option<Boolean> backward = new Option<Boolean>("S", false);
    private Option<Boolean> left = new Option<Boolean>("A", false);
    private Option<Boolean> right = new Option<Boolean>("D", false);
    private Option<Boolean> space = new Option<Boolean>("Jump", false);
    private Option<Boolean> lmb = new Option<Boolean>("Lmb", false);

    public ActionMacro() {
        super("ActionMacro", new String[]{""}, ModuleType.Macros);
        this.addValues(this.forward, this.backward, this.left, this.right, this.space, this.lmb);
        this.setModInfo("Press Keyboard or Mouse Buttons.");
    }

    @EventHandler
    private void idk(EventPreUpdate eventPreUpdate) {
        if (((Boolean)this.forward.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74351_w.func_151463_i(), (boolean)true);
        }
        if (((Boolean)this.backward.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74368_y.func_151463_i(), (boolean)true);
        }
        if (((Boolean)this.left.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74370_x.func_151463_i(), (boolean)true);
        }
        if (((Boolean)this.right.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74366_z.func_151463_i(), (boolean)true);
        }
        if (((Boolean)this.space.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74314_A.func_151463_i(), (boolean)true);
        }
        if (((Boolean)this.lmb.getValue()).booleanValue()) {
            KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74312_F.func_151463_i(), (boolean)true);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        KeyBinding.func_74506_a();
        super.onDisable();
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        KeyBinding.func_74506_a();
        this.setEnabled(false);
    }
}

