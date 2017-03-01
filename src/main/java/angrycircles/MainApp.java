package angrycircles;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;


public class MainApp extends Application {
 static final double WIDTH = 600, HEIGHT = 400;
 double height=400;
    Circle ballUi, point;
    Line line;
    Button btn;
    static Rectangle redBoxUi, groundBoxUi, greenBoxUi, blueBoxUi;
    static AnchorPane root;
    static VBox recVBox;

    private Node content;

        Sim sim = new Sim();
    @Override
    public void start(Stage primaryStage) throws Exception {


        //Node ground = getcenterpane();
        BorderPane game = new BorderPane();
        game.setCenter(new StackPane(createBackground()));
        root = new AnchorPane(game);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        AnchorPane.setTopAnchor(game, 0d);
        AnchorPane.setBottomAnchor(game, 0d);
        AnchorPane.setLeftAnchor(game, 0d);
        AnchorPane.setRightAnchor(game, 0d);
        primaryStage.setTitle("Angry Circles");
         ///  primaryStage.setFullScreen(false);
        // primaryStage.setResizable(false);

        createBall(Color.rgb(113, 74, 139));
        redBoxUi = createBox(Color.rgb(176, 69, 51));
        redBoxUi.heightProperty().bind(root.widthProperty().divide(10));
        redBoxUi.widthProperty().bind(redBoxUi.heightProperty());
        greenBoxUi = createBox(Color.rgb(48, 152, 67));
        greenBoxUi.heightProperty().bind(root.widthProperty().divide(20));
        greenBoxUi.widthProperty().bind(greenBoxUi.heightProperty());
        blueBoxUi = createBox(Color.rgb(46, 124, 172));
        blueBoxUi.heightProperty().bind(root.widthProperty().divide(10));
        blueBoxUi.widthProperty().bind(blueBoxUi.heightProperty());

        line = new Line();
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);

        point = new Circle();
        point.setFill(Color.BLACK);
        point.setRadius(4);
        createGround();

        btn = new Button();
        btn.setText("Launch!");

        VBox elm1 = new VBox();
        elm1.getChildren().addAll(loadContent());
        VBox elm2 = new VBox();
        elm2.setScaleShape(true);
        elm2.getChildren().addAll(loadContent());
        HBox row1 = new HBox();
        row1.setSpacing(10);
        row1.setPrefWidth(600);
        row1.getChildren().addAll(elm1, elm2);

        //Add ground to the application, this is where balls will land
        Sim.addGround(1000, 0);

        //Add left and right walls so balls will not move outside the viewing area.
        sim.addWall(-1, 0, 1, 60); //Left wall
        sim.addWall((float) scene.getWidth() / Sim.METERS_TO_PIXELS, 0, 0, Sim.toPosY((float) scene.getHeight(), (float) scene.getHeight())); //Right wall

        groundBoxUi.xProperty().bind(root.widthProperty().divide(10));
        groundBoxUi.yProperty().bind(root.heightProperty().multiply(2).divide(3));
        groundBoxUi.widthProperty().bind(root.widthProperty().subtract(root.widthProperty().divide(10).multiply(2)));
        groundBoxUi.heightProperty().bind(root.heightProperty().divide(8));

