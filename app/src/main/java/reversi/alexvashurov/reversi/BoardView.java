package reversi.alexvashurov.reversi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BoardView extends View {


    public final static String TAG = "REVERSI_DEBUG";
    private final ExecutorService engineExecutor = Executors.newSingleThreadExecutor();
    private final Runnable engineCalculatingTask = new Runnable() {
        @Override
        public void run() {

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
Log.i(TAG,"start calculating");
            Move move = Algorithm.getBetterMove(board);
            if (move == null)
                return;
            Message msg = (engineResultHandler.obtainMessage());
            Bundle bundle = new Bundle();
            bundle.putLong("calculatedMove", move.getPosition());
            msg.setData(bundle);
            engineResultHandler.sendMessage(msg);
        }
    };
    private final Handler engineResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            long calculatedMove = bundle.getLong("calculatedMove");
            Move move = new Move(calculatedMove);
            Log.i(TAG,"message handled "+move.toString());

            enterMove(move);
        }
    };

    private void enterMove(Move move) {

        if (move == null)
            return;

        isEngineCalculate = false;
        makeMove(move);
        invalidate();
        Log.i(TAG,"calculated move handled");
    }

    public boolean isEngineCalculate = false;

    private static final int SIZE = 8;
    private final Rect drawRect = new Rect();
    private final Paint paintGrid = new Paint();
    private Board board;
    private Paint myPaint = new Paint();
    private Paint stonePainter = new Paint();
    private float squareSize = 0;
    private List<Move> possibleMoves;
    private Move lastMove = null;
    private final GameActivity gameActivity;
    private boolean playVSComputer = false;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gameActivity = (GameActivity) getContext();

        try {

            board = Board.initialize();
            possibleMoves = board.getAllPossibleMoves(board.getCurrentPlayer());


        } catch (Exception e) {
            Log.i(TAG, "board constructing  " + e.toString());
        }
    }

    public void playVsComputer(boolean p) {
        playVSComputer = p;
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        try {


            int size = Math.min(xNew, yNew);
            int largestPadding = Math.max(Math.max(getPaddingBottom(), getPaddingTop()),
                    Math.max(getPaddingLeft(), getPaddingRight()));
            squareSize = (size - 2 * largestPadding) / SIZE;
        } catch (Exception e) {
            Log.i(TAG, "board size change " + e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEngineCalculate)
            return true;
        super.onTouchEvent(event);
        touchHandle(event);
        return true;
    }

    private void touchHandle(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();


        Move move = getMove(x, y);
        if (move == null) {
            return;
        }

        if (possibleMoves.contains(move)) {
            makeMove(move);
            invalidate();
            if (playVSComputer)
                computerMove();
        }
    }

    private void computerMove() {
        isEngineCalculate = true;
        engineExecutor.execute(engineCalculatingTask);
    }

    private void makeMove(Move move) {

        board.makeMove(move);
        possibleMoves = board.getAllPossibleMoves(board.getCurrentPlayer());
        lastMove = move;

        List<Board.Stone> stones = board.getBoardStones();

        int whiteStones = 0;
        int blackStones = 0;

        for (Board.Stone stone : stones) {
            if (stone.getPlayer() == Board.Player.WHITE)
                whiteStones++;
            else
                blackStones++;
        }

        gameActivity.showScore(whiteStones, blackStones);

    }

    private Move getMove(int x, int y) {

        int boardRange = getHeight();
        if (x < 0 || x > boardRange || y < 0 || y > boardRange) {
            return null;
        }
        int c = (int) (x / squareSize);
        int r = (int) (y / squareSize);

        c = 7 - c;
        r = 7 - r;
        if (c < 0 || c >= SIZE || r < 0 || r >= SIZE)
            return null;

        return new Move(board.getPosition(r, c));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        try {

            myPaint.reset();
            myPaint.setColor(Color.parseColor("#403026"));
            myPaint.setStrokeWidth(4.0f);
            myPaint.setAntiAlias(true);
            myPaint.setDither(true);


            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {

                    drawRect.set((int) (squareSize * x), (int) (squareSize * y), (int) (squareSize * (x + 1)), (int) (squareSize * (y + 1)));
                    if ((((x & 1) ^ 1) ^ (y & 1)) != 1) {
                        paintGrid.setColor(Color.parseColor("#80efebe8"));
                    } else {
                        paintGrid.setColor(Color.parseColor("#80b8c1c0"));
                    }
                    canvas.drawRect(drawRect, paintGrid);
                }
            }

            List<Board.Stone> stones = board.getBoardStones();

            if (lastMove != null) {
                int x = lastMove.getFile();
                int y = lastMove.getRank();
                drawRect.set((int) (squareSize * x),
                        (int) (squareSize * y),
                        (int) (squareSize * (x + 1)),
                        (int) (squareSize * (y + 1)));

                canvas.drawRect(drawRect, myPaint);
            }

            for (Board.Stone stone : stones) {
                if (stone.getPlayer() == Board.Player.WHITE) {
                    stonePainter.setColor(Color.WHITE);
                } else {
                    stonePainter.setColor(Color.BLACK);
                }

                canvas.drawOval((int) (squareSize * stone.getX()),
                        (int) (squareSize * stone.getY()),
                        (int) (squareSize * (stone.getX() + 1)),
                        (int) (squareSize * (stone.getY() + 1)),
                        stonePainter);


            }


        } catch (Exception e) {
            Log.i(TAG, "on board draw " + e.toString());
        }
    }
}


