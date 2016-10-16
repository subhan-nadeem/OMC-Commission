package me.snadeem.omceventscommissiontracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import static me.snadeem.omceventscommissiontracker.homeActivity.SHIFT_DATE;
import static me.snadeem.omceventscommissiontracker.newCardActivity.INS;
import static me.snadeem.omceventscommissiontracker.newCardActivity.INS_ACTIVE;
import static me.snadeem.omceventscommissiontracker.newCardActivity.SUPP;
import static me.snadeem.omceventscommissiontracker.newCardActivity.SUPP_ACTIVE;

public class shiftActivity extends AppCompatActivity {


    // TODO Add commission breakdown screen to navigation drawer
    // TODO Make stats appear only when not zero

    // Global constants
    public static final DecimalFormat dollar = new DecimalFormat("$#.##");
    public static final String CARD_TYPE = "cardType";
    public static final String TYPE_OMC = "omc";
    public static final String TYPE_GAS = "gas";
    public static final String TYPE_CASH = "cash";
    public static final String OMC_APPS = "omcApps";
    public static final String OMC_ACTIVES = "omcActives";
    public static final String GAS_APPS = "gasApps";
    public static final String GAS_ACTIVES = "gasActives";
    public static final String CASH_APPS = "cashApps";
    public static final String CASH_ACTIVES = "cashActives";
    public static final String COMMISSION = "commission";
    // Global variables
    public static SharedPreferences shift;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setIcon(R.mipmap.ic_launcher);
        bar.setDisplayShowTitleEnabled(false);

