package com.example.iqgamez;

public interface OnItemClickListener {
    void onCardClick(Card card, int position);
    void onCardsMatched(Card car1, Card card2);
    void onCardsNoMatch(Card card1, Card card2);
    void onGameWon();
}
