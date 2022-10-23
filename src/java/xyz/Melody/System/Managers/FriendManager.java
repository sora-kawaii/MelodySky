package xyz.Melody.System.Managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import xyz.Melody.Client;

public class FriendManager implements Manager {
   private static HashMap friends;

   public void init() {
      friends = new HashMap();
      List frriends = FileManager.read("Friends.txt");
      Iterator var3 = frriends.iterator();

      while(var3.hasNext()) {
         String v = (String)var3.next();
         if (v.contains(":")) {
            String name = v.split(":")[0];
            String alias = v.split(":")[1];
            friends.put(name, alias);
         } else {
            friends.put(v, v);
         }
      }

      Client.instance.getCommandManager().add(new FriendManager$1(this, "f", new String[]{"friend", "fren", "fr"}, "add/del/list name alias", "Manage client friends"));
   }

   public static boolean isFriend(String name) {
      return friends.containsKey(name);
   }

   public static String getAlias(Object friends2) {
      return (String)friends.get(friends2);
   }

   public static HashMap getFriends() {
      return friends;
   }

   static HashMap access$0() {
      return friends;
   }
}
