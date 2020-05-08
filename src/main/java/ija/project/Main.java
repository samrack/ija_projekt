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
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Map.fxml"));
//        BorderPane root = loader.load();
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        MainController controller = loader.getController();
        List<Drawable> elements = new ArrayList<>();


        /*---------------------------------Test data-----------------------------------------------*/
        Coordinate c1 = Coordinate.create(100,200);
        Coordinate c2 = Coordinate.create(500,500);
        Coordinate c3 = Coordinate.create(1000,600);
        Coordinate midC1C2 = Coordinate.create((c1.getX() + c2.getX())/2,(c1.getY() + c2.getY())/2);
        Coordinate midC2C3 = Coordinate.create((c2.getX() + c3.getX())/2,(c2.getY() + c3.getY())/2);
        Coordinate midC1C3 = Coordinate.create((c1.getX() + c3.getX())/2,(c1.getY() + c3.getY())/2);

        List<Coordinate> coordinates = new ArrayList<>(Arrays.asList(c1 ,c2, c3, midC1C2, midC2C3, midC2C3));

        ///
        Vehicle vehicle1 = new Vehicle("linka-41", c1, 20, new Path(Arrays.asList(c1,c2)));
        Vehicle vehicle2 = new Vehicle("linka-42", c2, 20, new Path(Arrays.asList(c2,c1)));
        Vehicle vehicle3 = new Vehicle("linka-43", c2, 20, new Path(Arrays.asList(c2,c3)));

        List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(vehicle1, vehicle2, vehicle3));

        ///
        Stop stop1 = new Stop("stop1", c1);
        Stop stop2 = new Stop("stop2", c2);
        Stop stop3 = new Stop("stop3", c3);
        Stop stop12 = new Stop("stop12", midC1C2);
        Stop stop23 = new Stop("stop23", midC2C3);
        Stop stop13 = new Stop("stop13", midC1C3);

        List<Stop> stops = new ArrayList<>(Arrays.asList(stop1, stop2, stop3));

        ///
        Street street1 = new Street("street1", Arrays.asList(stop1, stop12), c1, c2);
        Street street2 = new Street("street2", Arrays.asList(stop2, stop23), c2, c3);
        Street street3 = new Street("street3", Arrays.asList(stop2, stop13), c1, c3);

        List<Street> streets = new ArrayList<>(Arrays.asList(street1, street2, street3));

        ///
//        elements.addAll(vehicles);
//        elements.addAll(stops);
//        elements.addAll(streets);

        Data data = new Data(coordinates, vehicles, stops, streets);
//        Data data = new Data(coordinates, vehicles, stops, streets);

        /*--------------------------------------YAML-------------------------------------------------*/
        YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper mapper = new ObjectMapper(factory);

        /** Serializacia **/
        mapper.writeValue(new File("data.yml"), data);

        /** Deserializacia **/
//        Vehicle vehicle1 = mapper.readValue(new File("data.yml"), Vehicle.class);

//        Data data1 = mapper.readValue(new File("data.yml"), Data.class);
//        System.out.println(data1);
//
//        elements.add(data1.getVehicle());

///

//        controller.setElements(elements);
//        controller.startTimer(1);
    }
}