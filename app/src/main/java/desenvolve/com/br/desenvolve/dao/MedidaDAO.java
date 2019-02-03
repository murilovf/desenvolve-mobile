package desenvolve.com.br.desenvolve.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.jar.Attributes;

import desenvolve.com.br.desenvolve.activity.ConexaoActivity;
import desenvolve.com.br.desenvolve.domain.AlertaVl;
import desenvolve.com.br.desenvolve.domain.Bovino;
import desenvolve.com.br.desenvolve.domain.Medida;

/**
 * Created by Murilo on 17/02/2017.
 */
public class MedidaDAO {

    public static String mensagem = "";
    private static final String NAMESPACE = "http://dao.desenvolve.com.br";
    private static final String SALVAR = "salvar";
    private static final String LISTAR_MEDIDAS_BOVINO = "listarIndividual";
    private static final String LISTAR_LIMITE_LITERARIO = "listarLimiteLiterario";
    private static final String LISTAR_LIMITE_PESSOAL = "listarLimitePessoal";
    private static final String BUSCAR_ULTIMA_MEDIDA = "buscarUltimaMedida";

    public void salvar(int dias, int mes, double peso, double altura, double circunferencia, Date dataMedicao, String cod_bovino) throws SQLException{

        Medida medida = null;

        SoapObject salvar = new SoapObject(NAMESPACE, SALVAR);

        salvar.addProperty("dias", dias);
        salvar.addProperty("mes", mes);
        salvar.addProperty("peso", peso);
        salvar.addProperty("altura", altura);
        salvar.addProperty("circunferencia", circunferencia);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        salvar.addProperty("dataMedicao", sdf.format(dataMedicao));
        salvar.addProperty("cod_bovino", cod_bovino);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(salvar);

        MarshalFloat mf = new MarshalFloat();
        mf.register(envelope);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/MedidaDAO?wsdl", 50000);
            http.call("urn:" + SALVAR, envelope);

            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

            mensagem = "Medida cadastrada com sucesso";

        } catch (ConnectException e) {
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";
        } catch (Exception e2) {
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
        }


    }

    public ArrayList<Medida> listarIndividual(String codigo, String tipo) throws SQLException{
        ArrayList<Medida> lista = new ArrayList<Medida>();

        SoapObject listarMedidasBovino = new SoapObject(NAMESPACE, LISTAR_MEDIDAS_BOVINO);
        listarMedidasBovino.addProperty("codigo", codigo);
        listarMedidasBovino.addProperty("tipo", tipo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarMedidasBovino);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/MedidaDAO?wsdl", 50000);
            http.call("urn:" + LISTAR_MEDIDAS_BOVINO, envelope);

            try {
                Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                if (resposta != null) {
                    for (SoapObject so: resposta) {
                        Medida m = new Medida();
                        Bovino b = new Bovino();

                        m.setCodigo(Long.parseLong(so.getProperty("codigo").toString()));
                        m.setPeso(Double.parseDouble(so.getProperty("peso").toString()));
                        m.setAltura(Double.parseDouble(so.getProperty("altura").toString()));
                        m.setCircunferencia(Double.parseDouble(so.getProperty("circunferencia").toString()));
                        m.setDias(Integer.parseInt(so.getProperty("dias").toString()));
                        m.setMes(Integer.parseInt(so.getProperty("mes").toString()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        m.setDatamedicao(sdf.parse(so.getProperty("datamedicao").toString()));
                        b.setCodigo(Long.parseLong(codigo));
                        m.setBovino(b);

                        lista.add(m);

                    }

                }else{
                    mensagem = "Não foi encontrado nenhuma medida cadastrada para esse bovino";
                }

            }catch (ClassCastException e){
                SoapObject resposta = (SoapObject) envelope.getResponse();

                if (resposta != null) {
                    Medida m = new Medida();
                    Bovino b = new Bovino();

                    m.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                    m.setPeso(Double.parseDouble(resposta.getProperty("peso").toString()));
                    m.setAltura(Double.parseDouble(resposta.getProperty("altura").toString()));
                    m.setCircunferencia(Double.parseDouble(resposta.getProperty("circunferencia").toString()));
                    m.setDias(Integer.parseInt(resposta.getProperty("dias").toString()));
                    m.setMes(Integer.parseInt(resposta.getProperty("mes").toString()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    m.setDatamedicao(sdf.parse(resposta.getProperty("datamedicao").toString()));
                    b.setCodigo(Long.parseLong(codigo));
                    m.setBovino(b);

                    lista.add(m);
                }else{
                    mensagem = "Não foi encontrado nenhuma medida cadastrada para esse bovino";
                }

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

    public AlertaVl listarLimiteLiterario() throws SQLException{
        AlertaVl alertaVI = null;

        SoapObject listarLimiteLiterario = new SoapObject(NAMESPACE, LISTAR_LIMITE_LITERARIO);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarLimiteLiterario);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/MedidaDAO?wsdl", 50000);
            http.call("urn:" + listarLimiteLiterario, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            alertaVI = new AlertaVl();

            if (resposta != null) {
                alertaVI.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                alertaVI.setPeso(Double.parseDouble(resposta.getProperty("peso").toString()));
                alertaVI.setAltura(Double.parseDouble(resposta.getProperty("altura").toString()));
                alertaVI.setCircunferencia(Double.parseDouble(resposta.getProperty("circunferencia").toString()));
                alertaVI.setAtivo(Integer.parseInt(resposta.getProperty("ativo").toString()));

                return alertaVI;
            } else {
                mensagem = "Limite do alerta literário não encontrado!";
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

    public AlertaVl listarLimitePessoal() throws SQLException{
        AlertaVl alertaVI = null;

        SoapObject listarLimitePessoal = new SoapObject(NAMESPACE, LISTAR_LIMITE_PESSOAL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarLimitePessoal);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/MedidaDAO?wsdl", 50000);
            http.call("urn:" + listarLimitePessoal, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            alertaVI = new AlertaVl();

            if (resposta != null) {
                alertaVI.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                alertaVI.setPeso(Double.parseDouble(resposta.getProperty("peso").toString()));
                alertaVI.setAltura(Double.parseDouble(resposta.getProperty("altura").toString()));
                alertaVI.setCircunferencia(Double.parseDouble(resposta.getProperty("circunferencia").toString()));
                alertaVI.setAtivo(Integer.parseInt(resposta.getProperty("ativo").toString()));

                return alertaVI;
            } else {
                mensagem = "Limite do alerta pessoal não encontrado!";
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

    public String buscarUltimaMedida() throws SQLException{
        String codigo = null;

        SoapObject buscarUltimaMedida = new SoapObject(NAMESPACE, BUSCAR_ULTIMA_MEDIDA);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarUltimaMedida);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/MedidaDAO?wsdl", 50000);
            http.call("urn:" + buscarUltimaMedida, envelope);

            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

            if (resposta != null) {

                codigo = (resposta.getValue().toString());

            } else {
                mensagem = "Ultima medida não encontrada!";
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

        return codigo;
    }
}
