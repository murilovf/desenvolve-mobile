package desenvolve.com.br.desenvolve.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.dao.AlertaDAO;
import desenvolve.com.br.desenvolve.dao.BovinoDAO;
import desenvolve.com.br.desenvolve.dao.ConfiguracaoMedidaDAO;
import desenvolve.com.br.desenvolve.dao.MedidaDAO;
import desenvolve.com.br.desenvolve.domain.Alerta;
import desenvolve.com.br.desenvolve.domain.AlertaErro;
import desenvolve.com.br.desenvolve.domain.AlertaVl;
import desenvolve.com.br.desenvolve.domain.Bovino;
import desenvolve.com.br.desenvolve.domain.ConfiguracaoMedidas;
import desenvolve.com.br.desenvolve.domain.Medida;

public class MedidaActivity extends AppCompatActivity {

    //BIBLIOTECA DO SCAN QRCODE
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    static String mensagemVazio = "";

    private EditText txtFiltro;
    private TextView lblBovinoSelecionado;
    private EditText txtPeso;
    private EditText txtAltura;
    private EditText txtCircunferencia;
    private Button btnBuscar;
    private Button btnQrcode;
    private Button btnAtualizaMedidas;
    private Button btnHistoricoMedidas;

    private AlertaVl valoresAlertaLiterario;
    private AlertaVl valoresAlertaPessoal;
    private ConfiguracaoMedidas medidaLiteraria;
    private ConfiguracaoMedidas medidaPessoal;

