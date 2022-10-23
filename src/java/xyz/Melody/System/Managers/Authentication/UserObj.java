package xyz.Melody.System.Managers.Authentication;

public class UserObj {
   private String tkn;
   private String uuid;
   private String name;

   public UserObj(String name, String uuid, String tkn) {
      this.name = name;
      this.uuid = uuid;
      this.tkn = tkn;
   }

   public String getName() {
      return this.name;
   }

   public String getUuid() {
      return this.uuid;
   }

   public String getToken() {
      return this.tkn;
   }
}
