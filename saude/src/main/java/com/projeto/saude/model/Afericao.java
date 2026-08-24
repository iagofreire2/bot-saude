package com.projeto.saude.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_afericao")
public class Afericao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    // Pressão separada para facilitar cálculos e alertas futuros
    private Integer pressaoSistolica;
    private Integer pressaoDiastolica;

    private Integer glicemia;
    private Integer batimentosCardiacos;

    // Construtor vazio exigido pelo JPA
    public Afericao() {
    }

    public Afericao(Integer pressaoSistolica, Integer pressaoDiastolica, Integer glicemia, Integer batimentosCardiacos) {
        this.dataHora = LocalDateTime.now(); // Pega a hora exata do registro
        this.pressaoSistolica = pressaoSistolica;
        this.pressaoDiastolica = pressaoDiastolica;
        this.glicemia = glicemia;
        this.batimentosCardiacos = batimentosCardiacos;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Integer getPressaoSistolica() { return pressaoSistolica; }
    public void setPressaoSistolica(Integer pressaoSistolica) { this.pressaoSistolica = pressaoSistolica; }

    public Integer getPressaoDiastolica() { return pressaoDiastolica; }
    public void setPressaoDiastolica(Integer pressaoDiastolica) { this.pressaoDiastolica = pressaoDiastolica; }

    public Integer getGlicemia() { return glicemia; }
    public void setGlicemia(Integer glicemia) { this.glicemia = glicemia; }

    public Integer getBatimentosCardiacos() { return batimentosCardiacos; }
    public void setBatimentosCardiacos(Integer batimentosCardiacos) { this.batimentosCardiacos = batimentosCardiacos; }
}