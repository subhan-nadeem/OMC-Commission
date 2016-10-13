package me.snadeem.omceventscommissiontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static me.snadeem.omceventscommissiontracker.shiftActivity.COMMISSION;

public class endShiftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_shift);

        String commission = getIntent().getStringExtra(COMMISSION);
        TextView earnings = (TextView) findViewById(R.id.earnings);
        earnings.setText(commission);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, homeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
