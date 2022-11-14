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
        return toJSON().toString();
    }
    
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("typeName", typeName);
        j.put("checkable", checkable);
        //tags
        JSONArray tagsJSON = new JSONArray();
        tags.forEach(tagsJSON::put);
        j.put("tags", tagsJSON);
        // direct moves
        JSONArray directJSON = new JSONArray();
        directMoves.forEach(e -> {directJSON.put(e.toJSON());});
        j.put("directMoves", directJSON);
        // line moves
        JSONArray lineJSON = new JSONArray();
        lineMoves.forEach(e -> {lineJSON.put(e.toJSON());});
        j.put("lineMoves", lineJSON);
        return j;
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

        // TODO optimise this for cases where most moves have the same condition
        //   so most cases
        //   (all the conditions that are the same should be referenced to the same object)
        
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

    public PieceType(String typeName, ArrayList<String> tags, ArrayList<DirectMove> directMoves, ArrayList<LineMove> lineMoves) {
        // TODO think about this
        //   shouldn't really be used i think
        //   good for testing tho
        this.typeName = typeName;
        this.tags = tags;
        this.directMoves = directMoves;
        this.lineMoves = lineMoves;
        checkable = false;
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
            case "any_opposite" -> tags.add("any_opposite");
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
        pieceTypes.put("any_opposite", new PieceType("any_opposite"));
        pieceTypes.put("empty", new PieceType("empty"));
        pieceTypes.put("opposite", new PieceType("opposite"));
        pieceTypes.put("same", new PieceType("same"));
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
