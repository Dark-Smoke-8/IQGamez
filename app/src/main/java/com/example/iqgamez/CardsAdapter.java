package com.example.iqgamez;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.RecyclerViewHolder>{
    Context context;
    private ArrayList<Card> cardList;
    private OnItemClickListener listener;

    private Card firstCard = null;
    private RecyclerViewHolder firstHolder = null;
    private boolean isProcessing = false;

    public CardsAdapter(Context context, ArrayList<Card> cardsList, OnItemClickListener listener){
        this.cardList = cardsList;
        this.context=context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Card card = cardList.get(position);

        holder.imageViewCard.setImageResource(card.backImageResource);
        holder.itemView.setTag(position);
        holder.itemView.setEnabled(true);

        holder.itemView.setOnClickListener(v -> {

            if(isProcessing) return;
            int currentPosition = holder.getAbsoluteAdapterPosition();
            if(currentPosition == RecyclerView.NO_POSITION) return;

            Card selectedCard = cardList.get(currentPosition);

            if(listener != null){
                listener.onCardClick(selectedCard, currentPosition);
            }

            checkSelectedCards(selectedCard, holder, currentPosition);

        });
    }


    private void checkSelectedCards(Card selectedCard, RecyclerViewHolder holder, int position){
        holder.imageViewCard.setImageResource(selectedCard.getFrontImageResource());
        holder.itemView.setEnabled(false); //stop for clicking again

        if(firstCard == null){
            firstCard = selectedCard;
            firstHolder = holder;
        } else{
            //second card is selected will check for a match
            isProcessing = true; //stops other clicks

            if(matches(firstCard, selectedCard)){
                firstCard.setMatched(true);
                selectedCard.setMatched(true);
                if(listener!= null){
                    listener.onCardsMatched(firstCard, selectedCard);
                }

                if(allCardsMatched()){
                    if(listener!= null){
                        listener.onGameWon();
                    }
                }

                firstCard=null;
                firstHolder=null;
                isProcessing=false;
            } else{
                //No Match flip the cards back
                if(listener!= null){
                    listener.onCardsNoMatch(firstCard, selectedCard);
                }

                //flip delay
                new Handler().postDelayed(() -> {
                    if(firstHolder != null){
                        firstHolder.imageViewCard.setImageResource(firstCard.getBackImageResource());
                        firstHolder.itemView.setEnabled(true);
                    }

                    holder.imageViewCard.setImageResource(selectedCard.getBackImageResource());
                    holder.itemView.setEnabled(true);

                    firstCard = null;
                    firstHolder = null;
                    isProcessing=false;

                }, 1000); // one second delay before turning back
            }
        }
    }
    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewCard;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCard = itemView.findViewById(R.id.imageViewCard);
        }
    }

    public boolean matches(Card card,Card other){
        return card.getImageResource().equals(other.getImageResource());
    }

    private boolean allCardsMatched(){
        for(Card card: cardList){
            if(!card.isMatched()){
                return false;
            }
        }
        return true;
    }

}

