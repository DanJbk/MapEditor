import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectionIndicator extends Rectangle {

    private boolean fixedCoords;

    SelectionIndicator(){
        super();
        
        fixedCoords = false;

        setStrokeWidth(2);
        getStrokeDashArray().add(5.0);
        getStrokeDashArray().add(1.0);
        getStrokeDashArray().add(5.0);
        setFill(Color.TRANSPARENT);
        setStroke(Color.GREEN);
    }

    public void update(boolean sticktogrid){

        this.fixedCoords = sticktogrid;
        draw();
    }

    public void draw(){

        PVector[] coords = Main.drawingCoords.getSizeAndPlace();
        PVector size = coords[0];
        PVector place = coords[1];

        if (fixedCoords) {
            size.round();
            place.round();
        }

        setWidth(size.getX());
        setHeight(size.getY());
        setTranslateX(place.getX());
        setTranslateY(place.getY());
    }
}
