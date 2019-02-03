package desenvolve.com.br.desenvolve.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.dao.BovinoDAO;
import desenvolve.com.br.desenvolve.domain.Bovino;
import desenvolve.com.br.desenvolve.domain.Medida;

public class BovinoActivity extends AppCompatActivity {

    //BIBLIOTECA DO SCAN QRCODE
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Medida b = null;

    private TextView txtCodigo;
    private TextView txtDescricao;
    private TextView txtOrigem;
    private TextView txtRaca;
    private TextView txtDataNascimento;
    private TextView txtPesoAtual;
    private TextView txtAlturaAtual;
    private TextView txtCircunferenciaAtual;
    private EditText txtFiltro;
    private Button btnBuscar;
    private Button btnQrcode;
    private Button btnVisualizarGraficos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bovino);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Ficha do Bovino");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));
        bar.setDisplayHomeAsUpEnabled(true);

        esconderTeclado();

        txtCodigo = (TextView) findViewById(R.id.txtCodigo);
        txtDescricao = (TextView) findViewById(R.id.txtDescricao);
        txtOrigem = (TextView) findViewById(R.id.txtOrigem);
        txtRaca = (TextView) findViewById(R.id.txtRaca);
        txtDataNascimento = (TextView) findViewById(R.id.txtDataNascimento);
        txtPesoAtual = (TextView) findViewById(R.id.txtPesoAtual);
        txtAlturaAtual = (TextView) findViewById(R.id.txtAlturaAtual);
        txtCircunferenciaAtual = (TextView) findViewById(R.id.txtCircunferenciaAtual);
        txtFiltro = (EditText) findViewById(R.id.txtFiltro);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnQrcode = (Button) findViewById(R.id.btnQrcode);
        btnVisualizarGraficos = (Button) findViewById(R.id.btnVisualizarGraficos);

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

        btnVisualizarGraficos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BovinoGraficoActivity.bovinoSelecionado = b.getBovino();
                startActivity(new Intent(BovinoActivity.this, BovinoGraficoActivity.class));
            }
        });
    }

    private void buscarBovino(String filtro) {

        if (filtro.length() > 0) {
            try {
                BovinoDAO dao = new BovinoDAO();
                b = new Medida();

                b = dao.buscarPorCodigo(filtro);

                if (b != null) {
                    txtCodigo.setText(String.format("%06d", b.getBovino().getCodigo()));
                    txtDescricao.setText(b.getBovino().getNome().toString());
                    txtOrigem.setText(b.getBovino().getOrigem().toString());
                    txtRaca.setText(b.getBovino().getRaca().toString());
                    txtPesoAtual.setText(String.format("%.2f", b.getPeso()).toString().replace(".", ","));
                    txtAlturaAtual.setText(String.format("%.2f", b.getAltura()).toString().replace(".", ","));
                    txtCircunferenciaAtual.setText(String.format("%.2f", b.getCircunferencia()).toString().replace(".", ","));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    txtDataNascimento.setText(sdf.format(b.getBovino().getDatanascimento()));

                    btnVisualizarGraficos.setEnabled(true);
                } else {
                    Toast.makeText(BovinoActivity.this, BovinoDAO.mensagemErro, Toast.LENGTH_LONG).show();
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

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

    public void scanQR() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(BovinoActivity.this, "Scanner não encontrado", "Deseja fazer o download do scanner?", "Sim", "Não").show();
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
