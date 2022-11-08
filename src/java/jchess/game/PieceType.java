package jchess.game;

import javafx.scene.image.Image;
import jchess.ux.Controller;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PieceType {
    // meta data
    private static Map<String, PieceType> pieceTypes;
    private final String typeName;
    public Image graphicImageWhite;
    public Image graphicImageBlack;

    // game data
    public final ArrayList<DirectMove> directMoves;
    public final ArrayList<LineMove> lineMoves;
    public final ArrayList<String> tags;
    public final boolean checkable;

    private static boolean first = true;
    public static boolean doGraphics = true;

    public String toPrintString() {
        String out = "PieceType:" + typeName + ": \n";
        out += "  checkable: " + checkable + "\n";
        out += "  tags: " + tags + "\n";
        out += "  line moves: " + lineMoves + "\n";
        out += "  direct moves:" + directMoves + "\n";
        return out;
    }

    public String toString() {
        JSONObject j = new JSONObject();
        j.append("typeName", typeName);
        j.append("checkable", checkable);
        j.append("tags", new JSONArray(tags));
        j.append("directMoves", new JSONArray(directMoves));
        j.append("lineMoves", new JSONArray(lineMoves));
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

    public static PieceType getPieceType(String type) {
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
        // adding the "any" and "empty" pieceTypes if this is the first time this is called
        if (first) {
            first = false;
            pieceTypes.put("any", new PieceType(new JSONObject("""
                    {
                      "typeName": "any",
                      "graphicName": "any",
                      "movesHorizontally": false,
                      "movesVertically": false,
                      "movesDiagonally": false,
                      "checkable": false,
                      "tags": ["any", "abstract"],
                      "directMoves": []
                    }""")));
            pieceTypes.put("empty", new PieceType(new JSONObject("""
                    {
                      "typeName": "empty",
                      "graphicName": "empty",
                      "movesHorizontally": false,
                      "movesVertically": false,
                      "movesDiagonally": false,
                      "checkable": false,
                      "tags": ["empty", "abstract"],
                      "directMoves": []
                    }""")));
        }

        // loading the boolean properties
        this.typeName = jsonObject.getString("typeName");
        checkable = jsonObject.getBoolean("checkable");

        // loading the tags and direct moves arrays
        JSONArray tagsJSON = jsonObject.getJSONArray("tags");
        JSONArray directMovesJSON = jsonObject.getJSONArray("directMoves");
        JSONArray lineMovesJSON = jsonObject.getJSONArray("lineMoves");

        // loading tags
        tags = new ArrayList<>();
        for (Object tag : tagsJSON) {
            this.tags.add((String)tag);
        }
        // loading line moves
        lineMoves = new ArrayList<>();
        for (Object k : lineMovesJSON) {
            this.lineMoves.add(new LineMove(k.toString()));
        }
        // loading direct moves
        directMoves = new ArrayList<>();
        for (Object k : directMovesJSON) {
            this.directMoves.add(new DirectMove(k.toString()));
        }

        // loading the graphics
        if (doGraphics) {
            try {
                graphicImageWhite = new Image("graphics/" + jsonObject.getString("graphicName") + "_white.png",
                        Controller.squareSize, Controller.squareSize, false, false);
            } catch (IllegalArgumentException e) {
                System.out.println("[!!!] Graphics not found at path:");
                System.out.println("[!!!] graphics/" + jsonObject.getString("graphicName") + "_white.png");
            }
            try {
                graphicImageBlack = new Image("graphics/" + jsonObject.getString("graphicName") + "_black.png",
                        Controller.squareSize, Controller.squareSize, false, false);
            } catch (IllegalArgumentException e) {
                System.out.println("[!!!] Graphics not found at path:");
                System.out.println("[!!!] graphics/" + jsonObject.getString("graphicName") + "_black.png");
            }
        }
        pieceTypes.put(this.typeName, this);
    }

    public static Map<String, PieceType> getPieceTypes() {
        return pieceTypes;
    }

    public String getTypeName() {
        return typeName;
    }
}
