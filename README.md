# Lista de Tarefas
## 1. Tecnologias Utilizadas

* **Linguagem:** Kotlin (com foco em sintaxe idiomática e uso de Coroutines).
* **Banco de Dados:** Room Database (abstração do SQLite para persistência local robusta).

### Componentes de Arquitetura (Jetpack)

* **ViewModel:** Gestão de estado da UI e sobrevivência a mudanças de configuração.
* **Flow & LiveData:** Streams de dados reativos e assíncronos.
* **Fragment KTX & Activity Result API:** Comunicação moderna e segura entre telas.

### Interface (UI)

* Material Design 3
* Google Chips
* TabLayout
* FloatingActionButton

### Gerenciador de Dependências

* Gradle (Kotlin DSL / Groovy)

---

## 2. Arquitetura Adotada

O projeto foi desenvolvido seguindo o padrão **MVVM (Model-View-ViewModel)** aliado a princípios de **Clean Architecture**, promovendo separação de responsabilidades, testabilidade e manutenção facilitada.

### Camada de Domínio (Model)

Define a entidade `Task`, representando a regra de negócio central da aplicação.

### Camada de Dados (Data)

**DAO (Data Access Object)**
Interface responsável pela comunicação direta com o banco SQLite via Room.

**Repository**
Atua como mediador entre a fonte de dados (Room) e o restante da aplicação, abstraindo a origem dos dados e centralizando a lógica de acesso.

### Camada de Apresentação (UI)

**ViewModel**
Processa os dados vindos do repositório, aplica filtros lógicos e expõe estados para a interface.

**View (Activities / Fragments)**
Responsável apenas pela renderização visual e captura de eventos, mantendo-se desacoplada da lógica de persistência.

### Benefícios da Arquitetura

* Facilidade de testes
* Desacoplamento de código
* Manutenção simplificada
* Melhor organização do projeto
* Escalabilidade

---

## 3. Instruções de Execução

### Pré-requisitos

* Android Studio (versão Ladybug ou superior recomendada)
* Android SDK 34 ou superior
* Dispositivo físico ou emulador com Android 10 (API 29) ou superior

### Passos para Execução

1. Faça o download ou clone o repositório do projeto.
2. Abra o Android Studio e selecione **Open**, navegando até a pasta raiz do projeto.
3. Aguarde a sincronização do Gradle (Gradle Sync).
4. Certifique-se de que não há erros de dependências (especialmente a remoção da biblioteca `media3` incompatível com o SDK de compilação).
5. Clique no botão **Run** (ícone de play verde) ou utilize o atalho:

```
Shift + F10
```

