package de.tiedev.sellhive.cashpoint;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.springframework.boot.WebApplicationType.NONE;

public class CashPointFxApplication extends Application {

   private static final Logger log = LoggerFactory.getLogger(CashPointFxApplication.class);

    private ConfigurableApplicationContext applicationContext;
    private Parent rootNode;
    @Override
    public void init(){
        applicationContext = new SpringApplicationBuilder()
                .web(NONE)
                .sources(CashPointMain.class)
                .initializers(newApplicationContextInitializer(this))
                .run();

        try {
            InputStream inputStream = getClass().getResource("/bundles/messages.properties").openStream();
            ResourceBundle bundle = new PropertyResourceBundle(inputStream);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CashPointMain.fxml"), bundle);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            rootNode = fxmlLoader.load();
        } catch (Exception ignored) {}

    }

    private static ApplicationContextInitializer<GenericApplicationContext> newApplicationContextInitializer(Application application) {
        return ac -> {
            ac.registerBean(Application.class, () -> application);
            ac.registerBean(Parameters.class, application::getParameters);
            ac.registerBean(HostServices.class, application::getHostServices);
        };
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Spieletage Flohmarkt-Kassensystem");
        primaryStage.setScene(new Scene(rootNode, 1280, 850));
        primaryStage.show();
        /* Warum StageReadyEvent: weiß ich noch nicht :) */
        applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {

        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }
}
