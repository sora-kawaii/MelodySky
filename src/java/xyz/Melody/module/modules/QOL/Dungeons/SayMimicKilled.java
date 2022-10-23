package xyz.Melody.module.modules.QOL.Dungeons;

import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class SayMimicKilled extends Module {
   public Mode mode;

   public SayMimicKilled() {
      super("SayMimicKilled", new String[]{"as"}, ModuleType.Dungeons);
      this.mode = new Mode("Mode", SayMimicKilled.Chats.values(), SayMimicKilled.Chats.Party);
      this.addValues(new Value[]{this.mode});
      this.setModInfo("Say Mimic Killed When Mimic dead.");
   }

   public static enum Chats {
      Party,
      All;
   }
}
