package com.projeto.saude.repository;

import com.projeto.saude.model.Afericao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AfericaoRepository extends JpaRepository<Afericao, Long> {

    // Método customizado para quando formos gerar o extrato/relatório em PDF
    List<Afericao> findByDataHoraBetweenOrderByDataHoraDesc(LocalDateTime inicio, LocalDateTime fim);
}