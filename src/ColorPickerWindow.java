import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ColorPickerWindow {
    Stage currentstage;
    Canvas canvas;
    VBox canvaspane;
    Label massL = new Label("mass");
    Label frictionL = new Label("friction");
    Label bounceL = new Label("bounciness");
    ColorPicker picker = new ColorPicker();
    ChoiceBox<String> placeentity;
    CheckBox bouncecheck;
    CheckBox sticktogridbol;
    CheckBox multiplesmall;
    CheckBox createmaze;

    Spinner<Integer> massmeter = new Spinner<Integer>();
    Spinner<Double> frictionmeter = new Spinner<Double>();
    Spinner<Double> bouncemeter = new Spinner<Double>();
    Spinner<Integer> gridsize = new Spinner<Integer>();
    enum EntityType{Platform, PhysicsObject, Particle}

    public void start(Stage primaryStage) throws Exception{
        currentstage = primaryStage;
        canvas = new Canvas();
        canvaspane = new VBox(canvas);
        GridPane gridpane = new GridPane();


        picker.setValue(Color.ORANGE);
        bouncecheck = new CheckBox("Can bounce on floor");
        bouncecheck.setSelected(true);
        multiplesmall = new CheckBox("Multiple Small Boxes");
        multiplesmall.setSelected(false);
        multiplesmall.setOnAction(e ->{
            if (multiplesmall.isSelected()){
                Main.fill = true;
                return;
            }
            Main.fill = false;
        });
        createmaze = new CheckBox("Create Maze");
        createmaze.setSelected(false);
        createmaze.setOnAction(e ->{
            if (createmaze.isSelected()){
                Main.generateMaze = true;
                return;
            }
            Main.generateMaze = false;
        });
        sticktogridbol = new CheckBox("Stick to grid");
        sticktogridbol.setSelected(true);
        sticktogridbol.setOnAction(e ->{
            if (sticktogridbol.isSelected()){
                Main.sticktogrid = true;
                Main.indicatorbox.setStroke(Color.GREEN);
                multiplesmall.setDisable(false);
                createmaze.setDisable(false);
                return;
            }
            Main.indicatorbox.setStroke(Color.TRANSPARENT);
            Main.sticktogrid = false;
            multiplesmall.setDisable(true);
            multiplesmall.setSelected(false);
            Main.fill = false;
            createmaze.setDisable(true);
            createmaze.setSelected(false);
            Main.generateMaze = false;

        });
        placeentity = new ChoiceBox<>(FXCollections.observableArrayList("Platform", "PhysicsObject", "Particle"));
        placeentity.setValue("Platform");
        placeentity.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                fixbytype(number2);
            }
        });

        setmeter(massmeter,1, 1000000, 50,true);
        setmeter(frictionmeter,0.0, 1000000, 0.5,false);
        setmeter(bouncemeter,0.0, 2, 0.5,false);
        setmeter(gridsize,2, 10000, 100,true);

        gridsize.setEditable(true);
        gridsize.valueProperty().addListener(e  -> {
            Main.GRIDSIZE = gridsize.getValue();
        });

        fixbytype(gettype());

        canvaspane.getChildren().add(picker);
        canvaspane.getChildren().add(placeentity);
        canvaspane.getChildren().add(gridpane);

        gridpane.add(massL,0,0);
        gridpane.add(bounceL,0,1);
        gridpane.add(frictionL,0,2);
        gridpane.add(massmeter,1,0);
        gridpane.add(bouncemeter,1,1);
        gridpane.add(gridsize,1,4);
        gridpane.add(frictionmeter,1,2);
        gridpane.add(bouncecheck,0,3);
        gridpane.add(sticktogridbol,0,4);
        gridpane.add(multiplesmall,0,5);
        gridpane.add(createmaze,0,6);


        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(root, 300, 275));

        primaryStage.setScene(new Scene(canvaspane,400,200));
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();



//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16),
//                event -> {
//                    System.out.println(picker.getValue());
//                }));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
    }

    public void fixbytype(Number num){
        if (num.intValue() == 0){
            massmeter.setDisable(true);
            bouncemeter.setDisable(true);
            bouncecheck.setDisable(true);
            massL.setDisable(true);
            bounceL.setDisable(true);

            frictionmeter.getValueFactory().setValue(1.0);
            return;
        }
        massmeter.setDisable(false);
        bouncemeter.setDisable(false);
        massL.setDisable(false);
        bounceL.setDisable(false);
        bouncecheck.setDisable(false);
    }

    public void setmeter(Spinner spinner ,double min, double max, double first, boolean integar){
        spinner.setEditable(true);
        if(integar) {
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory((int)min, (int)max, (int)first));
            return;
        }
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, first));
    }

    public synchronized Color pick(){
        return picker.getValue();
    }

    public synchronized  boolean checkbounceflag(){
        return !bouncecheck.isSelected();
    }

    public synchronized int gettype(){
        return EntityType.valueOf(placeentity.getValue().toString()).ordinal();
    }

    public synchronized double[] mass_friction_bounce(){
        return new double[]{massmeter.getValue(),frictionmeter.getValue(),bouncemeter.getValue()};
    }

    ColorPickerWindow(){
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
