/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class FrozenTreasureESP
extends Module {
    public ArrayList<BlockPos> ices = new ArrayList();

    public FrozenTreasureESP() {
        super("FrozenTreasureESP", new String[]{"ftesp", "frozenesp"}, ModuleType.QOL);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("FrozenTreasure Bounding Box.");
    }

    @Override
    public void onDisable() {
        this.ices.clear();
        super.onDisable();
    }

    @EventHandler
    private void onR3D(EventRender3D eventRender3D) {
        if (ScoreboardUtils.scoreboardLowerContains("glacial cave")) {
            this.ices.clear();
            for (Entity entity : this.mc.theWorld.loadedEntityList) {
                Block block;
                EntityArmorStand entityArmorStand;
                if (!(entity instanceof EntityArmorStand) || (entityArmorStand = (EntityArmorStand)entity).getEquipmentInSlot(4) == null || (block = this.mc.theWorld.getBlockState(entityArmorStand.getPosition().up()).getBlock()) != Blocks.ice && block != Blocks.packed_ice) continue;
                int n = block == Blocks.ice ? Colors.GREEN.c : Colors.ORANGE.c;
                RenderUtil.drawSolidBlockESP(entityArmorStand.getPosition().up(), n, 3.0f, eventRender3D.getPartialTicks());
                if (this.ices.contains(entityArmorStand.getPosition().up())) continue;
                this.ices.add(entityArmorStand.getPosition().up());
            }
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        this.ices.clear();
    }
}

