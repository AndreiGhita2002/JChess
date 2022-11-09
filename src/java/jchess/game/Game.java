package jchess.game;

import jchess.util.Pair;
import jchess.util.Vec2;
import jchess.ux.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        
        // checking line movement
        // TODO redo all of this with proper condition checking
        for (LineMove lm : piece.type.lineMoves) {
            // inverts y if it's black
            Vec2 dir = new Vec2(lm.direction.x, piece.isWhite ? lm.direction.y : -lm.direction.y);
            out.addAll(drawLine(piece, dir, lm.condition));
        }

        // checking direct movement
        for (DirectMove dm : piece.type.directMoves) {
            int dx = pos.x + dm.displacement.x;
            int dy = piece.isWhite ? pos.y + dm.displacement.y : pos.y - dm.displacement.y;
            if (checkCondition(piece, dm)) {
                out.add(new Vec2(dx, dy));
            }
        }
        Controller.possibleMoves = out;
    }
    
    ArrayList<Vec2> drawLine(Piece piece, Vec2 dir, Condition condition) {
        // TODO implement using checkCondition()
        Vec2 pos = piece.position;
        ArrayList<Vec2> out = new ArrayList<>();
        for (int ix = pos.x + dir.x, iy = pos.y + dir.y;
             ix < scenario.terrain.dimensionX && iy < scenario.terrain.dimensionY;
             ix += dir.x, iy += dir.y) {
            if (outOfBounds(ix, iy)) {
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

    boolean outOfBounds(int x, int y) {
        return x > scenario.terrain.dimensionX || y > scenario.terrain.dimensionY || x < 0 || y < 0;
    }

    boolean checkCondition(Piece piece, Move move) {
        Condition con = move.condition;
        Vec2 pos = piece.position.add(move.condition.place);
        boolean valid;
        while (true) {
            String otherType = con.otherPiece.getTypeName();
            Piece pieceAtPos = getPiece(pos);

            // TODO what a mess, clean it up a bit!
            // testing if this condition is valid
            if (Objects.equals(otherType, "any")) {
                valid = true;
            } else if (Objects.equals(otherType, "opposite") && pieceAtPos != null
                    && pieceAtPos.isWhite != piece.isWhite) {
                valid = true;
            } else if (Objects.equals(otherType, "same") && pieceAtPos != null
                    && pieceAtPos.isWhite == piece.isWhite) {
                valid = true;
            } else if (Objects.equals(otherType, "empty") && pieceAtPos == null) {
                valid = true;
            } else if (pieceAtPos == null) {
                return false;
            } else valid = Objects.equals(otherType, pieceAtPos.type.getTypeName());
            if (!valid) return false;

            // advancing to the next condition in the chain
            if (con.next == null) break;
            con = con.next;
            pos = piece.position.add(move.condition.place);
        }
        return true;
    }

    boolean checkIfFree(int x, int y) {
        Vec2 v = new Vec2(x, y);
        if (outOfBounds(x, y))
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
