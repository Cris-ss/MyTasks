# MyTasks

## Descrição do Projeto

Este é um aplicativo Android de gerenciamento de tarefas que permite aos usuários adicionar, visualizar, editar e excluir tarefas. O aplicativo utiliza o Room para persistência de dados e o WorkManager para agendar notificações de lembrete para as tarefas. Com uma interface amigável, o usuário pode facilmente gerenciar suas atividades diárias.

## Funcionalidades

### 1. Adicionar Tarefas
- Os usuários podem adicionar novas tarefas preenchendo um formulário com título, descrição, data e horário.
- O aplicativo formata automaticamente a entrada de data e hora para garantir a consistência.

### 2. Listar Tarefas
- A tela principal apresenta uma lista de tarefas adicionadas, exibindo títulos e informações básicas.
- As tarefas são carregadas a partir do banco de dados usando o Room.

### 3. Visualizar Detalhes da Tarefa
- Ao clicar em uma tarefa, os usuários podem visualizar os detalhes, incluindo título, descrição, data e hora.

### 4. Editar Tarefas
- Os usuários podem editar tarefas existentes. Ao salvar as alterações, as notificações correspondentes são atualizadas.

### 5. Excluir Tarefas
- Tarefas podem ser removidas permanentemente através de uma opção de exclusão.

### 6. Notificações
- O aplicativo utiliza o WorkManager para agendar notificações para as tarefas. As notificações são acionadas na data e horário especificados pelo usuário.

## Autores

- **Cristian Souza da Cruz** - RA: 72300141

## Tecnologias Utilizadas

- Kotlin
- Android SDK
- Room Database
- WorkManager
- RecyclerView
- Material Design

## Instruções para Execução

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio

2. Abra o projeto no Android Studio.


3. Certifique-se de que o Gradle esteja configurado corretamente e que todas as dependências sejam baixadas.


4. Execute o aplicativo em um dispositivo Android ou emulador.


Contribuições são bem-vindas! Se você deseja ajudar a melhorar este projeto, sinta-se à vontade para abrir um "issue" ou "pull request".


Licença
Este projeto é de código aberto e está licenciado sob a Licença MIT.


### Detalhes a serem preenchidos

- **URL do repositório:** Substitua a URL do repositório pelo link real onde seu projeto está hospedado.
- **Autores:** Adicione mais autores se houver.
- **Licença:** Caso utilize uma licença diferente da MIT, modifique a seção correspondente.

Sinta-se à vontade para adicionar mais seções conforme necessário, como uma seção de perguntas frequentes ou uma lista de recursos.
