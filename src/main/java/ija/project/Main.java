package ija.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Map.fxml"));
        BorderPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainController controller = loader.getController();


        Coordinate c1 = Coordinate.create(100,200);
        Coordinate c2 = Coordinate.create(500,500);
        Coordinate c3 = Coordinate.create(1000,600);

        List<Drawable> elements = new ArrayList<>();

        elements.add(new Vehicle(c1, 20, new Path(Arrays.asList(c1,c2))));
        elements.add(new Street("TestStreet", c1, c2));

        controller.setElements(elements);
        controller.startTimer(1);
    }
}