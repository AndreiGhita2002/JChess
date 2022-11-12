package jchess.util;

import jchess.game.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONGenerator {
    public static void main(String[] args) {
        PieceType.doGraphics = false;
        Game game = new Game(new Scenario("nothing"));
        PieceType.tryFirstTimeInit();
        
        Vec2 oneF = new Vec2(0, 1);
        Condition cond = new Condition("any", new Vec2(0, 0));
        DirectMove m = new DirectMove(oneF, cond);
        
        System.out.println(cond.toPrintString());
        System.out.println();
        System.out.println(m.toPrintString());
        System.out.println();
        System.out.println(cond);
        System.out.println();
        System.out.println(m);
    
        File file = new File("./JsonGen_output.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("file already exists");
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("------------------\n");
            writer.write(cond.toString());
            writer.write("\n\n");
            writer.write(m.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("bro");
            e.printStackTrace();
        }
    }
}
