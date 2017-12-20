package ya.kokodoko;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

/**
 * Created by java on 2017/08/31.
 */
public class Mondai extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    //Declaration of variables
    private GoogleMap mMap;
    LatLng lMondai;
    Intent it;
    Globals globals;
    Double dlongitude,dlatitude;
    FrameLayout frameLayout;
    SupportMapFragment mapFragment;


    protected void onCreate(Bundle savedInstanceState) {
        //Setting for layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mondai);
        Log.e(getComponentName().getClassName(), "通過確認");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initialization of variables
        BootstrapButton bt2= findViewById(R.id.button);
        frameLayout=findViewById(R.id.framelayout);
        it=getIntent();
        globals = (Globals) this.getApplication();

        //Generate ramdom number
        if(globals.iModoruFlag==0){
            Random rn = new Random();
            globals.irandomnum = rn.nextInt(3) + 1;
        }

        //Set longitude latitude of question
        MondaiDataSet();

        //Get MapFragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bt2.setOnClickListener(new SampleClickListner());
    }

    public void MondaiDataSet() {
        //Set conditions of database
        String qry3 = "SELECT * FROM product WHERE mondai = "+ globals.iMondaiNumber+" AND ran= "+globals.irandomnum;
        Cursor cr = globals.db.rawQuery(qry3, null);
            //Extract data according to conditions
            if(cr!=null) {
                while (cr.moveToNext()) {
                    int g = cr.getColumnIndex("longitude");
                    int h = cr.getColumnIndex("latitude");
                    dlongitude = cr.getDouble(g);
                    dlatitude = cr.getDouble(h);
                }
                lMondai = new LatLng(dlongitude, dlatitude);
            }
        cr.close();

    }

    public void onMapReady(GoogleMap googleMap) {
        //Declaration of variables
        mMap = googleMap;
        //processes for display of the map
        googleMap.addMarker(new MarkerOptions().position(lMondai).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lMondai));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.setIndoorEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
    class SampleClickListner implements View.OnClickListener {
       public void onClick(View v) {
           mapFragment.onStop();
           finish();
           Intent intent = new Intent(getApplicationContext(), Kaitou.class);
           startActivity(intent);
        }
    }
    @Override
    public void onMapLoaded() {

    }
}
