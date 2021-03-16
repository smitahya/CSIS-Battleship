package sample;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import sample.Board.Cell;

public class BattleshipMain extends Application {


    private Board playerBoard;
    public Scene scene = null;
    public Text textDisplay=null;
    private Parent createContent() {
        BorderPane root = new BorderPane();
        Insets insets = new Insets(60,0,0, 20);
        //BorderPane bp = new BorderPane();

        //Node topNode = new Label("TOP");
        //bp.setTop(topNode);

        root.setPrefSize(600, 800);

        textDisplay = new Text();

        //Setting font to the text
        textDisplay.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));

        //Setting the text to be added.
        textDisplay.setText("");
        playerBoard = new Board(event -> {
            Cell cell = (Cell) event.getSource();
            playerBoard.fire(cell.x, cell.y, textDisplay);
        });

        VBox vbox = new VBox(50, playerBoard);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);

        root.setTop(textDisplay);
        BorderPane.setMargin(textDisplay, insets);


        return root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}