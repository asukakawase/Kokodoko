package ya.kokodoko;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by java on 2017/09/07.
 */
public class Score extends AppCompatActivity{
    //Declaration of Variables
    ListView lv1,lv2;
    String[] str=new String[10];
    int[] num=new int[10];
   // String[] numstr=new String[10];
    Globals globals;
    Button bt1,bt2;
    ImageView iv;
    TextView tv1,tv2;

    protected void onCreate(Bundle savedInstanceState) {
        //Setting for layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Log.e(getComponentName().getClassName(), "通過確認");

        //Initialization of global variables
        globals=(Globals)this.getApplication();

        //Initialization of variables
        lv1= (ListView) findViewById(R.id.listView1);
        iv= (ImageView) findViewById(R.id.imageView_old);
        tv1= (TextView) findViewById(R.id.trash);
        tv2= (TextView) findViewById(R.id.undo);


        ArrayAdapter<String> ad1;
        String[] sDatanull={"データなし"};
        iv.setAlpha(50);

        String[] strings=globals.getData();

        if(strings!=null){
            ad1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strings);
        }else{
            ad1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sDatanull);
        }
        lv1.setAdapter(ad1);

        //Call clickliistner
        tv1.setOnClickListener(new SampleClickListner());
        tv2.setOnClickListener(new SampleClickListner());
    }

    class SampleClickListner implements  View.OnClickListener {
        public void onClick(View v) {
            if(v==tv1) {
                new AlertDialog.Builder(Score.this)
                        .setTitle("Confirmation").
                        setMessage("Are you sure to delete all data?").
                        setPositiveButton("Yes", new SampleDialogClickListner()).
                        setNegativeButton("No", null).show();
            }else if(v==tv2){
                finish();
                //Create Intent
                Intent intent = new Intent(getApplicationContext(), Main.class);
                //Startup a screen for transition
                startActivity(intent);
            }
        }
    }
    class SampleDialogClickListner implements DialogInterface.OnClickListener{
        public void onClick(DialogInterface d, int w){
            globals.ClearData();
            ArrayAdapter<String> ad1;
            String[] sDatanull={"No Data"};
            ad1=new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,sDatanull);
            lv1.setAdapter(ad1);
        }
    }
}
