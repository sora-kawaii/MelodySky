package xyz.Melody.Event.value;

public abstract class Value {
   private String displayName;
   private String name;
   private Object value;

   public Value(String displayName, String name) {
      this.displayName = displayName;
      this.name = name;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }
}
