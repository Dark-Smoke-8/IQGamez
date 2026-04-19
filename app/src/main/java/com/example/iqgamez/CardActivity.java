package com.example.iqgamez;

import static java.lang.String.valueOf;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iqgamez.Card;
import com.example.iqgamez.CardsAdapter;
import com.example.iqgamez.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class CardActivity extends AppCompatActivity implements OnItemClickListener{
    RecyclerView recyclerView;
    CardsAdapter adapter;
    PrefManager prefManager;
    MediaPlayer gameSound;
    BroadcastReceiver batteryLowReceiver;
    String  difficulty;
    ArrayList<Card> cardList;
    ImageView btnBack;
    TextView txtMoves, txtMatches, txtTimer, txtEncore, txtEndTime, txtEndMoves, resultGame;
    private CountDownTimer gameTimer;
    View endScreenOverlay;
    private static final int GAME_TIME_SECONDS= 60;
    int moves = 0, matches=0, currentDifficulty, secondsLeft;

    private final int[] images = {R.drawable.card_a, R.drawable.card_b, R.drawable.card_c,
            R.drawable.card_d, R.drawable.card_m, R.drawable.card_l, R.drawable.card_g,
            R.drawable.card_q, R.drawable.card_p, R.drawable.card_w,
            R.drawable.card_z,R.drawable.card_blue_purple,R.drawable.card_hart,R.drawable.card_moon,
            R.drawable.card_peach, R.drawable.card_purple2, R.drawable.card_yellow,
            R.drawable.card_yellow_peach,R.drawable.card_sun, R.drawable.card_r,
            R.drawable.card_star};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cards);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        //initializing all needed variables
        prefManager = new PrefManager(this);

        txtMatches=findViewById(R.id.txtMatches);
        txtMoves=findViewById(R.id.txtMoves);
        txtEndMoves=findViewById(R.id.txtEndMoves);
        txtEndTime=findViewById(R.id.txtEndTime);
        resultGame=findViewById(R.id.resultGame);
        txtTimer = findViewById(R.id.txtTimer);
        btnBack=findViewById(R.id.btnBack);
        gameSound = MediaPlayer.create(this, R.raw.game_sound);
        gameSound.setLooping(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        endScreenOverlay = findViewById(R.id.endScreenOverlay);
        txtEncore = endScreenOverlay.findViewById(R.id.txtMatches);
        difficulty = getIntent().getStringExtra("difficulty");


        if("easy".equals(difficulty)){
            startGame(2);
        }else if("medium".equals(difficulty)){
            startGame(4);
        }else if("hard".equals(difficulty)){
            startGame(6);
        }


        btnBack.setOnClickListener(v -> backButtonDialog());
        endScreenOverlay=findViewById(R.id.endScreenOverlay);

        txtEncore=endScreenOverlay.findViewById(R.id.txtMatches);
    }

    //starts the game with visual fields keeping score
    //resetting all values and timer every time
    private void startGame(int pairCount){
        cardList = randomSelection(pairCount);

        //default message that is switched in case player loses
        resultGame.setText("YOU WIN!");

        txtMatches.setVisibility(View.VISIBLE);
        txtMoves.setVisibility(View.VISIBLE);

        currentDifficulty = pairCount;
        moves = 0;
        matches = 0;
        secondsLeft = 0;
        startTimer();

        updateStats();

        adapter = new CardsAdapter(this, cardList, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.VISIBLE);
        txtTimer.setVisibility(View.VISIBLE);

    }

    //creating a random selection of cards to be displayed
    //in random positions every time a level is selected
    private ArrayList<Card> randomSelection(int pairCount){
        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<Integer> shuffled = new ArrayList<>();
        for(int img: images){
            shuffled.add(img);
        }
        Collections.shuffle(shuffled);

        for(int i =0; i <pairCount; i++){
            int image = shuffled.get(i);
            String source = "img " + image;

            cards.add(new Card(R.drawable.card_back3, image, source));
            cards.add(new Card(R.drawable.card_back3, image, source));

        }
        Collections.shuffle(cards);
        return cards;
    }

    //screen message update when there is a match
    private void updateStats(){
        txtMoves.setText("Moves: " + moves);
        txtMatches.setText("Matches: " + matches + "/"+ currentDifficulty);
    }

    //updating moves when a card is selected
    @Override
    public void onCardClick(Card card, int position) {
        moves++;
        updateStats();
    }

    //updating stats when there is a match
    @Override
    public void onCardsMatched(Card car1, Card card2) {
        matches++;
        updateStats();
    }

    @Override
    public void onCardsNoMatch(Card card1, Card card2) {
        //can be added a sound effect of visual one
        Toast.makeText(this,"No! Try Again!", Toast.LENGTH_SHORT).show();
    }

    //idea for the timer from https://www.geeksforgeeks.org/android/countdowntimer-in-android-with-example/
    private void startTimer(){
        if (gameTimer != null) {gameTimer.cancel();}
        gameTimer = new CountDownTimer(GAME_TIME_SECONDS * 1000, 1000) {
            public void onTick(long millisUntilFinished){
                secondsLeft = (int) (millisUntilFinished / 1000);
                txtTimer.setText("Time: " + secondsLeft + "s");
            }
            @Override
            public void onFinish() {
                resultGame.setText("Time's up!");
                showEndScreen();
            }
        }.start();
    }

    //releasing the resources when the game is closed
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(gameTimer != null) gameTimer.cancel();
        gameSound.stop();;
        gameSound.release();

        if (batteryLowReceiver != null) {
            unregisterReceiver(batteryLowReceiver);
        }
    }

    //stopping music and timer
    @Override
    protected void onPause() {
        super.onPause();
        if (gameTimer != null) {
            gameTimer.cancel();
            gameSound.pause();
        }

        // Registering to receive Low Battery messages
        batteryLowReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (gameTimer != null) {
                    gameTimer.cancel();
                }

                if (gameSound != null && gameSound.isPlaying()) {
                    gameSound.pause();
                }

                new AlertDialog.Builder(CardActivity.this)
                        .setTitle("Low Battery!")
                        .setMessage("Your battery is low. The game has been paused. Please charge your device!")
                        .setPositiveButton("Resume", (dialog, which) -> {
                            if (gameSound != null) {
                                gameSound.start();
                            }
                            startTimer();
                        })
                        .setNegativeButton("Quit", (dialog, which) -> finish())
                        .show();
            }

        };
    }

    //starting/stopping timer, music, and receiver as needed
    @Override
    protected void onResume() {
        super.onResume();

        if (gameSound != null && !gameSound.isPlaying()) {
            gameSound.start();
        }

        batteryLowReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (gameTimer != null) {
                    gameTimer.cancel();
                }

                if (gameSound != null && gameSound.isPlaying()) {
                    gameSound.pause();
                }

                new AlertDialog.Builder(CardActivity.this)
                        .setTitle("Low Battery!")
                        .setMessage("Your battery is low. The game has been paused. Please charge your device!")
                        .setPositiveButton("Resume", (dialog, which) -> {
                            if (gameSound != null) {
                                gameSound.start();
                            }
                            startTimer();
                        })
                        .setNegativeButton("Quit", (dialog, which) -> finish())
                        .show();
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryLowReceiver, filter);

    }

    //getting the end screen with result and end animation
    @Override
    public void showEndScreen(){
        Handler handler = new Handler();

        endScreenOverlay.setVisibility(View.VISIBLE);
        txtEndMoves.setText(valueOf(moves));
        txtEndTime.setText(valueOf(secondsLeft));

        if (gameTimer != null) {gameTimer.cancel();}

        Intent intent = new Intent(CardActivity.this, ResultActivity.class);
        intent.putExtra("score", matches);
        intent.putExtra("gameName", "Card Game");
        intent.putExtra("isWin", matches == currentDifficulty);


        handler.postDelayed((new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }), GAME_TIME_SECONDS*30);

        gameSound.stop();
        gameSound.release();

    }

    //behavior of the back button while you play the game
    private void backButtonDialog() {
        gameTimer.cancel();
        if (gameSound != null) {
            gameSound.stop();
            gameSound.release();
        }
        new AlertDialog.Builder(this)
                .setMessage("You will lose your progress. Quit?")
                .setPositiveButton("Quit", (d, i) -> finish())
                .setNegativeButton("Continue", (d, i) -> startTimer())
                .show();
    }

}