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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.adapter.MedidaAdapter;
import desenvolve.com.br.desenvolve.adapter.VacinaAdapter;
import desenvolve.com.br.desenvolve.dao.MedidaDAO;
import desenvolve.com.br.desenvolve.dao.VacinaDAO;
import desenvolve.com.br.desenvolve.domain.Medida;
import desenvolve.com.br.desenvolve.domain.Vacina;

public class VacinaActivity extends AppCompatActivity {

    private static List<Vacina> listaVacinas = null;
    private static List<Vacina> listaVacinaFiltradas = null;
    private EditText txtFiltroVacina;
    private ListView vacinas;
    private static Vacina v = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacina);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Vacinas");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));
        bar.setDisplayHomeAsUpEnabled(true);

        txtFiltroVacina = (EditText) findViewById(R.id.txtFiltroVacina);

        vacinas = (ListView) findViewById(R.id.listVacinas);

        vacinas.setOnItemClickListener(onItemClick_List);

        listar();

        txtFiltroVacina.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carregarFiltro();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    AdapterView.OnItemClickListener onItemClick_List = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            v = new Vacina();
            if (txtFiltroVacina.getText().toString().length() > 0){
                v.setCodigo(listaVacinaFiltradas.get(position).getCodigo());
                v.setNome(listaVacinaFiltradas.get(position).getNome());
                v.setSituacao(listaVacinaFiltradas.get(position).getSituacao());
                v.setDatavacina(listaVacinaFiltradas.get(position).getDatavacina());
                v.setBovino(listaVacinaFiltradas.get(position).getBovino());
                v.setDataaplicacao(new Date());

                showDialog(VacinaActivity.this, "Atualizar estado da vacina", "Deseja atualizar o estado da vacina " + String.format("%06d",listaVacinaFiltradas.get(position).getCodigo()) + " - " + listaVacinaFiltradas.get(position).getNome() + "?", "Sim", "Não").show();
            }else{
                v.setCodigo(listaVacinas.get(position).getCodigo());
                v.setNome(listaVacinas.get(position).getNome());
                v.setSituacao(listaVacinas.get(position).getSituacao());
                v.setDatavacina(listaVacinas.get(position).getDatavacina());
                v.setBovino(listaVacinas.get(position).getBovino());
                v.setDataaplicacao(new Date());

                showDialog(VacinaActivity.this, "Atualizar estado da vacina", "Deseja atualizar o estado da vacina " + String.format("%06d",listaVacinas.get(position).getCodigo()) + " - " + listaVacinas.get(position).getNome() + "?", "Sim", "Não").show();
            }
        }
    };

    private AlertDialog showDialog(final AppCompatActivity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    VacinaDAO dao = new VacinaDAO();
                    dao.atualizaEstado(v);

                    Toast.makeText(VacinaActivity.this, VacinaDAO.mensagem, Toast.LENGTH_LONG).show();

                }catch (java.sql.SQLException e){
                    e.printStackTrace();
                }

                if (txtFiltroVacina.getText().toString().length() > 0){
                    carregarFiltro();
                }else{
                    listar();
                }

            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return downloadDialog.show();
    }

    public void carregarFiltro(){
        VacinaDAO dao = new VacinaDAO();
        try {
            listaVacinaFiltradas = dao.filtro(txtFiltroVacina.getText().toString());
            VacinaAdapter adapter = new VacinaAdapter(this, listaVacinaFiltradas);
            if (listaVacinaFiltradas != null && listaVacinaFiltradas.size() > 0){
                vacinas.setAdapter(adapter);
            }else{
                vacinas.setAdapter(null);
            }
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }

    }

    public void listar(){
        try {
            VacinaDAO dao = new VacinaDAO();
            listaVacinas = dao.listar();

            VacinaAdapter adapter = new VacinaAdapter(this, listaVacinas);

            if (listaVacinas != null && listaVacinas.size() > 0){


                vacinas.setAdapter(adapter);

            }else{
                Toast.makeText(VacinaActivity.this, VacinaDAO.mensagem, Toast.LENGTH_LONG).show();
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
