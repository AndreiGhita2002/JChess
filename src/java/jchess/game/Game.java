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
        Vec2 pos = piece.position;
        
        // checking horizontal movement
        if (piece.type.isMovesHorizontally()) {
            // right
            out.addAll(drawLine(piece, new Vec2(1, 0)));
            // left
            out.addAll(drawLine(piece, new Vec2(-1, 0)));
        }

        // checking vertical movement
        if (piece.type.isMovesVertically()) {
            // up (+y)
            out.addAll(drawLine(piece, new Vec2(0, 1)));
            // down (-y)
            out.addAll(drawLine(piece, new Vec2(0, -1)));
        }
        
        // checking diagonal movement
        if (piece.type.isMovesDiagonally()) {
            // up right + +
            out.addAll(drawLine(piece, new Vec2(1, 1)));
            // up left - +
            out.addAll(drawLine(piece, new Vec2(-1, 1)));
            // down right + -
            out.addAll(drawLine(piece, new Vec2(1, -1)));
            // down left - -
            out.addAll(drawLine(piece, new Vec2(-1, -1)));
        }

        // checking direct movement
        // TODO implement this using Move
        for (Vec2 move : piece.type.getDirectMoves()) {
            int dx = pos.x + move.x;
            int dy = piece.isWhite ? pos.y + move.y : pos.y - move.y;
            if (checkIfFree(dx, dy)) {
                out.add(new Vec2(dx, dy));
            }
        }
        Controller.possibleMoves = out;
    }
    
    ArrayList<Vec2> drawLine(Piece piece, Vec2 dir) {
        Vec2 pos = piece.position;
        ArrayList<Vec2> out = new ArrayList<>();
        for (int ix = pos.x + dir.x, iy = pos.y + dir.y;
             ix < scenario.terrain.dimensionX && iy < scenario.terrain.dimensionY;
             ix += dir.x, iy += dir.y) {
            if (!checkIfInBounds(ix, iy)) {
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
        return out;
    }

    boolean checkIfInBounds(int x, int y) {
        return x <= scenario.terrain.dimensionX && y <= scenario.terrain.dimensionY && x >= 0 && y >= 0;
    }

    boolean checkIfFree(int x, int y) {
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
        for (String t : PieceType.getPieceTypes().keySet()) {
            System.out.println(PieceType.getPieceTypes().get(t).toPrintString());
        }
    }
}
