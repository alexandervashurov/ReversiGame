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
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.util.List;

import static reversi.alexvashurov.reversi.Algorithm.setDEPTH;

public class SettingsActivity extends Activity {
    public final static String TAG = "REVERSI_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.i(TAG, "settings creation started");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_difficult);


        final Button easyButton = (Button) findViewById(R.id.button_Easy);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDEPTH(5);
            setContentView(R.layout.activity_main);


            }


        });
        final Button mediumButton = (Button) findViewById(R.id.button_Medium);
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDEPTH(7);
                setContentView(R.layout.activity_main);

            }


        });
        final Button hardButton = (Button) findViewById(R.id.button_Hard);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDEPTH(9);
                setContentView(R.layout.activity_main);

            }


        });

    };


    }

