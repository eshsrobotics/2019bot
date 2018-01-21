package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NetworkTablesInput extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    InputHandler inputHandler = new InputHandler();
    
    VBox main = new VBox();
    main.getChildren().add(new Text("Application loaded"));
    Scene scene = new Scene(main, 1920, 1080);
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        inputHandler.handle(getText(event), true);
      }
    });
    scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        inputHandler.handle(getText(event), false);
      }
    });
    stage.setTitle("NetworkTables Input");
    stage.setScene(scene);
    stage.setMaximized(true);
    stage.show();
  }
  
  private String getText(KeyEvent event) {
    return event.getCode().getName();
  }
}
