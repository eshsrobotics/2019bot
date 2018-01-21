package main;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class InputHandler {
  private NetworkTable inputTable;
  private Map<String, Boolean> pressedMap;
  
  public InputHandler(String robotIp) {
     pressedMap = new HashMap<>();
     NetworkTable.setClientMode();
     NetworkTable.setIPAddress(robotIp);
     inputTable = NetworkTable.getTable("inputTable");
   }
   
   public InputHandler() {
     this("10.17.59.2");
   }
  
   public void handle(String input, boolean pressed) {
     if (pressedMap.containsKey(input) && pressedMap.get(input) == pressed) {
       return;
     }
     System.out.println("Key " + input + " pressed " + pressed);
     inputTable.putBoolean(input, pressed);
     pressedMap.put(input, pressed);
   }
}
