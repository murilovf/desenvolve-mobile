package desenvolve.com.br.desenvolve.adapter;

import android.content.Context;
import android.graphics.Color;
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
import desenvolve.com.br.desenvolve.domain.Vacina;

/**
 * Created by Murilo on 27/02/2017.
 */
public class VacinaAdapter extends BaseAdapter {

    private List<Vacina> vacinas;

    //Classe utilizada para instanciar os objetos do XML
    private LayoutInflater inflater;

    public VacinaAdapter(Context context, List<Vacina> plistVacinas) {
        this.vacinas = plistVacinas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Vacina item) {
        this.vacinas.add(item);
        //Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return vacinas.size();
    }

    @Override
    public Object getItem(int position) {
        return vacinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //Pega o registro da lista e trasnfere para o objeto estadoVO
        Vacina vacinaVO = vacinas.get(position);

        //Utiliza o XML estado_row para apresentar na tela
        convertView = inflater.inflate(R.layout.layout_vacina, null);

        //Instância os objetos do XML
        TextView txtCodigoVacinaLista = (TextView)convertView.findViewById(R.id.txtCodigoVacinaLista);
        TextView txtBovinoLista = (TextView)convertView.findViewById(R.id.txtBovinoLista);
        TextView txtVacinaLista = (TextView)convertView.findViewById(R.id.txtVacinaLista);
        TextView txtDataVacinaLista = (TextView)convertView.findViewById(R.id.txtDataVacinaLista);
        TextView txtSituacaoLista = (TextView)convertView.findViewById(R.id.txtSituacaoLista);
        TextView txtDataAplicacaoLista = (TextView)convertView.findViewById(R.id.txtDataAplicacaoLista);

        //pega os dados que estão no objeto estadoVO e transfere para os objetos do XML
        txtCodigoVacinaLista.setText(String.format("%06d",vacinaVO.getCodigo()));
        txtBovinoLista.setText(vacinaVO.getBovino().getNome().toString());
        txtVacinaLista.setText(vacinaVO.getNome().toString());
        if (vacinaVO.getSituacao().toString().equals("1")){
            txtSituacaoLista.setText("APLICADO");
            txtSituacaoLista.setTextColor(Color.parseColor("#006400"));
        }else{
            txtSituacaoLista.setText("NÃO APLICADO");
            txtSituacaoLista.setTextColor(Color.RED);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        txtDataVacinaLista.setText(sdf.format(vacinaVO.getDatavacina()));
        if (vacinaVO.getDataaplicacao() == null){
            txtDataAplicacaoLista.setText("");
        }else{
            txtDataAplicacaoLista.setText(sdf.format(vacinaVO.getDataaplicacao()));
        }

        return convertView;
    }
}