        // Open SharedPreferences of current shift
        Intent intent = getIntent();
        String shiftDate = intent.getStringExtra(SHIFT_DATE);
        shift = getSharedPreferences(shiftDate, Context.MODE_PRIVATE);
        editor = shift.edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRatios();
        setCards();
    }

    public void addCard(View view) {
        Intent intent = new Intent(this, newCardActivity.class);

        switch (view.getId()) {
            case R.id.options:
                intent.putExtra(CARD_TYPE, TYPE_OMC);
                break;
            case R.id.gas:
                intent.putExtra(CARD_TYPE, TYPE_GAS);
                break;
            case R.id.cash:
                intent.putExtra(CARD_TYPE, TYPE_CASH);
                break;
        }
        startActivity(intent);
    }

    public void setCards() {

        int omcAppsNum = shift.getInt(OMC_APPS, 0);
        int omcActivesNum = shift.getInt(OMC_ACTIVES, 0);
        int gasAppsNum = shift.getInt(GAS_APPS, 0);
        int gasActivesNum = shift.getInt(GAS_APPS, 0);
        int cashAppsNum = shift.getInt(CASH_APPS, 0);
        int cashActivesNum = shift.getInt(CASH_ACTIVES, 0);
        double commission = calculateCommission();

        TextView commissionView = (TextView) findViewById(R.id.commissionNum);
        TextView omcApps = (TextView) findViewById(R.id.omcAppsNum);
        TextView omcActives = (TextView) findViewById(R.id.omcActivesNum);
        TextView gasApps = (TextView) findViewById(R.id.gasAppsNum);
        TextView gasActives = (TextView) findViewById(R.id.gasActivesNum);
        TextView cashApps = (TextView) findViewById(R.id.cashAppsNum);
        TextView cashActives = (TextView) findViewById(R.id.cashActivesNum);

        omcApps.setText(String.valueOf(omcAppsNum));
        omcActives.setText(String.valueOf(omcActivesNum));
        gasApps.setText(String.valueOf(gasAppsNum));
        gasActives.setText(String.valueOf(gasActivesNum));
        cashApps.setText(String.valueOf(cashAppsNum));
        cashActives.setText(String.valueOf(cashActivesNum));
        editor.putString(COMMISSION, String.valueOf(commission));
        editor.commit();

        commissionView.setText(dollar.format(commission));
    }

    public void setRatios() {

        // Local constants
        final DecimalFormat pct = new DecimalFormat("#%");
        final double TEN_PERCENT = 0.1;
        final double TWENTY_PERCENT = 0.2;
        final double FIFTY_PERCENT = 0.5;
        final double SIXTY_PERCENT = 0.6;
        final TextView suppRatioView = (TextView) findViewById(R.id.suppNum);
        final TextView insRatioView = (TextView) findViewById(R.id.insNum);
        final TextView cardRatioView = (TextView) findViewById(R.id.cardNum);

        double suppRatio = calculateSuppRatio();
        double insRatio = calculateInsRatio();
        double cardRatio = calculateCardRatio();

        if (suppRatio < TEN_PERCENT) {
            suppRatioView.setTextColor(getResources().getColor(R.color.red));
        } else if (suppRatio < TWENTY_PERCENT) {
            suppRatioView.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            suppRatioView.setTextColor(getResources().getColor(R.color.green));
        }
        suppRatioView.setText(pct.format(suppRatio));

        if (insRatio < TEN_PERCENT) {
            insRatioView.setTextColor(getResources().getColor(R.color.red));
        } else if (insRatio < TWENTY_PERCENT) {
            insRatioView.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            insRatioView.setTextColor(getResources().getColor(R.color.green));
        }
        insRatioView.setText(pct.format(insRatio));

        if (cardRatio < FIFTY_PERCENT) {
            cardRatioView.setTextColor(getResources().getColor(R.color.red));
        }
        else if (cardRatio < SIXTY_PERCENT) {
            cardRatioView.setTextColor(getResources().getColor(R.color.yellow));
        }
        else {
            cardRatioView.setTextColor(getResources().getColor(R.color.green));
        }
        cardRatioView.setText(pct.format(cardRatio));

    }

    public double calculateCommission() {
        final int OMC_COMMISSION = 21;
        final int GAS_COMMISSION = 11;
        final int CASH_COMMISSION = 11;
        final int SUPP_COMMISSION = 2;
        final int INS_COMMISSION = 3;
        double commission = 0;

        commission = OMC_COMMISSION * shift.getInt(OMC_ACTIVES, 0)
                + GAS_COMMISSION * shift.getInt(GAS_ACTIVES, 0)
                + CASH_COMMISSION * shift.getInt(CASH_ACTIVES, 0)
                + SUPP_COMMISSION * (shift.getInt(TYPE_OMC + SUPP_ACTIVE, 0)
                + shift.getInt(TYPE_GAS + SUPP_ACTIVE, 0) + shift.getInt(TYPE_CASH + SUPP_ACTIVE, 0))
                + INS_COMMISSION * (shift.getInt(TYPE_OMC + INS_ACTIVE, 0)
                + shift.getInt(TYPE_GAS + INS_ACTIVE, 0) + shift.getInt(TYPE_CASH + INS_ACTIVE, 0));
        return commission;
    }

    public double calculateSuppRatio() {
        double ratio = 0;
        double apps = shift.getInt(OMC_APPS, 0) + shift.getInt(GAS_APPS, 0)
                + shift.getInt(CASH_APPS, 0);
        double supps = shift.getInt(TYPE_OMC + SUPP, 0) + shift.getInt(TYPE_OMC + SUPP_ACTIVE, 0)
                + shift.getInt(TYPE_GAS + SUPP, 0) + shift.getInt(TYPE_GAS + SUPP_ACTIVE, 0)
                + shift.getInt(TYPE_CASH + SUPP, 0) + shift.getInt(TYPE_CASH + SUPP_ACTIVE, 0);
        if (apps != 0) {
            ratio = supps / apps;
            return ratio;
        }
        return 0;
    }

    public double calculateInsRatio() {
        double ratio = 0;
        double apps = shift.getInt(OMC_APPS, 0) + shift.getInt(GAS_APPS, 0)
                + shift.getInt(CASH_APPS, 0);
        double ins = shift.getInt(TYPE_OMC + INS, 0) + shift.getInt(TYPE_OMC + INS_ACTIVE, 0)
                + shift.getInt(TYPE_GAS + INS, 0) + shift.getInt(TYPE_GAS + INS_ACTIVE, 0)
                + shift.getInt(TYPE_CASH + INS, 0) + shift.getInt(TYPE_CASH + INS_ACTIVE, 0);
        if (apps != 0) {
            ratio = ins / apps;
            return ratio;
        }
        return 0;
    }

    public double calculateCardRatio() {
        double ratio = 0;
        double omcApps = shift.getInt(OMC_APPS, 0);
        double otherApps = shift.getInt(GAS_APPS,0) + shift.getInt(CASH_APPS,0);
        double totalApps = omcApps+otherApps;

        if (totalApps!=0){
            return omcApps/totalApps;
        }
        return 0;
    }

    public void endShift(View view) {
        Intent intent = new Intent(this, endShiftActivity.class);
        startActivity(intent);
    }

}
