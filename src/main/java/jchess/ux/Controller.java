package jchess.ux;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import jchess.game.Game;
import jchess.game.Piece;
import jchess.game.Scenario;
import jchess.util.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller extends Application {
    private static final int W = 1000;
    private static final int H = 1000;
    private static final Color light_colour = Color.NAVAJOWHITE;
    private static final Color dark_colour = Color.SADDLEBROWN;
    private static final Color background_colour = Color.BEIGE;
    private static final Color line_colour = Color.BLACK;
    private static final Color font_colour = Color.BLACK;

    public static Game game;
    public static ArrayList<GraphicPiece> graphicPieces;
    public static List<Vec2> possibleMoves;
    
    static Group root;
    static Group possibleMovesIcons;
    static Group debugGroup;
    static Scene gameScene;
    static Canvas boardCanvas;
    static GraphicPiece selectedPiece;
    
    public static int squareSize;
    public static final int offsetX = 50;
    public static final int offsetY = 50;
    public static int half;

    @Override
    public void start(Stage stage) {
        initGame();

        // initialising the Controller
        root = new Group();
        possibleMovesIcons = new Group();
        debugGroup = new Group();
        gameScene = new Scene(root, W, H);
        boardCanvas = new Canvas(W, H);

        root.getChildren().add(boardCanvas);
        root.getChildren().add(possibleMovesIcons);
        root.getChildren().add(debugGroup);
        stage.setTitle("quirky chess");
        stage.setScene(gameScene);
        stage.show();

        renderBackground(boardCanvas.getGraphicsContext2D(), game);
        for (GraphicPiece gp : graphicPieces) {
            root.getChildren().add(gp);
            gp.refresh();
        }

        // closing the application
        stage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Listener for keyboard inputs
        gameScene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case R -> //!!!!!// TEST THIS //!!!!!//
                        initGame();
                case T -> debugGroup.setVisible(!debugGroup.isVisible());
                case B -> System.out.println("bruh");
                case D -> {
                    System.out.println("§ Input x and y:");
                    Scanner scanner = new Scanner(System.in);
                    int inputX = scanner.nextInt();
                    int inputY = scanner.nextInt();
                    drawDiagonals(new Vec2(inputX, inputY));
                }
            }
        });

        // Listener for mouse click on the board:
        boardCanvas.setOnMouseReleased(event -> {
            Vec2 boardPos = getBoardPos((int) event.getSceneX(), (int) event.getSceneY());
            if (boardPos == null) {
                // if click was outside the board
                // then deselect
                possibleMovesIcons.getChildren().clear();
                selectedPiece = null;
            } else if (selectedPiece != null) {
                boolean legalMove = possibleMoves.contains(boardPos);
                if (legalMove) {
                    movePiece(boardPos);
                } else {
                    // if piece is selected, but move isn't legal
                    //TODO maybe add more feedback
                    System.out.println("sucks to suck (move illegal)");
                }
            }
        });
    }
    
    static void movePiece(Vec2 targetBoardPos) {
        // moving the piece
        game.move(selectedPiece.piece, targetBoardPos);
        // refreshing the selected piece
        selectedPiece.refresh();
        selectedPiece = null;
        // resetting possible moves
        possibleMoves = new ArrayList<>();
        possibleMovesIcons.getChildren().clear();
        System.out.println("Piece moved at " + targetBoardPos);
    }

    private void renderBackground(GraphicsContext gc, Game game) {
        int dimX = game.scenario.terrain.dimensionX;
        int dimY = game.scenario.terrain.dimensionY;

        // drawing the background
        gc.setFill(background_colour);
        gc.fillRect(0, 0, W, H);

        // drawing the board
        int counter = 0;
        gc.setStroke(line_colour);
        gc.setLineWidth(2);
        gc.setFont(Font.font(30));
        for (int i = 1; i <= dimX; i++) {
            gc.setStroke(font_colour);
            gc.setFill(font_colour);
            gc.strokeText(String.valueOf(i), half + 20, i * squareSize + half + 10);
            for (int j = 1; j <= dimY; j++) {
                gc.setStroke(font_colour);
                gc.setFill(font_colour);
                gc.strokeText(String.valueOf(i), i * squareSize + half - 10, half + 40);
                if (game.scenario.terrain.collisionMatrix.get(j-1).get(i-1) != 1) {
                    gc.setFill((counter % 2 == 0 ? light_colour : dark_colour));
                    gc.fillRect(i * squareSize - half + offsetX, j * squareSize - half + offsetY, squareSize, squareSize);
                    gc.setStroke(line_colour);
                    gc.strokeRect(i * squareSize - half + offsetX, j * squareSize - half + offsetY, squareSize, squareSize);
                    counter++;
                }
            }
            counter++;
        }
    }

    static public Vec2 getPixelPos(Vec2 boardPos) {
        return new Vec2((boardPos.x + 1) * squareSize + offsetX - half,
                (boardPos.y + 1) * squareSize + offsetY - half);
    }

    static public Vec2 getBoardPos(Vec2 pixelPos) {
        return getBoardPos(pixelPos.x, pixelPos.y);

    }

    static public Vec2 getPixelPos(int x, int y) {
        return getPixelPos(new Vec2(x, y));
        
    }
    
    static public Vec2 getBoardPos(int x, int y) {
        Vec2 out = new Vec2((x + half - offsetX) / squareSize - 1,
                (y + half - offsetY) / squareSize - 1);
        if (out.x > game.scenario.terrain.dimensionX || out.x < 0
                || out.y > game.scenario.terrain.dimensionY || out.y < 0) {
            return null;
        }
        return out;
    }

    static public void drawPossibleMoves() {
        final Color goodColour = Color.rgb(0, 200, 0, 0.3);
        final Color badColour = Color.rgb(200, 200, 0, 0.3);
        ArrayList<Circle> circles = new ArrayList<>();

        for (Vec2 move : possibleMoves) {
            Vec2 pos = getPixelPos(move);
            Color colour = selectedPiece.piece.isWhite == (game.turn % 2 == 1) ? goodColour : badColour;
            Circle circle = new Circle(pos.x + half, pos.y + half,
                    half / 1.5, colour);
            circles.add(circle);
            circle.mouseTransparentProperty().setValue(true);
        }
        possibleMovesIcons.getChildren().addAll(circles);
    }
    
    static public void drawDiagonals(Vec2 pos) {
        System.out.println("drawDiagonals(" + pos + ")!");
        ArrayList<Circle> circles = new ArrayList<>();
        int x = pos.x, y = pos.y;
        
        // up right + +
        for (int ix = x + 1, iy = y + 1;
             ix < game.scenario.terrain.dimensionX && iy < game.scenario.terrain.dimensionY;
             ix++, iy++) {
            Vec2 pixels = getPixelPos(ix, iy);
            Circle circle = new Circle(pixels.x + half, pixels.y + half,
                    half / 1.5, Color.BLUEVIOLET);
            circle.mouseTransparentProperty().setValue(true);
            circles.add(circle);
        }
        // up left - +
        for (int ix = x - 1, iy = y + 1;
             ix >= 0 && iy < game.scenario.terrain.dimensionY;
             ix--, iy++) {
            Vec2 pixels = getPixelPos(ix, iy);
            Circle circle = new Circle(pixels.x + half, pixels.y + half,
                    half / 1.5, Color.BLUEVIOLET);
            circle.mouseTransparentProperty().setValue(true);
            circles.add(circle);
        }
        // down right + -
        for (int ix = x + 1, iy = y - 1;
             ix < game.scenario.terrain.dimensionX && iy >= 0;
             ix++, iy--) {
            Vec2 pixels = getPixelPos(ix, iy);
            Circle circle = new Circle(pixels.x + half, pixels.y + half,
                    half / 1.5, Color.BLUEVIOLET);
            circle.mouseTransparentProperty().setValue(true);
            circles.add(circle);
        }
        // down left - -
        for (int ix = x - 1, iy = y - 1;
             ix >= 0 && iy >= 0;
             ix--, iy--) {
            Vec2 pixels = getPixelPos(ix, iy);
            Circle circle = new Circle(pixels.x + half, pixels.y + half,
                    half / 1.5, Color.BLUEVIOLET);
            circle.mouseTransparentProperty().setValue(true);
            circles.add(circle);
        }
        debugGroup.getChildren().addAll(circles);
    }
    
    static public void removeGraphicPiece(Piece piece) {
        for (GraphicPiece gp : graphicPieces) {
            if (gp.piece == piece) {
                graphicPieces.remove(gp);
                root.getChildren().remove(gp);
                return;
            }
        }
    }
    
    static private void initGame() {
        // initialising the game
        game = new Game(new Scenario("classic"));

        int squareSizeX = (W - 200) / game.scenario.terrain.dimensionX;
        int squareSizeY = (H - 200) / game.scenario.terrain.dimensionY;
        squareSize = Math.min(squareSizeX, squareSizeY);
        half = squareSize / 2;

        graphicPieces = new ArrayList<>();
        for (Piece p : game.whitePieces) {
            graphicPieces.add(new GraphicPiece(p, true));
        }
        for (Piece p : game.blackPieces) {
            graphicPieces.add(new GraphicPiece(p, false));
        }
        //TODO check this and use it in the rest of the code
    }

    public static void main(String[] args) {
        launch();
    }
}