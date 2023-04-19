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
        List<String> frriends = FileManager.read("Friends.txt");
        for (String v : frriends) {
            if (v.contains(":")) {
                String name = v.split(":")[0];
                String alias = v.split(":")[1];
                friends.put(name, alias);
                continue;
            }
            friends.put(v, v);
        }
        Client.instance.getCommandManager().add(new FriendManager$I(this, "f", new String[]{"friend", "fren", "fr"}, "add/del/list name alias", "Manage client friends"));
    }

    public static boolean isFriend(String name) {
        return friends.containsKey(name);
    }

    public static String getAlias(Object friends2) {
        return friends.get(friends2);
    }

    public static HashMap<String, String> getFriends() {
        return friends;
    }

    static HashMap<String, String> access$0() {
        return friends;
    }
}

