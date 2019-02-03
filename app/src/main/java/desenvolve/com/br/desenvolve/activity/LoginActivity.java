package desenvolve.com.br.desenvolve.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.dao.UsuarioDAO;
import desenvolve.com.br.desenvolve.domain.Usuario;

public class LoginActivity extends AppCompatActivity {

    private Button btnEntrar;
    private EditText txtUsuario;
    private EditText txtSenha;
    private ImageView btnConfigConexao;

    private Usuario usuarioLogado = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ConexaoActivity.dbDesenvolve = openOrCreateDatabase("db_desenvolve", MODE_PRIVATE, null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("Entrar");
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#797781")));

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        btnConfigConexao = (ImageView) findViewById(R.id.btnConfigConexao);

        btnConfigConexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ConexaoActivity.class));
            }
        });


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String usuario = txtUsuario.getText().toString();
                    String senha = txtSenha.getText().toString();

                    UsuarioDAO dao = new UsuarioDAO();
                    usuarioLogado = dao.autenticar(usuario, senha);

                    if (usuarioLogado != null) {
                        limparCampos();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, UsuarioDAO.mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void limparCampos(){
        txtUsuario.setText("");
        txtSenha.setText("");
    }

}
