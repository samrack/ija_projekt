package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "streetName", scope = Street.class)
public class Street implements Drawable {
    private String streetName;
    private List<Stop> stops = new ArrayList<>();
    private List<Coordinate> coordinates = new ArrayList<>();

    private Street () {
    }

    public Street (List<Stop> stops, Coordinate ... coordinates) {

        this.stops.addAll(stops);
        this.coordinates.addAll(Arrays.asList(coordinates));
    }

    public String getStreetName() {
        return streetName;
    }

    public List<Stop> getStops () {
        return Collections.unmodifiableList(stops);
    }

    public List<Coordinate> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    public Coordinate begin() {
        return coordinates.get(0);
    }

    public Coordinate end() { return coordinates.get(coordinates.size() - 1);
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return Arrays.asList(
                new Text(this.begin().getX() + Math.abs(this.begin().diffX(this.end())) / 2,
                         this.begin().getY() + Math.abs(this.begin().diffY(this.end())) / 2, streetName),
                new Line(this.begin().getX(), this.begin().getY(), this.end().getX(), this.end().getY())
        );
    }

    @Override
    public String toString() {
        return "Street{" +
                "streetName='" + streetName + '\'' +
                ", stops=" + stops +
                ", coordinates=" + coordinates +
                '}';
    }
}