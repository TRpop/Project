package com.idiots.gorail;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GraphActivity extends AppCompatActivity {

    private GraphView graph;
    //private Double[] xData, yData;
    HashMap<Integer, DataPoint> point;
    private BroadcastReceiver mReceiver;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graph = findViewById(R.id.graph);
        graph.setTitle("온도 분포");
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX) + " m";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " °C";
                }
            }
        });

        point = new HashMap<Integer, DataPoint>();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("BT_RECEIVE_CHANGED");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean stat = intent.getBooleanExtra("RECEIVING",true);
                if(!stat){
                    StringBuilder received = new StringBuilder();
                    Fifo<Double> xData = new Fifo<>();
                    Fifo<Double> yData = new Fifo<>();

                    int i, j;
                    int n = 0;

                    while(!MainActivity.q.isEmpty()){

                        if(MainActivity.q.front() != null){
                            received.append(MainActivity.q.front());
                        }
                        MainActivity.q.popFront();

                        if(n == 0){
                            i = received.indexOf("x");
                            j = received.indexOf("y");
                            n = Integer.parseInt(received.substring(received.indexOf("n")+1, i));
                        }

                        if(received.indexOf("b") > 0) {
                            String temp = received.substring(0, received.indexOf("b"));
                            i = temp.indexOf("x");
                            j = temp.indexOf("y");
                            while(j != -1) {
                                xData.append(Double.parseDouble(temp.substring(i+1, j)));
                                i = received.indexOf("x", i+1);
                                yData.append(Double.parseDouble(temp.substring(j+1, i)));
                                j = received.indexOf("y", j+1);
                            }
                            received.replace(0, received.indexOf("b")+1, "");
                        }
                    }

                    point.clear();

                    for(int k = 0; k < n; k++){
                        point.put(k, new DataPoint(xData.front(), yData.front()));
                        xData.popFront(); yData.popFront();
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

                    for(int k = 0; k < point.size(); k++){
                        series.appendData(point.get(k), true, point.size());
                    }

                    graph.removeAllSeries();

                    // set manual X bounds
                    /*
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setMinY(-150);
                    graph.getViewport().setMaxY(150);

                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(4);
                    graph.getViewport().setMaxX(80);
                    */
                    graph.getViewport().setYAxisBoundsManual(true);
                    graph.getViewport().setXAxisBoundsManual(true);

                    graph.getViewport().setScalable(true);
                    graph.getViewport().setScalableY(true);
                    graph.getViewport().setScrollable(true);
                    graph.getViewport().setScrollableY(true);

                    graph.addSeries(series);
                }
            }
        };

        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sync_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sync_menu:
                //TODO 그래프를 그리기 위한 데이터를 요청하고 받아온다.
                requestData();

                /*
                XYSeries series = new SimpleXYSeries(Arrays.asList(this.data), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "X");
                LineAndPointFormatter format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.TRANSPARENT, null);
                format.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
                plot.clear();

                // add a new series' to the xyplot:
                plot.addSeries(series, format);
                plot.redraw();
                */

                break;
            case R.id.save_menu:

                open_file(saveData());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void requestData(){
        MainActivity.mConnectedTask.write("@#");
    }

    File saveData(){

        verifyStoragePermissions(this);


        StringBuilder data = new StringBuilder();
        data.append("x,y\n");
        for(int i = 0; i < this.point.size(); i++){
            data.append(this.point.get(i).getX());
            data.append(",");
            data.append(this.point.get(i).getX());
            data.append("\n");
        }

        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ");
        String date = df.format(Calendar.getInstance().getTime());

        File path = new File(sdCardPath, "GraphData");
        File file = new File(path, date + ".csv");
        if(!path.exists()){
            path.mkdir();
        }

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                msg(file.getAbsolutePath());
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.toString().getBytes());
            fos.close();
        } catch(FileNotFoundException fnfe) {
            msg("지정된 파일을 생성할 수 없습니다.");
        } catch(IOException ioe) {
            msg("파일에 데이터를 쓸 수 없습니다.");
        }

        return file;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void open_file(File filename) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkURI = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", filename);
        intent.setDataAndType(apkURI, "application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void msg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
