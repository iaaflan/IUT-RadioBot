package aflan.bot.iut.radio.radiobot;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class RadioActivity extends ActionBarActivity implements OnClickListener {

    private ProgressBar progressBar;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;

    private Button buttonPlay;

    private Button buttonStopPlay;

    private MediaPlayer player;
    public String source = "http://usa8-vn.mixstream.net:8138";
    public String iutBOT = "http://50.22.219.37:24733";
    Typeface typeface;
    TextView textView,textView1;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>RadioBot</font>"));
        textView = (TextView)findViewById(R.id.textView);
        textView1 = (TextView)findViewById(R.id.textView2);
        typeface = Typeface.createFromAsset(getAssets(),"fonts/product_sans.ttf");
        textView.setTypeface(typeface);
        textView1.setTypeface(typeface);
        initializeUIElements();
        initializeMediaPlayer();
        textView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/ifaz-ahmed-3a9b17119")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    Toast.makeText(RadioActivity.this, "Failed to open the link.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initializeUIElements() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setMax(100);
        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar1.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        buttonStopPlay = (Button) findViewById(R.id.buttonStopPlay);
        buttonStopPlay.setOnClickListener(this);

    }

    public void onClick(View view) {

        if(haveNetworkConnection())
        {
            if (view == buttonPlay) {
                try {
                    textView.setText("Streaming...");
                    startPlaying();
                } catch (Exception e) {

                }
            } else if (view == buttonStopPlay) {
                textView.setText("Radio Bot");
                stopPlaying();
                progressBar.setVisibility(View.INVISIBLE);
                progressBar1.setVisibility(View.INVISIBLE);
                progressBar2.setVisibility(View.INVISIBLE);

            }
        }
        else
        {
            if (view == buttonPlay)
            {
                textView.setText("No Internet Connection");
            }
            else if (view == buttonStopPlay) {
                textView.setText("No Internet Connection");
            }

        }

    }

    private void startPlaying() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        player.prepareAsync();
        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        progressBar1.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(iutBOT);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //progressBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (player.isPlaying()) {
            player.stop();
        }*/
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}