import java.util.ArrayList;

public class Memento {

    private ArrayList<PVector> poslist;
    RectangleInfo recinfo;
    boolean removeditem;

    Memento(RectangleInfo rec){
        poslist = new ArrayList<PVector>();
        poslist.add(new PVector(rec.getPos()));
        recinfo = rec;
        removeditem = false;
    }

    Memento(RectangleInfo rec, boolean bool){   // todo test bool
        poslist = new ArrayList<PVector>();
        poslist.add(new PVector(rec.getPos()));
        recinfo = rec;
        removeditem = bool;
    }

    Memento(RectangleInfo rec, ArrayList<PVector> poseslist){
        poslist = new ArrayList<PVector>();
        poslist.addAll(poseslist);
        recinfo = rec;
        removeditem = false;
    }

    public RectangleInfo getRecinfo() {
        return recinfo;
    }

    public ArrayList<PVector> getPoslist() {
        return poslist;
    }

    public PVector getFirstPose(){
        return poslist.get(0);
    }
}
