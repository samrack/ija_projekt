package ija.project;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Street implements Drawable {

//    private String Id;
    private String name;
//    private List<Stop> stops = new ArrayList<>();
    private List<Coordinate> coordinates = new ArrayList<>();

    public Street (String name, Coordinate ... coordinates) {

//        this.Id = Id;
        this.name = name;
        this.coordinates.addAll(Arrays.asList(coordinates));

    }

//    public String getId() {
//        return Id;
//    }

    public String getName() {
        return name;
    }

    public List<Coordinate> getCoordinates() {

        return Collections.unmodifiableList(coordinates);
    }

    public Coordinate begin() {
        return coordinates.get(0);
    }

    public Coordinate end() { return coordinates.get(coordinates.size() - 1);
    }

    @Override
    public List<Shape> getGUI() {
        return Arrays.asList(
                new Text(this.begin().getX() + Math.abs(this.begin().diffX(this.end())) / 2,
                         this.begin().getY() + Math.abs(this.begin().diffY(this.end())) / 2, name),
                new Line(this.begin().getX(), this.begin().getY(), this.end().getX(), this.end().getY())
        );
    }
}
