package desenvolve.com.br.desenvolve.dao;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import desenvolve.com.br.desenvolve.activity.BovinoActivity;
import desenvolve.com.br.desenvolve.activity.ConexaoActivity;
import desenvolve.com.br.desenvolve.domain.Bovino;
import desenvolve.com.br.desenvolve.domain.Medida;

/**
 * Created by Murilo on 06/02/2017.
 */
public class BovinoDAO {

    public static String mensagemErro = "";
    private static final String NAMESPACE = "http://dao.desenvolve.com.br";
    private static final String BUSCAR_POR_CODIGO = "buscarPorCodigo";

    public Medida buscarPorCodigo(String filtro) throws SQLException {
        Bovino bovino = null;
        Medida medida = null;

        SoapObject buscaBovino = new SoapObject(NAMESPACE, BUSCAR_POR_CODIGO);
        buscaBovino.addProperty("filtro", filtro);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscaBovino);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/BovinoDAO?wsdl", 50000);
            http.call("urn:" + BUSCAR_POR_CODIGO, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            bovino = new Bovino();
            medida = new Medida();

            if (resposta != null) {
                medida.setPeso(Double.parseDouble(resposta.getProperty("peso").toString()));
                medida.setAltura(Double.parseDouble(resposta.getProperty("altura").toString()));
                medida.setCircunferencia(Double.parseDouble(resposta.getProperty("circunferencia").toString()));

                SoapObject bo = (SoapObject) resposta.getProperty("bovino");

                bovino.setCodigo(Long.parseLong(bo.getProperty("codigo").toString()));
                bovino.setNome(bo.getProperty("nome").toString());
                bovino.setOrigem(bo.getProperty("origem").toString());
                bovino.setRaca(bo.getProperty("raca").toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                bovino.setDatanascimento(sdf.parse(bo.getProperty("datanascimento").toString()));

                medida.setBovino(bovino);

                return medida;
            } else {
                mensagemErro = "Bovino não encontrado!";
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