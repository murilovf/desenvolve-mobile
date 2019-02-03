package desenvolve.com.br.desenvolve.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.dao.ConfiguracaoMedidaDAO;
import desenvolve.com.br.desenvolve.dao.MedidaDAO;
import desenvolve.com.br.desenvolve.domain.Bovino;
import desenvolve.com.br.desenvolve.domain.ConfiguracaoMedidas;
import desenvolve.com.br.desenvolve.domain.Medida;

public class BovinoGraficoActivity extends AppCompatActivity {

    static Bovino bovinoSelecionado = null;

    GraphView graph = null;
    RadioButton rdbPeso;
    RadioButton rdbAltura;
    RadioButton rdbCircunferencia;

    LineGraphSeries<DataPoint> seriesReal;
    LineGraphSeries<DataPoint> seriesLiteratura;
    LineGraphSeries<DataPoint> seriesPessoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bovino_grafico);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Gráficos - " + bovinoSelecionado.getNome());
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));
        bar.setDisplayHomeAsUpEnabled(true);

        rdbPeso = (RadioButton) findViewById(R.id.rdbGraficoPeso);
        rdbAltura = (RadioButton) findViewById(R.id.rdbGraficoAltura);
        rdbCircunferencia = (RadioButton) findViewById(R.id.rdbGraficoCircunferencia);

        graph = (GraphView) findViewById(R.id.graph);

        montarGrafico();

        rdbPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montarGrafico();
            }
        });

        rdbAltura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montarGrafico();
            }
        });

        rdbCircunferencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montarGrafico();
            }
        });
    }

    private void montarGrafico(){
        String tipo = "";

        graph.removeAllSeries();

        seriesReal = new LineGraphSeries<DataPoint>();
        seriesLiteratura = new LineGraphSeries<DataPoint>();
        seriesPessoal = new LineGraphSeries<DataPoint>();

        ArrayList<Medida> listaMedidasReal = new ArrayList<Medida>();
        ArrayList<ConfiguracaoMedidas> listaMedidasLiteratura = new ArrayList<ConfiguracaoMedidas>();
        ArrayList<ConfiguracaoMedidas> listaMedidasPessoal = new ArrayList<ConfiguracaoMedidas>();

        MedidaDAO dao = new MedidaDAO();
        ConfiguracaoMedidaDAO dao2 = new ConfiguracaoMedidaDAO();
        ConfiguracaoMedidaDAO dao3 = new ConfiguracaoMedidaDAO();

        try {
            listaMedidasReal = dao.listarIndividual(bovinoSelecionado.getCodigo().toString(), "grafico");
            listaMedidasLiteratura = dao2.listarLiteratura();
            listaMedidasPessoal = dao3.listarPessoal();

            //PREENCHE MEDIDAS REAL

            if (listaMedidasReal != null){

                double x = 0,y = 0;

                for (Medida m: listaMedidasReal) {

                    x = m.getDias();
                    if(rdbAltura.isChecked()){
                        y = m.getAltura();
                        tipo = "Altura";
                    }else if(rdbCircunferencia.isChecked()){
                        y = m.getCircunferencia();
                        tipo = "Circunferência";
                    }else{
                        y = m.getPeso();
                        tipo = "Peso";
                    }

                    seriesReal.appendData(new DataPoint(x,y), true, 1000);
                    seriesReal.setDrawDataPoints(true);
                    seriesReal.setColor(Color.rgb(197, 180, 127));
                }
            }


            //PREENCHE MEDIDAS LITERATURA

            if (listaMedidasLiteratura != null){

                double x2 = 0,y2 = 0;

                for (ConfiguracaoMedidas m: listaMedidasLiteratura) {

                    x2 = m.getMes() * 30;
                    if(rdbAltura.isChecked()){
                        y2 = m.getAltura();
                        tipo = "Altura";
                    }else if(rdbCircunferencia.isChecked()){
                        y2 = m.getCircunferencia();
                        tipo = "Circunferência";
                    }else{
                        y2 = m.getPeso();
                        tipo = "Peso";
                    }

                    seriesLiteratura.appendData(new DataPoint(x2,y2), true, 1000);
                    seriesLiteratura.setDrawDataPoints(true);
                    seriesLiteratura.setColor(Color.rgb(75, 178, 197));
                }
            }

            //PREENCHE MEDIDAS PESSOAL

            if (listaMedidasPessoal != null){

                double x3 = 0,y3 = 0;

                for (ConfiguracaoMedidas m: listaMedidasPessoal) {

                    x3 = m.getMes() * 30;
                    if(rdbAltura.isChecked()){
                        y3 = m.getAltura();
                        tipo = "Altura";
                    }else if(rdbCircunferencia.isChecked()){
                        y3 = m.getCircunferencia();
                        tipo = "Circunferência";
                    }else{
                        y3 = m.getPeso();
                        tipo = "Peso";
                    }

                    seriesPessoal.appendData(new DataPoint(x3,y3), true, 1000);
                    seriesPessoal.setDrawDataPoints(true);
                    seriesPessoal.setColor(Color.rgb(234, 162, 40));
                }

            }

            graph.addSeries(seriesReal);
            graph.addSeries(seriesLiteratura);
            graph.addSeries(seriesPessoal);

            seriesReal.setTitle(tipo + " real do bezerro");
            seriesLiteratura.setTitle(tipo + " ideal da literatura");
            seriesPessoal.setTitle(tipo + " ideal do produtor");

            graph.getViewport().setMaxX(400);
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setBackgroundColor(Color.WHITE);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            graph.getLegendRenderer().setTextSize(15);
            graph.getLegendRenderer().setWidth(300);

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
