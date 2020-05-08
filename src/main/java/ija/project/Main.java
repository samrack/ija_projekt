package ija.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.File;
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

        List<Coordinate> coordinates = new ArrayList<>();

        Coordinate c1 = Coordinate.create(100,200);
        Coordinate c2 = Coordinate.create(500,500);
        Coordinate c3 = Coordinate.create(1000,600);

        coordinates.add(c1);
        coordinates.add(c2);
        coordinates.add(c3);

//        Vehicle vehicle = new Vehicle(c1, 20, new Path(Arrays.asList(c1,c2)));

        List<Drawable> elements = new ArrayList<>();
//        elements.add(vehicle);
        elements.add(new Street("TestStreet", c1, c2));

        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);

//        Data data = new Data(coordinates, vehicle);


        /** Serializacia **/
//        mapper.writeValue(new File("data.yml"), data);

        /** Deserializacia **/
//        Vehicle vehicle1 = mapper.readValue(new File("data.yml"), Vehicle.class);

        Data data1 = mapper.readValue(new File("data.yml"), Data.class);
        System.out.println(data1);

        elements.add(data1.getVehicle());

        controller.setElements(elements);
        controller.startTimer(1);
    }
}