package xyz.Melody.injection.mixins.gui;

import java.awt.Color;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.GUI.ClientButton;
import xyz.Melody.GUI.Menu.MainMenu;

@Mixin({GuiMainMenu.class})
public abstract class MixinGuiMainMenu extends GuiScreen {
   @Shadow
   private DynamicTexture viewportTexture;
   @Shadow
   private ResourceLocation backgroundTexture;
   @Shadow
   private String splashText;
   @Shadow
   @Final
   private final Object threadLock = new Object();
   @Shadow
   private int field_92024_r;
   @Shadow
   private int field_92023_s;
   @Shadow
   private int field_92022_t;
   @Shadow
   private int field_92021_u;
   @Shadow
   private int field_92020_v;
   @Shadow
   private int field_92019_w;
   @Shadow
   private boolean field_183502_L;
   @Shadow
   private GuiScreen field_183503_M;
   @Shadow
   private String openGLWarning1;
   @Shadow
   private String openGLWarning2;
   @Final
   @Shadow
   private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
   @Shadow
   private float updateCounter;
   @Shadow
   private String openGLWarningLink;

   @Shadow
   public abstract void renderSkybox(int var1, int var2, float var3);

   @Shadow
   protected abstract void addDemoButtons(int var1, int var2);

   @Shadow
   protected abstract void addSingleplayerMultiplayerButtons(int var1, int var2);

   @Inject(
      method = "initGui",
      at = {@At("RETURN")}
   )
   private void initGui(CallbackInfo callbackInfo) {
      this.buttonList.add(new ClientButton(114, this.width - 100, 10, 60, 24, "MelodySky", (ResourceLocation)null, new Color(20, 20, 20, 80)));
      this.buttonList.add(new ClientButton(514, this.width - 10 - 24, 10, 25, 24, "", new ResourceLocation("Melody/icon/exit.png"), new Color(20, 20, 20, 60)));
      if (!Client.vanillaMenu) {
         this.mc.displayGuiScreen(new MainMenu());
      }

   }

   @Inject(
      method = "actionPerformed",
      at = {@At("HEAD")}
   )
   private void actionPerformed(GuiButton button, CallbackInfo callbackInfo) {
      switch (button.id) {
         case 114:
            Client.vanillaMenu = false;
            this.mc.displayGuiScreen(new MainMenu());
            break;
         case 514:
            this.mc.shutdown();
      }

   }
}
