package com.example.iqgamez;

import static java.lang.String.valueOf;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class CardActivity extends AppCompatActivity implements OnItemClickListener {
    Button btnEasy, btnMedium, btnHard, btnPlayAgain;
    RecyclerView recyclerView;
    CardsAdapter adapter;
    ArrayList<Card> cardList;
    TextView txtMoves, txtMatches, txtTimer, txtEncore, txtEndTime, txtEndMoves, resultGame;
    private CountDownTimer gameTimer;
    View endScreenOverlay;
    private static final int GAME_TIME_SECONDS= 60;
    int moves, matches, currentDifficulty, secondsLeft;

    private final int[] images = {R.drawable.apple, R.drawable.banana, R.drawable.orange,
            R.drawable.pineapple, R.drawable.mandarin, R.drawable.strawberry, R.drawable.cherry, R.drawable.cranberry};

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
        btnEasy = findViewById((R.id.btnEasy));
        btnMedium = findViewById((R.id.btnMedium));
        btnHard = findViewById((R.id.btnHard));

        btnEasy.setOnClickListener(V-> startGame(2));
        btnMedium.setOnClickListener(v-> startGame(4));
        btnHard.setOnClickListener(v-> startGame(6));

        txtMatches=findViewById(R.id.txtMatches);
        txtMoves=findViewById(R.id.txtMoves);
        txtEndMoves=findViewById(R.id.txtEndMoves);
        txtEndTime=findViewById(R.id.txtEndTime);
        resultGame=findViewById(R.id.resultGame);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        txtTimer = findViewById(R.id.txtTimer);
        endScreenOverlay=findViewById(R.id.endScreenOverlay);
        txtEncore=endScreenOverlay.findViewById(R.id.txtMatches);
        btnPlayAgain = endScreenOverlay.findViewById(R.id.btnPlayAgain);

        btnPlayAgain.setOnClickListener(v-> {
            endScreenOverlay.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            btnEasy.setVisibility((View.VISIBLE));
            btnMedium.setVisibility(View.VISIBLE);
            btnHard.setVisibility(View.VISIBLE);

        });


    }
    private void startGame(int pairCount){
        cardList = randomSelection(pairCount);

        resultGame.setText("YOU WIN!");


        //shows the counters with moves after the difficulty is selected
        txtMatches.setVisibility(View.VISIBLE);
        txtMoves.setVisibility(View.VISIBLE);

        currentDifficulty = pairCount;
        moves = 0;
        matches = 0;
        secondsLeft = 0;
        updateStats();

        adapter = new CardsAdapter(this, cardList, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.VISIBLE);
        txtTimer.setVisibility(View.VISIBLE);
        startTimer();
        btnEasy.setVisibility(View.GONE);
        btnMedium.setVisibility(View.GONE);
        btnHard.setVisibility(View.GONE);

    }

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

            cards.add(new Card(R.drawable.card_back, image, source));
            cards.add(new Card(R.drawable.card_back, image, source));

        }
        Collections.shuffle(cards);
        return cards;
    }

    private void updateStats(){
        txtMoves.setText("Moves: " + moves);
        txtMatches.setText("Matches: " + matches + "/"+ currentDifficulty);
    }

    @Override
    public void onCardClick(Card card, int position) {
        moves++;
        updateStats();
    }

    @Override
    public void onCardsMatched(Card car1, Card card2) {
        matches++;
        updateStats();
        //sound and confetti maybe
        Toast.makeText(this, "Match!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardsNoMatch(Card card1, Card card2) {
        //can be added a sound effect of visual one
        Toast.makeText(this,"No! Try Again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameWon() {
        showEndScreen();
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
                endGame(); //end animation
            }
        }.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(gameTimer != null) gameTimer.cancel();
    }

    private void endGame(){

        if(gameTimer != null)
            gameTimer.cancel();
        showEndScreen();
    }

    private void showEndScreen(){
        endScreenOverlay.setVisibility(View.VISIBLE);
        txtTimer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        txtMoves.setVisibility(View.GONE);
        txtMatches.setVisibility(View.GONE);
        txtEndMoves.setText(valueOf(moves));
        txtEndTime.setText(valueOf(secondsLeft));

        // String result = "test";
//        String result = (txtTimer.getText().toString().contains("up"))
//                ? "Time's up!"
//                : "YOU WON!";
//        txtEncore.setText((result +"\n\nMoves: "+ moves +", \n " +currentDifficulty+ "pairs"));
        //txtEncore.setText("this is not broken");
    }


}