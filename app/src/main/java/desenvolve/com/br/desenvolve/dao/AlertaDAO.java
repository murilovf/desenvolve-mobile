package desenvolve.com.br.desenvolve.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import desenvolve.com.br.desenvolve.activity.ConexaoActivity;
import desenvolve.com.br.desenvolve.domain.Alerta;
import desenvolve.com.br.desenvolve.domain.AlertaErro;

/**
 * Created by Murilo on 30/03/2017.
 */
public class AlertaDAO {

    public static String mensagem = "";
    private static final String NAMESPACE = "http://dao.desenvolve.com.br";
    private static final String CRIAR = "criar";

    public void criar(Long codigoBovino, Long codigoMedida, Date data, String categoria, ArrayList<AlertaErro> listaerros) throws Exception{

        SoapObject criar = new SoapObject(NAMESPACE, CRIAR);

        criar.addProperty("codigoBovino", codigoBovino);
        criar.addProperty("codigoMedida", codigoMedida);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        criar.addProperty("data", sdf.format(data));
        criar.addProperty("categoria", categoria);

        for (int i=0; i < listaerros.size(); i++){
            SoapObject lista = new SoapObject(NAMESPACE, "listaerros");
            lista.addProperty("categoria", listaerros.get(i).getCategoria());
            lista.addProperty("descricao", listaerros.get(i).getDescricao());
            criar.addSoapObject(lista);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(criar);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/AlertaDAO?wsdl", 50000);
            http.call("urn:" + CRIAR, envelope);

            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

        } catch (ConnectException e) {
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";
        } catch (Exception e2) {
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
        }

    }
}
