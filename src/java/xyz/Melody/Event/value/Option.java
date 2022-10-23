package xyz.Melody.Event.value;

public class Option extends Value {
   public Option(String name, Object enabled) {
      super(name, name);
      this.setValue(enabled);
   }
}
