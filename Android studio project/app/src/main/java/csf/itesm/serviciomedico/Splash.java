package csf.itesm.serviciomedico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private long tiempoDeEspera = 3000; //milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Cuando el timer se termina, la actividad se cambia con un intent
        TimerTask cambiarPantalla = new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent intent1 = new Intent().setClass(Splash.this,MainActivity.class);
                startActivity(intent1);
            }
        };

        //Se crea un nuevo timer
        Timer timer = new Timer();

        //Se ejecuta la tarea "cambiarPantalla" con el timer
        timer.schedule(cambiarPantalla,tiempoDeEspera);
    }
}
