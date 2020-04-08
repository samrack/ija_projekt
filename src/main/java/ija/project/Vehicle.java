package ija.project;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle implements Drawable, TimeUpdate {
    private Coordinate position;
    private double speed = 2;
    private double distance = 0;
    private Path path;
    private List<Shape> gui;


    public Vehicle(Coordinate position, double speed, Path path) {
        this.position = position;
        this.speed = speed;
        this.path = path;
        gui = new ArrayList<Shape>();
        gui.add(new Circle(position.getX(), position.getY(), 8, Color.BLUE));
    }


    private void moveGui(Coordinate coordinate) {
        for (Shape shape : gui) {
            shape.setTranslateX((coordinate.getX() - position.getX()) + shape.getTranslateX());
            shape.setTranslateY((coordinate.getY() - position.getY()) + shape.getTranslateY());
        }
    }

    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    @Override
    public void update(LocalTime time) {
        distance += speed;
        System.out.println(String.format("path len: %f, distance: %f", path.getPathLength(), distance));
        if(distance >= path.getPathLength())
            return;
        Coordinate coordinates = path.getCoordinateByDistance(distance);
        moveGui(coordinates);
        position = coordinates;
    }
}
