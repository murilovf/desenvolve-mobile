package desenvolve.com.br.desenvolve.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import desenvolve.com.br.desenvolve.R;

public class MainActivity extends AppCompatActivity {

    private Button btnFichaBovino;
    private Button btnCadastraMedidas;
    private Button btnVacinas;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Desenvolve");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));

        btnFichaBovino = (Button) findViewById(R.id.btnFichaBovino);
        btnCadastraMedidas = (Button) findViewById(R.id.btnCadastroMedidas);
        btnVacinas = (Button) findViewById(R.id.btnVacinas);
        btnSair = (Button) findViewById(R.id.btnSair);

        btnFichaBovino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BovinoActivity.class));
            }
        });

        btnCadastraMedidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MedidaActivity.class));
            }
        });

        btnVacinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VacinaActivity.class));
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
