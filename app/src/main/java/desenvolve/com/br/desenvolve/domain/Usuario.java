package desenvolve.com.br.desenvolve.domain;

import java.util.Date;

/**
 * Created by Murilo on 12/02/2017.
 */
public class Usuario {
    private Long codigo;
    private String nome;
    private Date datanascimento;
    private String login;
    private String senha;
    private Integer perfil;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getPerfil() {
        return perfil;
    }

    public void setPerfil(Integer perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return "Usuario [codigo=" + codigo + ", nome=" + nome + ", datanascimento=" + datanascimento
                + ", login=" + login + ", senha=" + senha + ", perfil=" + perfil + "]";
    }

}
