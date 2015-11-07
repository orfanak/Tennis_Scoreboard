package com.apps.orfanak.tennisscoreboard;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



public class MainActivity extends AppCompatActivity {

    private EditText tournament;
    private String tournamentTitle;

    private EditText tournamentRound;
    private String round;

    private EditText player1;
    private String playerOne;

    private EditText player2;
    private String playerTwo;

    //seekbar for number of sets
    private SeekBar seekBarSetsNumber;
    private TextView setsNumber;
    private String stringSets;



    //Hndling FAB button
    private FloatingActionButton fabButtonStart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        setup();




        //handling start FAB button
        fabButtonStart =(FloatingActionButton) findViewById(R.id.fabButtonID);

        fabButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, FirstSet.class);

                tournamentTitle = String.valueOf(tournament.getText());
                round = String.valueOf(tournamentRound.getText());
                playerOne = String.valueOf( player1.getText());
                playerTwo = String.valueOf(player2.getText());


                //parsing data to next activity
                i.putExtra("tournament", tournamentTitle);
                i.putExtra("round", round);
                i.putExtra("player1",playerOne);
                i.putExtra("player2",playerTwo);
                i.putExtra("sets", stringSets);
                startActivity(i);

            }
        });
    }

    private void setup() {

        //Handling tournament title
        tournament = (EditText) findViewById(R.id.editextTourTitleId);


        //Handling tournament round data
        tournamentRound = (EditText) findViewById(R.id.editextTourRoundId);


        //Handling player data
        player1 = (EditText) findViewById(R.id.editextFirstPlayerId);

        player2 = (EditText) findViewById(R.id.editextSecondPlayerId);


       //Handling number of sets seekbar
        seekBarSetsNumber = (SeekBar) findViewById(R.id.setsSeekBarId);
        setsNumber = (TextView) findViewById(R.id.setsNumberId);
        stringSets =  String.valueOf(seekBarSetsNumber.getProgress());
        setsNumber.setText(getString(R.string.game_sets) + stringSets);


        seekBarSetsNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int min=1;
                int max=3;
                int step=1;
                seekBarSetsNumber.setMax((max - min) / step);

                int value = min + (progress * step);

                setsNumber.setText(getString(R.string.game_sets) + value );
                stringSets =  String.valueOf(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


        return super.onOptionsItemSelected(item);
    }
}

