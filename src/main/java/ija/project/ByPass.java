package ija.project;

import java.util.ArrayList;
import java.util.List;

public class ByPass {

    private List<List<Street>> origStreetLists = new ArrayList<>();
    private List<Path> origPaths = new ArrayList<>();
    private Street replacedStreet;
    private List<Street> replacmenetStreets = new ArrayList<>();

    private List<Line> allLines = new ArrayList<>();

    private List<Line> affectedLines = new ArrayList<>();

    private List<Street> tmpList = new ArrayList<>();
    private Coordinate nextCoord;
    private Coordinate baseBegin;
    private Coordinate baseEnd;
    private List<Coordinate> byPassPath = new ArrayList<>();

    private int index;

    static final public int BYPASS_SPEED = 3;

    public ByPass(Street replacedStreet, List<Street> replacementStreets, List<Line> allLines) {

        this.replacedStreet = replacedStreet;

        this.replacmenetStreets = replacementStreets;

        this.allLines = allLines;

        this.tmpList = new ArrayList<>();

        this.baseBegin = replacedStreet.getBegin();

        this.baseEnd = replacedStreet.getEnd();

        findAffectedLines();

    }

    /**
     * @return Boolean
     */
    public Boolean activate() {
        if (isByPassValid()) {
            for (Line line : affectedLines) {
                line.updateLine(newPath(line), newStreetList(line));
            }
            return true;
        } else {
            return false;
        }
    }

    public void deactivate() {

        for (int i = 0; i < affectedLines.size(); i++) {
            for (Street street : replacmenetStreets) {
                origStreetLists.get(i).add(street);
            }
            affectedLines.get(i).updateLine(origPaths.get(i), origStreetLists.get(i));
        }
    }

    private void findAffectedLines() {
        for (Line line : allLines) {
            if (line.getStreetsList().contains(replacedStreet)) {
                affectedLines.add(line);
            }
        }
    }

    /**
     * @param line
     * @return List<Street>
     */
    private List<Street> newStreetList(Line line) {
        List<Street> streetsList = line.getStreetsList();
        origStreetLists.add(streetsList);
        index = streetsList.indexOf(replacedStreet);

        List<Street> newList = new ArrayList<>();

        // copy until replaced street
        for (int i = 0; i < index; i++) {
            newList.add(streetsList.get(i));
        }
        // put in replacement streets
        for (Street street : replacmenetStreets) {
            street.setStreetSpeed(BYPASS_SPEED);
            newList.add(street);
        }

        int appendIndex = streetsList.size() - (streetsList.size() - index);
        int restOfList = streetsList.size() - index - 1;
        // append rest of streets
        for (int i = 0; i < restOfList; i++) {
            newList.add(streetsList.get(appendIndex + i + 1));
        }
        newList.add(replacedStreet);

        return newList;
    }

    /**
     * @param line
     * @return Path
     */
    private Path newPath(Line line) {
        List<Coordinate> oldList = line.getPath().getPath();
        origPaths.add(line.getPath());

        int index = oldList.indexOf(baseBegin);

        List<Coordinate> newList = new ArrayList<>();

        // copy until replaced coord
        for (int i = 0; i < index; i++) {
            newList.add(oldList.get(i));
        }

        // put in replacement coords
        for (Coordinate coord : byPassPath) {
            newList.add(coord);
        }

        int appendIndex = oldList.size() - (oldList.size() - index);
        int restOfList = oldList.size() - index - 1;

        // append rest of path
        for (int i = 0; i < restOfList; i++) {
            newList.add(oldList.get(appendIndex + i + 1));
        }
        return new Path(newList);
    }

    /**
     * @return Boolean
     */
    private Boolean isByPassValid() {

        nextCoord = baseBegin;

        for (Street s : replacmenetStreets) {
            tmpList.add(s);
        }

        System.out.println("TMP " + tmpList);

        while (!nextCoord.equals(baseEnd)) {

            byPassPath.add(nextCoord);

            Street s = follows();
            if (s == null) {
                return false;
            }
            tmpList.remove(s);

        }

        byPassPath.add(baseEnd);

        // in case by pass goes from begin to end but then contains more streets beyond
        // end.
        if (!tmpList.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Checks and returns sthe next street in bypass
     * 
     * @return Street
     */
    private Street follows() {
        for (Street street : tmpList) {
            if (street.getBegin().equals(nextCoord)) {
                nextCoord = street.getEnd();

                return street;
            } else if (street.getEnd().equals(nextCoord)) {
                nextCoord = street.getBegin();
                return street;
            }
        }

        return null;
    }

    /**
     * @return List<Line>
     */
    public List<Line> getAffectedLines() {
        return affectedLines;
    }

    /**
     * @return List<Street>
     */
    public List<Street> getReplacmenetStreets() {
        return replacmenetStreets;
    }

    /**
     * @return int
     */
    public int getIndex() {
        return index;
    }
}