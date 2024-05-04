package jchess.game;

import jchess.util.Pair;
import jchess.util.Vec2;
import jchess.ux.GameScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameState {
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;
    public List<Pair<Piece, Vec2>> history;
    public Scenario scenario;
    public int turn;  // should be renamed a ply (chess term)

    public GameState(Scenario scenario) {
        // loading the scenario
        this.scenario = scenario;
        whitePieces = scenario.whitePieces;
        blackPieces = scenario.blackPieces;
        turn = 0;
        history = new ArrayList<>();
    }

    public List<Vec2> getAllPossibleMoves(Piece piece) {
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
            if (checkCondition(piece, dm, pos)) {
                out.add(new Vec2(pos.x + dm.displacement.x,
                        piece.isWhite ? pos.y + dm.displacement.y : pos.y - dm.displacement.y));
            }
        }
        return out;
    }
    
    ArrayList<Vec2> drawLine(Piece piece, Vec2 dir, Condition condition) {
        // TODO do this using Vec and use checkCondition()
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
            } else break;
        }
        return out;
    }

    boolean outOfBounds(int x, int y) {
        return x > scenario.terrain.dimensionX - 1 || y > scenario.terrain.dimensionY - 1 || x < 0 || y < 0;
    }
    
    boolean outOfBounds(Vec2 v) {
        return outOfBounds(v.x, v.y);
    }

    boolean checkCondition(Piece piece, DirectMove move, Vec2 pos) {
        Condition con = move.condition;
        Vec2 dv = new Vec2(pos.x + move.displacement.x,
                piece.isWhite ? pos.y + move.displacement.y : pos.y - move.displacement.y);
        Vec2 conditionPos = dv.add(piece.isWhite ? con.place : con.place.getInverse());

        if (outOfBounds(dv)) return false;
        
        boolean valid;
        while (true) {
            String otherType = con.otherPiece;
            Piece pieceAtPos = getPiece(conditionPos);

            // TODO what a mess, clean it up a bit!
            // testing if this condition is valid
            if (Objects.equals(otherType, "any")) {
                valid = true;
            }  else if (Objects.equals(otherType, "opposite") && pieceAtPos != null
                    && pieceAtPos.isWhite != piece.isWhite) {
                valid = true;
            } else if (Objects.equals(otherType, "same") && pieceAtPos != null
                    && pieceAtPos.isWhite == piece.isWhite) {
                valid = true;
            } else if (Objects.equals(otherType, "empty") && pieceAtPos == null) {
                valid = true;
            } else if (Objects.equals(otherType, "any_opposite")) {
                if (pieceAtPos == null) {
                    valid = true;
                } else {
                    valid = pieceAtPos.isWhite != piece.isWhite;
                }
            } else if (pieceAtPos == null) {
                return false;
            } else valid = Objects.equals(otherType, pieceAtPos.type.getTypeName());
            
            if (con.tags.contains("not_moved") && piece.moved) {
                valid = false;
            }
            
            if (!valid) return false;

            // advancing to the next condition in the chain
            if (con.next == null) break;
            con = con.next;
            conditionPos = piece.position.add(con.place);
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
    
    public Pair<Boolean, Piece> checkCheckMate() {
        boolean forWhite = turn % 2 == 1;
        
        ArrayList<Piece> activePieces = forWhite ? whitePieces : blackPieces;
        ArrayList<Piece> enemyPieces = forWhite ? blackPieces : whitePieces;
        
        // dangerMask stores the tiles that the enemy threatens
        // access piece at (x, y) with dangerMask[y * dimenstionY + x]
        // false = safe for king, true = unsafe for king
        ArrayList<Boolean> dangerMask = new ArrayList<>();
        // initialing
        for (int i = 0; i < scenario.terrain.dimensionY; i++) {
            for (int j = 0; j < scenario.terrain.dimensionX; j++) {
                dangerMask.add(false);
            }
        }
        // populating
        for (Piece enemy : enemyPieces) {
            List<Vec2> moves = getAllPossibleMoves(enemy);
            for (Vec2 move : moves) {
                int i = move.y * scenario.terrain.dimensionY + move.x;
                dangerMask.set(i, true);
            }
        }
        
        // for every checkable piece
        for (Piece piece : activePieces) {
            if (piece.type.checkable) {
                // 1. check if position is threatened
                int i = piece.position.y * scenario.terrain.dimensionY + piece.position.x;
                if (!dangerMask.get(i)) {
                    // if piece is not threatened, then it cannot be in checkmate
                    continue;
                }
                // 2. check if it can move
                boolean canMove = false;
                for (Vec2 move : getAllPossibleMoves(piece)) {
                    i = move.y * scenario.terrain.dimensionY + move.x;
                    if (!dangerMask.get(i)) {
                        // there is somewhere the piece can move
                        canMove = true;
                        break;
                    }
                }
                if (!canMove) {
                    return new Pair<>(true, piece);
                }
            }
        }
        return new Pair<>(false, null);
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

    public boolean move(Piece piece, Vec2 target) {
        // target is absolute
        // assume that target is legal
        // taking the piece
        if (turn % 2 == 0 && piece.isWhite || turn % 2 == 1 && !piece.isWhite)
            return false;
        if (!checkIfFree(target.x, target.y)) {
            if (piece.isWhite) {
                for (Piece blackPiece : blackPieces) {
                    if (blackPiece.position.equals(target)) {
                        blackPieces.remove(blackPiece);
                        GameScene.removeGraphicPiece(blackPiece);
                        break;
                    }
                }
            } else {
                for (Piece whitePiece : whitePieces) {
                    if (whitePiece.position.equals(target)) {
                        whitePieces.remove(whitePiece);
                        GameScene.removeGraphicPiece(whitePiece);
                        break;
                    }
                }
            }
            System.out.println("space isn't free, but piece couldn't be removed");
            System.out.println("check Game.move()");
        }
        history.add(new Pair<>(piece, target));
        piece.position = target;
        piece.moved = true;
        turn++;
        return true;
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
