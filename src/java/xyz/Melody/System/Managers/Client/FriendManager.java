/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Client;

import java.util.HashMap;
import java.util.List;
import xyz.Melody.Client;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.System.Managers.Client.FriendManager$I;
import xyz.Melody.System.Managers.Manager;

public final class FriendManager
implements Manager {
    private static HashMap<String, String> friends;

    @Override
    public void init() {
        friends = new HashMap();
        List<String> list = FileManager.read("Friends.txt");
        for (String string : list) {
            if (string.contains(":")) {
                String string2 = string.split(":")[0];
                String string3 = string.split(":")[1];
                friends.put(string2, string3);
                continue;
            }
            friends.put(string, string);
        }
        Client.instance.getCommandManager().add(new FriendManager$I(this, "f", new String[]{"friend", "fren", "fr"}, "add/del/list name alias", "Manage client friends"));
    }

    public static boolean isFriend(String string) {
        return friends.containsKey(string);
    }

    public static String getAlias(Object object) {
        return friends.get(object);
    }

    public static HashMap<String, String> getFriends() {
        return friends;
    }

    static HashMap<String, String> access$0() {
        return friends;
    }
}

