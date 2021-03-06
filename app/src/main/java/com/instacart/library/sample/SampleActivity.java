package com.instacart.library.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.instacart.library.truetime.TrueTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SampleActivity
      extends AppCompatActivity {

    @Bind(R.id.tt_time_gmt) TextView timeGMT;
    @Bind(R.id.tt_time_pst) TextView timePST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        new InitTrueTimeAsyncTask().execute();

        ButterKnife.bind(this);
    }

    @OnClick(R.id.tt_btn_refresh)
    public void onBtnRefresh() {
        if (!TrueTime.isInitialized()) {
            Toast.makeText(this, "Sorry TrueTime not yet initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("kg", String.format(" [now: %d] [new Date: %d]", TrueTime.now().getTime(), new Date().getTime()));
        timePST.setText(_formatDate(TrueTime.now(), "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT-07:00")) +
                        " [PST]");
        timeGMT.setText(_formatDate(TrueTime.now(), "yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("GMT")) + " [GMT]");
    }

    private String _formatDate(Date date, String pattern, TimeZone timeZone) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    // a little part of me died, having to use this
    private class InitTrueTimeAsyncTask
          extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            TrueTime.build().withNtpHost("0.north-america.pool.ntp.org").withConnectionTimeout(3_1428).initialize();
            return null;
        }
    }

}
