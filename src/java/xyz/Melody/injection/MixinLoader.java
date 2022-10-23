package xyz.Melody.injection;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.MixinEnvironment.Side;

@MCVersion("1.8.9")
public class MixinLoader implements IFMLLoadingPlugin {
   public MixinLoader() {
      MixinBootstrap.init();
      Mixins.addConfiguration("mixins.melody.json");
      MixinEnvironment.getDefaultEnvironment().setSide(Side.CLIENT);
   }

   @Nonnull
   public String[] getASMTransformerClass() {
      return new String[0];
   }

   @Nullable
   public String getModContainerClass() {
      return null;
   }

   @Nullable
   public String getSetupClass() {
      return null;
   }

   public void injectData(Map data) {
   }

   @Nullable
   public String getAccessTransformerClass() {
      return null;
   }
}
