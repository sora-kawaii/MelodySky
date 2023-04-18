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
    public void onTick(EventTick eventTick) {
        BlockPos blockPos;
        EntityItemFrame entityItemFrame;
        if (!Client.inDungeons || this.mc.currentScreen != null || this.mc.objectMouseOver == null) {
            return;
        }
        if (this.foundPattern && this.mc.objectMouseOver.entityHit instanceof EntityItemFrame && (entityItemFrame = (EntityItemFrame)this.mc.objectMouseOver.entityHit).getDisplayedItem() != null && entityItemFrame.getDisplayedItem().getItem() == Items.arrow && !this.clickedItemFrames.contains(blockPos = new BlockPos(entityItemFrame.posX, entityItemFrame.posY, entityItemFrame.posZ)) && requiredClicksForEntity.containsKey(blockPos)) {
            int n = requiredClicksForEntity.get(blockPos);
            int n2 = entityItemFrame.getRotation();
            if (n2 != n) {
                int n3 = n2 < n ? n - n2 : n - n2 + 8;
                for (int i = 0; i < n3; ++i) {
                    Client.rightClick();
                }
            }
            this.clickedItemFrames.add(blockPos);
        }
        if (this.ticks % 70 == 0) {
            this.calculatePattern();
            this.ticks = 0;
        }
        ++this.ticks;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load load) {
        this.foundPattern = false;
        this.clickedItemFrames.clear();
        requiredClicksForEntity.clear();
    }

    private void calculatePattern() {
        Object object;
        requiredClicksForEntity.clear();
        HashMap<BlockPos, Entity> hashMap = new HashMap<BlockPos, Entity>();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        ArrayList<BlockPos> arrayList2 = new ArrayList<BlockPos>();
        HashSet<BlockPos> hashSet = new HashSet<BlockPos>();
        for (Entity object3 : this.mc.theWorld.loadedEntityList) {
            if (!(object3 instanceof EntityItemFrame) || (object = ((EntityItemFrame)object3).getDisplayedItem()) == null) continue;
            Object object2 = ((ItemStack)object).getItem();
            if (object2 == Items.arrow) {
                hashMap.put(new BlockPos(object3.posX, object3.posY, object3.posZ), object3);
                continue;
            }
            if (object2 != Item.getItemFromBlock(Blocks.wool)) continue;
            if (EnumDyeColor.byMetadata(((ItemStack)object).getItemDamage()) == EnumDyeColor.LIME) {
                arrayList2.add(new BlockPos(object3.posX, object3.posY, object3.posZ));
                continue;
            }
            hashSet.add(new BlockPos(object3.posX, object3.posY, object3.posZ));
        }
        if (hashMap.size() >= 9 && arrayList2.size() != 0) {
            for (BlockPos blockPos : arrayList2) {
                object = blockPos.up();
                if (hashMap.containsKey(object)) {
                    arrayList.add(object);
                }
                if (hashMap.containsKey(object = blockPos.down())) {
                    arrayList.add(object);
                }
                if (hashMap.containsKey(object = blockPos.south())) {
                    arrayList.add(object);
                }
                if (!hashMap.containsKey(object = blockPos.north())) continue;
                arrayList.add(object);
            }
            for (int i = 0; i < 200; ++i) {
                if (arrayList.size() == 0) {
                    if (!this.foundPattern) {
                        this.foundPattern = true;
                    }
                    return;
                }
                ArrayList arrayList3 = new ArrayList(arrayList);
                arrayList.clear();
                for (Object object2 : arrayList3) {
                    BlockPos blockPos = ((BlockPos)object2).up();
                    if (hashSet.contains(blockPos)) {
                        requiredClicksForEntity.put((BlockPos)object2, 7);
                        continue;
                    }
                    blockPos = ((BlockPos)object2).down();
                    if (hashSet.contains(blockPos)) {
                        requiredClicksForEntity.put((BlockPos)object2, 3);
                        continue;
                    }
                    blockPos = ((BlockPos)object2).south();
                    if (hashSet.contains(blockPos)) {
                        requiredClicksForEntity.put((BlockPos)object2, 5);
                        continue;
                    }
                    blockPos = ((BlockPos)object2).north();
                    if (hashSet.contains(blockPos)) {
                        requiredClicksForEntity.put((BlockPos)object2, 1);
                        continue;
                    }
                    if (requiredClicksForEntity.containsKey(object2)) continue;
                    blockPos = ((BlockPos)object2).up();
                    if (hashMap.containsKey(blockPos) && !requiredClicksForEntity.containsKey(blockPos)) {
                        arrayList.add(blockPos);
                        requiredClicksForEntity.put((BlockPos)object2, 7);
                        continue;
                    }
                    blockPos = ((BlockPos)object2).down();
                    if (hashMap.containsKey(blockPos) && !requiredClicksForEntity.containsKey(blockPos)) {
                        arrayList.add(blockPos);
                        requiredClicksForEntity.put((BlockPos)object2, 3);
                        continue;
                    }
                    blockPos = ((BlockPos)object2).south();
                    if (hashMap.containsKey(blockPos) && !requiredClicksForEntity.containsKey(blockPos)) {
                        arrayList.add(blockPos);
                        requiredClicksForEntity.put((BlockPos)object2, 5);
                        continue;
                    }
                    blockPos = ((BlockPos)object2).north();
                    if (!hashMap.containsKey(blockPos) || requiredClicksForEntity.containsKey(blockPos)) continue;
                    arrayList.add(blockPos);
                    requiredClicksForEntity.put((BlockPos)object2, 1);
                }
            }
            this.foundPattern = false;
        }
    }
}

