package me.snadeem.omceventscommissiontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import static me.snadeem.omceventscommissiontracker.homeActivity.fadeIn;
import static me.snadeem.omceventscommissiontracker.homeActivity.fadeOut;
import static me.snadeem.omceventscommissiontracker.shiftActivity.CARD_TYPE;
import static me.snadeem.omceventscommissiontracker.shiftActivity.TYPE_CASH;
import static me.snadeem.omceventscommissiontracker.shiftActivity.TYPE_GAS;
import static me.snadeem.omceventscommissiontracker.shiftActivity.TYPE_OMC;
import static me.snadeem.omceventscommissiontracker.shiftActivity.editor;
import static me.snadeem.omceventscommissiontracker.shiftActivity.shift;

// TODO Add ability to modify all card stats

public class newCardActivity extends AppCompatActivity {

    final static String APP = "Apps";
    final static String ACTIVE = "Actives";
    final static String INS = "Ins";
    final static String INS_ACTIVE = "InsActive";
    final static String SUPP = "Supp";
    final static String SUPP_ACTIVE = "SuppActive";
    // Global variables
    public String cardType;
    CheckBox app;
    CheckBox active;
    CheckBox supp;
    CheckBox ins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_new_card);

        // Local Variables
        Intent intent = getIntent();
        cardType = intent.getStringExtra(CARD_TYPE);
        ImageView img = (ImageView) findViewById(R.id.newCardDisplay);
        ActionBar bar = getSupportActionBar();
        app = (CheckBox) findViewById(R.id.appBox);
        active = (CheckBox) findViewById(R.id.activeBox);
        supp = (CheckBox) findViewById(R.id.suppBox);
        ins = (CheckBox) findViewById(R.id.insBox);

        app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if ((app.isChecked())) {
                    fadeIn(active);
                    fadeIn(supp);
                    fadeIn(ins);
                }
                else {
                    fadeOut(active);
                    fadeOut(supp);
                    fadeOut(ins);
                }
        }});

        switch (cardType) {
            case TYPE_OMC:
                img.setImageResource(R.drawable.options_card);
                bar.setTitle("Options® MasterCard®");
                break;

            case TYPE_GAS:
                img.setImageResource(R.drawable.gas_card);
                bar.setTitle("Gas Advantage® MasterCard®");
                break;

            case TYPE_CASH:
                img.setImageResource(R.drawable.cash_card);
                bar.setTitle("Cash Advantage® MasterCard®");
                break;
        }
    }

    public void addCardTotals(View view) {

        // Local Variables
        final int numApps = shift.getInt(cardType + APP, 0);

        final int numActives = shift.getInt(cardType + ACTIVE, 0);
        final int numIns = shift.getInt(cardType + INS, 0);
        final int numInsActive = shift.getInt(cardType + INS_ACTIVE, 0);
        final int numSupp = shift.getInt(cardType + SUPP, 0);
        final int numSuppActive = shift.getInt(cardType + SUPP_ACTIVE, 0);


        if (app.isChecked()) {
            editor.putInt(cardType + APP, numApps + 1);

            if (active.isChecked()) {
                editor.putInt(cardType + ACTIVE, numActives + 1);

                if (supp.isChecked()) {
                    editor.putInt(cardType + SUPP_ACTIVE, numSuppActive + 1);
                }
                if (ins.isChecked()) {
                    editor.putInt(cardType + INS_ACTIVE, numInsActive + 1);
                }
            } else {
                if (supp.isChecked()) {
                    editor.putInt(cardType + SUPP, numSupp + 1);
                }
                if (ins.isChecked()) {
                    editor.putInt(cardType + INS, numIns + 1);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "No cards were added", Toast.LENGTH_LONG).show();
        }
        editor.commit();

        this.finish();
    }
}
