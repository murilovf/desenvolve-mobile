package desenvolve.com.br.desenvolve.domain;

import java.util.Date;

/**
 * Created by Murilo on 30/03/2017.
 */
public class Alerta {

    private Long codigo;
    private Medida medida;
    private Bovino bovino;
    private Date data;
    private String categoria;

    public Medida getMedida() {
        return medida;
    }
    public void setMedida(Medida medida) {
        this.medida = medida;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    public Bovino getBovino() {
        return bovino;
    }
    public void setBovino(Bovino bovino) {
        this.bovino = bovino;
    }
}
