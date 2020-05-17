package ija.project;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.*;

public class Itinerary implements Drawable, TimeUpdate {

    private Coordinate startPos;
    private Coordinate endPos;
    private Coordinate vehiclePos;
    private Vehicle vehicle;
    private Map<String, String> stopTexts = new HashMap<String, String>();

    private Shape lineGui;
    private Shape vehicleGui;
    private List<Shape> stopsGui;
    private List<Shape> stopTextsGui;

    private List<Shape> gui;

    public Itinerary (Vehicle vehicle) {
        this.vehicle = vehicle;
        this.startPos = Coordinate.create(0, 0);
        this.endPos = Coordinate.create(500,0);
        this.vehiclePos = startPos;
        setGui();
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    public void setGui() {

        //TODO nastavit polohu stopiek a vozidlo dat na zaciatok
        this.lineGui = new Line(0,0,500,0);
        this.vehicleGui = new Circle(0,0, 10, vehicle.getLine().getLineColor()))

        for(int i = 0; i < vehicle.getStoplist().size(); i++) {

            //TODO naplnit list mapou stopiek (nazov, cas odchodu)
            //TODO asi bude treba updateovat ke sa zmeni schedule
            String stopId = vehicle.getStoplist().get(i).getStopId();
            LocalTime stopTime = vehicle.getTimeslist().get(i);
            String stopTimeString = stopTime.toString();
            stopTexts.put(stopId, stopTimeString);

            //TODO doplnit suradnice k textom (podla stopsGui)
            stopTextsGui.add(new Text(0,0,stopId + stopTimeString));
        }

        this.gui.add(vehicleGui);
        this.gui.add(lineGui);
        this.gui.addAll(stopsGui);
        this.gui.addAll(stopTextsGui);
    }

    //TODO coordinate v ramci priamky
    private void moveGui (Coordinate coordinate) {

                this.gui.get(0).setTranslateX((coordinate.getX() - vehiclePos.getX()) + this.gui.get(0).getTranslateX());
    }

    public void setDistanceByPosition(Coordinate position) {

    }

    @Override
    public void update (LocalTime time) {


        moveGui();
        //TODO posun voz na linke podla celkovej prejdenej distance

        if(vehicle.isActive()) {
            //TODO show gui
        } else {
            //TODO hide gui
        }
    }

    public void newTime (LocalTime time) {

    }
}
