package com.example.iqgamez;

import android.view.View;
import android.widget.ImageView;

public class Card {
    String imageResource;
    int frontImageResource;
    int backImageResource;
    private boolean isMatched;

    public Card(int backImageResource, int frontImageResource, String imageResource){
        this.backImageResource=backImageResource;
        this.frontImageResource=frontImageResource;
        this.imageResource = imageResource;
        this.isMatched = false;
    }


    String getImageResource(){
        return imageResource;
    }

    int getBackImageResource(){
        return backImageResource;
    }
    int getFrontImageResource(){
        return frontImageResource;
    }

    void setBackImageResource(int backImageResource){
        this.backImageResource = backImageResource;
    }
    boolean isMatched(){ return isMatched;}
    public void setMatched(boolean matched){
        isMatched = matched;
    }

    public void flipCard(View view) {
        ImageView card = (ImageView) view;
        card.animate().alpha(0f).setDuration(150).withEndAction(() -> {
            card.setImageResource(getFrontImageResource());
            card.animate().alpha(1f).setDuration(150).start();

        });
    }

    public void flipCardToBack(View view){
        ImageView card = (ImageView) view;
        card.animate().alpha(0f).setDuration(150).withEndAction(() -> {
            card.setImageResource(getBackImageResource());
            card.animate().alpha(1f).setDuration(150).start();
            });
    }

}
