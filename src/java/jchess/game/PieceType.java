package jchess.game;

import javafx.scene.image.Image;
import jchess.ux.GameScene;
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
        j.put("typeName", typeName);
        j.put("checkable", checkable);
        j.put("tags", new JSONArray(tags));
        j.put("directMoves", new JSONArray(directMoves));
        j.put("lineMoves", new JSONArray(lineMoves));
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
        if (first) firstTimeInit();

        // loading the boolean properties
        this.typeName = jsonObject.getString("typeName");
        checkable = jsonObject.getBoolean("checkable");

        // loading tags
        tags = new ArrayList<>();
        if (jsonObject.has("tags")) {
            JSONArray tagsJSON = jsonObject.getJSONArray("tags");
            for (Object tag : tagsJSON) {
                this.tags.add((String)tag);
            }
        }
        directMoves = new ArrayList<>();
        if (jsonObject.has("directMoves")) {
            JSONArray directMovesJSON = jsonObject.getJSONArray("directMoves");
            for (Object k : directMovesJSON) {
                this.directMoves.add(new DirectMove(k.toString()));
            }
        }
        lineMoves = new ArrayList<>();
        if (jsonObject.has("lineMoves")) {
            JSONArray lineMovesJSON = jsonObject.getJSONArray("lineMoves");

            for (Object k : lineMovesJSON) {
                this.lineMoves.add(new LineMove(k.toString()));
            }
        }

        if (doGraphics) {
            try {
                graphicImageWhite = new Image("graphics/" + jsonObject.getString("graphicName") + "_white.png",
                        GameScene.squareSize, GameScene.squareSize, false, false);
            } catch (IllegalArgumentException e) {
                System.out.println("[!!!] Graphics not found at path:");
                System.out.println("[!!!] graphics/" + jsonObject.getString("graphicName") + "_white.png");
            }
            try {
                graphicImageBlack = new Image("graphics/" + jsonObject.getString("graphicName") + "_black.png",
                        GameScene.squareSize, GameScene.squareSize, false, false);
            } catch (IllegalArgumentException e) {
                System.out.println("[!!!] Graphics not found at path:");
                System.out.println("[!!!] graphics/" + jsonObject.getString("graphicName") + "_black.png");
            }
        }
        pieceTypes.put(this.typeName, this);
    }

    private PieceType(String name) {
        typeName = name;
        graphicImageBlack = null;
        graphicImageWhite = null;
        directMoves = null;
        lineMoves = null;
        checkable = false;
        tags = new ArrayList<>();
        switch (name) {
            case "any" -> tags.add("any");
            case "empty" -> tags.add("empty");
            case "opposite" -> tags.add("opposite_colour");
            case "same" -> tags.add("same_colour");
        }
        tags.add("abstract");
    }
    
    private static void firstTimeInit() {
        // adding the abstract pieceTypes if this is the first time this is called
        first = false;
        pieceTypes.put("any", new PieceType("any"));
        pieceTypes.put("empty", new PieceType("empty"));
        pieceTypes.put("opposite", new PieceType("opposite"));
        pieceTypes.put("sane", new PieceType("same"));
    }
    
    public static void tryFirstTimeInit() {
        if (pieceTypes == null) pieceTypes = new HashMap<>();
        if (first) firstTimeInit();
        else System.out.println("couldn't do first time init");
    }

    public static Map<String, PieceType> getPieceTypes() {
        return pieceTypes;
    }

    public String getTypeName() {
        return typeName;
    }
}
