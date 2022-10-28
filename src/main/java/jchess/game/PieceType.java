package jchess.game;

import javafx.scene.image.Image;
import jchess.util.Vec2;
import jchess.ux.Controller;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PieceType {
    // meta data
    public static Map<String, PieceType> pieceTypes;
    public String typeName;
    public Image graphicImageWhite;
    public Image graphicImageBlack;

    // game data
    ArrayList<Vec2> directMoves;
    boolean movesHorizontally;
    boolean movesVertically;
    boolean movesDiagonally;
    boolean checkable;
    ArrayList<String> tags;

    //TODO make all the fields private and add getters

    public String toPrintString() {
        String out = "PieceType:" + typeName + ": \n";
        out += "  moves horizontally: " + movesHorizontally + "\n";
        out += "  moves vertically: " + movesVertically + "\n";
        out += "  moves diagonally: " + movesDiagonally + "\n";
        out += "  checkable: " + checkable + "\n";
        out += "  direct moves: " + directMoves + "\n";
        out += "  tags: " + tags + "\n";
        return out;
    }

    public String toString() {
        JSONObject j = new JSONObject();
        j.append("typeName", typeName);
        j.append("movesHorizontally", movesDiagonally);
        j.append("movesVertically", movesVertically);
        j.append("movesDiagonally", movesDiagonally);
        j.append("checkable", checkable);
        j.append("tags", new JSONArray(tags));
        j.append("directMoves", new JSONArray(directMoves));
        return j.toString();
    }

    static boolean existsFromName(String name) {
        if (pieceTypes == null) {
            return false;
        }
        for (String k : pieceTypes.keySet()) {
            if (k.equals(name)) {
                return true;
            }
        }
        return false;
    }

    static PieceType getType(String type) {
        if (existsFromName(type)) {
            return pieceTypes.get(type);
        }
        return getPieceType(type);
    }

    static PieceType getPieceType(String type) {
        if (pieceTypes != null) {
            for (String k : pieceTypes.keySet()) {
                if (type.equals(k)) {
                    return pieceTypes.get(type);
                }
            }
        } else {
            pieceTypes = new HashMap<>();
        }
        String fileString;
        String fileName = "pieces/" + type + ".json";

        InputStream inputStream = PieceType.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
                fileString = new String(inputStream.readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return new PieceType(new JSONObject(fileString));
    }

    PieceType(JSONObject jsonObject) {
        // loading the boolean properties
        this.typeName = jsonObject.getString("typeName");
        movesHorizontally = jsonObject.getBoolean("movesHorizontally");
        movesVertically = jsonObject.getBoolean("movesVertically");
        movesDiagonally = jsonObject.getBoolean("movesDiagonally");
        checkable = jsonObject.getBoolean("checkable");

        // loading the tags and direct moves arrays
        JSONArray tagsJSON = jsonObject.getJSONArray("tags");
        JSONArray directMovesJSON = jsonObject.getJSONArray("directMoves");
        tags = new ArrayList<>();
        directMoves = new ArrayList<>();
        for (Object tag : tagsJSON) {
            this.tags.add((String)tag);
        }
        for (Object k : directMovesJSON) {
            this.directMoves.add(new Vec2(k.toString()));
        }

        // loading the graphics
        try {
            graphicImageWhite = new Image("graphics/" + jsonObject.getString("graphicName") + "_white.png",
                    Controller.squareSize, Controller.squareSize, false, false);
            graphicImageBlack = new Image("graphics/" + jsonObject.getString("graphicName") + "_black.png",
                    Controller.squareSize, Controller.squareSize, false, false);
        } catch (IllegalArgumentException e) {
            System.out.println("[!!!] Graphics not found at path:");
            System.out.println("[!!!] graphics/" + jsonObject.getString("graphicName") + "_white.png");
        }
        pieceTypes.put(this.typeName, this);
    }
}
