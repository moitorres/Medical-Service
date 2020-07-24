package csf.itesm.serviciomedico;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

public class Grafica extends AppCompatActivity {

    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafica);

        //Guarda la gráfica del xml en un linechart
        chart = (LineChart) findViewById(R.id.chart);
    }

    public void setData(){

        int[] data = new int[10];
        ArrayList<Integer> entries = new ArrayList<>();

        for(int i=0; i<10; i++){

        }
    }
}
