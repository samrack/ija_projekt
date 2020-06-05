package ija.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class, controls loading data, aplication start and end.
 * 
 * @author Samuel Stuchly xstuch06
 * @author Samuel Spisak xspisa02
 */
public class Main extends Application {

        static final byte VEHICLES_PER_LINE = 10;

        /**
         * @param args
         */
        public static void main(String[] args) {
                launch(args);

        }

        /**
         * @param primaryStage
         * @throws Exception
         */
        @Override
        public void start(Stage primaryStage) throws Exception {
                final String dir = System.getProperty("user.dir");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Map.fxml"));
                if (null == loader.getLocation()) {

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

                Data data1 = mapper.readValue(new File("data/data1.yml"), Data.class);

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

                Boolean redAlreadySet = false;
                EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

                                Vehicle clickedVehicle;
                                Stop clickedStop;
                                Text clickedText;

                                Boolean alreadySelected = false;

                                for (Drawable elem : elements) {
                                        if (elem instanceof Vehicle) {
                                                if (elem.getGUI().get(0) == event.getTarget()) {
                                                        clickedVehicle = (Vehicle) elem;

                                                        controller.unsetHighligtedLine();
                                                        controller.activeVehicle = clickedVehicle;
                                                        controller.setItinerary((Vehicle) elem);
                                                        controller.setHighlightedLine();
                                                }
                                        }

                                        else if (event.getTarget() instanceof Text && !alreadySelected
                                                        && controller.canSetByPass) {
                                                alreadySelected = true;
                                                clickedText = (Text) event.getTarget();
                                                controller.clickedStreetName = clickedText;
                                                controller.setHighlightStreetName();

                                        }

                                }
                        }
                };

                for (int i = 0; i < linesList.size(); i++) {
                        for (int j = 0; j < VEHICLES_PER_LINE; j++) {
                                try {

                                        Vehicle v = new Vehicle(
                                                        "bus" + linesList.get(i).getId() + "_" + String.valueOf(j),
                                                        linesList.get(i), departTimesList.get(j), handler);
                                        vList.add(v);

                                } catch (Exception e) {
                                        e.printStackTrace();
                                }

                        }
                }

                try {
                        elements.addAll(vList);
                } catch (Exception e) {
                        e.printStackTrace();
                }

                elements.addAll(data1.getStops());
                elements.addAll(data1.getStreets());
                for (Drawable elem : elements) {

                        if (elem instanceof Stop) {
                                ((Stop) elem).setHandler(handler);
                        }
                        if (elem instanceof Street) {
                                ((Street) elem).setHandler(handler);
                        }
                }

                controller.setStreetsList(data1.getStreets());
                controller.setAllLines(data1.getLines());

                controller.setElements(elements);
                controller.startTimer(1);
        }

        @Override
        public void stop() {
                System.exit(0);
        }

}