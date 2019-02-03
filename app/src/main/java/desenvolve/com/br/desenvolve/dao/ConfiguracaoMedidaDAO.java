package desenvolve.com.br.desenvolve.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import desenvolve.com.br.desenvolve.activity.ConexaoActivity;
import desenvolve.com.br.desenvolve.domain.Bovino;
import desenvolve.com.br.desenvolve.domain.ConfiguracaoMedidas;
import desenvolve.com.br.desenvolve.domain.Medida;

/**
 * Created by Murilo on 05/03/2017.
 */
public class ConfiguracaoMedidaDAO {
    public static String mensagem = "";
    private static final String NAMESPACE = "http://dao.desenvolve.com.br";
    private static final String LISTAR_LITERATURA = "listarLiteratura";
    private static final String LISTAR_PESSOAL = "listarPessoal";
    private static final String BUSCAR_MEDIDA_LITERATURA = "buscarMedidaLiteratura";
    private static final String BUSCAR_MEDIDA_PESSOAL = "buscarMedidaPessoal";

    public ArrayList<ConfiguracaoMedidas> listarLiteratura() throws SQLException {
        ArrayList<ConfiguracaoMedidas> lista = new ArrayList<ConfiguracaoMedidas>();

        SoapObject listarLiteratura = new SoapObject(NAMESPACE, LISTAR_LITERATURA);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarLiteratura);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/ConfiguracaoMedidaDAO?wsdl", 50000);
            http.call("urn:" + LISTAR_LITERATURA, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

            for (SoapObject so: resposta) {
                ConfiguracaoMedidas m = new ConfiguracaoMedidas();

                m.setCodigo(Long.parseLong(so.getProperty("codigo").toString()));
                m.setPeso(Double.parseDouble(so.getProperty("peso").toString()));
                m.setAltura(Double.parseDouble(so.getProperty("altura").toString()));
                m.setCircunferencia(Double.parseDouble(so.getProperty("circunferencia").toString()));
                m.setMes(Integer.parseInt(so.getProperty("mes").toString()));

                lista.add(m);

            }

            return lista;

        }catch (ConnectException e){
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";
            return null;
        }catch (Exception e2){
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
            return null;
        }

    }

    public ArrayList<ConfiguracaoMedidas> listarPessoal() throws SQLException{
        ArrayList<ConfiguracaoMedidas> lista = new ArrayList<ConfiguracaoMedidas>();

        SoapObject listarPessoal = new SoapObject(NAMESPACE, LISTAR_PESSOAL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarPessoal);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/ConfiguracaoMedidaDAO?wsdl", 50000);
            http.call("urn:" + LISTAR_PESSOAL, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

            for (SoapObject so: resposta) {
                ConfiguracaoMedidas m = new ConfiguracaoMedidas();

                m.setCodigo(Long.parseLong(so.getProperty("codigo").toString()));
                m.setPeso(Double.parseDouble(so.getProperty("peso").toString()));
                m.setAltura(Double.parseDouble(so.getProperty("altura").toString()));
                m.setCircunferencia(Double.parseDouble(so.getProperty("circunferencia").toString()));
                m.setMes(Integer.parseInt(so.getProperty("mes").toString()));

                lista.add(m);

            }

            return lista;

        }catch (ConnectException e){
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";
            return null;
        }catch (Exception e2){
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
            return null;
        }

    }

    public ConfiguracaoMedidas buscarMedidaLiteratura(Integer mes) throws SQLException{
        ConfiguracaoMedidas medida = null;

        SoapObject buscarMedida = new SoapObject(NAMESPACE, BUSCAR_MEDIDA_LITERATURA);
        buscarMedida.addProperty("mes", mes);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarMedida);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/ConfiguracaoMedidaDAO?wsdl", 50000);
            http.call("urn:" + BUSCAR_MEDIDA_LITERATURA, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            medida = new ConfiguracaoMedidas();

            if (resposta != null) {
                medida.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                medida.setMes(Integer.parseInt(resposta.getProperty("mes").toString()));
                medida.setPeso(Double.parseDouble(resposta.getProperty("peso").toString()));
                medida.setAltura(Double.parseDouble(resposta.getProperty("altura").toString()));
                medida.setCircunferencia(Double.parseDouble(resposta.getProperty("circunferencia").toString()));

                return medida;
            } else {
                mensagem = "Medida literatura não encontrada!";
                return null;
            }

        } catch (ConnectException e) {
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
            return null;
        }

    }

    public ConfiguracaoMedidas buscarMedidaPessoal(Integer mes) throws SQLException{
        ConfiguracaoMedidas medida = null;

        SoapObject buscarMedida = new SoapObject(NAMESPACE, BUSCAR_MEDIDA_PESSOAL);
        buscarMedida.addProperty("mes", mes);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarMedida);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/ConfiguracaoMedidaDAO?wsdl", 50000);
            http.call("urn:" + BUSCAR_MEDIDA_PESSOAL, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            medida = new ConfiguracaoMedidas();

            if (resposta != null) {
                medida.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                medida.setMes(Integer.parseInt(resposta.getProperty("mes").toString()));
                medida.setPeso(Double.parseDouble(resposta.getProperty("peso").toString()));
                medida.setAltura(Double.parseDouble(resposta.getProperty("altura").toString()));
                medida.setCircunferencia(Double.parseDouble(resposta.getProperty("circunferencia").toString()));

                return medida;
            } else {
                mensagem = "Medida pessoal não encontrada!";
                return null;
            }

        } catch (ConnectException e) {
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
            return null;
        }

    }
}
