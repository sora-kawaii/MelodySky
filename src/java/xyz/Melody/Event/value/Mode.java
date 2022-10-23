package xyz.Melody.Event.value;

public class Mode extends Value {
   private Enum[] modes;

   public Mode(String name, Enum[] modes, Enum value) {
      super(name, name);
      this.modes = modes;
      this.setValue(value);
   }

   public Enum[] getModes() {
      return this.modes;
   }

   public String getModeAsString() {
      return ((Enum)this.getValue()).name();
   }

   public void setMode(String mode) {
      Enum[] arrV = this.modes;
      int n = arrV.length;

      for(int n2 = 0; n2 < n; ++n2) {
         Enum e = arrV[n2];
         if (e.name().equalsIgnoreCase(mode)) {
            this.setValue(e);
         }
      }

   }

   public boolean isValid(String name) {
      Enum[] arrV = this.modes;
      int n = arrV.length;

      for(int n2 = 0; n2 < n; ++n2) {
         Enum e = arrV[n2];
         if (e.name().equalsIgnoreCase(name)) {
            return true;
         }
      }

      return false;
   }
}
