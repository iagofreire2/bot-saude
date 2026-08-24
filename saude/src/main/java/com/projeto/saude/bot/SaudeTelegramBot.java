package com.projeto.saude.bot;

import com.projeto.saude.model.Afericao;
import com.projeto.saude.service.AfericaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.format.DateTimeFormatter; // Adicione isso lá no topo junto com os outros imports
import java.util.List;

@Component
public class SaudeTelegramBot extends TelegramLongPollingBot {

    private final AfericaoService afericaoService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    public SaudeTelegramBot(@Value("${telegram.bot.token}") String botToken, AfericaoService afericaoService) {
        super(botToken);
        this.afericaoService = afericaoService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    // Método que é acionado toda vez que alguém manda mensagem pro bot
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String mensagemTexto = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (mensagemTexto.startsWith("/registrar")) {
                processarRegistro(chatId, mensagemTexto);
            } else if (mensagemTexto.startsWith("/extrato")) {
                processarExtrato(chatId); // Chamada para o novo método
            } else if (mensagemTexto.startsWith("/start")) {
                String mensagemBoasVindas = "Olá! 👋 Bem-vindo(a) ao seu Assistente de Saúde Domiciliar.\n\n"
                        + "Estou aqui para ajudar a organizar o histórico de saúde de forma simples e rápida, aposentando o caderninho de anotações! 📓➡️📱\n\n"
                        + "🩺 *Como registrar uma nova medição:*\n"
                        + "Digite /registrar seguido dos 4 valores abaixo, separados apenas por um espaço:\n"
                        + "1️⃣ Pressão Sistólica (o valor maior, ex: 120)\n"
                        + "2️⃣ Pressão Diastólica (o valor menor, ex: 80)\n"
                        + "3️⃣ Glicemia (ex: 95)\n"
                        + "4️⃣ Batimentos Cardíacos (ex: 70)\n\n"
                        + "📝 *Exemplo prático:*\n"
                        + "Se a pressão foi 12x8, a glicemia 95 e o coração a 70 bpm, você deve digitar exatamente assim:\n"
                        + "`/registrar 120 80 95 70`\n\n"
                        + "📊 *Para consultar o histórico:*\n"
                        + "A qualquer momento, digite /extrato para receber o relatório das medições dos últimos dias.";

                enviarMensagem(chatId, mensagemBoasVindas);
            } else {
                enviarMensagem(chatId, "Comando não reconhecido. Use /registrar ou /extrato.");
            }
        }
    }

    private void processarRegistro(long chatId, String texto) {
        try {
            // Divide o texto pelos espaços. Ex: ["/registrar", "120", "80", "95", "70"]
            String[] partes = texto.split(" ");

            if (partes.length < 5) {
                enviarMensagem(chatId, "⚠️ Formato incorreto. Use: /registrar Sistolica Diastolica Glicemia Batimentos");
                return;
            }

            Integer sistolica = Integer.parseInt(partes[1]);
            Integer diastolica = Integer.parseInt(partes[2]);
            Integer glicemia = Integer.parseInt(partes[3]);
            Integer bpm = Integer.parseInt(partes[4]);

            // Salva no banco de dados
            Afericao afericao = afericaoService.registrarAfericao(sistolica, diastolica, glicemia, bpm);

            // Pega a análise de alertas que fizemos na etapa anterior
            String analise = afericaoService.analisarAfericao(afericao);

            enviarMensagem(chatId, "✅ Registro salvo com sucesso!\n\n" + analise);

        } catch (NumberFormatException e) {
            enviarMensagem(chatId, "⚠️ Erro: Por favor, digite apenas números após o comando.");
        } catch (Exception e) {
            enviarMensagem(chatId, "⚠️ Ocorreu um erro ao salvar: " + e.getMessage());
        }
    }

    private void processarExtrato(long chatId) {
        try {
            // Busca o histórico dos últimos 7 dias chamando o Service
            List<Afericao> historico = afericaoService.obterHistoricoRecente(7);

            if (historico.isEmpty()) {
                enviarMensagem(chatId, "📭 Nenhum registro encontrado nos últimos 7 dias.");
                return;
            }

            StringBuilder relatorio = new StringBuilder("📅 *Extrato dos Últimos 7 Dias:*\n\n");

            // Formatador para deixar a data no padrão brasileiro
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Afericao af : historico) {
                relatorio.append("🔹 *").append(af.getDataHora().format(formatter)).append("*\n");
                relatorio.append("🩸 Pressão: ").append(af.getPressaoSistolica()).append("x").append(af.getPressaoDiastolica()).append("\n");
                relatorio.append("🍬 Glicemia: ").append(af.getGlicemia()).append(" mg/dL\n");
                relatorio.append("❤️ BPM: ").append(af.getBatimentosCardiacos()).append("\n\n");
            }

            enviarMensagem(chatId, relatorio.toString());

        } catch (Exception e) {
            enviarMensagem(chatId, "⚠️ Erro ao gerar extrato: " + e.getMessage());
        }
    }

    private void enviarMensagem(long chatId, String texto) {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(String.valueOf(chatId));
        mensagem.setText(texto);

        try {
            execute(mensagem);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}