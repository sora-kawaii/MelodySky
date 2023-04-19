package xyz.Melody.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.System.Managers.Client.ModuleManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.macros.AutoRuby;
import xyz.Melody.module.modules.macros.GemstoneNuker;

public class Module {
    public String name;
    private String suffix;
    private int color;
    private String[] alias;
    private String modInfo;
    private boolean enabled;
    public boolean enabledOnStartup = false;
    private int key;
    public List values;
    public ModuleType type;
    private boolean removed;
    public Minecraft mc = Minecraft.getMinecraft();
    public ScaledResolution mainWindow;
    public static Random random = new Random();

    public Module(String name, String[] alias, ModuleType type) {
        this.mainWindow = new ScaledResolution(this.mc);
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.suffix = "";
        this.key = 0;
        this.removed = false;
        this.enabled = false;
        this.values = new ArrayList();
        this.modInfo = "";
    }

    public Module(String name, ModuleType type) {
        this.mainWindow = new ScaledResolution(this.mc);
        this.name = name;
        this.alias = new String[0];
        this.type = type;
        this.suffix = "";
        this.key = 0;
        this.removed = false;
        this.enabled = false;
        this.values = new ArrayList();
        this.modInfo = "";
    }

    public String getName() {
        return this.name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public ModuleType getType() {
        return this.type;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean wasRemoved() {
        return this.removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getModInfo() {
        return this.modInfo;
    }

    public void setModInfo(String modInfo) {
        this.modInfo = modInfo;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(Object obj) {
        String suffix = obj.toString();
        if (suffix.isEmpty()) {
            this.suffix = suffix;
        } else {
            this.suffix = String.format("§7- §f%s§7", EnumChatFormatting.GRAY + suffix);
        }

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            if (this.name == "FreeCam" && !this.mc.thePlayer.onGround) {
                Helper.sendMessage("[WARNING] FreeCam can only be used on Ground.");
                this.enabled = false;
                return;
            }

            this.onEnable();
            EventBus.getInstance().register(this);
            this.regFML(this);
            if (ModuleManager.loaded && this.getType() != ModuleType.QOL && this.getType() != ModuleType.Swapping && this.getName() != "ClickGui") {
                if (this.getType() == ModuleType.Macros && !Client.instance.getModuleManager().getModuleByClass(AutoRuby.class).isEnabled() && !(this instanceof GemstoneNuker)) {
                    Helper.sendMessage("[Macro] " + EnumChatFormatting.DARK_AQUA + this.getName() + EnumChatFormatting.GRAY + " Now" + EnumChatFormatting.GREEN + " Enabled" + EnumChatFormatting.GRAY + ".");
                }

                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                NotificationPublisher.queue("Module", this.getName() + " Enabled", NotificationType.INFO, 1000);
            }
        } else {
            EventBus.getInstance().unregister(this);
            this.unregFML(this);
            if (ModuleManager.loaded && this.getType() != ModuleType.QOL && this.getType() != ModuleType.Swapping && this.getName() != "ClickGui") {
                if (this.getType() == ModuleType.Macros && !Client.instance.getModuleManager().getModuleByClass(AutoRuby.class).isEnabled() && !(this instanceof GemstoneNuker)) {
                    Helper.sendMessage("[Macro] " + EnumChatFormatting.DARK_AQUA + this.getName() + EnumChatFormatting.GRAY + " Now" + EnumChatFormatting.RED + " Disabled" + EnumChatFormatting.GRAY + ".");
                }

                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 0.8F));
                NotificationPublisher.queue("Module", this.getName() + " Disabled", NotificationType.INFO, 1000);
            }

            this.onDisable();
        }

    }

    private void regFML(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    private void unregFML(Object obj) {
        MinecraftForge.EVENT_BUS.unregister(obj);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    protected void addValues(Value... values) {
        Value[] var5 = values;
        int var4 = values.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Value value = var5[var3];
            this.values.add(value);
        }

    }

    public List getValues() {
        return this.values;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
        String content = "";
        Client.instance.getModuleManager();

        Module m;
        for(Iterator var4 = ModuleManager.getModules().iterator(); var4.hasNext(); content = content + String.format("%s:%s%s", m.getName(), Keyboard.getKeyName(m.getKey()), System.lineSeparator())) {
            m = (Module)var4.next();
        }

        FileManager.save("Binds.txt", content, false);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void makeCommand() {
        if (this.values.size() > 0) {
            String options = "";
            String other = "";
            for (final Value<?> v : this.values) {
                if (!(v instanceof Mode)) {
                    if (options.isEmpty()) {
                        options = ((StringBuilder)options).toString() + v.getName();
                    }
                    else {
                        options = ((StringBuilder)options).toString() + String.format(", %s", v.getName());
                    }
                }
            }
            for (final Value<?> v : this.values) {
                if (v instanceof Mode) {
                    final Mode<?> mode = (Mode)v;
                    Enum<?>[] modes;
                    for (int length = ((Object[])(Object[])(modes = (Enum<?>[])mode.getModes())).length, i = 0; i < length; ++i) {
                        final Enum<?> e = modes[i];
                        if (other.isEmpty()) {
                            other = ((StringBuilder)other).toString() + e.name().toLowerCase();
                        }
                        else {
                            other = ((StringBuilder)other).toString() + String.format(", %s", e.name().toLowerCase());
                        }
                    }
                }
            }
            Client.instance.getCommandManager().add(new Command(this.name, this.alias, String.format("%s%s", s.isEmpty() ? "" : String.format("%s,", s), s2.isEmpty() ? "" : String.format("%s", s2)), "Setup this module") {

                @Override
                public String execute(String[] args) {
                    if (args.length >= 2) {
                        Value option = null;
                        Numbers fuck = null;
                        Mode xd = null;
                        for (Value<?> v : values) {
                            if (!(v instanceof Option) || !v.getName().equalsIgnoreCase(args[0])) continue;
                            option = (Option)v;
                        }
                        if (option != null) {
                            option.setValue((Boolean)option.getValue() == false);
                            Helper.sendMessage(String.format("> %s has been set to %s", option.getName(), option.getValue()));
                        } else {
                            for (Value<?> v : values) {
                                if (!(v instanceof Numbers) || !v.getName().equalsIgnoreCase(args[0])) continue;
                                fuck = (Numbers)v;
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
                            for (Value<?> v : values) {
                                if (!args[0].equalsIgnoreCase(v.getDisplayName()) || !(v instanceof Mode)) continue;
                                xd = (Mode)v;
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
                        Value option = null;
                        for (Value<?> fuck1 : values) {
                            if (!(fuck1 instanceof Option) || !fuck1.getName().equalsIgnoreCase(args[0])) continue;
                            option = (Option)fuck1;
                        }
                        if (option != null) {
                            option.setValue((Boolean)option.getValue() == false);
                            String fuck2 = option.getName().substring(1);
                            String xd2 = option.getName().substring(0, 1).toUpperCase();
                            if (((Boolean)option.getValue()).booleanValue()) {
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
            });
        }
    }
}
