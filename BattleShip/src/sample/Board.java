package sample;

import java.util.*;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Board extends Parent {
    private VBox rows = new VBox();
    public int ships = 1;
    public int guesses = 0;
    Map<String, Integer> shipInfo = new HashMap<>();
    List<List<String>> hits = new ArrayList<>();
    List<List<String>> locations = new ArrayList<>();
    public int boardSize = 7;
    private Text textDisplay=null;

    public void generateShipLocations() {
        List<String> locations;
        for (int i = 0; i < shipInfo.get("numShips"); i++) {
            do {
                locations = generateShip();
            }
            while (collision(locations));
            this.locations.set(i, locations);
        }
        System.out.print("Ships array: ");
        System.out.print(this.locations);
    }

    public boolean collision(List<String> locations) {
        for (int i = 0; i < shipInfo.get("numShips"); i++) {
            for (int j = 0; j < locations.size(); j++) {
                if (this.locations.get(i).indexOf(locations.get(j)) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> generateShip() {
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        double randNum = rand.nextDouble();
        int direction = (int) Math.floor(randNum*2);
        int row, col;

        if (direction == 1) { // horizontal
            randNum = rand.nextDouble();
            row = (int) Math.floor(randNum * boardSize);
            randNum = rand.nextDouble();
            col = (int) Math.floor(randNum * (boardSize - shipInfo.get("shipLength") + 1));
        } else { // vertical
            randNum = rand.nextDouble();
            row = (int) Math.floor(randNum * (boardSize - shipInfo.get("shipLength") + 1));
            randNum = rand.nextDouble();
            col = (int) Math.floor(randNum * boardSize);
        }

        List<String> newShipLocations = new ArrayList<>();
        for (int i = 0; i < shipInfo.get("numShips"); i++) {
            if (direction == 1) {
                newShipLocations.add(row + "" + (col + i));
            } else {
                newShipLocations.add((row + i) + "" + col);
            }
        }
        return newShipLocations;
    }

    public boolean fire(int x, int y,Text textDisplay) {
/*
        Text text = new Text();

        //Setting font to the text
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        //setting the position of the text
        text.setX(50);
        text.setY(130);

        //Setting the text to be added.
        text.setText("Hi how are you");

        //Creating a Group object
        Group root = new Group(text);
*/

        //Creating a scene object
        this.textDisplay=textDisplay;

        String guess = y+""+x;
        Cell cell = getCell(x, y);
        guesses++;
        for (int i = 0; i < shipInfo.get("numShips"); i++) {
            List<String> shipLoc = locations.get(i);
            List<String> shipHit = hits.get(i);
            int index = shipLoc.indexOf(guess);

            // here's an improvement! Check to see if the ship
            // has already been hit, message the user, and return true.
             if (index >= 0) {
                 if (shipHit.get(index) == "hit") {
                     //view.displayMessage("Oops, you already hit that location!");
                     System.out.println("Oops, you already hit that location!");
                     changeTextDisplay("Oops, you already hit that location!", Color.YELLOWGREEN);
                     return true;
                 }
                shipHit.set(index,"hit");
                /*view.displayHit(guess);
                view.displayMessage("HIT!");*/
                System.out.println("HIT");
                 changeTextDisplay("HIT!", Color.GREEN);

                 cell.setFill(Color.GREEN);
                cell.setStroke(Color.WHITE);
                if (isSunk(shipHit)) {
                    //view.displayMessage("You sank my battleship!");
                    System.out.println("You sank my battleship");
                    changeTextDisplay("You sank my battleship", Color.GREEN);
                    shipInfo.put("shipsSunk", shipInfo.get("shipsSunk")+1);
                }
                 if (shipInfo.get("shipsSunk") == shipInfo.get("numShips")) {
                     System.out.println("You sank all my battleships, in " + this.guesses + " guesses");
                     changeTextDisplay("You sank all my battleships, in " + this.guesses + " guesses", Color.GREEN);
                 }
                return true;
            }
        }
        /*view.displayMiss(guess);
        view.displayMessage("You missed.");*/
        System.out.println("MISS");
        changeTextDisplay("MISS!", Color.RED);
        cell.setFill(Color.RED);
        cell.setStroke(Color.WHITE);
        return false;
    }

    public boolean isSunk(List<String> hit) {
        for (int i = 0; i < shipInfo.get("numShips"); i++)  {
            if (hit.get(i) != "hit") {
                return false;
            }
        }
        return true;
    }

    public Board(EventHandler<? super MouseEvent> handler) {
        //this.enemy = enemy;
        for (int y = 0; y < boardSize; y++) {
            HBox row = new HBox();
            for (int x = 0; x <boardSize; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
        fillHash();
        generateShipLocations();
    }

    public void fillHash(){
        shipInfo.put("numShips", 3);
        shipInfo.put("shipLength", 3);
        shipInfo.put("shipsSunk", 0);
        for(int i = 0; i<3; i++)
            locations.add(Arrays.asList("0","0","0"));
        for(int i = 0; i<3; i++)
            hits.add(Arrays.asList("","",""));
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    public class Cell extends Rectangle {
        public int x, y;
        private Board board;

        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }
    }

    public void changeTextDisplay(String text, Paint color){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                textDisplay.setText(text);
                textDisplay.setFill(color);
            }
        });
    }
}
