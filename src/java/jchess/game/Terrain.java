package jchess.game;

import java.util.ArrayList;

public class Terrain {
    public int dimensionX;
    public int dimensionY;
    public String type;
    public ArrayList<ArrayList<Integer>> collisionMatrix;
    // TODO add coloured tiles
    // TODO add non rectangle shapes

    Terrain(int dimX, int dimY) {
        this.dimensionX = dimX;
        this.dimensionY = dimY;
        this.type = "empty_rectangle";

        //initializing the collision matrix with 0
        collisionMatrix = new ArrayList<>();
        for (int i = 0; i < dimensionX; i++) {
            collisionMatrix.add(new ArrayList<>());
            for (int j = 0; j < dimensionY; j++) {
                collisionMatrix.get(i).add(0);
            }
        }
    }
}
