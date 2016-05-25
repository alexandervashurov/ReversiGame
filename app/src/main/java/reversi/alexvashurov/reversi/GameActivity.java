package reversi.alexvashurov.reversi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends Activity {

    public final static String TAG = "REVERSI_DEBUG";

    private BoardView boardView;

    private TextView scoreText;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            Log.i(TAG, "game creation started");
            setContentView(R.layout.activity_game);

            Bundle extras = getIntent().getExtras();

            boolean playVSComp = (extras != null) && extras.getBoolean("PLAY_VS_COMP", false);

            boardView = (BoardView) findViewById(R.id.board);
            scoreText = (TextView) findViewById(R.id.score_text);
            boardView.playVsComputer(playVSComp);

            showScore(2,2);

        }



    public void showScore(int whiteScore, int blackScore) {
        String text = "Stones: White " + whiteScore + " Black " + blackScore;
        scoreText.setText(text);
        scoreText.setTextColor(Color.BLACK);
        scoreText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}
