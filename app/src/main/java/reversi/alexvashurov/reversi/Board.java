package reversi.alexvashurov.reversi;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by AlexVashurov on 24.03.16.
 */
public class Board implements Cloneable {
    private final static long LEFT_MASK = -9187201950435737472L;
    private final static long RIGHT_MASK = 72340172838076673L;
    private long blackBoard;
    private long whiteBoard;
    private Player currentPlayer;
    public final static String TAG = "REVERSI_DEBUG";

    private Board(long blackBoard, long whiteBoard) {
        this.whiteBoard = whiteBoard;
        this.blackBoard = blackBoard;
        this.currentPlayer = Player.WHITE;
    }

    private Board(long blackBoard, long whiteBoard, Player currentPlayer) {
        this.blackBoard = blackBoard;
        this.whiteBoard = whiteBoard;
        this.currentPlayer = currentPlayer;
    }

    @Override
    protected Board clone(){
        return new Board(blackBoard, whiteBoard, currentPlayer);
    }

    public static Board initialize() {
        return new Board(68853694464L, 34628173824L);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Stone> getBoardStones() {
        List<Stone> stones = new ArrayList<>();
        try {
            long whiteStones = whiteBoard;
            long blackStones = blackBoard;


            while (whiteStones != 0L) {
                long stone = Long.highestOneBit(whiteStones);
                whiteStones ^= stone;
                int x = getFile(stone);
                int y = getRank(stone);

                stones.add(new Stone(Player.WHITE, x, y));

            }

            while (blackStones != 0L) {
                long stone = Long.highestOneBit(blackStones);
                blackStones ^= stone;
                int x = getFile(stone);
                int y = getRank(stone);

                stones.add(new Stone(Player.BLACK, x, y));

            }
        } catch (Exception e) {
            Log.i(TAG, "get stones " + e.toString());
        }
        return stones;
    }

    public int getRank(long position) {
        int index = Long.numberOfLeadingZeros(position);
        return (index / 8);
    }

    public int getFile(long position) {
        int index = Long.numberOfLeadingZeros(position);
        return (index % 8);
    }

    public long getPosition(int rank, int file) {
        int shift = rank * 8 + file;
        return 1L << shift;
    }

    private long getEnemyBoard(Player player) {
        if (player == Player.WHITE)
            return blackBoard;
        else
            return whiteBoard;
    }

    private long getPlayerBoard(Player player) {
        if (player == Player.BLACK)
            return blackBoard;
        else
            return whiteBoard;
    }

    public long doubleBoard() {
        return (blackBoard | whiteBoard);
    }

    public long freeSquares() {
        return ~doubleBoard();
    }

    private void changePieceAtPosition(Player player, long position) {

        if (player == Player.WHITE) {
            blackBoard ^= position;
            whiteBoard |= position;
        } else {
            blackBoard |= position;
            whiteBoard ^= position;
        }
    }

    private void setPieceAtPosition(Player player, long position) {
        if (player == Player.WHITE) {
            whiteBoard |= position;
        } else {
            blackBoard |= position;
        }
    }

    public void makeMove(Move move) {
        makeMove(currentPlayer, move);
        currentPlayer = changePlayer();
    }

    public void makeMove(Player player, Move move) {


        List<Move> piecesToTurn = new LinkedList<Move>();
        List<Move> endPoints = getAllPiecesToChange(player, move);
        piecesToTurn.addAll(endPoints);
        for (Move m : piecesToTurn) {
            changePieceAtPosition(player, m.getPosition());
        }
        setPieceAtPosition(player, move.getPosition());


    }

    public List<Move> getAllPossibleMoves() {
        return getAllPossibleMoves(currentPlayer);
    }

    public List<Move> getAllPossibleMoves(Player player) {

        List<Move> possibleMoves = new ArrayList<Move>();
        possibleMoves.addAll(movesDown(player));
        possibleMoves.addAll(movesDownLeft(player));
        possibleMoves.addAll(movesDownRight(player));
        possibleMoves.addAll(movesLeft(player));
        possibleMoves.addAll(movesRight(player));
        possibleMoves.addAll(movesUp(player));
        possibleMoves.addAll(movesUpLeft(player));
        possibleMoves.addAll(movesUpRight(player));


        return possibleMoves;
    }

    private List<Move> movesDown(Player player) {
        List<Move> downMoves = new LinkedList<Move>();

        long playerBoard = getPlayerBoard(player);

        long enemyBoard = getEnemyBoard(player);

        long potentialMoves = shiftDown(playerBoard) & enemyBoard;

        long emptyBoard = freeSquares();

        while (potentialMoves != 0) {

            long legalMoves = shiftDown(potentialMoves) & emptyBoard;

            downMoves.addAll(getLongCoordinates(legalMoves));

            potentialMoves = shiftDown(potentialMoves) & enemyBoard;
        }

        return downMoves;
    }

    private List<Move> movesDownLeft(Player player) {
        List<Move> downLeftMoves = new LinkedList<Move>();
        long potentialMoves = shiftDownLeft(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftDownLeft(potentialMoves) & emptyBoard;

            downLeftMoves.addAll(getLongCoordinates(legalMoves));

            potentialMoves = shiftDownLeft(potentialMoves) & getEnemyBoard(player);
        }
        return downLeftMoves;
    }

    private List<Move> movesDownRight(Player player) {
        LinkedList<Move> downRightMoves = new LinkedList<Move>();

        long potentialMoves = shiftDownRight(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftDownRight(potentialMoves) & emptyBoard;
            downRightMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shiftDownRight(potentialMoves) & getEnemyBoard(player);
        }
        return downRightMoves;
    }

    private List<Move> movesLeft(Player player) {
        List<Move> leftMoves = new LinkedList<Move>();
        long potentialMoves = shiftLeft(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftLeft(potentialMoves) & emptyBoard;
            leftMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shiftLeft(potentialMoves) & getEnemyBoard(player);

        }
        return leftMoves;
    }

    private List<Move> movesRight(Player player) {
        List<Move> rightMoves = new LinkedList<Move>();
        long potentialMoves = shiftRight(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftRight(potentialMoves) & emptyBoard;
            rightMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shiftRight(potentialMoves) & getEnemyBoard(player);
        }
        return rightMoves;
    }

    private List<Move> movesUp(Player player) {
        List<Move> upMoves = new LinkedList<Move>();
        long potentialMoves = shiftUp(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftUp(potentialMoves) & emptyBoard;
            upMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shiftUp(potentialMoves) & getEnemyBoard(player);
        }
        return upMoves;
    }

    private List<Move> movesUpLeft(Player player) {
        List<Move> upLeftMoves = new LinkedList<Move>();
        long potentialMoves = shiftUpLeft(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftUpLeft(potentialMoves) & emptyBoard;
            upLeftMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shiftUpLeft(potentialMoves) & getEnemyBoard(player);
        }
        return upLeftMoves;
    }

    private List<Move> movesUpRight(Player player) {
        List<Move> upRightMoves = new LinkedList<Move>();
        long potentialMoves = shiftUpRight(getPlayerBoard(player)) & getEnemyBoard(player);
        long emptyBoard = freeSquares();
        while (potentialMoves != 0) {
            long legalMoves = shiftUpRight(potentialMoves) & emptyBoard;
            upRightMoves.addAll(getLongCoordinates(legalMoves));
            potentialMoves = shiftUpRight(potentialMoves) & getEnemyBoard(player);
        }
        return upRightMoves;
    }

    private List<Move> getLongCoordinates(long legalMoves) {
        List<Move> possibleMoves = new ArrayList<Move>();
        while (legalMoves != 0L) {

            long move = Long.highestOneBit(legalMoves);

            legalMoves ^= move;


            possibleMoves.add(new Move(move));

        }

        return possibleMoves;
    }

    List<Move> getAllPiecesToChange(Player player, Move moveFrom) {
        List<Move> piecesToChange = new ArrayList<>();


        piecesToChange.addAll(getEndPointUpLeft(player, moveFrom));
        piecesToChange.addAll(getEndPointUp(player, moveFrom));
        piecesToChange.addAll(getEndPointUpRight(player, moveFrom));
        piecesToChange.addAll(getEndPointLeft(player, moveFrom));
        piecesToChange.addAll(getEndPointRight(player, moveFrom));
        piecesToChange.addAll(getEndPointDownLeft(player, moveFrom));
        piecesToChange.addAll(getEndPointDown(player, moveFrom));
        piecesToChange.addAll(getEndPointDownRight(player, moveFrom));


        return piecesToChange;
    }

    private boolean validEndPoint(Player player, long potentialEndPoint) {
        return (potentialEndPoint & getPlayerBoard(player)) == potentialEndPoint;
    }

    private List<Move> getEndPointDown(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftDown(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftDown(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointDownLeft(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftDownLeft(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftDownLeft(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointDownRight(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftDownRight(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftDownRight(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointLeft(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftLeft(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftLeft(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointRight(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftRight(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftRight(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointUp(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftUp(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftUp(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointUpLeft(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftUpLeft(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftUpLeft(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private List<Move> getEndPointUpRight(Player color, Move moveFrom) {
        long startPoint = moveFrom.getPosition();
        long potentialEndPoint = shiftUpRight(startPoint);

        List<Move> potential = new ArrayList<>();

        while (potentialEndPoint != 0) {
            if (validEndPoint(color, potentialEndPoint)) {
                return potential;
            }
            if ((potentialEndPoint & doubleBoard()) == 0L)
                return new ArrayList<Move>();

            potential.add(new Move(potentialEndPoint));
            potentialEndPoint = shiftUpRight(potentialEndPoint);
        }
        return new ArrayList<Move>();
    }

    private long shiftDown(long position) {
        return position >>> 8;
    }

    private long shiftDownLeft(long position) {
        long dlShift = position >>> 7;
        return dlShift & ~RIGHT_MASK;
    }

    private long shiftDownRight(long position) {
        long drShift = position >>> 9;
        return drShift & ~LEFT_MASK;
    }

    private long shiftLeft(long position) {
        long lShift = position << 1;
        return lShift & ~RIGHT_MASK;
    }

    private long shiftRight(long position) {
        long rShift = position >>> 1;
        return rShift & ~LEFT_MASK;
    }

    private long shiftUp(long position) {
        return position << 8;
    }

    private long shiftUpLeft(long position) {
        long ulShift = position << 9;
        return ulShift & ~RIGHT_MASK;
    }

    private long shiftUpRight(long position) {
        long urShift = position << 7L;
        return urShift & ~LEFT_MASK;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Black\n");
        builder.append(Long.toBinaryString(blackBoard));
        builder.append("\n");
        builder.append("White\n");
        builder.append(Long.toBinaryString(whiteBoard));
        builder.append("\n");
        return builder.toString();
    }

    public enum Player {
        WHITE,
        BLACK;
    }

    private Player changePlayer() {
        if (currentPlayer == Player.WHITE) return Player.BLACK;
        return Player.WHITE;
    }

    public class Stone {
        private final Player player;
        private final int x;
        private final int y;

        public Stone(Player player, int x, int y) {
            this.player = player;
            this.x = x;
            this.y = y;
        }

        public Player getPlayer() {
            return player;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Stone stone = (Stone) o;

            if (x != stone.x) return false;
            if (y != stone.y) return false;
            return player == stone.player;

        }

        @Override
        public int hashCode() {
            int result = player.hashCode();
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }

}
