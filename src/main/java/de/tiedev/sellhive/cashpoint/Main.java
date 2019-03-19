package de.tiedev.sellhive.cashpoint;

import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import de.tiedev.sellhive.cashpoint.spring.config.SpringRootConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class Main extends Application {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private ConfigurableApplicationContext springContext;
	private Parent rootNode;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Spieletage Flohmarkt-Kassensystem");
		primaryStage.setScene(new Scene(rootNode, 1280, 850));
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		springContext.close();
	}

	@Override
	public void init() throws Exception {
		SpringApplication app = new SpringApplication(SpringRootConfig.class);
		springContext = app.run();
		InputStream inputStream = getClass().getResource("/bundles/messages.properties").openStream();
		ResourceBundle bundle = new PropertyResourceBundle(inputStream);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CashPointMain.fxml"), bundle);
		fxmlLoader.setControllerFactory(springContext::getBean);
		rootNode = fxmlLoader.load();

	}

	public static void main(String[] args) {
		launch(args);
		System.exit(0);
	}

}
