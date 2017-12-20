package ya.kokodoko;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class Globals extends Application {
    int iMondaiKaisu, iMondaiNumber, iseikaiNumber, irandomnum, idataNum,iPrefNum,iSoundFlag,iModoruFlag;
    SQLiteDatabase db;
    String sPrefstr;
    MediaPlayer mp1,mp2;
    ArrayList strings;
    Long lnokori;

    public void allInit() {
        //Declaration of variables
        iMondaiKaisu = 0;
        iMondaiNumber = 1;
        iseikaiNumber = 0;
        irandomnum = 0;
        iModoruFlag=0;
        idataNum = 100;
        iPrefNum=0;
        iSoundFlag=0;
        sPrefstr="";
        strings = new ArrayList();
        mp1=null;
        mp2=null;
        lnokori= 0L;


        //Set Database
        String str = "data/data/" + getPackageName() + "/Sample.db";
        db = SQLiteDatabase.openOrCreateDatabase(str, null);
        String qry0 = "DROP TABLE IF EXISTS product";
        String qry1 = "CREATE TABLE product" +
                "(id INTEGER PRIMARY KEY, mondai INTEGER, ran INTEGER, option STRING," +
                "longitude DOUBLE,latitude DOUBLE)";
        db.execSQL(qry0);
        db.execSQL(qry1);

        //Read Database
        DatabaseRead();
    }

    public void DatabaseRead() {
        String[] qry2 = new String[idataNum];
        int cnt = 0;
        // Call AssetManager
        AssetManager assetManager = this.getResources().getAssets();
        try {
            // Read CSV File
            InputStream is = assetManager.open("Mondai_Database.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            String array[] = null;

            // Read data line by line
            while ((line = bufferReader.readLine()) != null) {
                array = line.split(",");
                int mondainum = Integer.parseInt(array[1]);
                int rannum = Integer.parseInt(array[2]);
                Double longitude = Double.parseDouble(array[4]);
                Double latitude = Double.parseDouble(array[5]);
                qry2[cnt] = "INSERT INTO product(mondai, ran, option,longitude,latitude) VALUES ("
                        + mondainum + "," + rannum + ", " + "'" + array[3] + "'," + longitude + "," + latitude + ")";
                cnt++;
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < cnt; i++) {
            db.execSQL(qry2[i]);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }

    public void saveData(int seikaicnt) {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String date = year + "年" + (month + 1) + "月" + day + "日　"
                + hour + "時" + minute + "分" + second + "秒";
        sPrefstr=date+"      正解数："+String.valueOf(iseikaiNumber);

        String stringItem = pref.getString("key","")+","+sPrefstr;
        editor.putString("key",stringItem);
        editor.commit();

    }

    public String[] getData() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String stringItem = pref.getString("key","");
        if(stringItem != null && stringItem.length() != 0){
            return stringItem.split(",");
        }else{
            return null;
        }
    }

    public void ClearData() {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


}
