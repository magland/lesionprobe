package lesionprobe;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author magland
 */
public class LesionProbe extends Application {

	LPMainWidget m_widget;

	@Override
	public void start(Stage primaryStage) {
		LPMainWidget m_widget = new LPMainWidget();
		m_widget.setDataPath("/home/magland/data/LesionProbe");

		VBox root = new VBox();
		root.getChildren().addAll(/*menubar,*/m_widget);

		Scene scene = new Scene(root, 800, 450);

		primaryStage.setTitle("LesionProbe");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		m_widget.do_test();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
