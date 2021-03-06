package com.apps.orfanak.tennisscoreboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.GregorianCalendar;


public class FirstSet extends AppCompatActivity {

    private static final  String TAG = "TennisScoreboard+" ;
    private String tournament;
    private String round;
    private String playerOne;
    private String playerTwo;
    private String sets;
    private TextView textViewTournament;
    private TextView textViewRound;
    private Button player1;
    private Button player2;
    private Button nextSetButton;
    private ImageButton replaySetButton;
    private ImageButton shareButton;
    private ImageButton cancel;
    private int numSets;
    private TextView set1player1;
    private TextView set1player2;
    private TextView pointsP1;
    private TextView pointsP2;
    private boolean setEnd=false;
    private int gamesP1 = 0;
    private int gamesP2 = 0;
    private int totalGames =6;
    int maxPoints = 7;
    private File imageFileTennis;
    private String oldPoints1;
    private String oldPoints2;
    private int oldGamesP1;
    private int oldGamesP2;
    private ImageButton tennisBallP1;
    private ImageButton tennisBallP2;
    private  String tournamentTitle;
    private int setsP1=0;
    private int setsP2=0;
    private AlertDialog.Builder dialog;
    private boolean tiebreak=false;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_set);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            tournament = extras.getString("tournament");
            round = extras.getString("round");
            playerOne = extras.getString("player1");
            playerTwo = extras.getString("player2");
            sets = extras.getString("sets");

        }

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3283484166219147/3009830131");

        setUpPlaySet();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                if(setEnd){

                    checkEndMatch();
                }
                else {
                    shareImage();
                }
            }
        });

        requestNewInterstitial();


        tennisBallP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tennisBallP1.setAlpha((float) 1.0);
                tennisBallP2.setVisibility(View.INVISIBLE);
                tennisBallP1.setEnabled(false);
                tennisBallP2.setEnabled(false);
                player1.setEnabled(true);
                player2.setEnabled(true);
            }
        });

        tennisBallP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tennisBallP2.setAlpha((float) 1.0);
                tennisBallP1.setVisibility(View.INVISIBLE);
                tennisBallP1.setEnabled(false);
                tennisBallP2.setEnabled(false);
                player1.setEnabled(true);
                player2.setEnabled(true);
            }
        });

        player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (gamesP1 == 6 && gamesP2 == 6) {
                tiebreak=true;
                   gainPointP1TieBreak();

                } else {
                    gainPointP1();

                }
            }
        });

        player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (gamesP1==6&&gamesP2==6) {
                    tiebreak=true;
                   gainPointP2TieBreak();
                }
                else{
                    gainPointP2();

                }
            }
        });



        replaySetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(FirstSet.this);

                //set Title
                dialog.setTitle(R.string.replay_set_title);

                //set message
                dialog.setMessage(R.string.replay_set_quest);

                //set cancelable
                dialog.setCancelable(false);

                // set an icon
                dialog.setIcon(android.R.drawable.btn_star_big_on);

                //Toast.makeText(getApplicationContext(),"It will restart the app",Toast.LENGTH_LONG).show();

                //se Positive button
                dialog.setPositiveButton(getResources().getString(R.string.positive_button),

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                gamesP1=0;
                                gamesP2=0;
                                pointsP1.setText("0");
                                pointsP2.setText("0");
                                maxPoints=7;
                                totalGames=6;
                                set1player1.setText("0");
                                set1player2.setText("0");
                                setEnd=false;
                                setUpPlaySet();
                                player1.setEnabled(true);
                                player2.setEnabled(true);
                                tennisBallP1.setEnabled(true);
                                tennisBallP1.setVisibility(View.VISIBLE);
                                tennisBallP2.setEnabled(true);
                                tennisBallP2.setVisibility(View.VISIBLE);
                                nextSetButton.setVisibility(View.INVISIBLE);
                                cancel.setEnabled(true);
                                cancel.setVisibility(View.VISIBLE);

                            }
                        });

                // set negative button
                dialog.setNegativeButton(getResources().getString(R.string.negative_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                // create dialog
                AlertDialog alertD1 = dialog.create();

                //show dialog
                alertD1.show();


            }







        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }else {
                    shareImage();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tiebreak) {
                    pointsP1.setText(oldPoints1);
                    pointsP2.setText(oldPoints2);
                } else {
                    if (pointsP1.getText().toString().equals("Ad") || pointsP2.getText().toString().equals("Ad")) {
                        Log.i(TAG, "Handling Ad case");
                    } else {

                        String currentPointsP1 = String.valueOf(pointsP1.getText());
                        String currentPointsP2 = String.valueOf(pointsP2.getText());

                        int pointsPlayer1 = Integer.valueOf(currentPointsP1);
                        int pointsPlayer2 = Integer.valueOf(currentPointsP2);


                        if (pointsPlayer1 == 0 && pointsPlayer2 == 0 || setEnd) {
                            revertServe();
                        }

                        pointsP1.setText(oldPoints1);
                        pointsP2.setText(oldPoints2);
                        gamesP1 = oldGamesP1;
                        gamesP2 = oldGamesP2;
                        set1player1.setText(String.valueOf(oldGamesP1));
                        set1player2.setText(String.valueOf(oldGamesP2));


                        if (setEnd) {
                            setEnd = false;
                            player1.setEnabled(true);
                            player2.setEnabled(true);
                        }
                    }

                }
            }
        });


    }


    private void gainPointP2() {


        // reading current points
        String currentPointsP1 = String.valueOf(pointsP1.getText());
        String currentPointsP2 = String.valueOf(pointsP2.getText());
        oldPoints1 = currentPointsP1;
        oldPoints2 = currentPointsP2;
        oldGamesP1=gamesP1;
        oldGamesP2=gamesP2;



        if(currentPointsP2.equals("0")){
            pointsP2.setText("15");
        }
        else if(currentPointsP2.equals("15")){
            pointsP2.setText("30");
        }
        else if(currentPointsP2.equals("30")){
            pointsP2.setText("40");
        }
        else if(currentPointsP2.equals("40") && currentPointsP1.equals("40")){
            pointsP2.setText("Ad");
        }
        else if(currentPointsP2.equals("40") && currentPointsP1.equals("Ad")){
            pointsP1.setText("40");
            pointsP2.setText("40");
        }
        else
        {
            pointsP1.setText("0");
            pointsP2.setText("0");

            gamesP2++;
            gainGame(set1player2, gamesP2);
        }

    }

    private void gainPointP1() {


        // reading current points
        String currentPointsP1 = String.valueOf(pointsP1.getText());
        String currentPointsP2 = String.valueOf(pointsP2.getText());
        oldPoints1 = currentPointsP1;
        oldPoints2 = currentPointsP2;
        oldGamesP1=gamesP1;
        oldGamesP2=gamesP2;



        if(currentPointsP1.equals("0")){
            pointsP1.setText("15");
        }
        else if(currentPointsP1.equals("15")){
            pointsP1.setText("30");
        }
        else if(currentPointsP1.equals("30")){
            pointsP1.setText("40");
        }
        else if(currentPointsP1.equals("40") && currentPointsP2.equals("40")){
            pointsP1.setText("Ad");
        }
        else if(currentPointsP1.equals("40") && currentPointsP2.equals("Ad")){
            pointsP1.setText("40");
            pointsP2.setText("40");
        }
        else
        {
            pointsP1.setText("0");
            pointsP2.setText("0");

            gamesP1++;
            gainGame(set1player1, gamesP1);
        }


    }

    private void gainGame(TextView gamesPlayer, int games) {

        gamesPlayer.setText(String.valueOf(games));

        checkEndOfSet();

        if(tennisBallP1.getVisibility()== View.VISIBLE){
            tennisBallP1.setVisibility(View.INVISIBLE);
            tennisBallP2.setVisibility(View.VISIBLE);
            tennisBallP1.setAlpha((float) 1);
            tennisBallP2.setAlpha((float) 1);
        }
        else if(tennisBallP2.getVisibility()==View.VISIBLE){
            tennisBallP2.setVisibility(View.INVISIBLE);
            tennisBallP1.setVisibility((View.VISIBLE));
            tennisBallP1.setAlpha((float) 1);
            tennisBallP2.setAlpha((float) 1);
        }
    }

    private void checkEndOfSet(){


        if(gamesP1==totalGames && gamesP2<totalGames - 1){
            setEnd = true;
            setsP1=1;
            setsP2=0;

            Toast.makeText(getApplicationContext(),"Set winner is " + String.valueOf(player1.getText()),Toast.LENGTH_LONG).show();
            player1.setEnabled(false);
            player2.setEnabled(false);
            nextSet();
        }
        else if(gamesP2==totalGames && gamesP1<totalGames - 1){
            setEnd = true;
            setsP1=0;
            setsP2=1;
            Toast.makeText(getApplicationContext(),"Set winner is " + String.valueOf(player2.getText()),Toast.LENGTH_LONG).show();
            player1.setEnabled(false);
            player2.setEnabled(false);
            nextSet();
        }
        else if(gamesP1==totalGames - 1 && gamesP2==totalGames - 1){
            // Toast.makeText(getApplicationContext(),"Tie Break",Toast.LENGTH_LONG).show();
            setEnd=false;
            totalGames = 7;
        }

        if(setEnd)
        {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
               checkEndMatch();
            }
        }

        checkEndMatch();

    }

    private void checkEndMatch() {

        if (setEnd && numSets==1){
            nextSetButton.setVisibility(View.GONE);
            nextSetButton.setEnabled(false);
            tennisBallP1.setVisibility(View.GONE);
            tennisBallP2.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(),"End of Match",Toast.LENGTH_LONG).show();

            pointsP1.setVisibility(View.GONE);
            pointsP2.setVisibility(View.GONE);

        }


    }


    private void gainPointP1TieBreak(){
        // reading current points in tie break
        String currentPointsP1 = String.valueOf(pointsP1.getText());
        String currentPointsP2 = String.valueOf(pointsP2.getText());

        oldPoints1 = currentPointsP1;
        oldPoints2 = currentPointsP2;

        int pointsPlayer1 = Integer.valueOf(currentPointsP1);
        int pointsPlayer2 = Integer.valueOf(currentPointsP2);




        if(pointsPlayer1 == maxPoints-1 && pointsPlayer2 == maxPoints - 1){
            maxPoints++;
        }
        if(pointsPlayer1 == maxPoints-1 && pointsPlayer2< maxPoints-1 ){
            pointsPlayer1++;
            pointsP1.setText(String.valueOf(pointsPlayer1));
            setEnd = true;
            nextSet();
            Toast.makeText(getApplicationContext(),"Set winner is " + String.valueOf(player1.getText()),Toast.LENGTH_LONG).show();
            player1.setEnabled(false);
            player2.setEnabled(false);
            gamesP1++;
            gainGame(set1player1, gamesP1);
           // checkEndMatch();
        } else {
            pointsPlayer1++;
            pointsP1.setText(String.valueOf(pointsPlayer1));
        }

        serveTieBreak();

    }

    private void gainPointP2TieBreak(){
        // reading current points
        String currentPointsP1 = String.valueOf(pointsP1.getText());
        String currentPointsP2 = String.valueOf(pointsP2.getText());

        oldPoints1 = currentPointsP1;
        oldPoints2 = currentPointsP2;

        int pointsPlayer1 = Integer.valueOf(currentPointsP1);
        int pointsPlayer2 = Integer.valueOf(currentPointsP2);


        if(pointsPlayer1 == maxPoints-1 && pointsPlayer2 == maxPoints - 1){
            maxPoints++;
        }
        if(pointsPlayer2 == maxPoints-1 && pointsPlayer1< maxPoints-1 ){
            pointsPlayer2++;
            pointsP2.setText(String.valueOf(pointsPlayer2));
            setEnd = true;
            nextSet();
            Toast.makeText(getApplicationContext(),"Set winner is " + String.valueOf(player2.getText()),Toast.LENGTH_LONG).show();
            player1.setEnabled(false);
            player2.setEnabled(false);
            gamesP2++;
            gainGame(set1player2, gamesP2);
           // checkEndMatch();
        }

        else {
            pointsPlayer2++;
            pointsP2.setText(String.valueOf(pointsPlayer2));
        }


        serveTieBreak();
    }

    private void serveTieBreak() {

        // reading current points in tie break
        String currentPointsP1 = String.valueOf(pointsP1.getText());
        String currentPointsP2 = String.valueOf(pointsP2.getText());

        int pointsPlayer1 = Integer.valueOf(currentPointsP1);
        int pointsPlayer2 = Integer.valueOf(currentPointsP2);

        int sumPoints = pointsPlayer1 + pointsPlayer2;

        if((sumPoints==1||sumPoints==5||sumPoints==9||sumPoints==13||sumPoints==17||sumPoints==21||sumPoints==25||sumPoints==29) && tennisBallP1.getVisibility()==View.VISIBLE) {
            tennisBallP1.setVisibility(View.INVISIBLE);
            tennisBallP2.setVisibility(View.VISIBLE);

        }
        else if((sumPoints==3||sumPoints==7||sumPoints==11||sumPoints==15||sumPoints==19||sumPoints==23||sumPoints==27||sumPoints==31) && tennisBallP1.getVisibility()==View.INVISIBLE){
            tennisBallP1.setVisibility(View.VISIBLE);
            tennisBallP2.setVisibility(View.INVISIBLE);
        }

        else if((sumPoints==1||sumPoints==5||sumPoints==9||sumPoints==13) && tennisBallP2.getVisibility()==View.VISIBLE){
            tennisBallP2.setVisibility(View.INVISIBLE);
            tennisBallP1.setVisibility(View.VISIBLE);

        }
        else if((sumPoints==3||sumPoints==7||sumPoints==11||sumPoints==15) && tennisBallP2.getVisibility()==View.INVISIBLE){
            tennisBallP2.setVisibility(View.VISIBLE);
            tennisBallP1.setVisibility(View.INVISIBLE);
        }



    }


    private void setUpPlaySet() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        //initializing textviews
        textViewTournament = (TextView) findViewById(R.id.textViewTitleID);
        textViewTournament.setText(tournament);

        textViewRound = (TextView) findViewById(R.id.textViewRoundID);
        textViewRound.setText(round);

        //initializing player button
        player1 = (Button) findViewById(R.id.buttonPlayer1);
        player1.setText(playerOne);
        player1.setTextColor(Color.WHITE);

        player2 =(Button) findViewById(R.id.buttonPlayer2);
        player2.setText(playerTwo);
        player2.setTextColor(Color.WHITE);


        //initializing other buttons
        nextSetButton = (Button) findViewById(R.id.buttonNextSetID);
        replaySetButton = (ImageButton) findViewById(R.id.buttonReplaySet);
        shareButton = (ImageButton) findViewById(R.id.buttonShareID);
        cancel = (ImageButton) findViewById(R.id.cancelbuttonID);

        nextSetButton.setVisibility(View.GONE);
        nextSetButton.setEnabled(true);


        //storing data for sets and supertiebreak points
        numSets = Integer.valueOf(sets);

        //setting up textviews for set1
        set1player1 = (TextView) findViewById(R.id.textViewSet1P1Id);
        set1player2 = (TextView) findViewById(R.id.textViewSet1P2Id);


        //setting up textviews for player points
        pointsP1 = (TextView) findViewById(R.id.pointsP1);
        pointsP2 = (TextView) findViewById(R.id.pointsP2);



        // setting up set status
        //setEnd = false;

        //setting up buttons
        player1.setEnabled(false);
        player2.setEnabled(false);

        //initializing ball icons for serving
        tennisBallP1 = (ImageButton) findViewById(R.id.serveP1ID);
        tennisBallP2 = (ImageButton) findViewById(R.id.serveP2ID);
        tennisBallP1.setEnabled(true);
        tennisBallP2.setEnabled(true);

        //Toast.makeText(getApplicationContext(), R.string.message_first_serve, Toast.LENGTH_LONG).show();


    }

    //share screenshot image
    public void shareImage(){

        // image naming and path  to include sd card  appending name you choose for file
        String mPath = Environment.getExternalStorageDirectory().toString() + "/scrShotTennis.jpeg";

       // create bitmap screen capture
        Bitmap bitmap;
        // View v1= findViewById(R.id.image);
        View v2 = getWindow().getDecorView().getRootView();

        v2.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v2.getDrawingCache());
        v2.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        imageFileTennis = new File(mPath);

        try {
            fout = new FileOutputStream(imageFileTennis);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //sharing
        Uri uri = Uri.fromFile(new File(mPath));
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void nextSet(){

        if(setEnd){
            nextSetButton.setVisibility(View.VISIBLE);
            nextSetButton.setEnabled(true);

            cancel.setEnabled(false);
            cancel.setVisibility(View.VISIBLE);

            nextSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                        Intent i = new Intent(FirstSet.this, SecondSet.class);
                        tournamentTitle = String.valueOf(textViewTournament.getText());
                        round = String.valueOf(textViewRound.getText());
                        playerOne = String.valueOf(player1.getText());
                        playerTwo = String.valueOf(player2.getText());
                        String resultSet1P1 = String.valueOf(set1player1.getText());
                        String resultSet1P2 = String.valueOf(set1player2.getText());


                        //parsing data to next activity
                        i.putExtra("tournament", tournamentTitle);
                        i.putExtra("round", round);
                        i.putExtra("player1", playerOne);
                        i.putExtra("player2", playerTwo);
                        i.putExtra("sets", sets);
                        i.putExtra("set1P1", resultSet1P1);
                        i.putExtra("set1P2", resultSet1P2);
                        i.putExtra("setsP1", setsP1);
                        i.putExtra("setsP2", setsP2);

                        startActivity(i);
                }

            });

        }
        else
        {
            nextSetButton.setVisibility(View.INVISIBLE);
        }

    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        textViewTournament.setText(savedInstanceState.getString("tournament"));
        textViewRound.setText(savedInstanceState.getString("round"));

        player1.setText(savedInstanceState.getString("player1"));
        player2.setText(savedInstanceState.getString("player2"));

        set1player1.setText(savedInstanceState.getString("set1player1"));
        set1player2.setText(savedInstanceState.getString("set1player2"));
        pointsP1.setText(savedInstanceState.getString("pointsP1"));
        pointsP2.setText(savedInstanceState.getString("pointsP2"));
        gamesP1 = savedInstanceState.getInt ("gamesP1");
        gamesP2 = savedInstanceState.getInt("gamesP2");
        tennisBallP1.setAlpha(savedInstanceState.getFloat("alphaTennisBallP1"));
        tennisBallP2.setAlpha(savedInstanceState.getFloat("alphaTennisBallP2"));
        tennisBallP1.setEnabled(savedInstanceState.getBoolean("booleanP1"));
        tennisBallP2.setEnabled(savedInstanceState.getBoolean("booleanP2"));


        //handling visibility of tennis balls that indicate who serves
        if(savedInstanceState.getInt("tennisBallP1Vis")==View.VISIBLE){
            tennisBallP1.setVisibility(View.VISIBLE);
        }
        else {tennisBallP1.setVisibility(View.INVISIBLE);}

        if(savedInstanceState.getInt("tennisBallP2Vis")==View.VISIBLE){
            tennisBallP2.setVisibility(View.VISIBLE);
        }
        else {tennisBallP2.setVisibility(View.INVISIBLE);}


        player1.setEnabled(savedInstanceState.getBoolean("player1en"));
        player2.setEnabled(savedInstanceState.getBoolean("player2en"));

        setEnd = savedInstanceState.getBoolean("setEnd");

        if(setEnd){
            nextSet();
        }

        super.onRestoreInstanceState(savedInstanceState);
        // Read values from the "savedInstanceState"-object and put them in your textview
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        outState.putString("tournament", textViewTournament.getText().toString());
        outState.putString("round", textViewRound.getText().toString());
        outState.putString("player1", player1.getText().toString());
        outState.putString("player2", player2.getText().toString());
        outState.putString("set1player1", set1player1.getText().toString());
        outState.putString("set1player2", set1player2.getText().toString());
        outState.putString("pointsP1", pointsP1.getText().toString());
        outState.putString("pointsP2", pointsP2.getText().toString());
        outState.putInt("gamesP1", gamesP1);
        outState.putInt("gamesP2", gamesP2);
        outState.putFloat("alphaTennisBallP1", tennisBallP1.getAlpha());
        outState.putFloat("alphaTennisBallP2", tennisBallP2.getAlpha());
        outState.putBoolean("booleanP1", tennisBallP1.isEnabled());
        outState.putBoolean("booleanP2", tennisBallP2.isEnabled());
        outState.putInt("tennisBallP1Vis", tennisBallP1.getVisibility());
        outState.putInt("tennisBallP2Vis", tennisBallP2.getVisibility());
        outState.putBoolean("player1en", player1.isEnabled());
        outState.putBoolean("player2en", player2.isEnabled());
        outState.putBoolean("setEnd", setEnd);




        // Save the values you need from your textview into "outState"-object
        super.onSaveInstanceState(outState);
    }




  public void startNewGame(){

      dialog = new AlertDialog.Builder(this);

      //set Title
      dialog.setTitle(R.string.start_new_title);

              //set message
              dialog.setMessage(R.string.start_new_question);

      //set cancelable
      dialog.setCancelable(false);

      // set an icon
      dialog.setIcon(android.R.drawable.btn_star_big_on);

      //Toast.makeText(getApplicationContext(),"It will restart the app",Toast.LENGTH_LONG).show();

      //se Positive button
      dialog.setPositiveButton(getResources().getString(R.string.positive_button),

              new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                      Intent i = new Intent(FirstSet.this, MainActivity.class);

                      startActivity(i);


                  }
              });


      // set negative button
      dialog.setNegativeButton(getResources().getString(R.string.negative_button),
              new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {


                      dialog.cancel();
                  }
              });

      // create dialog
      AlertDialog alertD1 = dialog.create();

      //show dialog
      alertD1.show();


  }


    public void revertServe(){
        if(tennisBallP1.getVisibility()== View.VISIBLE && tennisBallP2.getVisibility() == View.INVISIBLE){
            tennisBallP1.setVisibility(View.INVISIBLE);
            tennisBallP2.setVisibility(View.VISIBLE);
        }
        else if(tennisBallP1.getVisibility()== View.INVISIBLE && tennisBallP2.getVisibility() == View.VISIBLE){
            tennisBallP1.setVisibility(View.VISIBLE);
            tennisBallP2.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_restart){

            startNewGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
