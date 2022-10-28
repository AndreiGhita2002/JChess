package jchess.ux;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jchess.game.Piece;
import jchess.util.Vec2;

public class GraphicPiece extends ImageView {
    Piece piece;
    boolean isWhite;

    void refresh() {
        Vec2 v = Controller.getPixelPos(piece.position);
        relocate(v.x, v.y);
    }

    GraphicPiece(Piece piece, boolean isWhite) {
        this.piece = piece;
        this.isWhite = isWhite;
        Image image = isWhite ? piece.type.graphicImageWhite : piece.type.graphicImageBlack;
        this.setImage(image);
        this.setFitWidth(Controller.squareSize);
        this.setFitHeight(Controller.squareSize);

        this.setOnMouseReleased(event -> {
            if (Controller.selectedPiece == null) { // if no piece is selected
                Controller.selectedPiece = this;
                Controller.game.getAllPossibleMoves(piece);
                Controller.drawPossibleMoves();
            } else if (Controller.selectedPiece == this) { // if this piece is selected
                Controller.selectedPiece = null;
                Controller.possibleMovesIcons.getChildren().clear();
                Controller.possibleMoves = null;
            } else { // if there is a selected piece
                if (Controller.possibleMoves.contains(new Vec2(piece.position.x, piece.position.y))) {
                    // if this piece can be taken by selected piece
                    Controller.movePiece(piece.position);
                } else {
                    // if selected piece can't move here
                    // select this piece instead
                    Controller.possibleMovesIcons.getChildren().clear();
                    Controller.selectedPiece = this;
                    Controller.game.getAllPossibleMoves(piece);
                    Controller.drawPossibleMoves();
                }
            }
            System.out.println(" Selected piece: " + Controller.selectedPiece);
        });
    }
}
