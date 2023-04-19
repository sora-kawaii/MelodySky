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
    public void drawSlot(Slot slot, CallbackInfo ci) {
        AutoEnchantTable aet;
        if (slot == null) {
            return;
        }
        ItemStack stack = slot.getStack();
        if (stack != null && (aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).onStackRender(stack, slot.inventory, slot.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition)) {
            ci.cancel();
            return;
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    @Redirect(method="drawSlot", at=@At(value="INVOKE", target="Lnet/minecraft/inventory/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    public ItemStack drawSlot_getStack(Slot slot) {
        AutoEnchantTable aet;
        ItemStack newStack;
        ItemStack stack = slot.getStack();
        if (stack != null && (newStack = (aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).overrideStack(slot.inventory, slot.getSlotIndex(), stack)) != null) {
            stack = newStack;
        }
        return stack;
    }

    @Redirect(method="drawScreen", at=@At(value="INVOKE", target="Lnet/minecraft/inventory/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    public ItemStack drawScreen_getStack(Slot slot) {
        AutoEnchantTable aet;
        ItemStack newStack;
        if (this.theSlot != null && this.theSlot == slot && this.theSlot.getStack() != null && (newStack = (aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).overrideStack(this.theSlot.inventory, this.theSlot.getSlotIndex(), this.theSlot.getStack())) != null) {
            return newStack;
        }
        return slot.getStack();
    }

    @Inject(method="drawScreen", at={@At(value="HEAD")})
    private void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        HUD hud = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        this.dick(true);
        hud.handleContainer(this.translate, this.opacity, this.width, this.height);
    }

    @Inject(method="drawSlot", at={@At(value="HEAD")}, cancellable=true)
    private void beforeDrawSlot(Slot slot, CallbackInfo callbackInfo) {
        DrawSlotEvent event = new DrawSlotEvent(this.inventorySlots, slot);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method="handleMouseClick", at={@At(value="HEAD")}, cancellable=true)
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        AutoEnchantTable aet;
        GuiContainer $this = (GuiContainer)((Object)this);
        AtomicBoolean ret = new AtomicBoolean(false);
        if (ret.get()) {
            return;
        }
        if (slotIn != null && slotIn.getStack() != null && (aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class)).onStackClick(slotIn.getStack(), $this.inventorySlots.windowId, slotId, clickedButton, clickType)) {
            ci.cancel();
        }
    }

    @Override
    protected void drawDefaultBackground() {
    }

    private void dick(boolean truefalse) {
        if (!truefalse) {
            return;
        }
        HUD hud = (HUD)Client.instance.getModuleManager().getModuleByClass(HUD.class);
        if (!hud.isEnabled() || !((Boolean)hud.blur.getValue()).booleanValue()) {
            this.drawWorldBackground(0);
        }
    }
}

