package com.projeto.saude.service;

import com.projeto.saude.model.Afericao;
import com.projeto.saude.repository.AfericaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AfericaoServiceTest {

    // Cria um objeto "falso" do repositório para não precisarmos do banco de dados real
    @Mock
    private AfericaoRepository repository;

    // Injeta o repositório falso dentro da nossa classe de serviço real
    @InjectMocks
    private AfericaoService service;

    private Afericao afericaoNormal;
    private Afericao afericaoAlta;

    // Este método roda antes de cada teste para preparar os dados
    @BeforeEach
    void setUp() {
        afericaoNormal = new Afericao(120, 80, 95, 75); // 12x8, glicose 95, 75 bpm
        afericaoAlta = new Afericao(150, 95, 140, 110); // 15x9.5, glicose 140, 110 bpm
    }

    @Test
    void deveRegistrarAfericaoComSucesso() {
        // Simula o comportamento do banco: quando pedirmos para salvar, retorne a aferição
        when(repository.save(any(Afericao.class))).thenReturn(afericaoNormal);

        Afericao resultado = service.registrarAfericao(120, 80, 95, 75);

        // Verifica se o resultado não é nulo e se os dados estão corretos
        assertNotNull(resultado);
        assertEquals(120, resultado.getPressaoSistolica());

        // Verifica se o método save do repositório foi chamado exatamente 1 vez
        verify(repository, times(1)).save(any(Afericao.class));
    }

    @Test
    void deveLancarExcecaoQuandoPressaoForNula() {
        // Testa a regra de negócio que impede salvar dados sem a pressão arterial
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.registrarAfericao(null, 80, 90, 70);
        });

        assertEquals("Os valores da pressão arterial não podem ser nulos.", exception.getMessage());

        // Garante que o banco de dados NÃO foi chamado (segurança)
        verify(repository, never()).save(any(Afericao.class));
    }

    @Test
    void deveGerarAlertaParaValoresAltos() {
        // Testa se a inteligência do sistema detecta alterações
        String analise = service.analisarAfericao(afericaoAlta);

        assertTrue(analise.contains("Pressão arterial elevada"));
        assertTrue(analise.contains("Glicemia alterada"));
        assertTrue(analise.contains("Frequência cardíaca fora do padrão"));
    }

    @Test
    void deveGerarStatusNormalParaValoresControlados() {
        // Testa se o sistema reconhece quando a saúde está sob controle
        String analise = service.analisarAfericao(afericaoNormal);

        assertTrue(analise.contains("Pressão arterial dentro da normalidade"));
        assertTrue(analise.contains("Glicemia controlada"));
        assertTrue(analise.contains("Frequência cardíaca normal"));
    }
}