package desenvolve.com.br.desenvolve.domain;

/**
 * Created by Murilo on 24/03/2017.
 */
public class AlertaErro {
    private Long codigo;
    private Alerta alerta;
    private String categoria;
    private String descricao;


    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    public Alerta getAlerta() {
        return alerta;
    }
    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
