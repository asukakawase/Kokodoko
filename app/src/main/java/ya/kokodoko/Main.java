package ya.kokodoko;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.view.BootstrapTextView;

public class Main extends AppCompatActivity {
 //   BootstrapButton bt;
    Button bt1,bt2;
    ImageView iv1;
    Bitmap bmp;
    Matrix m;
    Globals globals;
    TextView tv1,tv2;


    BootstrapTextView tv3,tv4;
    BootstrapText.Builder builder1,builder2;
    BootstrapText bootstrapText1,bootstrapText2;
    int cnt=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       //Setting for Layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(getComponentName().getClassName(), "通過確認");

        //Initialization of variables
        m=new Matrix();
        tv1= (TextView) findViewById(R.id.title_view);
        bt1= (Button) findViewById(R.id.Start);
        bt2= (Button) findViewById(R.id.button2);
        tv2= (TextView) findViewById(R.id.score);
        tv3= (BootstrapTextView) findViewById(R.id.volume_off);
        tv4= (BootstrapTextView) findViewById(R.id.volume_on);

        globals=(Globals)this.getApplication();
        globals.allInit();

        builder1 = new BootstrapText.Builder(this, true);
        builder2 = new BootstrapText.Builder(this, true);
        bootstrapText1 = builder1.build();
        bootstrapText1 = builder1.addFontAwesomeIcon("fa_volume_off").build();
        tv3.setBootstrapText(bootstrapText1);


        //Call clickliistner
        bt1.setOnClickListener(new SampleClickListner());
        bt2.setOnClickListener(new SampleClickListner());
        tv2.setOnClickListener(new SampleClickListner());
    }

    class SampleClickListner implements View.OnClickListener{
        public void onClick(View v) {
            if(v==bt1) {
                //Create intent
                Intent intent = new Intent(getApplicationContext(), Mondai.class);
                //Startup a screen for transition
                startActivity(intent);
            }else if(v==bt2){
                if(globals.iSoundFlag==0) {
                    globals.iSoundFlag = 1;
                    if(bootstrapText2==null) {
                        bootstrapText2=builder2.build();
                        bootstrapText2 = builder2.addFontAwesomeIcon("fa_volume_up").build();
                        tv3.setBootstrapText(bootstrapText2);
                    }else{
                        tv3.setBootstrapText(bootstrapText2);
                    }
                }else{
                    globals.iSoundFlag = 0;
                    tv3.setBootstrapText(bootstrapText1);
                }

            }else if(v==tv2){
              //  finish();
                //Create intent
                Intent intent = new Intent(getApplicationContext(), Score.class);
                //Call clickliistner
                startActivity(intent);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        globals.mp1= MediaPlayer.create(this,R.raw.correct1);
        globals.mp2= MediaPlayer.create(this,R.raw.incorrect1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
