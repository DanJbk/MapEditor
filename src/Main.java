import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main extends Application  implements Runnable {

    private static Stage currentstage;
    private static StackPane stackPane;
    private ColorPickerWindow colorpick;

    static SelectionIndicator indicatorbox;
    private Label info;
    static DrawingCoords drawingCoords;
    private static CareTaker caretaker;

    static int renderoffset = 2;    //use to remove and create shapes in correct places

    static PVector cameravel = new PVector(0,0);
    static PVector camerapose = new PVector(0,0);

    public static double zoom = 1;

    public static int GRIDSIZE = 100;
    private static ArrayList<RectangleInfo> shArr = new ArrayList<>();

    public static boolean fill = false;
    public static boolean sticktogrid = true;
    public static boolean generateMaze = false;

    public static boolean mousedown = false;
    boolean Wkey = false;
    boolean Akey = false;
    boolean Skey = false;
    boolean Dkey = false;
    boolean Fkey = false;
    boolean Hkey = false;


    @Override
    public void start(Stage primaryStage) throws Exception{
        currentstage = primaryStage;
        Canvas canvas = new Canvas();
        stackPane = new StackPane(canvas);
        //stackPane.getChildren().add(new RectangleInfo
        // (new PVector(0,0),new PVector(10000,10000),Color.BLACK)); todo add background control

        startstage(primaryStage);
        run();                                                                  //  start colorpicker window
        setUpKeys();
        addindicator();                                                 // adds box indicator
        setupInfoLabel(info);
        drawingCoords = new DrawingCoords();     // add new coordinates tool
        caretaker = new CareTaker();
        setupTimeLine();
    }



    public void startstage(Stage primaryStage){
        //primaryStage.setScene(new Scene(root, 300, 275));
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(stackPane,1500,700));

        primaryStage.show();
    }

    public void setupTimeLine(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16),
                event -> {
                    keyLogic();
                    draw();
                    info.setText(updateInfo());
                    indicatorbox.update(sticktogrid);
                    drawingCoords.update(GRIDSIZE,zoom,sticktogrid,mousedown);
                    resetcamera();
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setUpKeys(){
        currentstage.addEventHandler(KeyEvent.ANY,e ->{
            if (e.getEventType().equals(KeyEvent.KEY_PRESSED)) {
                switch (e.getText()) {
                    case "W":
                    case "w":
                        Wkey = true;
                        break;
                    case "S":
                    case "s":
                        Skey = true;
                        break;
                    case "A":
                    case "a":
                        Akey = true;
                        break;
                    case "D":
                    case "d":
                        Dkey = true;
                        break;
                    case "f":
                    case "F":
                        Fkey = true;
                        break;
                    case "O":
                    case "o":
                        zoom -= 0.1;
                        break;
                    case "I":
                    case "i":
                        zoom += 0.1;
                        break;
                    case "H":
                    case "h":

                        break;
                }
            } else if (e.getEventType().equals(KeyEvent.KEY_RELEASED)){
                switch (e.getText()) {
                    case "W":
                    case "w":
                        Wkey = false;
                        break;
                    case "S":
                    case "s":
                        Skey = false;
                        break;
                    case "A":
                    case "a":
                        Akey = false;
                        break;
                    case "D":
                    case "d":
                        Dkey = false;
                        break;
                    case "f":
                    case "F":
                        Fkey = false;
                        break;
                    case "g":
                    case "G":
                        break;
                    case "R":
                    case "r":
                        caretaker.redo();
                        break;
                    case "U":
                    case "u":
                        caretaker.undo();
                }
            }
        });

        stackPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e ->{

            if(!e.isPrimaryButtonDown()){return;}                                        // check if primary button

            indicatorbox.setStroke(Color.GREEN);
            mousedown = true;
        });

        stackPane.addEventHandler(MouseEvent.MOUSE_RELEASED, e ->{

            if (!mousedown){                                                                                // check if secondary button
                return;
            }

            leftMouseClick();

            mousedown = false;

            PVector[] sizeAndPlace = drawingCoords.getSizeAndPlace();
            PVector size = sizeAndPlace[0];
            PVector place = sizeAndPlace[1];

            size.set(size.mult(1/Main.zoom));
            place.set(place.mult(1/Main.zoom));

            if (sticktogrid) {
                size.round();
                place.round();
            }

            place.set(place.subvector(Main.camerapose));

            if(fill){
                ArrayList<PVector> pvl = fillRec(sizeAndPlace);
                size = new PVector(GRIDSIZE,GRIDSIZE);
                caretaker.insert(new RectangleInfo(sizeAndPlace[1],size,colorpick.pick()),pvl);

                return;
            }

            if (generateMaze){

                ArrayList<PVector> pvl = fillMaze(sizeAndPlace);

                if (pvl == null){return;}
                size = new PVector(GRIDSIZE,GRIDSIZE);
                PVector pv = pvl.get(0).
                        addvector(sizeAndPlace[1]).
                        addvector(sizeAndPlace[0].mult(-0.5)).
                        add(0.5 * GRIDSIZE);

                ArrayList<PVector> places = addVecToPvArray(pvl,pv);

                caretaker.insert(new RectangleInfo(sizeAndPlace[1],size,colorpick.pick()),places);


                return;
            }

            placebox(sizeAndPlace[0],sizeAndPlace[1],colorpick.pick());

            caretaker.insert(new RectangleInfo(sizeAndPlace[1],sizeAndPlace[0],colorpick.pick()));

        });
    }

    public void saveToFile(){

        System.out.println(shArr.size());
        for (RectangleInfo rec : shArr) {

            Color fill = ((Color)rec.getFill());

            System.out.print(        rec.getPos());
            System.out.print(" " + rec.getSize());

            System.out.print(" color RGB:");
            System.out.print(" " + fill.getRed());
            System.out.print(" " + fill.getGreen());
            System.out.print(" " + fill.getBlue());

            System.out.println();
        }
    }

    public void loadFromFile(String str){

    }

    public void setupInfoLabel(Label lb){
        info = new Label("");
        info.setTranslateX(-currentstage.getHeight()/2 - 200);
        info.setTranslateY(-currentstage.getWidth()/5);
        stackPane.getChildren().add(info);
    }

    public String updateInfo(){
        return Math.round(camerapose.getX()) + " " + Math.round(camerapose.getY()) + "\nZoom: " + Math.round(zoom*100);
    }

    public ArrayList<PVector> fillMaze(PVector[] sizeAndPlace){

        ArrayList<PVector> coordList = MazeGenorator.generateMaze(sizeAndPlace[0].mult(1.0/GRIDSIZE),GRIDSIZE);

        if (coordList == null){return null;}
        for (PVector pVector : coordList) {
            PVector place =
                    pVector.
                            addvector(sizeAndPlace[1]).
                            addvector(sizeAndPlace[0].mult(-0.5)).
                            add(0.5 * GRIDSIZE);

            placebox(new PVector(GRIDSIZE, GRIDSIZE), place, colorpick.pick());
        }
        return coordList;
    }

    public ArrayList<PVector> fillRec(PVector[] temp){

        ArrayList<PVector> arr = new ArrayList<>();

        PVector temptemp = temp[0].absvector().mult(1.0/GRIDSIZE);
        for (int i = 0; i < temptemp.getX(); i++){
            for (int j = 0; j < temptemp.getY(); j++){
                PVector place =
                        new PVector(i,j).
                        mult(GRIDSIZE).
                        addvector(temp[1]).
                        addvector(temp[0].mult(-0.5)).
                        add(GRIDSIZE*0.5);

                placebox(new PVector(GRIDSIZE,GRIDSIZE),place,colorpick.pick());
                arr.add(place);
            }
        }
        return arr;
    }

    private ArrayList<PVector> addVecToPvArray(ArrayList<PVector> arr,PVector pv){
        for (PVector vec : arr){
            vec.set(vec.addvector(pv));
        }
        return arr;
    }

    public void resetcamera(){
        cameravel.set(0,0);
    }

    public void addindicator(){
        indicatorbox = new SelectionIndicator();
        stackPane.getChildren().add(indicatorbox);
    }



    public void leftMouseClick(){

    }

    public void placebox(PVector size, PVector place, Color color){

        RectangleInfo newRec = new RectangleInfo(place,size,color);

//        newRec.setTranslateX(place.getX());
//        newRec.setTranslateY(place.getY());
        addShape(newRec);
    }

    public void addcircle(PVector xy){

    }

    public static void addShape(RectangleInfo sh){

        sh. addEventHandler(MouseEvent.MOUSE_PRESSED, e ->{
            if(e.isSecondaryButtonDown()){
                Main.removeShape(sh); //todo sort out memento
                caretaker.insert(sh,true);
            }
        });

        shArr.add(sh);

        int arrsize = stackPane.getChildren().size();
        stackPane.getChildren().add(arrsize , sh);
    }

    public static void removeShape(){
        shArr.remove(shArr.size()-1);
        stackPane.getChildren().remove(stackPane.getChildren().size() - 1);
    }

    public static void removeShape(RectangleInfo rec){
        shArr.remove(rec);
        stackPane.getChildren().remove(rec);
    }

    public void keyLogic(){
        if (Wkey){
            cameravel.setY(cameravel.getY()+10);
            camerapose.setY(camerapose.getY()+10);
        }
        if (Skey){
            cameravel.setY(cameravel.getY()-10);
            camerapose.setY(camerapose.getY()-10);
        }
        if (Akey){
            cameravel.setX(cameravel.getX()+10);
            camerapose.setX(camerapose.getX()+10);
        }
        if (Dkey){
            cameravel.setX(cameravel.getX()-10);
            camerapose.setX(camerapose.getX()-10);
        }
        if(Fkey) {
            saveToFile();
        }
    }

    public void draw(){
        for (RectangleInfo shape: shArr) {
            shape.update();
//            shape.setTranslateX(shape.getTranslateX() + cameravel.getX());
//            shape.setTranslateY(shape.getTranslateY() + cameravel.getY());
        }
    }

    public void undo(){

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static PVector mouseWinPose(){
        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();
        double wx = currentstage.getX();
        double wy = currentstage.getY();
        double wh = currentstage.getHeight();
        double ww = currentstage.getWidth();

        PVector screenpos = new PVector(x,y);
        PVector windowpos = new PVector(wx,wy);
        PVector windowsize = new PVector(ww,wh+23);

        PVector mousetowindow = new PVector(screenpos.subvector(windowpos));    // coordinates relative to window
        PVector correctpose = new PVector(mousetowindow.subvector(windowsize.mult(0.5))); // coordinates relativeto canvas
                                                                                                                                                                     // (0,0) is at the center of  the window
        return correctpose;
    }

    @Override
    public void run() {
        colorpick = new ColorPickerWindow();
    }
}
