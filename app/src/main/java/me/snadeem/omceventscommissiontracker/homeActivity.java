package me.snadeem.omceventscommissiontracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.text.DateFormatSymbols;
import java.util.Calendar;

// TODO Add navigation drawer to all screens
// TODO Center app logo
// TODO Add settings in drawer, enabling commission rates to be changed

public class homeActivity extends AppCompatActivity {


    public final static String SHIFT_DATE = "shiftDate";
    // Global constants
    final static String SPACE = " ";
    public static String selected_date;

    // Variable Initialization
    EditText dateBox;
    int current_year = Calendar.getInstance().get(Calendar.YEAR);
    int current_month = Calendar.getInstance().get(Calendar.MONTH);
    int current_day = Calendar.getInstance().get(Calendar.DATE);

    public static void fadeIn(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0);
        final int DURATION = 1000;
        view.animate().setDuration(DURATION).alpha(1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void fadeOut (final View view) {
        final int DURATION = 800;
        view.animate().setDuration(DURATION).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fadeIn((RelativeLayout) findViewById(R.id.homeActivity));

        initializeActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPayPeriodCalculator(view);
            }
        });

        selected_date = getMonth(current_month) + SPACE + current_day + ", " + current_year;
        // Initialize date box to current date
        dateBox = (EditText) findViewById(R.id.date);

        dateBox.setText(selected_date);

        dateBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog mDatePicker = new DatePickerDialog(homeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear,
                                                  int selectedmonth, int selectedday) {
                                current_year = datepicker.getYear();
                                current_day = datepicker.getDayOfMonth();
                                current_month = datepicker.getMonth();
                                selected_date = getMonth(current_month) +
                                        SPACE + current_day +
                                        ", " + current_year;
                                dateBox.setText(selected_date);
                            }
                        }, current_year, current_month, current_day);
                mDatePicker.show();
            }
        });
    }

    public void startShift(View view) {
        Intent intent = new Intent(this, shiftActivity.class);
        intent.putExtra(SHIFT_DATE, selected_date);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void launchPayPeriodCalculator(View view) {
        Intent intent = new Intent(this, payPeriodActivity.class);
        startActivity(intent);
    }

    public void initializeActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(true);
        bar.setIcon(R.mipmap.ic_launcher);
        bar.setDisplayShowTitleEnabled(false);
    }
}
