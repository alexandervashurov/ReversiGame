package reversi.alexvashurov.reversi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.textservice.TextInfo;
import android.widget.Button;

import org.w3c.dom.Text;

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


        final Button settingsButton = (Button) findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);

                }


                });

        };


    }


