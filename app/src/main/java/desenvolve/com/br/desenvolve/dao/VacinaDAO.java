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
import desenvolve.com.br.desenvolve.domain.Medida;
import desenvolve.com.br.desenvolve.domain.Vacina;

/**
 * Created by Murilo on 27/02/2017.
 */
public class VacinaDAO {
    public static String mensagem = "";
    private static final String NAMESPACE = "http://dao.desenvolve.com.br";
    private static final String LISTAR = "listar";
    private static final String FILTRO = "filtro";
    private static final String ATUALIZA_ESTADO = "atualizaEstado";

    public ArrayList<Vacina> listar() throws SQLException {
        ArrayList<Vacina> lista = new ArrayList<Vacina>();

        SoapObject listarVacinas = new SoapObject(NAMESPACE, LISTAR);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarVacinas);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/VacinaDAO?wsdl", 50000);
            http.call("urn:" + LISTAR, envelope);

            try {
                Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                if (resposta != null){

                    for (SoapObject so: resposta) {
                        Vacina v = new Vacina();
                        Bovino b = new Bovino();

                        v.setCodigo(Long.parseLong(so.getProperty("codigo").toString()));
                        v.setNome(so.getProperty("nome").toString());
                        v.setSituacao(Integer.parseInt(so.getProperty("situacao").toString()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        v.setDatavacina(sdf.parse(so.getProperty("datavacina").toString()));
                        if(so.getProperty("dataaplicacao") == null){
                            v.setDataaplicacao(null);
                        }else{
                            v.setDataaplicacao(sdf.parse(so.getProperty("dataaplicacao").toString()));
                        }


                        SoapObject bo = (SoapObject) so.getProperty("bovino");

                        b.setCodigo(Long.parseLong(bo.getProperty("codigo").toString()));
                        b.setNome(bo.getProperty("nome").toString());

                        v.setBovino(b);

                        lista.add(v);

                    }

                }else{
                    mensagem = "Não foi encontrado nenhuma vacina";
                }


            }catch (ClassCastException e){

                SoapObject resposta = (SoapObject) envelope.getResponse();

                if (resposta != null) {
                    Vacina v = new Vacina();
                    Bovino b = new Bovino();

                    v.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                    v.setNome(resposta.getProperty("nome").toString());
                    v.setSituacao(Integer.parseInt(resposta.getProperty("situacao").toString()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    v.setDatavacina(sdf.parse(resposta.getProperty("datavacina").toString()));
                    if(resposta.getProperty("dataaplicacao") == null){
                        v.setDataaplicacao(null);
                    }else{
                        v.setDataaplicacao(sdf.parse(resposta.getProperty("dataaplicacao").toString()));
                    }

                    SoapObject bo = (SoapObject) resposta.getProperty("bovino");

                    b.setCodigo(Long.parseLong(bo.getProperty("codigo").toString()));
                    b.setNome(bo.getProperty("nome").toString());

                    v.setBovino(b);

                    lista.add(v);

                }else{
                    mensagem = "Não foi encontrado nenhuma vacina";
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

    public void atualizaEstado(Vacina v) throws SQLException{
        SoapObject atualizaEstado = new SoapObject(NAMESPACE, ATUALIZA_ESTADO);
        SoapObject vac = new SoapObject(NAMESPACE, "v");

        vac.addProperty("codigo", v.getCodigo());
        vac.addProperty("situacao", v.getSituacao());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        vac.addProperty("dataaplicacao", sdf.format(v.getDataaplicacao()));

        atualizaEstado.addSoapObject(vac);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(atualizaEstado);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/VacinaDAO?wsdl", 50000);
            http.call("urn:" + ATUALIZA_ESTADO, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

            mensagem = "Estado da vacina atualizado com sucesso";

        }catch (ConnectException e){
            e.printStackTrace();
            mensagem = "Falha na conexão. Verifique as configurações do aplicativo";

        }catch (Exception e2){
            e2.printStackTrace();
            mensagem = "Ocorreu uma falha inesperada no aplicativo";
        }

    }

    public ArrayList<Vacina> filtro(String filtro) throws SQLException {
        ArrayList<Vacina> lista = new ArrayList<Vacina>();

        SoapObject filtroVacinas = new SoapObject(NAMESPACE, FILTRO);
        filtroVacinas.addProperty("filtro", filtro);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(filtroVacinas);

        envelope.implicitTypes = true;

        try {
            HttpTransportSE http = new HttpTransportSE("http://" + ConexaoActivity.getIp() + "/WebService/services/VacinaDAO?wsdl", 50000);
            http.call("urn:" + FILTRO, envelope);

            try {
                Vector<SoapObject> resposta = (Vector<SoapObject>) envelope.getResponse();

                if (resposta != null){

                    for (SoapObject so: resposta) {
                        Vacina v = new Vacina();
                        Bovino b = new Bovino();

                        v.setCodigo(Long.parseLong(so.getProperty("codigo").toString()));
                        v.setNome(so.getProperty("nome").toString());
                        v.setSituacao(Integer.parseInt(so.getProperty("situacao").toString()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        v.setDatavacina(sdf.parse(so.getProperty("datavacina").toString()));
                        if(so.getProperty("dataaplicacao") == null){
                            v.setDataaplicacao(null);
                        }else{
                            v.setDataaplicacao(sdf.parse(so.getProperty("dataaplicacao").toString()));
                        }


                        SoapObject bo = (SoapObject) so.getProperty("bovino");

                        b.setCodigo(Long.parseLong(bo.getProperty("codigo").toString()));
                        b.setNome(bo.getProperty("nome").toString());

                        v.setBovino(b);

                        lista.add(v);

                    }

                }else{
                    mensagem = "Não foi encontrado nenhuma vacina";
                }


            }catch (ClassCastException e){

                SoapObject resposta = (SoapObject) envelope.getResponse();

                if (resposta != null) {
                    Vacina v = new Vacina();
                    Bovino b = new Bovino();

                    v.setCodigo(Long.parseLong(resposta.getProperty("codigo").toString()));
                    v.setNome(resposta.getProperty("nome").toString());
                    v.setSituacao(Integer.parseInt(resposta.getProperty("situacao").toString()));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    v.setDatavacina(sdf.parse(resposta.getProperty("datavacina").toString()));
                    if(resposta.getProperty("dataaplicacao") == null){
                        v.setDataaplicacao(null);
                    }else{
                        v.setDataaplicacao(sdf.parse(resposta.getProperty("dataaplicacao").toString()));
                    }

                    SoapObject bo = (SoapObject) resposta.getProperty("bovino");

                    b.setCodigo(Long.parseLong(bo.getProperty("codigo").toString()));
                    b.setNome(bo.getProperty("nome").toString());

                    v.setBovino(b);

                    lista.add(v);

                }else{
                    mensagem = "Não foi encontrado nenhuma vacina";
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
}
