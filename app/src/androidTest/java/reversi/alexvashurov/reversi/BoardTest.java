package reversi.alexvashurov.reversi;

import junit.framework.AssertionFailedError;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

/**
 * Created by AlexVashurov on 24.03.16.
 */
public class BoardTest {
    @Ignore


    @Test
    public void printTest() {
        Board board = Board.initialize();
        System.out.println(board.toString());
        System.out.println(Long.toBinaryString(board.doubleBoard()));
        System.out.println(Long.toBinaryString(board.freeSquares()));
    }

    @Test
    public void moveTest() {
        Board board = Board.initialize();
        Assert.assertEquals(4, board.getAllPossibleMoves().size());
    }
    @Test
    public void moveTest1() {
        Board board = Board.initialize();
        List<Move> moves=board.getAllPossibleMoves();
        board.makeMove(moves.get(0));
        Assert.assertEquals(3, board.getAllPossibleMoves().size());
    }

    @Test
    public void moveTest2() {
        Board board = Board.initialize();
        List<Move> moves=board.getAllPossibleMoves();
        board.makeMove(moves.get(1));
        Assert.assertNotEquals(6, board.getAllPossibleMoves().size());
    }
   @Test
    public void moveTest3() {
      Board board = Board.initialize();
       List<Move> moves = board.getAllPossibleMoves();
       board.makeMove(moves.get(0));
       board.makeMove(moves.get(1));
       board.makeMove(moves.get(2));
       List<Board.Stone> stones = board.getBoardStones();
       Assert.assertEquals(7, stones.size());
   }
    @Test
    public void moveTest4(){
        Board board = Board.initialize();
        List<Move> moves = board.getAllPossibleMoves();
        List<Board.Stone> stones = board.getBoardStones();
        board.makeMove(moves.get(0));

        Assert.assertEquals(4, stones.size());

    }


   
}
