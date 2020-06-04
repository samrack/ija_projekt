package ija.project;

import java.util.ArrayList;
import java.util.List;








// TODO : Cancel byPass has to work ! ----- Done !
//        Bypass confirm ked su vehicles na ulici musi byt postponed  -> docielime tak ze nastavime flag v vehicle tkory bude spustat vzdy v update a vonkajsia funkcia nastavi len flag 
//        Nastavit spomalenie ulic --------- Done !






public class ByPass {

    private List<List<Street>> origStreetLists = new ArrayList<>();
    private List<Path> origPaths = new ArrayList<>();
    private Street replacedStreet;
    private List<Street> replacmenetStreets = new ArrayList<>();

    private List<Line> allLines= new ArrayList<>();
    
    private List<Line> affectedLines= new ArrayList<>();


    private List<Street> tmpList = new ArrayList<>();
    private Coordinate nextCoord;
    private Coordinate baseBegin;
    private Coordinate baseEnd;
    private List<Coordinate> byPassPath = new ArrayList<>();

    static final public int BYPASS_SPEED = 3;

    
    public ByPass(Street replacedStreet, List<Street> replacementStreets,List<Line> allLines){
        
        this.replacedStreet = replacedStreet;
        
        this.replacmenetStreets = replacementStreets;
        
        this.allLines = allLines;
        
        this.tmpList = new ArrayList<>();
        
        this.baseBegin = replacedStreet.getBegin();
        
        this.baseEnd = replacedStreet.getEnd();
        
        findAffectedLines();
        
    }


	public List<Line> activate(){
        if(isByPassValid()){
            List<Line> lines = new ArrayList<>();
            for (Line line : affectedLines) {
                //line.updateLine(newPath(line), newStreetList(line));
                lines.add(new Line(line.getId() + "_new", newPath(line), line.getStopsList(), newStreetList(line)));    
            }
            return lines;
        }
        else{
            System.out.println("SOMETHING WRONG with bypass");
            return null;
        }
    }

    public void deactivate(){
        for (int i = 0; i < affectedLines.size(); i++) {
            System.out.println(origPaths);
            System.out.println(origStreetLists);
            affectedLines.get(i).updateLine(origPaths.get(i), origStreetLists.get(i));
        }
    }   


    private void findAffectedLines(){
        for (Line line : allLines) {
            if(line.getStreetsList().contains(replacedStreet)){
                affectedLines.add(line);
            }
        }
    }

    private List<Street> newStreetList(Line line) {
        List<Street> streetsList = line.getStreetsList();
        origStreetLists.add(streetsList);
        int index = streetsList.indexOf(replacedStreet);
        
        List<Street> newList = new ArrayList<>();

        // copy until replaced street
        for (int i =0; i < index;i++ ){
            newList.add(streetsList.get(i));
        }
        System.out.println("pred forom"+this.replacmenetStreets);
        // put in replacement streets
        for (Street street : replacmenetStreets) {
            street.setStreetSpeed(BYPASS_SPEED);
            newList.add(street);
        }
        
        int appendIndex = streetsList.size() - ( streetsList.size() - index )  ;
        int restOfList = streetsList.size() - index - 1;
        // append rest of streets
        for (int i= 0; i < restOfList; i++){
            newList.add(streetsList.get(appendIndex + i  ));
        }
        System.out.println("TOTO JE NOVY LIST "+ replacmenetStreets);
        System.out.println("TOTO JE NOVY LIST "+newList);
        return newList;
    }

    private Path newPath(Line line){
        List<Coordinate> oldList = line.getPath().getPath();
        origPaths.add(line.getPath());
        
        int index = oldList.indexOf(baseBegin);
        
        List<Coordinate> newList = new ArrayList<>();

        // copy until replaced coord
        for (int i = 0; i < index; i++){
            newList.add(oldList.get(i));
        }
        

        // put in replacement coords    
        for (Coordinate coord : byPassPath) {
            newList.add(coord);
        }
        
        int appendIndex = oldList.size() - ( oldList.size() - index ) ;
        ;
        int restOfList = oldList.size() - index - 1;

        // append rest of path
        for (int i= 0; i < restOfList; i++){
            newList.add(oldList.get(appendIndex + i ));
        }
        System.out.println("TUSOM ++++++++++++++++");
        System.out.println(line.getPath());
        System.out.println(line.getPath().getPath());
        return new Path(newList);
    }



    public Boolean isByPassValid(){
        
        nextCoord = baseBegin;
        
        for (Street s: replacmenetStreets) {
            tmpList.add(s);
        }
       
    System.out.println("TMP " + tmpList);

        while (!nextCoord.equals(baseEnd)){
            
            byPassPath.add(nextCoord);
            
            Street s = follows();
            if(s == null){
                System.out.println("ERROR INCORRECT BYPASS PATH !");
                return false;
            }
            tmpList.remove(s);
            System.out.println("TMP " + tmpList);
            System.out.println("REP" + replacmenetStreets);

        }
       
        byPassPath.add(baseEnd);

        // in case by pass goes from begin to end but then contains more streets beyond end.
        if (!tmpList.isEmpty()){
            System.out.println("im confused , bypass passes through end of street but doesnt stop there." );
            return false;
        }

        return true;
    }

    
    private Street follows() {
        for (Street street : tmpList) {
            if(street.getBegin().equals(nextCoord)){
                nextCoord = street.getEnd();
                
                return street;
            } 
            else if (street.getEnd().equals(nextCoord)){
                nextCoord = street.getBegin();
                return street;
            }
        }

        return null;
    }


    public List<Line> getAffectedLines() {
        return affectedLines;
    }

}