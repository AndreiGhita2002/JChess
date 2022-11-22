package jchess.ux;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jchess.game.Piece;
import jchess.util.Vec2;

public class GraphicPiece extends ImageView {
    Piece piece;
    boolean isWhite;

    void refresh() {
        Vec2 v = GameScene.getPixelPos(piece.position);
        relocate(v.x, v.y);
    }

    GraphicPiece(Piece piece, boolean isWhite) {
        this.piece = piece;
        this.isWhite = isWhite;
        Image image = isWhite ? piece.type.graphicImageWhite : piece.type.graphicImageBlack;
        this.setImage(image);
        this.setFitWidth(GameScene.squareSize);
        this.setFitHeight(GameScene.squareSize);

        this.setOnMouseReleased(event -> {
            if (GameScene.selectedPiece == null) { // if no piece is selected
                GameScene.selectedPiece = this;
                GameScene.game.getAllPossibleMoves(piece);
                GameScene.drawPossibleMoves();
            } else if (GameScene.selectedPiece == this) { // if this piece is selected
                GameScene.selectedPiece = null;
                GameScene.possibleMovesIcons.getChildren().clear();
                GameScene.possibleMoves = null;
            } else { // if there is a selected piece
                if (GameScene.possibleMoves.contains(new Vec2(piece.position.x, piece.position.y))) {
                    // if this piece can be taken by selected piece
                    GameScene.movePiece(piece.position);
                } else {
                    // if selected piece can't move here
                    // select this piece instead
                    GameScene.possibleMovesIcons.getChildren().clear();
                    GameScene.selectedPiece = this;
                    GameScene.game.getAllPossibleMoves(piece);
                    GameScene.drawPossibleMoves();
                }
            }
            System.out.println(" Selected piece: " + GameScene.selectedPiece);
        });
    }
}
