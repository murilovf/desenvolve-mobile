package desenvolve.com.br.desenvolve.domain;

import java.util.Date;

/**
 * Created by Murilo on 27/02/2017.
 */
public class Vacina {

    private Long codigo;
    private Bovino bovino;
    private String nome;
    private Integer situacao;
    private Date datavacina;
    private Date dataaplicacao;

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
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Integer getSituacao() {
        return situacao;
    }
    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }
    public Date getDatavacina() {
        return datavacina;
    }
    public void setDatavacina(Date datavacina) {
        this.datavacina = datavacina;
    }

    public Date getDataaplicacao() {
        return dataaplicacao;
    }

    public void setDataaplicacao(Date dataaplicacao) {
        this.dataaplicacao = dataaplicacao;
    }

    @Override
    public String toString() {
        return "Vacina [codigo=" + codigo + ", bovino=" + bovino + ", nome=" + nome + ", situacao=" + situacao
                + ", datavacina=" + datavacina + "]";
    }
}
