# TaskApp — Gerenciador de Tarefas (Kotlin + MVVM + Clean Architecture)

## 1. Visão Geral do Projeto

O **TaskApp** é um gerenciador de tarefas pessoal moderno, focado em organização por prioridades e status de conclusão.
O aplicativo foi construído com uma abordagem reativa, onde a interface reflete automaticamente as mudanças no banco de dados em tempo real, sem necessidade de recarregar manualmente a tela.

O projeto foi desenvolvido com foco em:

* Arquitetura escalável
* Código desacoplado
* Persistência local robusta
* Interface moderna com Material Design 3
* Atualizações reativas usando Flow e LiveData

---

## 2. Arquitetura Técnica (MVVM + Clean Architecture)

O projeto segue os padrões modernos recomendados pelo Google, utilizando **MVVM** aliado à **Clean Architecture**.

### Model (Task)

Representação dos dados da aplicação:

* ID
* Título
* Descrição
* Prioridade
* Status (Pendente / Concluído)

Essa camada contém apenas a regra de negócio central da aplicação.

### Data (Room + Repository)

* Uso do **Room Database** para persistência local baseada em SQLite
* Implementação de **DAO (Data Access Object)** para acesso aos dados
* Uso de **Repository** para centralizar a lógica de dados e abstrair a origem das informações

O Repository atua como mediador entre ViewModel e banco de dados.

### ViewModel (TaskViewModel)

Responsável pela lógica da aplicação:

* Gerenciamento do estado da UI
* Aplicação de filtros por prioridade e status
* Comunicação com o banco via Coroutines
* Exposição de dados reativos com Flow e LiveData
* Sobrevivência a mudanças de configuração

### View (Fragment / Activity)

Responsável apenas por:

* Renderização da interface
* Captura de eventos do usuário
* Observação dos estados da ViewModel

A View não possui lógica de persistência, mantendo o desacoplamento da arquitetura.

---

## 3. Tecnologias Utilizadas

### Linguagem

* Kotlin (sintaxe idiomática + Coroutines)

### Banco de Dados

* Room Database (abstração do SQLite)

### Componentes Jetpack

* ViewModel
* LiveData
* Kotlin Flow
* Fragment KTX
* Activity Result API

### Interface (UI)

* Material Design 3
* TabLayout
* Material Chips
* FloatingActionButton
* RecyclerView

### Gerenciador de Dependências

* Gradle (Kotlin DSL / Groovy)

---

## 4. Funcionalidades Implementadas

### A. Gestão de Tarefas (CRUD)

**Criação e Persistência**

* Adição de novas tarefas
* Persistência local utilizando Room (SQLite)
* Atualização automática da lista

**Edição em Tempo Real**

* Tela de detalhes transformada em interface editável
* Uso de EditText para edição de título e descrição
* Atualização imediata no banco de dados

**Exclusão**

* Remoção permanente de tarefas
* Atualização automática da UI após exclusão

---

### B. Sistema de Filtros 

**Filtro por Status (Abas)**

* Implementação com TabLayout
* Separação entre tarefas Pendentes e Concluídas
* Atualização automática ao marcar CheckBox
* Movimentação dinâmica entre abas

**Filtro por Prioridade (Chips)**

* Chips de prioridade: Alta, Média e Baixa
* Filtragem combinada com status atual
* Atualização reativa da lista

---

### C. Experiência do Usuário (UX/UI)

**Saudação Dinâmica**

* Identificação automática da hora do sistema
* Exibição de:

  * Bom dia
  * Boa tarde
  * Boa noite

**Feedback Visual de Conclusão**

* Opacidade reduzida para tarefas concluídas
* Texto riscado indicando finalização
* CheckBox sincronizado com status

**Empty State**

* Exibição de layout informativo quando não há tarefas
* Mensagem específica para filtros vazios
* Melhor experiência de uso

---



## 5. Instruções de Execução

### Pré-requisitos

* Android Studio (versão Ladybug ou superior recomendada)
* Android SDK 34 ou superior
* Dispositivo físico ou emulador com Android 10 (API 29) ou superior

### Passos para Execução

1. Faça o download ou clone o repositório do projeto
2. Abra o Android Studio e selecione Open
3. Navegue até a pasta raiz do projeto
4. Aguarde a sincronização do Gradle (Gradle Sync)
5. Verifique se não há erros de dependências
6. Certifique-se da remoção da biblioteca media3 incompatível com o SDK de compilação
7. Clique em Run ou utilize o atalho:

```
Shift + F10
```

---

## 6. Benefícios da Arquitetura

* Código desacoplado
* Fácil manutenção
* Alta escalabilidade
* Facilidade de testes
* UI reativa
* Separação clara de responsabilidades
* Padrão recomendado pelo Google
