package idiots.com.temp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Arrays;

public class GraphActivity extends AppCompatActivity {

    private GraphView graph;
    private Double[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graph = findViewById(R.id.graph);
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
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                for(int i = 0; i < this.data.length; i++){
                    series.appendData(new DataPoint(i, this.data[i]), true, this.data.length);
                }

                // set manual X bounds
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(-150);
                graph.getViewport().setMaxY(150);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(4);
                graph.getViewport().setMaxX(80);

                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);
                graph.getViewport().setScrollable(true);
                graph.getViewport().setScrollableY(true);

                graph.addSeries(series);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void requestData(){

        this.data = new Double[100];  //TODO 현재 임시코드, 데이터 받아오는 코드 작성
        for(int i = 0; i < 100; i++) data[i] = Double.valueOf(i);

    }
}
