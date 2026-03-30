# TasksApp


. Arquitetura do Projeto
O projeto está dividido em camadas lógicas para isolar a interface do usuário da lógica de negócio e do acesso a dados.
A. Camada de Domínio (Domain)
•
Modelos (Task): Define a estrutura de dados da tarefa, incluindo ID, título, descrição, prioridade e status de conclusão.
•
Regras de Negócio: Contém as entidades puras do sistema.
B. Camada de Dados (Data)
•
Room Database (AppDatabase): Implementação da persistência local via SQLite.
•
DAO (TaskDao): Interface que define os comandos SQL (Insert, Update, Delete, Query) para interagir com o banco de dados.
•
Repository (TaskRepository): Atua como uma única fonte de verdade. Ele decide de onde os dados vêm e abstrai a complexidade do banco de dados para o restante do app.
C. Camada de UI (Apresentação - MVVM)
•
View (MainActivity, TaskListFragment, TaskDetailActivity): Responsável apenas por exibir os dados e capturar interações do usuário.
•
ViewModel (TaskViewModel): Gerencia o estado da UI. Ele utiliza Kotlin Flows para emitir atualizações de dados de forma reativa e sobrevive a mudanças de configuração (como rotação de tela).
•
ViewModelFactory: Necessário para injetar a dependência do repositório no ViewModel.
2. Funcionalidades Principais
2.1. Gestão de Tarefas (CRUD)
•
Criação: Através de um FloatingActionButton que abre a NewTaskActivity. Os dados são validados e salvos no banco de dados.
•
Visualização/Edição: Ao clicar em uma tarefa, o usuário é levado à tela de detalhes. Lá, ele pode editar o título, a descrição e o status.
•
Exclusão: Funcionalidade integrada na tela de detalhes para remover tarefas obsoletas.
2.2. Sistema de Filtros Inteligentes
•
Filtro por Status: Utiliza um TabLayout para separar tarefas em Pendentes e Concluídas. Ao marcar uma tarefa como concluída, ela é movida automaticamente entre as abas graças à reatividade do Flow.
•
Filtro por Prioridade: Uso de Material Chips para filtrar a lista atual (seja pendente ou concluída) por níveis: Alta, Média ou Baixa.
2.3. Interface Dinâmica e UX
•
Saudação Temporal: A MainActivity detecta o horário do sistema para exibir uma saudação personalizada (Bom dia, Boa tarde ou Boa noite) ao usuário.
•
Empty State: O TaskListFragment gerencia automaticamente a visibilidade de uma ilustração/texto de "Lista Vazia" quando não há tarefas que correspondam aos filtros aplicados.
•
Comunicação via Result API: Uso de registerForActivityResult para comunicação segura e moderna entre Activities, substituindo o antigo onActivityResult.
3. Stack Tecnológica
•
Linguagem: Kotlin
•
Persistência: Room Database
•
Concorrência: Kotlin Coroutines & Flow
•
UI Components: Material Design 3 (Chips, TabLayout, FloatingActionButton)
•
Jetpack Libraries: ViewModel, LiveData/Flow, Fragment KTX, Activity KTX.
4. Fluxo de Dados (Exemplo: Marcar como Concluída)
1.
O Usuário clica no CheckBox no TaskAdapter.
2.
O Adapter aciona um callback para o TaskListFragment.
3.
O Fragment chama viewModel.update(task).
4.
O ViewModel inicia uma viewModelScope.launch e chama o repository.update(task).
5.
O Repository executa a atualização no TaskDao.
6.
O Banco de Dados (Room) emite uma nova lista via Flow.
7.
O ViewModel recebe a lista, aplica os filtros ativos e emite para a UI.
8.
O Fragment recebe a nova lista e o adapter.updateList() atualiza a tela instantaneamente.