        root.getChildren().addAll(row1, groundBoxUi, blueBoxUi, ballUi, redBoxUi, greenBoxUi);

        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            System.out.println("Width: " + newSceneWidth);
        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            System.out.println("Height: " + (height=(double) newSceneHeight));
        });

        scene.setOnMousePressed((MouseEvent mouseEvent) -> {
            System.out.println(mouseEvent.getClickCount());
            System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getSceneY());
            
            ballUi.setCenterX(sim.ball.getPosition().x * Sim.METERS_TO_PIXELS);
            ballUi.setCenterY(sim.ball.getPosition().y * -Sim.METERS_TO_PIXELS);
            line.setStartX(ballUi.getCenterX());
            line.setStartY(ballUi.getCenterY());
            line.setEndX(mouseEvent.getSceneX());
            line.setEndY(mouseEvent.getSceneY());
            line.strokeWidthProperty().bind(root.widthProperty().divide(root.heightProperty()));
            
            double alpha = Math.atan((line.getStartY() - line.getEndY()) / (line.getStartX() - line.getEndX()));
            System.out.println("angle " + alpha + "radians, " + Math.toDegrees(alpha) + " degrees.");
            
            double Hypotenuse = (Math.pow(mouseEvent.getSceneY(), 2)) + (Math.pow(mouseEvent.getSceneX(), 2));
            Hypotenuse = Math.sqrt(Hypotenuse);
            System.out.println("Hypotenuse: " + Hypotenuse);
            
            point.setCenterX(mouseEvent.getSceneX());
            point.setCenterY(mouseEvent.getSceneY());
            Pane lineBox = new Pane();
            lineBox.getChildren().addAll(line, point);
            root.getChildren().addAll(lineBox);
        });

        sim.start(() -> {
           Platform.runLater(() -> {update();
            });
        });


        //Create button to start simulation.
        btn.setLayoutX((WIDTH / 2) - 15);
        btn .setLayoutY((height - 30));

            root.getChildren().add(btn);
        //root.getChildren().add(ball.node);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((evt) -> System.exit(0));
        primaryStage.show();

    }

    private Node createBackground() {
        Region region = new Region();
        region.setStyle("-fx-background-color: linear-gradient(from 0% 15% to 0% 100%, deepskyblue, #ffffff)");
        return region;
    }

    private Node loadContent() {
        SVGPath svgContent = new SVGPath();
        //     cloud1.setScaleX(-Utils.WIDTH/3);
        svgContent.setFill(Color.WHITE);
        svgContent.setContent("M 449.13759,377.39183 A 141.56682,67.412773 0 0 1 307.57077,444.8046 141.56682,67.412773 0 0 1 166.00395,377.39183 141.56682,67.412773 0 0 1 307.57077,309.97905 141.56682,67.412773 0 0 1 449.13759,377.39183 Z m 3.37063,40.44767 A 141.56682,67.412773 0 0 1 310.9414,485.25227 141.56682,67.412773 0 0 1 169.37458,417.8395 141.56682,67.412773 0 0 1 310.9414,350.42672 141.56682,67.412773 0 0 1 452.50822,417.8395 Z m -96.0632,-7.58397 A 141.56682,67.412773 0 0 1 214.8782,477.6683 141.56682,67.412773 0 0 1 73.311378,410.25553 141.56682,67.412773 0 0 1 214.8782,342.84275 141.56682,67.412773 0 0 1 356.44502,410.25553 Z");
        Group content = new Group();
        content.getChildren().add(svgContent);
        return content;
    }

    private void createGround() {
        groundBoxUi = new Rectangle();
        groundBoxUi.setFill(Color.DODGERBLUE);
        groundBoxUi.setWidth(480);
        groundBoxUi.setHeight(50);
        LinearGradient linearGrad = new LinearGradient(
                0, // start X
                0, // start Y
                0, // end X
                1, // end Y
                true, // proportional
                CycleMethod.NO_CYCLE, // cycle colors
                // stops
                new Stop( 0.1f, Color.rgb( 234, 198, 38, .784)),
                new Stop( 1.0f, Color.rgb( 168, 124, 25, .784)));
        groundBoxUi.setFill(linearGrad);
    }

    private Rectangle createBox(Color color) {
        Rectangle box = new Rectangle();
        box.setFill(color);
        return box;
    }

    private void createBall(Color color) {
        ballUi = new Circle();
        ballUi.setRadius(10);
        ballUi.setFill(color);
    }

    private void update() {
                greenBoxUi.setX(Sim.toPixelPosX(sim.greenBox.getPosition().x, (float) greenBoxUi.getHeight()));
                greenBoxUi.setY(-Sim.toPixelPosY(sim.greenBox.getPosition().y + (float) greenBoxUi.getWidth() / Sim.METERS_TO_PIXELS, (float) greenBoxUi.getWidth()));
                
                redBoxUi.setX(Sim.toPixelPosX(sim.redBox.getPosition().x, (float) redBoxUi.getHeight()));
                redBoxUi.setY(-Sim.toPixelPosY(sim.redBox.getPosition().y + (float) redBoxUi.getWidth() / Sim.METERS_TO_PIXELS, (float) blueBoxUi.getWidth()));
                                
                blueBoxUi.setX(Sim.toPixelPosX(sim.blueBox.getPosition().x, (float) blueBoxUi.getHeight()));
                blueBoxUi.setY(-Sim.toPixelPosY(sim.blueBox.getPosition().y + (float) blueBoxUi.getWidth() / Sim.METERS_TO_PIXELS, (float) blueBoxUi.getWidth()));
                
                ballUi.setCenterX(sim.ball.getPosition().x * Sim.METERS_TO_PIXELS);
                ballUi.setCenterY(sim.ball.getPosition().y * -Sim.METERS_TO_PIXELS);
                ballUi.setRadius(sim.ballRadius + Sim.METERS_TO_PIXELS);
      
    }



    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
