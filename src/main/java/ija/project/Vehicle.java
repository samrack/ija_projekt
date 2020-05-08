package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lineID", scope = Vehicle.class)
@JsonDeserialize(converter = Vehicle.CallConstructor.class)
public class Vehicle implements Drawable, TimeUpdate {
    private String lineID;
    private Coordinate position;
    private double speed = 2;
    private double distance = 0;
    private Path path;
    @JsonIgnore
    private List<Shape> gui;

    private Vehicle(){
    }

    public Vehicle(String id, Coordinate position, double speed, Path path) {
        lineID = id;
        this.position = position;
        this.speed = speed;
        this.path = path;
        setGui();
    }

    /**
     * Updates time for vehicle:
     *  - sets new distance driven based on current vehicle speed
     *  - this distance is used to update vehicle position
     * **/
    @Override
    public void update(LocalTime time) {
        distance += speed;
        System.out.println(String.format("path len: %f, distance: %f", path.getPathLength(), distance));
        if(distance > path.getPathLength())
            return;
        Coordinate coordinates = path.getNextPosition(distance);
        moveGui(coordinates);
        position = coordinates;
    }

    private void moveGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - position.getY()) + shape.getTranslateY());
        }
    }

    private void setGui() {
        this.gui = new ArrayList<Shape>();
        this.gui.add(new Circle(position.getX(), position.getY(), 10, Color.BLUE));
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    public String getLineID() {
        return lineID;
    }

    public Coordinate getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    public Path getPath() {
        return path;
    }

    static class CallConstructor extends StdConverter<Vehicle, Vehicle> {

        @Override
        public Vehicle convert (Vehicle value) {
            value.setGui();
            return value;
        }
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "lineID='" + lineID + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                ", path=" + path +
                '}';
    }
}
