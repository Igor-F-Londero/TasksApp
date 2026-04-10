# Lista de Tarefas
Tecnologias Utilizadas
•
Linguagem: Kotlin (focando em sintaxe idiomática e Coroutines).
•
Banco de Dados: Room Database (abstração do SQLite para persistência local robusta).
•
Componentes de Arquitetura (Jetpack):
◦
ViewModel: Para gestão de estado da UI e sobrevivência a mudanças de configuração.
◦
Flow & LiveData: Para streams de dados reativos e assíncronos.
◦
Fragment KTX & Activity Result API: Para comunicação moderna e segura entre telas.
•
Interface (UI): Material Design 3 (utilizando Google Chips, TabLayout e FloatingActionButton).
•
Gerenciador de Dependências: Gradle (Kotlin DSL/Groovy).
4. Arquitetura Adotada
O projeto foi desenvolvido seguindo o padrão MVVM (Model-View-ViewModel) aliado a princípios de Clean Architecture:
•
Camada de Domínio (Model): Define a entidade Task, representando a regra de negócio central.
•
Camada de Dados (Data):
◦
DAO (Data Access Object): Interface de comunicação com o SQLite.
◦
Repository: Atua como mediador entre a fonte de dados (Room) e o restante da aplicação, abstraindo a origem dos dados.
•
Camada de Apresentação (UI):
◦
ViewModel: Processa os dados do repositório, aplica filtros lógicos e expõe estados para a View.
◦
View (Activities/Fragments): Responsável apenas pela renderização visual e captura de eventos, mantendo-se "burra" em relação à lógica de persistência.
Benefícios: Facilidade de testes, desacoplamento de código e manutenção simplificada.
5. Instruções de Execução
Para rodar o projeto em um ambiente de desenvolvimento ou avaliação:
1.
Pré-requisitos:
◦
Android Studio (versão Ladybug ou superior recomendada).
◦
Android SDK 34 (ou superior).
◦
Dispositivo físico ou Emulador com Android 10 (API 29) ou superior.
2.
Passos para Execução:
◦
Faça o download ou clone o repositório do projeto.
◦
Abra o Android Studio e selecione "Open", navegando até a pasta raiz do projeto.
◦
Aguarde a sincronização do Gradle (Gradle Sync).
◦
Certifique-se de que não há erros de dependências (especialmente a remoção da biblioteca media3 incompatível com o SDK de compilação).
◦
Clique no botão Run (ícone de play verde) ou use o atalho Shift + F10.


