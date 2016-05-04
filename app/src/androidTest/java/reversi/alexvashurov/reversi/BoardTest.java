package reversi.alexvashurov.reversi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

/**
 * Created by AlexVashurov on 24.03.16.
 */
public class BoardTest {
    @Ignore
    @Test
    public void firstTest() {

    }



    @Test
    public void printTest() {
        Board board = Board.initialize();
        System.out.println(board.toString());
        System.out.println(Long.toBinaryString(board.doubleBoard()));
        System.out.println(Long.toBinaryString(board.freeSquares()));
    }

//    @Test
//    public void moveTest() {
//        Board board = Board.initialize();
//        Assert.assertEquals(4, board.getAllPossibleMoves().size());
//    }

}
