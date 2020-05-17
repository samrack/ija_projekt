package ija.project;

import javafx.scene.shape.Shape;
import java.util.List;

public class Itinerary implements Drawable {

    private Vehicle vehicle;
    private List<Shape> gui;

    public Itinerary (Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public List<Shape> getGUI() {
        return null;
    }
}
