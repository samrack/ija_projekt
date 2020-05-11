package ija.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

        public static void main(String[] args) {
                launch(args);

     
        }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Map.fxml"));
        if (null == loader.getLocation()){
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

        /** Serializacia **/
      //  mapper.writeValue(new File("data.yml"), data);

        String filePathString = "data.yml";
        /** Deserializacia **/
        File f = new File(filePathString);
        if(f.exists() && !f.isDirectory()) { 
            System.out.println("IM ALIVE IM A FILE" );
        }
        else{System.out.println("NOT A FILE PEPEHANDS");}

        Data data1 = mapper.readValue(new File("data.yml"), Data.class);
       // System.out.println(data1);


       // TODO : upravit vehicles posititons
        elements.addAll(data1.getVehicles());
        elements.addAll(data1.getStops());
        elements.addAll(data1.getStreets());


        controller.setElements(elements);
        controller.startTimer(1);
  }


        @Override
        public void stop(){
                System.out.println("Stage is closing");
                //System.exit(0);
                // Save file
        }

}