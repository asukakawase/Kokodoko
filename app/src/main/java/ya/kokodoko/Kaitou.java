package ya.kokodoko;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.view.BootstrapTextView;

public class Kaitou extends AppCompatActivity {
    //Declaration of variables
    RadioButton radio1,radio2,radio3;
    BootstrapButton buttonanswer,buttonback,buttonnext,bt_backToStart,buttonresult;
    TextView tv1;
    BootstrapTextView tv2;
    ImageView iv1,iv2;
    RadioButton kaitouradioButton, checkedradioButton;
    Globals globals;
    BootstrapText.Builder builder;
    BootstrapText bootstrapText;
    RadioButton[] radio=new RadioButton[3];
    CountDown countDown;
    TextView timerText;
    int iendflag;
    CountDown cd;

    protected void onCreate(Bundle savedInstanceState) {
        //Setting for layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_kaitou);
        Log.e(getComponentName().getClassName(), "通過確認");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            //Retrieve Objects of each parts
            radio[0]= (RadioButton) findViewById(R.id.radioButton1);
            radio[1]= (RadioButton) findViewById(R.id.radioButton2);
            radio[2]= (RadioButton) findViewById(R.id.radioButton3);

            timerText = (TextView) findViewById(R.id.timer);
            timerText.setText("0");

            buttonanswer= (BootstrapButton) findViewById(R.id.button_answercheck);
            buttonback= (BootstrapButton) findViewById(R.id.button_back);
            buttonnext= (BootstrapButton) findViewById(R.id.button_next);
            buttonnext.setVisibility(View.INVISIBLE);
            buttonresult= (BootstrapButton) findViewById(R.id.button_result);
            buttonresult.setVisibility(View.INVISIBLE);
            tv1= (TextView) findViewById(R.id.textView);
            iv1= (ImageView) findViewById(R.id.imageView2);
            tv2= (BootstrapTextView) findViewById(R.id.Awesome);
        //    cd = null;

        //Get global variables
        globals = (Globals) this.getApplication();

        //Draw transparent background color
        iv1.setAlpha(100);

        //Initialization of variables for AwesomeTextView
        builder = new BootstrapText.Builder(this, true);
        bootstrapText = builder.build();

        //Set options for questions
        KaitouDataSet();

        //Call ClickListner for ButtonClick
        buttonanswer.setOnClickListener(new SampleClickListner());
        buttonback.setOnClickListener(new SampleClickListner());
        buttonnext.setOnClickListener(new SampleClickListner());
        buttonresult.setOnClickListener(new SampleClickListner());

        if(globals.iModoruFlag==1) {
            countDown = new CountDown(globals.lnokori, 100);
            countDown.start();

        }else{
            countDown = new CountDown(15000, 100);
            countDown.start();
        }
    }

    public void KaitouDataSet() {
        int cnt = 0;

        //Set commands in the databese
        String qry3 = "SELECT * FROM product WHERE mondai = "+ globals.iMondaiNumber;
        Cursor cr = globals.db.rawQuery(qry3, null);
            //Extract data according to conditions
            if(cr!=null) {
                while (cr.moveToNext()) {
                    int g = cr.getColumnIndex("option");
                    String str = cr.getString(g);
                    radio[cnt].setText(str);
                    cnt++;
                }
            }
        cr.close();
    }

    class SampleClickListner implements View.OnClickListener{
        public void onClick(View v) {
            if(v==buttonback) {
                countDown.cancel();
                globals.iModoruFlag=1;
                setResult(RESULT_OK);
                finish();
                //Create intent
                Intent intent = new Intent(getApplicationContext(), Mondai.class);
                //Startup a screen for transition
                startActivity(intent);
            }else if(v==buttonanswer) {
                countDown.onFinish();
            }else if(v==buttonnext) {
                finish();
                //Create intent
                Intent intent = new Intent(getApplicationContext(), Mondai.class);
                //Startup a screen for transition
                startActivity(intent);
            }else if(v==buttonresult){
                ResultDispaly();
            }
            else if(v==bt_backToStart) {
                finish();
                globals.saveData(globals.iseikaiNumber);
                globals.mp1.release();
                globals.mp2.release();
                //Create intent
                Intent intent = new Intent(getApplicationContext(), Main.class);
                //Startup a screen for transition
                startActivity(intent);
            }
        }
    }

    public void ResultDispaly() {
        setContentView(R.layout.activity_result);

        //Retrive Objects of each parts
        TextView seikai= (TextView) findViewById(R.id.seikai);
        bt_backToStart= (BootstrapButton) findViewById(R.id.button_backStart);
        iv2= (ImageView) findViewById(R.id.imageView3);

        //Draw transparent background color
        iv2.setAlpha(100);

        //Display the number of correct answers
        seikai.setText(String.valueOf(globals.iseikaiNumber));

        //Process for pressing button of "Back to Start menu"
        bt_backToStart.setOnClickListener(new SampleClickListner());

        //Close Database
        globals.db.close();
    }
    public void KaitouCheck() {
        countDown.cancel();
        globals.iModoruFlag=0;
        globals.lnokori=0L;
        timerText.setText("00");
        //Disacle the button of "Back"
        buttonback.setEnabled(false);
        //Disacle the button of "Check Answers"
        buttonanswer.setEnabled(false);
        // Get Objects for Radio group
        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
        // Get ID of checked radio button
        int id = rg.getCheckedRadioButtonId();
        checkedradioButton = (RadioButton) findViewById(id);

        //Set corect radiobutton
        kaitouradioButton=radio[globals.irandomnum-1];

        //Check answer
        if (kaitouradioButton == checkedradioButton) {
            if(globals.iSoundFlag==1){
                try {
                    globals.mp1.prepare();
                }catch (Exception e){}
                globals.mp1.start();
            }
            tv1.setText("Correct!!");
            globals.iseikaiNumber++;
            bootstrapText = builder.addFontAwesomeIcon("fa-circle-o").build();
            tv2.setBootstrapText(bootstrapText);
            bootstrapText = builder.build();
        }else {
            if(globals.iSoundFlag==1){
                try {
                    globals.mp2.prepare();
                }catch (Exception e){}
                globals.mp2.start();
            }
            tv1.setText("Wrong！！");
            bootstrapText = builder.addFontAwesomeIcon("fa-times").build();
            tv2.setBootstrapText(bootstrapText);
        }

        //increment for number of questions that are done
        globals.iMondaiKaisu++;
        globals.iMondaiNumber++;

        //Display result for number of correct answers if question is last
        if(globals.iMondaiKaisu==5){
            buttonnext.setVisibility(View.INVISIBLE);
            buttonnext.setEnabled(false);
            buttonresult.setVisibility(View.VISIBLE);
            buttonresult.setEnabled(true);
        }else{
            //Display button of 'next'
            buttonnext.setVisibility(View.VISIBLE);
            buttonnext.setEnabled(true);
        }
    }

    //A Function for countdown
    class CountDown extends CountDownTimer {
        long mm,ss,ms;
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //Process for finishing counting. Display the incorrect icon.
        public void onFinish() {
            KaitouCheck();
        }

        public void onTick(long millisUntilFinished) {
            mm= millisUntilFinished / 1000 / 60;
            ss = millisUntilFinished / 1000 % 60;
            ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            timerText.setText(String.format("%1$02d", ss));
            globals.lnokori=millisUntilFinished;
        }

    }
}
