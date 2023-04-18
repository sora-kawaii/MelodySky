/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module;

import java.util.ArrayList;
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
import xyz.Melody.module.Module$I;
import xyz.Melody.module.ModuleType;
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
    public List<Value<?>> values;
    public ModuleType type;
    private boolean removed;
    public Minecraft mc = Minecraft.getMinecraft();
    public ScaledResolution mainWindow = new ScaledResolution(this.mc);
    public static Random random = new Random();

    public Module(String string, String[] stringArray, ModuleType moduleType) {
        this.name = string;
        this.alias = stringArray;
        this.type = moduleType;
        this.suffix = "";
        this.key = 0;
        this.removed = false;
        this.enabled = false;
        this.values = new ArrayList();
        this.modInfo = "";
    }

    public Module(String string, ModuleType moduleType) {
        this.name = string;
        this.alias = new String[0];
        this.type = moduleType;
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

    public void setRemoved(boolean bl) {
        this.removed = bl;
    }

    public String getModInfo() {
        return this.modInfo;
    }

    public void setModInfo(String string) {
        this.modInfo = string;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(Object object) {
        String string = object.toString();
        this.suffix = string.isEmpty() ? string : String.format("\u00a77- \u00a7f%s\u00a77", (Object)((Object)EnumChatFormatting.GRAY) + string);
    }

    public void setEnabled(boolean bl) {
        this.enabled = bl;
        if (bl) {
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
                    Helper.sendMessage("[Macro] " + (Object)((Object)EnumChatFormatting.DARK_AQUA) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " Now" + (Object)((Object)EnumChatFormatting.GREEN) + " Enabled" + (Object)((Object)EnumChatFormatting.GRAY) + ".");
                }
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
                NotificationPublisher.queue("Module", this.getName() + " Enabled", NotificationType.INFO, 1000);
            }
        } else {
            EventBus.getInstance().unregister(this);
            this.unregFML(this);
            if (ModuleManager.loaded && this.getType() != ModuleType.QOL && this.getType() != ModuleType.Swapping && this.getName() != "ClickGui") {
                if (this.getType() == ModuleType.Macros && !Client.instance.getModuleManager().getModuleByClass(AutoRuby.class).isEnabled() && !(this instanceof GemstoneNuker)) {
                    Helper.sendMessage("[Macro] " + (Object)((Object)EnumChatFormatting.DARK_AQUA) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " Now" + (Object)((Object)EnumChatFormatting.RED) + " Disabled" + (Object)((Object)EnumChatFormatting.GRAY) + ".");
                }
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 0.8f));
                NotificationPublisher.queue("Module", this.getName() + " Disabled", NotificationType.INFO, 1000);
            }
            this.onDisable();
        }
    }

    private void regFML(Object object) {
        MinecraftForge.EVENT_BUS.register(object);
    }

    private void unregFML(Object object) {
        MinecraftForge.EVENT_BUS.unregister(object);
    }

    public void setColor(int n) {
        this.color = n;
    }

    public int getColor() {
        return this.color;
    }

    protected void addValues(Value<?> ... valueArray) {
        Value<?>[] valueArray2 = valueArray;
        int n = valueArray.length;
        for (int i = 0; i < n; ++i) {
            Value<?> value = valueArray2[i];
            this.values.add(value);
        }
    }

    public List<Value<?>> getValues() {
        return this.values;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int n) {
        this.key = n;
        String string = "";
        Client.instance.getModuleManager();
        for (Module module : ModuleManager.getModules()) {
            string = string + String.format("%s:%s%s", module.getName(), Keyboard.getKeyName(module.getKey()), System.lineSeparator());
        }
        FileManager.save("Binds.txt", string, false);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void makeCommand() {
        if (this.values.size() > 0) {
            String s = "";
            String s2 = "";
            for (final Value value : this.values) {
                if (!(value instanceof Mode)) {
                    if (s.isEmpty()) {
                        s = s.toString() + value.getName();
                    }
                    else {
                        s = s.toString() + String.format(", %s", value.getName());
                    }
                }
            }
            for (final Value value2 : this.values) {
                if (value2 instanceof Mode) {
                    Enum[] modes;
                    for (int length = (modes = ((Mode<Enum>)value2).getModes()).length, i = 0; i < length; ++i) {
                        final Enum enum1 = modes[i];
                        if (s2.isEmpty()) {
                            s2 = s2.toString() + enum1.name().toLowerCase();
                        }
                        else {
                            s2 = s2.toString() + String.format(", %s", enum1.name().toLowerCase());
                        }
                    }
                }
            }
            Client.instance.getCommandManager().add(new Command(this.name, this.alias, String.format("%s%s", s.isEmpty() ? "" : String.format("%s,", s), s2.isEmpty() ? "" : String.format("%s", s2)), "Setup this module") {

                @Override
                public String execute(String[] stringArray) {
                    if (stringArray.length >= 2) {
                        Value value = null;
                        Numbers numbers = null;
                        Mode mode = null;
                        for (Value<?> value2 : values) {
                            if (!(value2 instanceof Option) || !value2.getName().equalsIgnoreCase(stringArray[0])) continue;
                            value = (Option)value2;
                        }
                        if (value != null) {
                            value.setValue((Boolean)value.getValue() == false);
                            Helper.sendMessage(String.format("> %s has been set to %s", value.getName(), value.getValue()));
                        } else {
                            for (Value<?> value2 : values) {
                                if (!(value2 instanceof Numbers) || !value2.getName().equalsIgnoreCase(stringArray[0])) continue;
                                numbers = (Numbers)value2;
                            }
                            if (numbers != null) {
                                if (MathUtil.parsable(stringArray[1], (byte)4)) {
                                    double d = MathUtil.round(Double.parseDouble(stringArray[1]), 1);
                                    numbers.setValue(d);
                                    Helper.sendMessage(String.format("> %s has been set to %s", numbers.getName(), numbers.getValue()));
                                } else {
                                    Helper.sendMessage("> " + stringArray[1] + " is not a number");
                                }
                            }
                            for (Value<?> value2 : values) {
                                if (!stringArray[0].equalsIgnoreCase(value2.getDisplayName()) || !(value2 instanceof Mode)) continue;
                                mode = (Mode)value2;
                            }
                            if (mode != null) {
                                if (mode.isValid(stringArray[1])) {
                                    mode.setMode(stringArray[1]);
                                    Helper.sendMessage(String.format("> %s set to %s", mode.getName(), mode.getModeAsString()));
                                } else {
                                    Helper.sendMessage("> " + stringArray[1] + " is an invalid mode");
                                }
                            }
                        }
                        if (numbers == null && value == null && mode == null) {
                            this.syntaxError("Valid .<module> <setting> <mode if needed>");
                        }
                    } else if (stringArray.length >= 1) {
                        Value value = null;
                        for (Value<?> value3 : values) {
                            if (!(value3 instanceof Option) || !value3.getName().equalsIgnoreCase(stringArray[0])) continue;
                            value = (Option)value3;
                        }
                        if (value != null) {
                            Value<?> value3;
                            value.setValue((Boolean)value.getValue() == false);
                            value3 = value.getName().substring(1);
                            String string = value.getName().substring(0, 1).toUpperCase();
                            if (((Boolean)value.getValue()).booleanValue()) {
                                Helper.sendMessage(String.format("> %s has been set to \u00a7a%s", string + (String)((Object)value3), value.getValue()));
                            } else {
                                Helper.sendMessage(String.format("> %s has been set to \u00a7c%s", string + (String)((Object)value3), value.getValue()));
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

