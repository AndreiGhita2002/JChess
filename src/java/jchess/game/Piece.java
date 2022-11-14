package jchess.game;

import jchess.util.Vec2;

public class Piece {
    public Vec2 position;
    public PieceType type;
    public boolean isWhite;
    public boolean moved = false;

    public String toPrintString() {
        String colour = isWhite ? "white" : "black";
        return type.getTypeName() + "{pos: " + position + ", " + colour + "}";
    }
    
    public boolean containsTag(String tag) {
        return type.tags.contains(tag);
    }

    public Piece(Vec2 position, String typeName, boolean isWhite) {
        this.position = position;
        this.isWhite = isWhite;
        this.type = PieceType.getPieceType(typeName);
    }
    
    public Piece(Vec2 position, PieceType type) {
        this.position = position;
        this.type = type;
    }
}
