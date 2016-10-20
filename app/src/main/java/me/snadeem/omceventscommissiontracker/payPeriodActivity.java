package me.snadeem.omceventscommissiontracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static me.snadeem.omceventscommissiontracker.homeActivity.SPACE;
import static me.snadeem.omceventscommissiontracker.homeActivity.fadeIn;
import static me.snadeem.omceventscommissiontracker.homeActivity.getMonth;
import static me.snadeem.omceventscommissiontracker.homeActivity.initializeActionBar;
import static me.snadeem.omceventscommissiontracker.shiftActivity.COMMISSION;
import static me.snadeem.omceventscommissiontracker.shiftActivity.dollar;

public class payPeriodActivity extends AppCompatActivity {

    // Global variables
    int start_year = Calendar.getInstance().get(Calendar.YEAR);
    int start_month = Calendar.getInstance().get(Calendar.MONTH);
    int start_day = Calendar.getInstance().get(Calendar.DATE);
    int end_year = start_year;
    int end_month = start_month;
    int end_day = start_day;
    String start_date;
    String end_date;
    EditText startDate;
    EditText endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_period);
        final String TITLE_TEXT = "Pay Period Calculator";
        initializeActionBar(getSupportActionBar(),this, TITLE_TEXT);

        // Initialize both date boxes to today's date
        startDate = (EditText) findViewById(R.id.dateOne);
        endDate = (EditText) findViewById(R.id.dateTwo);
        start_date = end_date = getMonth(start_month) + SPACE + start_day + ", " + start_year;

        startDate.setText(start_date);
        endDate.setText(end_date);

        // Check start date box
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog mDatePicker = new DatePickerDialog(payPeriodActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear,
                                                  int selectedmonth, int selectedday) {
                                start_year = datepicker.getYear();
                                start_day = datepicker.getDayOfMonth();
                                start_month = datepicker.getMonth();
                                start_date = getMonth(start_month) +
                                        SPACE + start_day +
                                        ", " + start_year;
                                startDate.setText(start_date);
                            }
                        }, start_year, start_month, start_day);
                mDatePicker.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog mDatePicker = new DatePickerDialog(payPeriodActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker datepicker, int selectedyear,
                                                  int selectedmonth, int selectedday) {
                                end_year = datepicker.getYear();
                                end_day = datepicker.getDayOfMonth();
                                end_month = datepicker.getMonth();
                                end_date = getMonth(end_month) +
                                        SPACE + end_day +
                                        ", " + end_year;
                                endDate.setText(end_date);
                            }
                        }, end_year, end_month, end_day);
                mDatePicker.show();
            }
        });
    }

    public void calculateStats(View view) {

        // Local constants
        final String calendar_start_date = start_year + "-" + (start_month + 1) + "-" + start_day;
        final SimpleDateFormat calendarDate = new SimpleDateFormat("yyyy-MM-dd");
        final TextView total = (TextView) findViewById(R.id.total);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.calculations);

        // Local variables
        String sharedPrefName;
        Calendar c = Calendar.getInstance();
        SharedPreferences data;
        double totalCommission = 0;

        try {
            // Set current date to the start date, as specified by user
            c.setTime(calendarDate.parse(calendar_start_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Iterate through statistics from start date to end date as specified by user
        while (true) {

            // If current date is equal to the end date, break loop
            if (c.get(Calendar.DATE) == end_day && c.get(Calendar.MONTH) == end_month
                    && c.get(Calendar.YEAR) == end_year) {
                break;
            }

            // Retrieve SharedPref and data specified to current date
            sharedPrefName = getMonth(c.get(Calendar.MONTH)) + SPACE + c.get(Calendar.DATE) + ", " +
                    c.get(Calendar.YEAR);
            data = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);

            // Add to total commission
            totalCommission += Double.parseDouble(data.getString(COMMISSION, "0"));
            c.add(Calendar.DATE, 1);
        }

        // Final data fetching for the end date
        sharedPrefName = getMonth(c.get(Calendar.MONTH)) + SPACE + c.get(Calendar.DATE) + ", " +
                c.get(Calendar.YEAR);
        data = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        totalCommission += Double.parseDouble(data.getString(COMMISSION, "0"));

        // Send commission to textview
        fadeIn(layout);
        total.setText(dollar.format(totalCommission));
    }
}
