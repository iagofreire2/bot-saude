# 🩺 Assistente de Saúde Domiciliar (Telegram Bot)

Uma API desenvolvida em Java (Spring Boot) e integrada ao Telegram para o monitoramento e registro digital de sinais vitais em ambiente domiciliar.

Este projeto foi desenvolvido como requisito para a disciplina de Atividade Extensionista II do curso de Análise e Desenvolvimento de Sistemas (UNINTER), com foco em Tecnologia Aplicada à Inclusão Digital e alinhado ao ODS 3 (Saúde e Bem-Estar).

## 🎯 Objetivo
Substituir o uso de cadernos físicos por uma interface conversacional acessível via smartphone. O sistema permite o registro prático de métricas de saúde (pressão arterial, glicemia e frequência cardíaca) de pacientes idosos, gerando alertas automatizados e relatórios para facilitar o acompanhamento médico contínuo.

## ✨ Funcionalidades
* **Registro Interativo:** Inserção rápida de dados de saúde diretamente pelo chat do celular via Telegram.
* **Inteligência e Alertas:** Validação de regras de negócio em tempo real, informando imediatamente ao usuário se a pressão, glicemia ou batimentos estão fora dos padrões de normalidade.
* **Histórico (Extrato):** Geração instantânea de um relatório contendo as medições dos últimos 7 dias, formatado para facilitar a leitura.
* **Armazenamento Seguro:** Persistência de dados utilizando banco de dados relacional.

## 🛠️ Tecnologias Utilizadas
* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.x
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** H2 Database (em memória, para testes e homologação)
* **Integração:** Telegram Bots API (telegrambots-spring-boot-starter)
* **Qualidade e Testes:** JUnit 5 e Mockito para validação das regras de negócio.

## 🚀 Como Executar o Projeto

1. Clone o repositório:
   git clone https://github.com/seu-usuario/assistente-saude-telegram.git

2. Crie o seu Bot no Telegram:
    * Inicie uma conversa com o @BotFather no Telegram.
    * Envie o comando /newbot e siga as instruções para gerar o seu Token.

3. Configure as Variáveis:
    * Abra o arquivo src/main/resources/application.properties.
    * Insira o nome de usuário do seu bot e o token gerado:
      telegram.bot.username=seu_nome_de_bot
      telegram.bot.token=SEU_TOKEN_AQUI

4. Execute a Aplicação:
    * Via IDE (IntelliJ, Eclipse, etc.), rode a classe principal SaudeApplication.java ou utilize o Maven:
      mvn spring-boot:run

## 📱 Comandos do Bot
No Telegram, inicie uma conversa com o seu bot e utilize os seguintes comandos:
* /start - Exibe a mensagem de boas-vindas e o tutorial de uso.
* /registrar [sistólica] [diastólica] [glicemia] [bpm] - Salva uma nova medição. (Ex: /registrar 120 80 95 70).
* /extrato - Retorna o histórico de medições recentes.

## 👨‍💻 Autor
Iago de Jesus Freire
Estudante de Análise e Desenvolvimento de Sistemas
Projeto de Extensão Universitária - UNINTER