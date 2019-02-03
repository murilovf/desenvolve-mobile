package desenvolve.com.br.desenvolve.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Murilo on 17/02/2017.
 */
public class Medida {
    private Long codigo;
    private Bovino bovino;
    private Integer dias;
    private Integer mes;
    private Double peso;
    private Double altura;
    private Double circunferencia;
    private Date datamedicao;
    private Date alteracao;

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Date getAlteracao() {
        return alteracao;
    }
    public void setAlteracao(Date alteracao) {
        this.alteracao = alteracao;
    }
    public Integer getDias() {
        return dias;
    }
    public void setDias(Integer dias) {
        this.dias = dias;
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
    public Double getPeso() {
        return peso;
    }
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    public Double getAltura() {
        return altura;
    }
    public void setAltura(Double altura) {
        this.altura = altura;
    }
    public Double getCircunferencia() {
        return circunferencia;
    }
    public void setCircunferencia(Double circunferencia) {
        this.circunferencia = circunferencia;
    }
    public Date getDatamedicao() {
        return datamedicao;
    }
    public void setDatamedicao(Date datamedicao) {
        this.datamedicao = datamedicao;
    }
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return "\nPeso: " + peso + "\nAltura: " + altura + "\nCircunferência " + circunferencia + "\nDias: " + dias + "\nData da Medida: "
                + sdf.format(datamedicao);
    }

}
