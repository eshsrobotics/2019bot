package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NetworkTablesInput extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    InputHandler inputHandler;
    if (getParameters().getRaw().isEmpty()) {
      inputHandler = new InputHandler();
    } else {
      inputHandler = new InputHandler(getParameters().getRaw().get(0));
    }
    
    VBox main = new VBox();
    main.getChildren().add(new Text("Application loaded"));
    Scene scene = new Scene(main, 1920, 1080);
    
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        inputHandler.handle(getKeyText(event), true);
      }
    });
    scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        inputHandler.handle(getKeyText(event), false);
      }
    });
    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        inputHandler.handle(getMouseText(event), true);
      }
    });
    scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        inputHandler.handle(getMouseText(event), false);
      }
    });
    
    
    stage.setTitle("NetworkTables Input");
    stage.setScene(scene);
    stage.setMaximized(true);
    stage.show();
  }
  
  private String getKeyText(KeyEvent event) {
    return event.getCode().getName();
  }
  
  private String getMouseText(MouseEvent event) {
    return event.getButton() == MouseButton.PRIMARY ? "Left Mouse" : "Right Mouse";
  }
}
