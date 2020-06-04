package ija.project;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Stop on a line
 * 
 * @author Samuel Stuchly xstuch06
 * @author Samuel Spisak xspisa02
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "stopId", scope = Stop.class)
@JsonDeserialize(converter = Stop.CallConstructor.class)
public class Stop implements Drawable {
    private String stopId;
    private Coordinate coordinate;
    public EventHandler<MouseEvent> handler;

    @JsonIgnore
    private List<Shape> gui;

    public Stop() {
    }

    public Stop(Coordinate coordinate, EventHandler<MouseEvent> handler) {

        this.coordinate = coordinate;
        this.handler = handler;
        setGui();
    }

    private void setGui() {
        this.gui = new ArrayList<Shape>();
        this.gui.add(new Circle(coordinate.getX(), coordinate.getY(), 12, Color.RED));
        this.gui.get(0).setOnMouseClicked(this.handler);
    }

    /**
     * @param handler
     */
    public void setHandler(EventHandler<MouseEvent> handler) {
        this.handler = handler;
        setGui();
    }

    /**
     * @return List<Shape>
     */
    @JsonIgnore
    @Override
    public List<Shape> getGUI() {
        return gui;
    }

    /**
     * @return String
     */
    public String getStopId() {
        return stopId;
    }

    /**
     * @return Coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {

        if (obj.getClass() == this.getClass()) {

            Stop testStop = (Stop) obj;
            return testStop.getStopId().equals(this.stopId);
        }
        return false;
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + stopId.hashCode();
        return result;
    }

    static class CallConstructor extends StdConverter<Stop, Stop> {

        @Override
        public Stop convert(Stop value) {
            value.setGui();
            return value;
        }
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "Stop{" + "stopId='" + stopId + '\'' + ", coordinate=" + coordinate + ", handler= " + handler + '}';
    }
}
