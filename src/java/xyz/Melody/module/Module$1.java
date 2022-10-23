package xyz.Melody.module;

import java.util.Iterator;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.math.MathUtil;

class Module$1 extends Command {
   private final Module m;
   final Module this$0;

   Module$1(Module var1, String $anonymous0, String[] $anonymous1, String $anonymous2, String $anonymous3) {
      super($anonymous0, $anonymous1, $anonymous2, $anonymous3);
      this.this$0 = var1;
      this.m = var1;
   }

   public String execute(String[] args) {
      Option option;
      if (args.length >= 2) {
         option = null;
         Numbers fuck = null;
         Mode xd = null;
         Iterator var6 = this.m.values.iterator();

         Value v;
         while(var6.hasNext()) {
            v = (Value)var6.next();
            if (v instanceof Option && v.getName().equalsIgnoreCase(args[0])) {
               option = (Option)v;
            }
         }

         if (option != null) {
            option.setValue(!(Boolean)option.getValue());
            Helper.sendMessage(String.format("> %s has been set to %s", option.getName(), option.getValue()));
         } else {
            var6 = this.m.values.iterator();

            while(var6.hasNext()) {
               v = (Value)var6.next();
               if (v instanceof Numbers && v.getName().equalsIgnoreCase(args[0])) {
                  fuck = (Numbers)v;
               }
            }

            if (fuck != null) {
               if (MathUtil.parsable(args[1], (byte)4)) {
                  double v1 = MathUtil.round(Double.parseDouble(args[1]), 1);
                  fuck.setValue(v1);
                  Helper.sendMessage(String.format("> %s has been set to %s", fuck.getName(), fuck.getValue()));
               } else {
                  Helper.sendMessage("> " + args[1] + " is not a number");
               }
            }

            var6 = this.m.values.iterator();

            while(var6.hasNext()) {
               v = (Value)var6.next();
               if (args[0].equalsIgnoreCase(v.getDisplayName()) && v instanceof Mode) {
                  xd = (Mode)v;
               }
            }

            if (xd != null) {
               if (xd.isValid(args[1])) {
                  xd.setMode(args[1]);
                  Helper.sendMessage(String.format("> %s set to %s", xd.getName(), xd.getModeAsString()));
               } else {
                  Helper.sendMessage("> " + args[1] + " is an invalid mode");
               }
            }
         }

         if (fuck == null && option == null && xd == null) {
            this.syntaxError("Valid .<module> <setting> <mode if needed>");
         }
      } else if (args.length >= 1) {
         option = null;
         Iterator xd1 = this.m.values.iterator();

         while(xd1.hasNext()) {
            Value fuck1 = (Value)xd1.next();
            if (fuck1 instanceof Option && fuck1.getName().equalsIgnoreCase(args[0])) {
               option = (Option)fuck1;
            }
         }

         if (option != null) {
            option.setValue(!(Boolean)option.getValue());
            String fuck2 = option.getName().substring(1);
            String xd2 = option.getName().substring(0, 1).toUpperCase();
            if ((Boolean)option.getValue()) {
               Helper.sendMessage(String.format("> %s has been set to §a%s", xd2 + fuck2, option.getValue()));
            } else {
               Helper.sendMessage(String.format("> %s has been set to §c%s", xd2 + fuck2, option.getValue()));
            }
         } else {
            this.syntaxError("Valid .<module> <setting> <mode if needed>");
         }
      } else {
         Helper.sendMessage(String.format("%s Values: \n %s", this.getName().substring(0, 1).toUpperCase() + this.getName().substring(1).toLowerCase(), this.getSyntax(), "false"));
      }

      return null;
   }
}
