package me.snadeem.omceventscommissiontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static me.snadeem.omceventscommissiontracker.homeActivity.fadeIn;
import static me.snadeem.omceventscommissiontracker.homeActivity.initializeActionBar;
import static me.snadeem.omceventscommissiontracker.homeActivity.selected_date;
import static me.snadeem.omceventscommissiontracker.shiftActivity.COMMISSION;
import static me.snadeem.omceventscommissiontracker.shiftActivity.dollar;
import static me.snadeem.omceventscommissiontracker.shiftActivity.shift;

public class endShiftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_shift);
        final String TITLE_TEXT = "";
        initializeActionBar(getSupportActionBar(),this, TITLE_TEXT);
        fadeIn(findViewById(R.id.endShiftAcitivity));

        // Local constants
        final double commission = Double.parseDouble(shift.getString(COMMISSION, "0"));
        final String salesDate = "on " + selected_date + ".";

        TextView earnings = (TextView) findViewById(R.id.earnings);
        TextView date = (TextView) findViewById(R.id.earnedDate);
        date.setText(salesDate);
        earnings.setText(dollar.format(commission));
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, homeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
