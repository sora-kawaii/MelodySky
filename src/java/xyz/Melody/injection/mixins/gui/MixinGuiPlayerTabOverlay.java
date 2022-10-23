package xyz.Melody.injection.mixins.gui;

import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({GuiPlayerTabOverlay.class})
public abstract class MixinGuiPlayerTabOverlay {
   @Shadow
   @Final
   private static Ordering field_175252_a;
   @Shadow
   @Final
   private Minecraft mc;
   @Shadow
   private IChatComponent header;
   @Shadow
   private IChatComponent footer;

   @Shadow
   public abstract String getPlayerName(NetworkPlayerInfo var1);

   @Shadow
   protected abstract void drawScoreboardValues(ScoreObjective var1, int var2, String var3, int var4, int var5, NetworkPlayerInfo var6);

   @Shadow
   protected abstract void drawPing(int var1, int var2, int var3, NetworkPlayerInfo var4);
}
