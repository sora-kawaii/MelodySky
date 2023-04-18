/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.GUI.CustomUI;

import java.util.ArrayList;
import java.util.List;
import xyz.Melody.Event.EventBus;
import xyz.Melody.GUI.CustomUI.Functions.BigRat;
import xyz.Melody.GUI.CustomUI.Functions.CurrentServerInfo;
import xyz.Melody.GUI.CustomUI.Functions.Day;
import xyz.Melody.GUI.CustomUI.Functions.FPS;
import xyz.Melody.GUI.CustomUI.Functions.FishingPotion;
import xyz.Melody.GUI.CustomUI.Functions.KeyStrokes;
import xyz.Melody.GUI.CustomUI.Functions.LCPS;
import xyz.Melody.GUI.CustomUI.Functions.MiningOverlay;
import xyz.Melody.GUI.CustomUI.Functions.NPlayerList;
import xyz.Melody.GUI.CustomUI.Functions.RCPS;
import xyz.Melody.GUI.CustomUI.Functions.TargetHUD;
import xyz.Melody.GUI.CustomUI.HUDApi;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.System.Managers.Manager;

public final class HUDManager
implements Manager {
    public static boolean loaded = false;
    public static List<HUDApi> apis = new ArrayList<HUDApi>();

    @Override
    public void init() {
        apis.add(new FishingPotion());
        apis.add(new TargetHUD());
        apis.add(new NPlayerList());
        apis.add(new Day());
        apis.add(new BigRat());
        apis.add(new MiningOverlay());
        apis.add(new CurrentServerInfo());
        apis.add(new KeyStrokes());
        apis.add(new LCPS());
        apis.add(new RCPS());
        apis.add(new FPS());
        this.readXYE();
        EventBus.getInstance().register(this);
        loaded = true;
    }

    public static List<HUDApi> getApis() {
        return apis;
    }

    public static HUDApi getApiByName(String string) {
        for (HUDApi hUDApi : apis) {
            if (!hUDApi.getName().equalsIgnoreCase(string)) continue;
            return hUDApi;
        }
        return null;
    }

    private void readXYE() {
        List<String> list = FileManager.read("HUD.txt");
        for (String string : list) {
            String string2 = string.split(":")[0];
            String string3 = string.split(":")[1];
            String string4 = string3.split(":")[0];
            String string5 = string.split(":")[2];
            String string6 = string5.split(":")[0];
            String string7 = string.split(":")[3];
            HUDApi hUDApi = HUDManager.getApiByName(string2);
            if (hUDApi == null) continue;
            hUDApi.x = Integer.parseInt(string4);
            hUDApi.y = Integer.parseInt(string6);
            hUDApi.setEnabled(Boolean.parseBoolean(string7));
        }
    }
}

