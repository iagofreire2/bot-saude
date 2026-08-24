package com.projeto.saude.service;

import com.projeto.saude.model.Afericao;
import com.projeto.saude.repository.AfericaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AfericaoService {

    private final AfericaoRepository repository;

    // Injeção de dependência via construtor (melhor prática em Java/Spring)
    public AfericaoService(AfericaoRepository repository) {
        this.repository = repository;
    }

    // Método para salvar os dados vindos do Telegram
    public Afericao registrarAfericao(Integer sistolica, Integer diastolica, Integer glicemia, Integer bpm) {

        // Validação de negócio: Não faz sentido registrar sem a pressão ou glicemia
        if (sistolica == null || diastolica == null) {
            throw new IllegalArgumentException("Os valores da pressão arterial não podem ser nulos.");
        }

        Afericao novaAfericao = new Afericao(sistolica, diastolica, glicemia, bpm);
        return repository.save(novaAfericao);
    }

    // Método para puxar o relatório dos últimos 'X' dias
    public List<Afericao> obterHistoricoRecente(int dias) {
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(dias);
        LocalDateTime dataFim = LocalDateTime.now();

        return repository.findByDataHoraBetweenOrderByDataHoraDesc(dataInicio, dataFim);
    }

    // Regra de negócio extra: Inteligência do sistema para gerar alertas no chat
    public String analisarAfericao(Afericao afericao) {
        StringBuilder analise = new StringBuilder("📊 *Resumo da Avaliação:*\n");

        // Alerta básico de pressão (Pode ser ajustado conforme a recomendação médica dela)
        if (afericao.getPressaoSistolica() >= 140 || afericao.getPressaoDiastolica() >= 90) {
            analise.append("⚠️ *Atenção:* Pressão arterial elevada (").append(afericao.getPressaoSistolica()).append("x").append(afericao.getPressaoDiastolica()).append(").\n");
        } else {
            analise.append("✅ Pressão arterial dentro da normalidade.\n");
        }

        // Alerta de Glicemia
        if (afericao.getGlicemia() != null) {
            if (afericao.getGlicemia() > 130) {
                analise.append("⚠️ *Atenção:* Glicemia alterada (").append(afericao.getGlicemia()).append(" mg/dL).\n");
            } else {
                analise.append("✅ Glicemia controlada.\n");
            }
        }

        // Alerta de Batimentos Cardíacos (BPM)
        if (afericao.getBatimentosCardiacos() != null) {
            if (afericao.getBatimentosCardiacos() < 50 || afericao.getBatimentosCardiacos() > 100) {
                analise.append("⚠️ *Atenção:* Frequência cardíaca fora do padrão de repouso (").append(afericao.getBatimentosCardiacos()).append(" bpm).\n");
            } else {
                analise.append("✅ Frequência cardíaca normal.\n");
            }
        }

        return analise.toString();
    }
}