package xyz.Melody.Event.value;

public class Numbers extends Value {
   private String name;
   public Number min;
   public Number max;
   public Number inc;
   private boolean integer;

   public Numbers(String name, Number value, Number min, Number max, Number inc) {
      super(name, name);
      this.setValue(value);
      this.min = min;
      this.max = max;
      this.inc = inc;
      this.integer = false;
   }

   public Number getMinimum() {
      return this.min;
   }

   public Number getMaximum() {
      return this.max;
   }

   public void setIncrement(Number inc) {
      this.inc = inc;
   }

   public Number getIncrement() {
      return this.inc;
   }

   public String getId() {
      return this.name;
   }
}
