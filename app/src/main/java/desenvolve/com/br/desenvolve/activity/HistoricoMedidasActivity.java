package desenvolve.com.br.desenvolve.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import desenvolve.com.br.desenvolve.adapter.MedidaAdapter;
import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.dao.MedidaDAO;
import desenvolve.com.br.desenvolve.domain.Medida;

public class HistoricoMedidasActivity extends AppCompatActivity {

    static String bovinoSelecionado = "";

    private List<Medida> listaMedidas = null;
    private TextView lblBovinoHistoricoSelecionado;

    private ListView medidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_medidas);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Histórico de Medidas");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));
        bar.setDisplayHomeAsUpEnabled(true);

        lblBovinoHistoricoSelecionado = (TextView) findViewById(R.id.lblBovinoHistoricoSelecionado);
        lblBovinoHistoricoSelecionado.setText(bovinoSelecionado);
        medidas = (ListView) findViewById(R.id.listMedidas);

        try {
            MedidaDAO dao = new MedidaDAO();
            //listaMedidas = new ArrayList<Medida>();
            listaMedidas = dao.listarIndividual(bovinoSelecionado.toString().substring(0,6), "lista");

            if (listaMedidas != null && listaMedidas.size() > 0 ){
                MedidaAdapter adapter = new MedidaAdapter(this, listaMedidas);

                medidas.setAdapter(adapter);

            }else{
                Toast.makeText(HistoricoMedidasActivity.this, MedidaDAO.mensagem, Toast.LENGTH_LONG).show();
            }


        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }


    }

    //AÇÃO DO BOTAO VOLTAR LOCALIZADO NA BARRA DE CIMA
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
