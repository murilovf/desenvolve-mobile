package desenvolve.com.br.desenvolve.dao;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import desenvolve.com.br.desenvolve.activity.ConexaoActivity;
import desenvolve.com.br.desenvolve.domain.Usuario;

/**
 * Created by Murilo on 12/02/2017.
 */
public class UsuarioDAO {

    public static String mensagemErro = "";
    private static final String NAMESPACE = "http://dao.desenvolve.com.br";
    private static final String AUTENTICAR = "autenticar";

    public Usuario autenticar(String login, String senha) throws SQLException {
        Usuario usuario = null;

        SoapObject autenticar = new SoapObject(NAMESPACE, AUTENTICAR);
        autenticar.addProperty("login", login);
        autenticar.addProperty("senha", senha);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(autenticar);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/UsuarioDAO?wsdl", 20000);
            http.call("urn:" + AUTENTICAR, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            usuario = new Usuario();

            if (resposta != null) {
                usuario.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                usuario.setNome(resposta.getProperty("nome").toString());
                usuario.setLogin(resposta.getProperty("login").toString());
                usuario.setSenha(resposta.getProperty("senha").toString());
                usuario.setPerfil(Integer.parseInt(resposta.getProperty("perfil").toString()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                usuario.setDatanascimento(sdf.parse(resposta.getProperty("datanascimento").toString()));

                return usuario;
            } else {
                mensagemErro = "Usuário ou senha inválidos";
                return null;
            }

        } catch (ConnectException e) {
            e.printStackTrace();
            mensagemErro = "Falha na conexão. Verifique as configurações do aplicativo";
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            mensagemErro = "Ocorreu uma falha inesperada no aplicativo";
            return null;
        }
    }
}
