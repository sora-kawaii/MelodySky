package xyz.Melody.module.modules.others;

import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class ClientCommands extends Module {
   public ClientCommands() {
      super("ClientCommands", new String[]{"cc", "ccm", "command"}, ModuleType.Others);
      this.setModInfo("Enable .xxx Commands.");
      this.setEnabled(true);
   }
}
