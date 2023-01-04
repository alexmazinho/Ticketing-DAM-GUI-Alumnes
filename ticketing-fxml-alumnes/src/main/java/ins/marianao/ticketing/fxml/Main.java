package ins.marianao.ticketing.fxml;

import java.util.Locale;
import java.util.ResourceBundle;

import ins.marianao.ticketing.fxml.manager.ResourceManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			/*
			 * BorderPane root = new BorderPane(); Scene scene = new Scene(root,400,400);
			 * scene.getStylesheets().add(getClass().getResource("application.css").
			 * toExternalForm()); primaryStage.setScene(scene); primaryStage.show();
			 */

			ResourceManager.getInstance().setStage(primaryStage);
			ResourceManager.getInstance().setApp(this);

			Locale.setDefault(Locale.ENGLISH);
			//ResourceBundle resourceBundle = ResourceBundle.getBundle("ins/marianao/ticketing/fxml/resources/i18n/messages");
			ResourceBundle applicationBundle = ResourceBundle.getBundle("application");
			ResourceManager.getInstance().setApplicationBundle(applicationBundle);
			ResourceBundle translationsBundle = ResourceBundle.getBundle("i18n/messages");
			ResourceManager.getInstance().setTranslationBundle(translationsBundle);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAppTicketsGUI.fxml"), translationsBundle);
			VBox root = (VBox) loader.load();

			ResourceManager.getInstance().setMenuController(loader.getController());
			
			Scene scene = new Scene(root, 900, 600);

			// scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Catamaran:regular,bold,bolditalic");
			scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Catamaran");
			scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Dosis");
			scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Special+Elite");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// // No cal, es carrega a les vistes

			primaryStage.setScene(scene);

			// controller.obrirInici();
			// primaryStage.setResizable(false);
			primaryStage.setTitle("Sistema de Gestió de Tickets");
			primaryStage.setMinWidth(900);
			primaryStage.setMaxWidth(1200);
			primaryStage.setMinHeight(600);
			primaryStage.setMaxHeight(800);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch();
	}
}
