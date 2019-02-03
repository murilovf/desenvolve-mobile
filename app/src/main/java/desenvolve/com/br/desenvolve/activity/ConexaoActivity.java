package desenvolve.com.br.desenvolve.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;

import desenvolve.com.br.desenvolve.R;

public class ConexaoActivity extends AppCompatActivity {

    private TextView txtIp;
    private Button btnSalvarConexao;
    private Button btnCancelarConexao;
    public static SQLiteDatabase dbDesenvolve = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexao);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Configurar Conexão");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));
        bar.setDisplayHomeAsUpEnabled(true);

        txtIp = (TextView) findViewById(R.id.txtIp);
        btnSalvarConexao = (Button) findViewById(R.id.btnSalvarConexao);
        btnCancelarConexao = (Button) findViewById(R.id.btnCancelarConexao);

        dbDesenvolve = openOrCreateDatabase("db_desenvolve", MODE_PRIVATE, null);

        dbDesenvolve.execSQL("CREATE TABLE IF NOT EXISTS tbl_conexao(con_codigo INT(3), con_ip VARCHAR) ");

        lerDadosConexao();

        btnSalvarConexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbDesenvolve.execSQL("UPDATE tbl_conexao SET con_ip = '" + txtIp.getText().toString() +
                        "' WHERE con_codigo = 1 ");

                Toast.makeText(ConexaoActivity.this, "Conexão salva com sucesso", Toast.LENGTH_LONG).show();

            }
        });

        btnCancelarConexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void lerDadosConexao() {
        Cursor cursor = dbDesenvolve.rawQuery("SELECT con_codigo, con_ip FROM tbl_conexao ", null);

        int indiceColunaCodigo = cursor.getColumnIndex("con_codigo");
        int indiceColunaIp = cursor.getColumnIndex("con_ip");

        if (cursor.getCount() == 0) {
            dbDesenvolve.execSQL("INSERT INTO tbl_conexao (con_codigo, con_ip) VALUES (1, '') ");
            lerDadosConexao();
        } else {
            cursor.moveToFirst();
            txtIp.setText(cursor.getString(indiceColunaIp));
        }
    }

    public static String getIp() {
//        String ip = "";
//        Cursor cursor = dbDesenvolve.rawQuery("SELECT con_ip FROM tbl_conexao ", null);
//
//        int indiceColunaIp = cursor.getColumnIndex("con_ip");
//
//        if (cursor.getCount() == 0) {
//            dbDesenvolve.execSQL("INSERT INTO tbl_conexao (con_codigo, con_ip) VALUES (1, '') ");
//        } else {
//            cursor.moveToFirst();
//            ip = cursor.getString(indiceColunaIp);
//        }
        //return "192.168.1.102:8080";
        return "desenvolve.unisul.br";
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
