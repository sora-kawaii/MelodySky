/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons.Devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoArrowAlign
extends Module {
    private final Set<BlockPos> clickedItemFrames = new HashSet<BlockPos>();
    private static final Map<BlockPos, Integer> requiredClicksForEntity = new HashMap<BlockPos, Integer>();
    private int ticks = 1;
    private boolean foundPattern;

    public AutoArrowAlign() {
        super("AutoArrowAlign", new String[]{"aaa"}, ModuleType.Dungeons);
        this.setModInfo("Auto Do A.A. When CrossHair Hovered.");
    }

    @EventHandler
    public void onTick(EventTick event) {
        BlockPos itemFrameFixedPos;
        EntityItemFrame itemFrame;
        if (!Client.inDungeons || this.mc.currentScreen != null || this.mc.objectMouseOver == null) {
            return;
        }
        if (this.foundPattern && this.mc.objectMouseOver.entityHit instanceof EntityItemFrame && (itemFrame = (EntityItemFrame)this.mc.objectMouseOver.entityHit).getDisplayedItem() != null && itemFrame.getDisplayedItem().getItem() == Items.arrow && !this.clickedItemFrames.contains(itemFrameFixedPos = new BlockPos(itemFrame.posX, itemFrame.posY, itemFrame.posZ)) && requiredClicksForEntity.containsKey(itemFrameFixedPos)) {
            int requiredRotation = requiredClicksForEntity.get(itemFrameFixedPos);
            int currentRotation = itemFrame.getRotation();
            if (currentRotation != requiredRotation) {
                int clickAmount = currentRotation < requiredRotation ? requiredRotation - currentRotation : requiredRotation - currentRotation + 8;
                for (int i = 0; i < clickAmount; ++i) {
                    Client.rightClick();
                }
            }
            this.clickedItemFrames.add(itemFrameFixedPos);
        }
        if (this.ticks % 70 == 0) {
            this.calculatePattern();
            this.ticks = 0;
        }
        ++this.ticks;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.foundPattern = false;
        this.clickedItemFrames.clear();
        requiredClicksForEntity.clear();
    }

    private void calculatePattern() {
        requiredClicksForEntity.clear();
        HashMap<BlockPos, Entity> itemFrames = new HashMap<BlockPos, Entity>();
        ArrayList<BlockPos> currentItemFrames = new ArrayList<BlockPos>();
        ArrayList<BlockPos> startItemFrames = new ArrayList<BlockPos>();
        HashSet<BlockPos> endItemFrames = new HashSet<BlockPos>();
        for (Entity entity : this.mc.theWorld.loadedEntityList) {
            ItemStack displayed;
            if (!(entity instanceof EntityItemFrame) || (displayed = ((EntityItemFrame)entity).getDisplayedItem()) == null) continue;
            Item item = displayed.getItem();
            if (item == Items.arrow) {
                itemFrames.put(new BlockPos(entity.posX, entity.posY, entity.posZ), entity);
                continue;
            }
            if (item != Item.getItemFromBlock(Blocks.wool)) continue;
            if (EnumDyeColor.byMetadata(displayed.getItemDamage()) == EnumDyeColor.LIME) {
                startItemFrames.add(new BlockPos(entity.posX, entity.posY, entity.posZ));
                continue;
            }
            endItemFrames.add(new BlockPos(entity.posX, entity.posY, entity.posZ));
        }
        if (itemFrames.size() >= 9 && startItemFrames.size() != 0) {
            for (BlockPos pos : startItemFrames) {
                BlockPos adjacent = pos.up();
                if (itemFrames.containsKey(adjacent)) {
                    currentItemFrames.add(adjacent);
                }
                if (itemFrames.containsKey(adjacent = pos.down())) {
                    currentItemFrames.add(adjacent);
                }
                if (itemFrames.containsKey(adjacent = pos.south())) {
                    currentItemFrames.add(adjacent);
                }
                if (!itemFrames.containsKey(adjacent = pos.north())) continue;
                currentItemFrames.add(adjacent);
            }
            for (int i = 0; i < 200; ++i) {
                if (currentItemFrames.size() == 0) {
                    if (!this.foundPattern) {
                        this.foundPattern = true;
                    }
                    return;
                }
                ArrayList list = new ArrayList(currentItemFrames);
                currentItemFrames.clear();
                for (BlockPos pos2 : list) {
                    BlockPos adjacent2 = pos2.up();
                    if (endItemFrames.contains(adjacent2)) {
                        requiredClicksForEntity.put(pos2, 7);
                        continue;
                    }
                    adjacent2 = pos2.down();
                    if (endItemFrames.contains(adjacent2)) {
                        requiredClicksForEntity.put(pos2, 3);
                        continue;
                    }
                    adjacent2 = pos2.south();
                    if (endItemFrames.contains(adjacent2)) {
                        requiredClicksForEntity.put(pos2, 5);
                        continue;
                    }
                    adjacent2 = pos2.north();
                    if (endItemFrames.contains(adjacent2)) {
                        requiredClicksForEntity.put(pos2, 1);
                        continue;
                    }
                    if (requiredClicksForEntity.containsKey(pos2)) continue;
                    adjacent2 = pos2.up();
                    if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                        currentItemFrames.add(adjacent2);
                        requiredClicksForEntity.put(pos2, 7);
                        continue;
                    }
                    adjacent2 = pos2.down();
                    if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                        currentItemFrames.add(adjacent2);
                        requiredClicksForEntity.put(pos2, 3);
                        continue;
                    }
                    adjacent2 = pos2.south();
                    if (itemFrames.containsKey(adjacent2) && !requiredClicksForEntity.containsKey(adjacent2)) {
                        currentItemFrames.add(adjacent2);
                        requiredClicksForEntity.put(pos2, 5);
                        continue;
                    }
                    adjacent2 = pos2.north();
                    if (!itemFrames.containsKey(adjacent2) || requiredClicksForEntity.containsKey(adjacent2)) continue;
                    currentItemFrames.add(adjacent2);
                    requiredClicksForEntity.put(pos2, 1);
                }
            }
            this.foundPattern = false;
        }
    }
}

