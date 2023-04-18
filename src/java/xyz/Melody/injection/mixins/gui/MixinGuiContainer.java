/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.gui;

import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.container.DrawSlotEvent;
import xyz.Melody.GUI.ClickNew.Opacity;
import xyz.Melody.Utils.animate.Translate;
import xyz.Melody.injection.mixins.gui.MixinGuiScreen;
import xyz.Melody.module.modules.QOL.AutoEnchantTable;
import xyz.Melody.module.modules.others.HUD;

@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer
extends MixinGuiScreen {
    @Shadow
    public Container inventorySlots;
    @Shadow
    private Slot theSlot;
    public Translate translate = new Translate(0.0f, 0.0f);
    public Opacity opacity = new Opacity(1);
    private static final String TARGET_GETSTACK = "Lnet/minecraft/inventory/Slot;getStack()Lnet/minecraft/item/ItemStack;";

    @Inject(method="initGui", at={@At(value="HEAD")})
    private void init(CallbackInfo callbackInfo) {
        this.opacity = new Opacity(1);
        super.initGui();
    }

    @Inject(method="drawSlot", at={@At(value="HEAD")}, cancellable=true)
    public void drawSlot(Slot slot, CallbackInfo callbackInfo) {
        AutoEnchantTable autoEnchantTable;
        if (slot == null) {
            return;
        }
        ItemStack itemStack = slot.getStack();
        if (itemStack != null && (autoEnchantTable = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).onStackRender(itemStack, slot.inventory, slot.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition)) {
            callbackInfo.cancel();
            return;
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    @Redirect(method="drawSlot", at=@At(value="INVOKE", target="Lnet/minecraft/inventory/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    public ItemStack drawSlot_getStack(Slot slot) {
        AutoEnchantTable autoEnchantTable;
        ItemStack itemStack;
        ItemStack itemStack2 = slot.getStack();
        if (itemStack2 != null && (itemStack = (autoEnchantTable = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).overrideStack(slot.inventory, slot.getSlotIndex(), itemStack2)) != null) {
            itemStack2 = itemStack;
        }
        return itemStack2;
    }

    @Redirect(method="drawScreen", at=@At(value="INVOKE", target="Lnet/minecraft/inventory/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    public ItemStack drawScreen_getStack(Slot slot) {
        AutoEnchantTable autoEnchantTable;
        ItemStack itemStack;
        if (this.theSlot != null && this.theSlot == slot && this.theSlot.getStack() != null && (itemStack = (autoEnchantTable = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).overrideStack(this.theSlot.inventory, this.theSlot.getSlotIndex(), this.theSlot.getStack())) != null) {
            return itemStack;
        }
        return slot.getStack();
    }

    @Inject(method="drawScreen", at={@At(value="HEAD")})
    private void drawScreen(int n, int n2, float f, CallbackInfo callbackInfo) {
        HUD hUD = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        this.dick(true);
        hUD.handleContainer(this.translate, this.opacity, this.width, this.height);
    }

    @Inject(method="drawSlot", at={@At(value="HEAD")}, cancellable=true)
    private void beforeDrawSlot(Slot slot, CallbackInfo callbackInfo) {
        DrawSlotEvent drawSlotEvent = new DrawSlotEvent(this.inventorySlots, slot);
        EventBus.getInstance().call(drawSlotEvent);
        if (drawSlotEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method="handleMouseClick", at={@At(value="HEAD")}, cancellable=true)
    public void handleMouseClick(Slot slot, int n, int n2, int n3, CallbackInfo callbackInfo) {
        AutoEnchantTable autoEnchantTable;
        GuiContainer guiContainer = (GuiContainer)((Object)this);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (atomicBoolean.get()) {
            return;
        }
        if (slot != null && slot.getStack() != null && (autoEnchantTable = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).onStackClick(slot.getStack(), guiContainer.inventorySlots.windowId, n, n2, n3)) {
            callbackInfo.cancel();
        }
    }

    @Override
    protected void drawDefaultBackground() {
    }

    private void dick(boolean bl) {
        if (!bl) {
            return;
        }
        HUD hUD = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (!hUD.isEnabled() || !((Boolean)hUD.blur.getValue()).booleanValue()) {
            this.drawWorldBackground(0);
        }
    }
}

