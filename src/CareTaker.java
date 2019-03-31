import java.util.ArrayList;

public class CareTaker {

    private int index;
    private ArrayList<Memento> arr;

    CareTaker(){
        index = 0;
        arr = new ArrayList<>();
    }

    public void insert(RectangleInfo rec) {
        if (index < arr.size() ){
            arr.set(index, new Memento(rec));
        }else {
            arr.add(new Memento(rec));
        }
        index++;
    }

    public void insert(RectangleInfo rec, boolean bool) {   //todo test
        if (index < arr.size() ){
            arr.set(index, new Memento(rec,bool));
        }else {
            arr.add(new Memento(rec,bool));
        }
        index++;
    }

    public void insert(RectangleInfo rec, ArrayList<PVector> pv) {
        if (index < arr.size() ){
            arr.set(index, new Memento(rec,pv));
        } else {
            arr.add(new Memento(rec, pv));
        }
        index++;
    }


    public void redo(){


        if (index == arr.size()){ return;}

        if (arr.get(index).removeditem){    // check if item removed, destroy item
            destroy();
        } else {
            create();
        }
        index++;
    }

    public void undo(){
        if (index == 0){ return;}
        index--;
        if (arr.get(index).removeditem) {   // if remove item create item instead
            create();
            return;
        }
        destroy();
    }

    private void destroy(){
        for (int i = 0; i < arr.get(index).getPoslist().size();i++){
        Main.removeShape();
        }
    }

    private void create(){
        for (int i = 0; i < arr.get(index).getPoslist().size(); i++) {
            RectangleInfo rec = new RectangleInfo(arr.get(index).recinfo);
            rec.setPos(arr.get(index).getPoslist().get(i));
            rec.setSize(arr.get(index).getRecinfo().getSize());
            Main.addShape(rec);
        }
    }
}
