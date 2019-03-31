import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class MazeGenorator {

    enum Direction {UP,RIGHT,DOWN,LEFT}
    private static char Cchar = '#';

    private static Stack<PVector> walker;
    public static ArrayList<PVector> generateMaze(int x, int y, int gridsize){

        if (x < 3 || y < 3){ return null;}

        if (x%2 ==0){x--;}
        if (y%2 ==0){y--;}
        PVector size = new PVector(x,y);
        PVector pos = new PVector(1,1);

        char[][] mazeMatrix = new char[y][x];
        mazeMatrix[1][1] = Cchar;

        walker = new Stack<>();
        walker.push(new PVector(1,1));

        while (true){
            //print(size,mazeMatrix);
            Direction[] dir = checkDirections(mazeMatrix, pos, size);

            if (dir.length > 0){
                Direction nextstep = getRandomDirection(dir,dir.length);

                if (dir.length > 1) {
                    walker.push(new PVector(pos));
                }
                step(mazeMatrix,pos,nextstep);
                pos.set(pos.addvector(directionToPVector(nextstep)));

            } else{
                pos.set(stepBack(mazeMatrix, pos, size));
                if (pos.getX() == 1 && pos.getY() == 1){
                    return convertToGridCoords(mazeMatrix,size,gridsize);
                }
            }
        }
    }

    public static ArrayList<PVector> generateMaze(PVector pos, int gridsize){return generateMaze((int)pos.getX(),(int)pos.getY(),gridsize);}

    private static ArrayList<PVector> convertToGridCoords(char[][] mat, PVector size, int gridsize){
        ArrayList<PVector> vec = new ArrayList<>();

        for(int j =0; j< size.getY();j++){
            for(int i =0; i< size.getX();i++){
                if ((mat[j][i] != Cchar || (i == size.getX() - 1 || j == size.getY() - 1)) && !(i == size.getX() - 1 && j == size.getY() - 2) && !(i == 0 && j == 1)) {
                    vec.add(new PVector(i, j).mult(gridsize));
                }
            }
        }
        return vec;
    }

    private static void print(PVector xy, char mat[][]){
        for(int j =0; j< xy.getY();j++){
            for(int i =0; i< xy.getX();i++){
                System.out.print(mat[j][i]);
            }
            System.out.print("\n");
        }
        System.out.println("\n\n");
    }

    private static PVector directionToPVector(Direction dir){
        switch (dir){
            case UP:
                return new PVector(0,-2);
            case RIGHT:
                return new PVector(2,0);
            case DOWN:
                return new PVector(0,2);
            case LEFT:
                return new PVector(-2,0);
        }
        return null;
    }

    private static Direction getRandomDirection(Direction[] directions, int size){
        int dir = (int)(Math.random()*(size));
        return directions[dir];
    }

    private static Direction[] checkDirections(char mat[][],PVector pos, PVector size){
        Direction[] directions = new Direction[4];  //todo fix
        int index = 0;

        if (pos.getY() > 1) {
            if (mat[(int) pos.getY() - 2][(int) pos.getX()] != Cchar) {
                directions[index] = Direction.UP;
                index++;
            }
        }
        if (pos.getX() < size.getX()-2) {
            if (mat[(int) pos.getY()][(int) pos.getX() + 2] != Cchar) {
                directions[index] = Direction.RIGHT;
                index++;
            }
        }
        if (pos.getY() < size.getY()-2) {
            if (mat[(int) pos.getY()+2][(int) pos.getX()] != Cchar) {
                directions[index] = Direction.DOWN;
                index++;
            }
        }
        if (pos.getX() > 1) {
            if (mat[(int) pos.getY()][(int) pos.getX() - 2] != Cchar) {
                directions[index] = Direction.LEFT;
                index++;
            }
        }
        return Arrays.copyOf(directions,index);
    }

    private static PVector stepBack(char[][] mat, PVector pos,PVector size){
        PVector newpos = new PVector(pos);
        while (checkDirections(mat,newpos,size).length == 0 && !newpos.equals(new PVector(1,1))){
            newpos.set(walker.pop());
        }
        return newpos;
    }

    private static void step(char[][] mat, PVector pos, Direction nextstep){
        switch (nextstep){
            case UP:
                mat[(int)pos.getY() - 1][(int)pos.getX()] = Cchar;
                mat[(int)pos.getY() - 2][(int)pos.getX()] = Cchar;
                break;
            case RIGHT:
                mat[(int)pos.getY()][(int)pos.getX() + 1] = Cchar;
                mat[(int)pos.getY()][(int)pos.getX() + 2] = Cchar;
                break;
            case DOWN:
                mat[(int)pos.getY() + 1][(int)pos.getX()] = Cchar;
                mat[(int)pos.getY() + 2][(int)pos.getX()] = Cchar;
                break;
            case LEFT:
                mat[(int)pos.getY()][(int)pos.getX() - 1] = Cchar;
                mat[(int)pos.getY()][(int)pos.getX() - 2] = Cchar;
                break;
        }
    }
}
