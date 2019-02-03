package desenvolve.com.br.desenvolve.domain;

import java.util.Date;

/**
 * Created by Murilo on 06/02/2017.
 */
public class Bovino {

    private Long codigo;
    private String nome;
    private Date datanascimento;
    private String raca;
    private String origem;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(Date datanascimento) {
        this.datanascimento = datanascimento;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    @Override
    public String toString() {
        return "Bovino [codigo=" + codigo + ", nome=" + nome + ", datanascimento=" + datanascimento + ", raca=" + raca
                + ", origem=" + origem + "]";
    }
}
