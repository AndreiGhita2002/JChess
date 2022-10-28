package jchess.game;

import javafx.util.Pair;
import jchess.util.Vec2;
import jchess.ux.Controller;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;
    public List<Pair<Piece, Vec2>> history;
    public Scenario scenario;
    public int turn;

    public Game(Scenario scenario) {
        // loading the scenario
        this.scenario = scenario;
        whitePieces = scenario.whitePieces;
        blackPieces = scenario.blackPieces;
        turn = 0;
        history = new ArrayList<>();
    }

    public void getAllPossibleMoves(Piece piece) {
        List<Vec2> out = new ArrayList<>();
        int x = piece.position.x;
        int y = piece.position.y;
        
        // checking horizontal movement
        if (piece.type.movesHorizontally) {
            // right
            for (int ix = x + 1; ix < scenario.terrain.dimensionX; ix++) {
                if (checkIfFree(ix, y)) {
                    out.add(new Vec2(ix, y));
                } else if (!piece.type.tags.contains("jumps") && checkIfInBounds(x, y)){
                    // if piece can't jump, then it stops searching
                    break;
                }
            }
            // left
            for (int ix = x - 1; ix > 0; ix--) {
                if (checkIfFree(ix, y)) {
                    out.add(new Vec2(ix, y));
                } else if (!piece.type.tags.contains("jumps") && checkIfInBounds(x, y)){
                    break;
                }
            }
        }

        // checking vertical movement
        if (piece.type.movesVertically) {
            // up (+y)
            for (int iy = y + 1; iy < scenario.terrain.dimensionY; iy++) {
                if (checkIfFree(x, iy)) {
                    out.add(new Vec2(x, iy));
                } else if (!piece.type.tags.contains("jumps") && checkIfInBounds(x, y)){
                    break;
                }
            }
            // down (-y)
            for (int iy = y - 1; iy > 0; iy--) {
                if (checkIfFree(x, iy)) {
                    out.add(new Vec2(x, iy));
                } else if (!piece.type.tags.contains("jumps") && checkIfInBounds(x, y)){
                    break;
                }
            }
        }
        
        // checking diagonal movement (ugly | works | slow)
        // TODO holy shit make this look better
        if (piece.type.movesDiagonally) {
            // up right + +
            for (int ix = x + 1, iy = y + 1; ix < scenario.terrain.dimensionX && iy < scenario.terrain.dimensionY; ix++, iy++) {
                if (!checkIfInBounds(x, y)) {
                    break;
                } else if (checkIfFree(ix, iy)) {
                    // free space
                    out.add(new Vec2(ix, iy));
                } else if (piece.isWhite != getPiece(ix, iy).isWhite || piece.containsTag("jumps")) {
                    // space with enemy piece
                    out.add(new Vec2(ix, iy));
                    break;
                } else {
                    break;
                }
            }
            // up left - +
            for (int ix = x - 1, iy = y + 1; ix >= 0 && iy < scenario.terrain.dimensionY; ix--, iy++) {
                if (!checkIfInBounds(x, y)) {
                    break;
                } else if (checkIfFree(ix, iy)) {
                    // free space
                    out.add(new Vec2(ix, iy));
                } else if (piece.isWhite != getPiece(ix, iy).isWhite || piece.containsTag("jumps")) {
                    // space with enemy piece
                    out.add(new Vec2(ix, iy));
                    break;
                } else {
                    break;
                }
            }
            // down right + -
            for (int ix = x + 1, iy = y - 1; ix < scenario.terrain.dimensionX && iy >= 0; ix++, iy--) {
                if (!checkIfInBounds(x, y)) {
                    break;
                } else if (checkIfFree(ix, iy)) {
                    // free space
                    out.add(new Vec2(ix, iy));
                } else if (piece.isWhite != getPiece(ix, iy).isWhite || piece.containsTag("jumps")) {
                    // space with enemy piece
                    out.add(new Vec2(ix, iy));
                    break;
                } else {
                    break;
                }
            }
            // down left - -
            for (int ix = x - 1, iy = y - 1; ix >= 0 && iy >= 0; ix--, iy--) {
                if (!checkIfInBounds(x, y)) {
                    break;
                } else if (checkIfFree(ix, iy)) {
                    // free space
                    out.add(new Vec2(ix, iy));
                } else if (piece.isWhite != getPiece(ix, iy).isWhite || piece.containsTag("jumps")) {
                    // space with enemy piece
                    out.add(new Vec2(ix, iy));
                    break;
                } else {
                    break;
                }
            }
        }

        // checking direct movement
        for (Vec2 move : piece.type.directMoves) {
            int dx = x + move.x;
            int dy = piece.isWhite ? y + move.y : y - move.y;
            if (checkIfFree(dx, dy)) {
                out.add(new Vec2(dx, dy));
            }
        }
        Controller.possibleMoves = out;
    }
    
    ArrayList<Vec2> drawLine() {
        //TODO please IMPLEMENT THIS
        // !!!!
        // !!!!
        // !!!!
        // !!!!
        return null;
    }

    boolean checkIfInBounds(int x, int y) {
        return x <= scenario.terrain.dimensionX && y <= scenario.terrain.dimensionY && x >= 0 && y >= 0;
    }

    boolean checkIfFree(int x, int y) {
        //TODO fix case when piece can be taken
        Vec2 v = new Vec2(x, y);
        if (!checkIfInBounds(x, y))
            return false;
        if (x >= scenario.terrain.dimensionX || x < 0 || y >= scenario.terrain.dimensionY || y < 0)
            return false;
        if (scenario.terrain.collisionMatrix.get(x).get(y) == 1)
            return false;
        for (Piece p : whitePieces) {
            if (p.position.equals(v)) {
                return false;
            }
        }
        for (Piece p : blackPieces) {
            if (p.position.equals(v)) {
                return false;
            }
        }
        return true;
    }
    
    Piece getPiece(int x, int y) {
        for (Piece p : whitePieces) {
            if (p.position.equals(x, y)) {
                return p;
            }
        }
        for (Piece p : blackPieces) {
            if (p.position.equals(x, y)) {
                return p;
            }
        }
        return null;
    }

    Piece getPiece(Vec2 pos) {
        for (Piece p : whitePieces) {
            if (p.position.equals(pos)) {
                return p;
            }
        }
        for (Piece p : blackPieces) {
            if (p.position.equals(pos)) {
                return p;
            }
        }
        return null;
    }

    public void move(Piece piece, Vec2 target) {
        // target is absolute
        // assume that target is legal
        // taking the piece
        if (!checkIfFree(target.x, target.y)) {
            if (piece.isWhite) {
                for (Piece blackPiece : blackPieces) {
                    if (blackPiece.position.equals(target)) {
                        blackPieces.remove(blackPiece);
                        Controller.removeGraphicPiece(blackPiece);
                        break;
                    }
                }
            } else {
                for (Piece whitePiece : whitePieces) {
                    if (whitePiece.position.equals(target)) {
                        whitePieces.remove(whitePiece);
                        Controller.removeGraphicPiece(whitePiece);
                        break;
                    }
                }
            }
            System.out.println("space isn't free, but piece couldn't be removed");
            System.out.println("check Game.move()");
        }
        history.add(new Pair<>(piece, target));
        piece.position = target;
        turn++;
    }

    void printState() {
        System.out.println("Scenario:  " + scenario.name);
        System.out.println(scenario.description);

        System.out.println("-White Pieces: ");
        for (Piece p : whitePieces) {
            System.out.print(" " + p.toPrintString());
        }
        System.out.println(".");

        System.out.println("-Black Pieces: ");
        for (Piece p : blackPieces) {
            System.out.print(" " + p.toPrintString());
        }
        System.out.println(".");
        System.out.println();
        for (String t : PieceType.pieceTypes.keySet()) {
            System.out.println(PieceType.pieceTypes.get(t).toPrintString());
        }
    }
}
