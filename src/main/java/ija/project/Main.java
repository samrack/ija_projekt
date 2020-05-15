package ija.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

        static final byte VEHICLES_PER_LINE = 10;

        public static void main(String[] args) {
                launch(args);

        }

        @Override
        public void start(Stage primaryStage) throws Exception {
                final String dir = System.getProperty("user.dir");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Map.fxml"));
                if (null == loader.getLocation()) {
                        System.out.println("ZAS JE NULL !!!!!!!!");

                }

                BorderPane root = loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();

                MainController controller = loader.getController();
                List<Drawable> elements = new ArrayList<>();

                /*--------------------------------------YAML-------------------------------------------------*/
                YAMLFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
                ObjectMapper mapper = new ObjectMapper(factory);

                Data data1 = mapper.readValue(new File("data/data.yml"), Data.class);

                List<Line> linesList = data1.getLines();

                // ----- set color to lines -----//
                linesList.get(0).setLineColor(Color.BLUE);
                linesList.get(1).setLineColor(Color.GREEN);
                linesList.get(2).setLineColor(Color.ORANGE);

                /* list of depart time of buses of the line */
                List<Integer> departTimesList = Arrays.asList(0, 6, 12, 18, 24, 30, 36, 42, 48, 54);

                // list of vehicles
                LocalTime time = LocalTime.now();
                List<Vehicle> vList = new ArrayList<>();

                for (int i = 0; i < 1; i++) {
                        for (int j = 0; j < 1; j++) {
                                try {

                                        Vehicle v = new Vehicle(
                                                        "bus" + linesList.get(i).getId() + "_" + String.valueOf(j),
                                                        linesList.get(i), departTimesList.get(j));
                                        vList.add(v);
                                } catch (Exception e) {
                                        System.out.println(e + " CHYBA");
                                }

                        }
                }

                try {
                        elements.addAll(vList);
                } catch (Exception e) {
                        System.out.println("chytil som chybu");
                }

                elements.addAll(data1.getStops());
                elements.addAll(data1.getStreets());

                controller.setStreetsList(data1.getStreets());

                controller.setElements(elements);
                controller.startTimer(1);
        }

        @Override
        public void stop() {
                System.out.println("Stage is closing");
                System.exit(0);
        }

}