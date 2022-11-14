package jchess.util;

import jchess.game.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONGenerator {
    
    private static String genBishop() {
        Condition con = new Condition("any_opposite", new Vec2(0, 0));
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<DirectMove> direct = new ArrayList<>();
        ArrayList<LineMove> lineMoves = new ArrayList<>();
        lineMoves.add(new LineMove(new Vec2(1, 1), con));
        lineMoves.add(new LineMove(new Vec2(1, -1), con));
        lineMoves.add(new LineMove(new Vec2(-1, 1), con));
        lineMoves.add(new LineMove(new Vec2(-1, -1), con));
        PieceType bishop = new PieceType("bishop", tags, direct, lineMoves);
        return bishop.toString();
    }
    
    private static String genHorse() {
        Condition con = new Condition("any_opposite", new Vec2(0, 0));
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<DirectMove> direct = new ArrayList<>();
        ArrayList<LineMove> lineMoves = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int a = i % 2 == 0 ? -1 : 1;
            int b = i > 2 ? -2 : 2;
            direct.add(new DirectMove(new Vec2(a, b), con));
            direct.add(new DirectMove(new Vec2(b, a), con));
        }
        PieceType horse = new PieceType("horse", tags, direct, lineMoves);
        return horse.toString();
    }
    
    private static String genKing() {
        Condition con = new Condition("any_opposite", new Vec2(0, 0));
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<DirectMove> direct = new ArrayList<>();
        ArrayList<LineMove> lineMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int a = i < 7 && i > 2 ? 0 : 1;
            if (i < 6 && i > 3) a = -1;
            int b = i > 3 ? -1 : 1;
            if (i == 0 || i == 4) b = 0;
            direct.add(new DirectMove(new Vec2(a, b), con));
        }
        PieceType king = new PieceType("king", tags, direct, lineMoves);
        return king.toString();
    }
    
    private static String genQueen() {
        Condition con = new Condition("any_opposite", new Vec2(0, 0));
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<DirectMove> direct = new ArrayList<>();
        ArrayList<LineMove> lineMoves = new ArrayList<>();
        lineMoves.add(new LineMove(new Vec2(1, 1), con));
        lineMoves.add(new LineMove(new Vec2(1, -1), con));
        lineMoves.add(new LineMove(new Vec2(-1, 1), con));
        lineMoves.add(new LineMove(new Vec2(-1, -1), con));
        lineMoves.add(new LineMove(new Vec2(1, 0), con));
        lineMoves.add(new LineMove(new Vec2(0, 1), con));
        lineMoves.add(new LineMove(new Vec2(-1, 0), con));
        lineMoves.add(new LineMove(new Vec2(0, -1), con));
        PieceType queen = new PieceType("queen", tags, direct, lineMoves);
        return queen.toString();
    }
    
    private static String genRook() {
        Condition con = new Condition("any_opposite", new Vec2(0, 0));
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<DirectMove> direct = new ArrayList<>();
        ArrayList<LineMove> lineMoves = new ArrayList<>();
        lineMoves.add(new LineMove(new Vec2(1, 0), con));
        lineMoves.add(new LineMove(new Vec2(0, 1), con));
        lineMoves.add(new LineMove(new Vec2(-1, 0), con));
        lineMoves.add(new LineMove(new Vec2(0, -1), con));
        PieceType rook = new PieceType("rook", tags, direct, lineMoves);
        return rook.toString();
    }
    
    private static String genPawn() {
        Condition opposite = new Condition("opposite", new Vec2(0, 0));
        Condition empty = new Condition("empty", new Vec2(0, 0));
        Condition notMovedAndEmpty = new Condition("empty", new Vec2(0, 0));
        notMovedAndEmpty.tags.add("not_moved");
        ArrayList<DirectMove> direct = new ArrayList<>();
        direct.add(new DirectMove(new Vec2(0, 1), empty));
        direct.add(new DirectMove(new Vec2(0, 2), notMovedAndEmpty));
        direct.add(new DirectMove(new Vec2(-1, 1), opposite));
        direct.add(new DirectMove(new Vec2(1, 1), opposite));
        PieceType pawn = new PieceType("pawn", new ArrayList<>(), direct, new ArrayList<>());
        return pawn.toString();
    }
    
    public static void main(String[] args) {
        // init
        PieceType.doGraphics = false;
        Game game = new Game(new Scenario("nothing"));
        PieceType.tryFirstTimeInit();
        String output = "";
        
        output += genBishop() + "\n\n";
        output += genHorse()  + "\n\n";
        output += genKing()   + "\n\n";
        output += genQueen()  + "\n\n";
        output += genRook()   + "\n\n";
        output += genPawn()   + "\n\n";
        
        File file = new File("./JsonGen_output.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("file already exists");
        }
        try {
            FileWriter writer = new FileWriter(file);
            System.out.println(output);
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            System.out.println("bro");
            e.printStackTrace();
        }
    }
}
