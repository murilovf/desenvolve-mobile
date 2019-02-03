package desenvolve.com.br.desenvolve.domain;

/**
 * Created by Murilo on 05/03/2017.
 */
public class ConfiguracaoMedidas {
    private Long codigo;
    private Integer mes;
    private Double peso;
    private Double altura;
    private Double circunferencia;

    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    public Integer getMes() {
        return mes;
    }
    public void setMes(Integer mes) {
        this.mes = mes;
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

}