    private ArrayList<AlertaErro> listadeerros;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medida);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Cadastro de Medidas");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));
        bar.setDisplayHomeAsUpEnabled(true);

        esconderTeclado();

        txtFiltro = (EditText) findViewById(R.id.txtFiltroBovinoMedida);
        txtPeso = (EditText) findViewById(R.id.txtPeso);
        txtAltura = (EditText) findViewById(R.id.txtAltura);
        txtCircunferencia = (EditText) findViewById(R.id.txtCircunferencia);
        lblBovinoSelecionado = (TextView) findViewById(R.id.lblBovinoSelecionado);
        btnBuscar = (Button) findViewById(R.id.btnBuscarBovinoMedida);
        btnQrcode = (Button) findViewById(R.id.btnQrcodeMedida);
        btnAtualizaMedidas = (Button) findViewById(R.id.btnAtualizarMedidas);
        btnHistoricoMedidas = (Button) findViewById(R.id.btnHistoricoMedidas);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                esconderTeclado();

                String filtro = txtFiltro.getText().toString();
                buscarBovino(filtro);

            }
        });

        btnQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                esconderTeclado();
                scanQR();

            }
        });

        btnAtualizaMedidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificaCamposPreenchidos()){
                    salvarMedida();
                }else{
                    Toast.makeText(MedidaActivity.this, mensagemVazio, Toast.LENGTH_LONG).show();
                    mensagemVazio = "";
                }
            }
        });

        btnHistoricoMedidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoricoMedidasActivity.bovinoSelecionado = lblBovinoSelecionado.getText().toString();
                startActivity(new Intent(MedidaActivity.this, HistoricoMedidasActivity.class));
            }
        });
    }

    private void salvarMedida(){
        try {
            //ArrayList<AlertaErro> listadeerros;
            MedidaDAO dao = new MedidaDAO();
            ConfiguracaoMedidaDAO daoConfig = new ConfiguracaoMedidaDAO();


            String codigoBovino = lblBovinoSelecionado.getText().toString().substring(0,6);

            Medida m = new Medida();
            m.setPeso(Double.parseDouble(txtPeso.getText().toString()));
            m.setAltura(Double.parseDouble(txtAltura.getText().toString()));
            m.setCircunferencia(Double.parseDouble(txtCircunferencia.getText().toString()));
            m.setDias(buscaDias());
            m.setMes(buscaMes(m.getDias()));
            m.setDatamedicao(new Date(System.currentTimeMillis()));

            Bovino b = new Bovino();
            b.setCodigo(Long.parseLong(codigoBovino));

            m.setBovino(b);

            dao.salvar(m.getDias(), m.getMes(), m.getPeso(), m.getAltura(), m.getCircunferencia(), m.getDatamedicao(), codigoBovino);

            valoresAlertaLiterario = dao.listarLimiteLiterario();
            valoresAlertaPessoal = dao.listarLimitePessoal();
            medidaLiteraria = daoConfig.buscarMedidaLiteratura(m.getMes());
            medidaPessoal = daoConfig.buscarMedidaPessoal(m.getMes());

            listadeerros = new ArrayList<>();

            verificaAlerta(m, dao.buscarUltimaMedida());

            Toast.makeText(MedidaActivity.this, MedidaDAO.mensagem, Toast.LENGTH_LONG).show();

            limparCampos();
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    private void verificaAlerta(Medida medida, String codigo) {
        int contadorLiterario = 0;
        int contadorPessoal = 0;

        DecimalFormat df = new DecimalFormat("0.##");

        //----Literarios------------------------------------------------------------

        if (valoresAlertaLiterario.getAtivo() == 1){

            double diferencaLiterariaPeso = Math.abs(medida.getPeso()-medidaLiteraria.getPeso());
            Double valorLiterarioPeso=Double.parseDouble(df.format(diferencaLiterariaPeso).replaceAll(",", "."));

            double diferencaLiterariaAltura = Math.abs(medida.getAltura()-medidaLiteraria.getAltura());
            Double valorLiterarioAltura=Double.parseDouble(df.format(diferencaLiterariaAltura).replaceAll(",", "."));

            double diferencaLiterariaCircunferencia = Math.abs(medida.getCircunferencia()-medidaLiteraria.getCircunferencia());
            Double valorLiterarioCircunferencia=Double.parseDouble(df.format(diferencaLiterariaCircunferencia).replaceAll(",", "."));

            if(valorLiterarioPeso > valoresAlertaLiterario.getPeso() ){
                contadorLiterario = contadorLiterario+1;
                AlertaErro lp = new AlertaErro();
                lp.setCategoria("Literário");
                lp.setDescricao("O peso possui uma diferença de "+valorLiterarioPeso+" do valor configurado");
                listadeerros.add(lp);
            }
            if(valorLiterarioAltura > valoresAlertaLiterario.getAltura() ){
                contadorLiterario = contadorLiterario+1;
                AlertaErro la = new AlertaErro();
                la.setCategoria("Literário");
                la.setDescricao("A altura possui uma diferença de "+valorLiterarioAltura+" do valor configurado");
                listadeerros.add(la);
            }
            if(valorLiterarioCircunferencia > valoresAlertaLiterario.getCircunferencia() ){
                contadorLiterario = contadorLiterario+1;
                AlertaErro lc = new AlertaErro();
                lc.setCategoria("Literário");
                lc.setDescricao("A circunferência Torácica possui uma diferença de "+valorLiterarioCircunferencia+" do valor configurado");
                listadeerros.add(lc);
            }

        }


        //----Pessoal-------------------------------------------------------------

        if (valoresAlertaPessoal.getAtivo() == 1){

            double diferencaPessoalPeso = Math.abs(medida.getPeso()-medidaPessoal.getPeso());
            Double valorPessoalPeso=Double.parseDouble(df.format(diferencaPessoalPeso).replaceAll(",", "."));

            double diferencaPessoalAltura = Math.abs(medida.getAltura()-medidaPessoal.getAltura());
            Double valorPessoalAltura=Double.parseDouble(df.format(diferencaPessoalAltura).replaceAll(",", "."));

            double diferencaPessoalCircunferencia = Math.abs(medida.getCircunferencia()-medidaPessoal.getCircunferencia());
            Double valorPessoalCircunferencia=Double.parseDouble(df.format(diferencaPessoalCircunferencia).replaceAll(",", "."));

            if(valorPessoalPeso > valoresAlertaPessoal.getPeso() ){
                contadorPessoal = contadorPessoal+1;
                AlertaErro pp = new AlertaErro();
                pp.setCategoria("Pessoal");
                pp.setDescricao("O peso possui uma diferença de "+valorPessoalPeso+" do valor configurado");
                listadeerros.add(pp);
            }
            if(valorPessoalAltura > valoresAlertaPessoal.getAltura() ){
                contadorPessoal = contadorPessoal+1;
                AlertaErro pa = new AlertaErro();
                pa.setCategoria("Pessoal");
                pa.setDescricao("A altura possui uma diferença de "+valorPessoalAltura+" do valor configurado");
                listadeerros.add(pa);
            }
            if(valorPessoalCircunferencia > valoresAlertaPessoal.getCircunferencia() ){
                contadorPessoal = contadorPessoal+1;
                AlertaErro pc = new AlertaErro();
                pc.setCategoria("Pessoal");
                pc.setDescricao("A circunferência torácica possui uma diferença de "+valorPessoalCircunferencia+" do valor configurado");
                listadeerros.add(pc);
            }

        }

        //ele só vai cair em um tipo de alerta

        if(contadorLiterario>0){
            if(contadorPessoal>0){
                Alerta alerta = new Alerta();
                alerta.setBovino(medida.getBovino());
                alerta.setCategoria("Erro Padrão Pessoal e Literário");
                alerta.setData(new Date());
                Medida m = new Medida();
                m.setCodigo(Long.parseLong(codigo));
                alerta.setMedida(m);
                AlertaDAO dao = new AlertaDAO();
                try {
                    dao.criar(alerta.getBovino().getCodigo(), alerta.getMedida().getCodigo(), alerta.getData(), alerta.getCategoria(), listadeerros);
                } catch (Exception e) {
                    Toast.makeText(MedidaActivity.this, "Ocorreu algum erro inesperado", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }else{
                Alerta alerta = new Alerta();
                alerta.setBovino(medida.getBovino());
                alerta.setCategoria("Erro Padrão Literário");
                alerta.setData(new Date());
                Medida m = new Medida();
                m.setCodigo(Long.parseLong(codigo));
                alerta.setMedida(m);
                AlertaDAO dao = new AlertaDAO();
                try {
                    dao.criar(alerta.getBovino().getCodigo(), alerta.getMedida().getCodigo(), alerta.getData(), alerta.getCategoria(), listadeerros);
                } catch (Exception e) {
                    Toast.makeText(MedidaActivity.this, "Ocorreu algum erro inesperado", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }else if (contadorPessoal>0){
            Alerta alerta = new Alerta();
            alerta.setBovino(medida.getBovino());
            alerta.setCategoria("Erro Padrão Pessoal");
            alerta.setData(new Date());
            Medida m = new Medida();
            m.setCodigo(Long.parseLong(codigo));
            alerta.setMedida(m);
            AlertaDAO dao = new AlertaDAO();
            try {
                dao.criar(alerta.getBovino().getCodigo(), alerta.getMedida().getCodigo(), alerta.getData(), alerta.getCategoria(), listadeerros);
            } catch (Exception e) {
                Toast.makeText(MedidaActivity.this, "Ocorreu algum erro inesperado", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }


    }

    private Integer buscaDias(){
        try {
            String codigoBovino = lblBovinoSelecionado.getText().toString().substring(0,6);

            BovinoDAO dao2 = new BovinoDAO();

            Medida bovinoNascimento = new Medida();
            bovinoNascimento = dao2.buscarPorCodigo(codigoBovino);
            DateTime dtToday = new DateTime(); //pega data e hora atual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataNascimento = sdf.format(bovinoNascimento.getBovino().getDatanascimento());
            DateTime dtOther = new DateTime(DateTime.parse(dataNascimento));
            Duration dur = new Duration(dtOther, dtToday);

            return (int) dur.getStandardDays();

        }catch (java.sql.SQLException e){
            e.printStackTrace();
            return 0;
        }

    }

    private Integer buscaMes(int dia){
        int mes = 0;

        if (dia <= 30){
            mes = 1;
        }
        if (dia > 30 && dia <= 60){
            mes = 2;
        }
        if (dia > 60 && dia <= 90){
            mes = 3;
        }
        if (dia > 90 && dia <= 120){
            mes = 4;
        }
        if (dia > 120 && dia <= 150){
            mes = 5;
        }
        if (dia > 150 && dia <= 180){
            mes = 6;
        }
        if (dia > 180 && dia <= 210){
            mes = 7;
        }
        if (dia > 210 && dia <= 240){
            mes = 8;
        }
        if (dia > 240 && dia <= 270){
            mes = 9;
        }
        if (dia > 270 && dia <= 300){
            mes = 10;
        }
        if (dia > 300 && dia <= 330){
            mes = 11;
        }
        if (dia > 330 && dia <= 360){
            mes = 12;
        }
        if (dia > 360 && dia <= 390){
            mes = 13;
        }
        if (dia > 390 && dia <= 420){
            mes = 14;
        }
        if (dia > 420 && dia <= 450){
            mes = 15;
        }
        if (dia > 480 && dia <= 510){
            mes = 16;
        }
        if (dia > 540 && dia <= 570){
            mes = 17;
        }
        if (dia > 600 && dia <= 630){
            mes = 18;
        }
        if (dia > 630 && dia <= 660){
            mes = 19;
        }
        if (dia > 690 && dia <= 720){
            mes = 20;
        }
        if (dia > 720 && dia <= 750){
            mes = 21;
        }
        if (dia > 780 && dia <= 810){
            mes = 22;
        }
        if (dia > 840 && dia <= 870){
            mes = 23;
        }
        if (dia > 900 && dia <= 930){
            mes = 24;
        }
        if (dia > 960 && dia <= 990){
            mes = 25;
        }
        if (dia > 990 && dia <= 1020){
            mes = 26;
        }
        if (dia > 1020 && dia <= 1050){
            mes = 27;
        }
        if (dia > 1050 && dia <= 1080) {
            mes = 28;
        }
        if (dia > 1080 && dia <= 1110){
            mes = 29;
        }
        if (dia > 1110 && dia <= 1140){
            mes = 30;
        }

        return mes;

    }

    private void buscarBovino(String filtro) {

        if (filtro.length() > 0) {
            try {
                BovinoDAO dao = new BovinoDAO();
                Medida b = new Medida();

                b = dao.buscarPorCodigo(filtro);

                if (b != null) {
                    lblBovinoSelecionado.setText(String.format("%06d", b.getBovino().getCodigo()) + " - " + b.getBovino().getNome().toString());
                    txtPeso.setEnabled(true);
                    txtAltura.setEnabled(true);
                    txtCircunferencia.setEnabled(true);
                    btnAtualizaMedidas.setEnabled(true);
                    btnHistoricoMedidas.setEnabled(true);
                } else {
                    Toast.makeText(MedidaActivity.this, BovinoDAO.mensagemErro, Toast.LENGTH_LONG).show();
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean verificaCamposPreenchidos(){
        if (txtAltura.getText().toString().equals("")){
            mensagemVazio += "Campo Altura deve ser preenchido\n";
        }

        if (txtPeso.getText().toString().equals("")){
            mensagemVazio += "Campo Peso deve ser preenchido\n";
        }

        if (txtCircunferencia.getText().toString().equals("")){
            mensagemVazio += "Campo Circunferência deve ser preenchido\n";
        }

        if (mensagemVazio.trim().length() > 0){
            return false;
        }else{
            return true;
        }

    }

    private void esconderTeclado() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limparCampos(){
        txtPeso.setText("");
        txtAltura.setText("");
        txtCircunferencia.setText("");
    }

    public void scanQR() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(MedidaActivity.this, "Scanner não encontrado", "Deseja fazer o download do scanner?", "Sim", "Não").show();
        }
    }

    private static AlertDialog showDialog(final AppCompatActivity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Leitura:" + contents + "Formato:" + format, Toast.LENGTH_LONG);
                toast.show();

                buscarBovino(contents);

            }
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
