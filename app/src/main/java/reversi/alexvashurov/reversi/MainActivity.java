package reversi.alexvashurov.reversi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import static reversi.alexvashurov.reversi.Algorithm.setDEPTH;
public class MainActivity extends Activity {
    public final static String TAG = "REVERSI_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.i(TAG, "main creation started");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        Button humanButton = (Button) findViewById(R.id.button_play);
        humanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    i.putExtra("PLAY_VS_COMP", false);
                    startActivity(i);
                } catch (Exception e) {
                    Log.i(TAG, "in button press " + e.toString());
                }
            }
        });

        Button compButton = (Button) findViewById(R.id.comp_button);
        compButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    i.putExtra("PLAY_VS_COMP", true);
                    startActivity(i);
                } catch (Exception e) {
                    Log.i(TAG, "in button press " + e.toString());
                }
            }
        });

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setPositiveButton("Easy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDEPTH(5);
                dialog.cancel();
            }
        });
        dialog.setNeutralButton("Medium", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDEPTH(7);
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("Hard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDEPTH(9);
                dialog.cancel();
            }
        });
        dialog.setTitle("Difficulty");
        dialog.setMessage("Choose difficulty");


        final Button settingsButton = (Button) findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }


        });

    }

}


