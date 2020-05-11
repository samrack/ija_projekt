package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stopId", scope = Stop.class)
@JsonDeserialize(converter = Stop.CallConstructor.class)
public class Stop implements Drawable {
    //private Line line;
    private String stopId;
    private Coordinate coordinate;
    @JsonIgnore
    private List<Shape> gui;

    public Stop () {
    }

    public Stop(Coordinate coordinate) {

        this.coordinate = coordinate;
        setGui();
    }

    private void setGui() {
        this.gui = new ArrayList<Shape>();
        this.gui.add(new Circle(coordinate.getX(), coordinate.getY(), 12, Color.RED));
    }

    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    public String getStopId() {
        return stopId;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals (Object obj) {

        if (obj.getClass() == this.getClass()) {

            Stop testStop = (Stop) obj;
            return testStop.getStopId().equals(this.stopId);
        }
        return false;
    }

    @Override
    public int hashCode () {

        final int prime = 31;
        int result = 1;
        result = prime * result + stopId.hashCode();
        return result;
    }

    static class CallConstructor extends StdConverter<Stop, Stop> {

        @Override
        public Stop convert (Stop value) {
            value.setGui();
            return value;
        }
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stopId='" + stopId + '\'' +
                ", coordinate=" + coordinate +
                '}';
    }
}
