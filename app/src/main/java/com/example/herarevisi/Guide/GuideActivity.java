package com.example.herarevisi.Guide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.herarevisi.R;
import com.google.android.material.card.MaterialCardView;

public class GuideActivity extends AppCompatActivity {

    CardView cardView, cardView2, cardView3, cardView4, cardView5;
    ImageView arrow, arrow2, arrow3, arrow4, arrow5;
    Group hiddenGroup, hiddenGroup2, hiddenGroup3, hiddenGroup4, hiddenGroup5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        cardView = findViewById(R.id.base_cardview1);
        cardView2 = findViewById(R.id.base_cardview2);
        cardView3 = findViewById(R.id.base_cardview3);
        cardView4 = findViewById(R.id.base_cardview4);
        cardView5 = findViewById(R.id.base_cardview5);

        arrow = findViewById(R.id.show);
        arrow2 = findViewById(R.id.show2);
        arrow3 = findViewById(R.id.show3);
        arrow4 = findViewById(R.id.show4);
        arrow5 = findViewById(R.id.show5);

        hiddenGroup = findViewById(R.id.card_group);
        hiddenGroup2 = findViewById(R.id.card_group2);
        hiddenGroup3 = findViewById(R.id.card_group3);
        hiddenGroup4 = findViewById(R.id.card_group4);
        hiddenGroup5 = findViewById(R.id.card_group5);


        arrow.setOnClickListener(view -> {
            if (hiddenGroup.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenGroup.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.down);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                hiddenGroup.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.up);
            }
        });

        arrow2.setOnClickListener(view -> {
            if (hiddenGroup2.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenGroup2.setVisibility(View.GONE);
                arrow2.setImageResource(R.drawable.down);
            } else {
                TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                hiddenGroup2.setVisibility(View.VISIBLE);
                arrow2.setImageResource(R.drawable.up);
            }
        });

        arrow3.setOnClickListener(view -> {
            if (hiddenGroup3.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(cardView3, new AutoTransition());
                hiddenGroup3.setVisibility(View.GONE);
                arrow3.setImageResource(R.drawable.down);
            } else {
                TransitionManager.beginDelayedTransition(cardView3, new AutoTransition());
                hiddenGroup3.setVisibility(View.VISIBLE);
                arrow3.setImageResource(R.drawable.up);
            }
        });

        arrow4.setOnClickListener(view -> {
            if (hiddenGroup4.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(cardView4, new AutoTransition());
                hiddenGroup4.setVisibility(View.GONE);
                arrow4.setImageResource(R.drawable.down);
            } else {
                TransitionManager.beginDelayedTransition(cardView4, new AutoTransition());
                hiddenGroup4.setVisibility(View.VISIBLE);
                arrow4.setImageResource(R.drawable.up);
            }
        });

        arrow5.setOnClickListener(view -> {
            if (hiddenGroup5.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(cardView5, new AutoTransition());
                hiddenGroup5.setVisibility(View.GONE);
                arrow5.setImageResource(R.drawable.down);
            } else {
                TransitionManager.beginDelayedTransition(cardView5, new AutoTransition());
                hiddenGroup5.setVisibility(View.VISIBLE);
                arrow5.setImageResource(R.drawable.up);
            }
        });

    }
}