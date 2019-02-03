package desenvolve.com.br.desenvolve.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import desenvolve.com.br.desenvolve.R;
import desenvolve.com.br.desenvolve.domain.Medida;

/**
 * Created by Murilo on 23/02/2017.
 */
public class MedidaAdapter extends BaseAdapter {
    private List<Medida> medidas;

    //Classe utilizada para instanciar os objetos do XML
    private LayoutInflater inflater;

    public MedidaAdapter(Context context, List<Medida> plistMedidas) {
        this.medidas = plistMedidas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Medida item) {
        this.medidas.add(item);
        //Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return medidas.size();
    }

    @Override
    public Object getItem(int position) {
        return medidas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //Pega o registro da lista e trasnfere para o objeto estadoVO
        Medida medidaVO = medidas.get(position);

        //Utiliza o XML estado_row para apresentar na tela
        convertView = inflater.inflate(R.layout.layout_historico_medidas, null);

        //Instância os objetos do XML
        TextView txtPesoLista = (TextView)convertView.findViewById(R.id.txtPesoLista);
        TextView txtAlturaLista = (TextView)convertView.findViewById(R.id.txtAlturaLista);
        TextView txtCircunferenciaLista = (TextView)convertView.findViewById(R.id.txtCircunferenciaLista);
        TextView txtDiasLista = (TextView)convertView.findViewById(R.id.txtDiasLista);
        TextView txtDataMedidaLista = (TextView)convertView.findViewById(R.id.txtDataMedidaLista);

        //pega os dados que estão no objeto estadoVO e transfere para os objetos do XML
        txtPesoLista.setText(String.format("%.2f", medidaVO.getPeso()).toString().replace(".", ","));
        txtAlturaLista.setText(String.format("%.2f", medidaVO.getAltura()).toString().replace(".", ","));
        txtCircunferenciaLista.setText(String.format("%.2f", medidaVO.getCircunferencia()).toString().replace(".", ","));
        txtDiasLista.setText(medidaVO.getDias().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        txtDataMedidaLista.setText(sdf.format(medidaVO.getDatamedicao()));

        return convertView;
    }

}
