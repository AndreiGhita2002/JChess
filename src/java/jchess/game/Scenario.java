package jchess.game;

import jchess.util.Vec2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scenario {
    public String name;
    public String displayName;
    public String description;
    public Terrain terrain;
    public String whiteStart;
    public String blackStart;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;
    public ArrayList<MultiMove> multiMoves; //TODO properly implement this

    public Scenario(String scenariosName) {
        name = scenariosName;
        String fileString;
        String fileName = "scenarios/" + scenariosName + ".json";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
                fileString = new String(inputStream.readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        JSONObject jsonObject = new JSONObject(fileString);

        // loading the string fields
        displayName = jsonObject.getString("displayName");
        description = jsonObject.getString("description");
        whiteStart = jsonObject.getString("whiteStart");
        blackStart = jsonObject.getString("blackStart");

        // loading the terrain
        String terrainType = jsonObject.getString("terrainType");
        if (terrainType.equals("empty_rectangle")) {
            JSONArray dims = jsonObject.getJSONArray("terrain");
            int x = (int)dims.get(0);
            int y = (int)dims.get(1);
            this.terrain = new Terrain(x, y);
        } else {
            throw new IllegalArgumentException("unsupported terrain type: " + terrainType);
        }

        // loading the game pieces
        // starting with the white pieces:
        JSONObject wp = jsonObject.getJSONObject("whitePieces");
        whitePieces = new ArrayList<>();
        for (Iterator<String> it = wp.keys(); it.hasNext(); ) {
            String p = it.next();
            PieceType.getPieceType(p);

            List<Object> starts = wp.getJSONArray(p).toList();
            int x, y;
            for (int i = 0; i < starts.size(); i += 2) {
                x = (int) starts.get(i);
                y = (int) starts.get(i + 1);
                Vec2 pos = new Vec2(x, y);
                whitePieces.add(new Piece(pos, p, true));
            }
        }

        // for the black pieces:
        JSONObject bp = jsonObject.getJSONObject("blackPieces");
        blackPieces = new ArrayList<>();
        for (Iterator<String> it = bp.keys(); it.hasNext(); ) {
            String p = it.next();
            PieceType.getPieceType(p);

            List<Object> starts = bp.getJSONArray(p).toList();
            int x, y;
            for (int i = 0; i < starts.size(); i += 2) {
                x = (int) starts.get(i);
                y = (int) starts.get(i + 1);
                Vec2 pos = new Vec2(x, y);
                blackPieces.add(new Piece(pos, p, false));
            }
        }
    }
}
