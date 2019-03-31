import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleInfo extends Rectangle {

    private PVector pos;
    private PVector size;

    boolean phyiscsEnabled;

    double mass;
    double bounce;
    double roughtness;

    RectangleInfo(PVector pos, PVector size, Color color ){
        super(size.getX(),size.getY(),color);
        this.pos = pos;
        this.size = size;
    }

    RectangleInfo(RectangleInfo rec){
        this(new PVector(rec.getPos()),new PVector(rec.getSize()),(Color)rec.getFill());
    }

    public void update(){
        this.setTranslateX((pos.getX()+Main.camerapose.getX())*Main.zoom);
        this.setTranslateY((pos.getY()+Main.camerapose.getY())*Main.zoom);
        this.setWidth(Main.zoom*size.getX());
        this.setHeight(Main.zoom*size.getY());
    }

    public PVector getPos() {
        return pos;
    }

    public PVector getSize() {
        return size;
    }

    public void setPos(PVector pos) {
        this.pos = new PVector(pos);
    }

    public void setSize(PVector size) {
        this.size = new PVector(size);
    }
}
