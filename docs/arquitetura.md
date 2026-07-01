# Arquitetura do Projeto — Proteção Ocular (Wear OS)

> Documento de especificação técnica (Spec Driven Design)
> Disciplina: Desenvolvimento para Dispositivos Portáteis
> Tipo: Documento de Arquitetura

---

## 1. Contexto do Projeto

### 1.1 Visão Geral

O **Proteção Ocular** é uma aplicação Wear OS que monitora a luminosidade do ambiente em tempo real, utilizando o sensor de luz do dispositivo (`TYPE_LIGHT`), e orienta o usuário sobre condições de iluminação inadequadas para o uso de telas ou leitura — prevenindo cansaço visual causado por ambientes muito escuros (esforço de foco) ou muito claros (ofuscamento/reflexo).

O aplicativo classifica continuamente o ambiente em categorias de luminosidade e reage automaticamente na interface, sem exigir ação manual do usuário para a leitura básica, mas oferecendo controles simples de interação (ligar/desligar monitoramento, consultar histórico).

### 1.2 Problema a ser resolvido

Usuários de smartwatch frequentemente usam telas (celular, tablet, o próprio relógio) em condições de luz inadequadas, sem perceber, o que contribui para fadiga ocular ao longo do dia. O relógio, por estar sempre no pulso, é um ponto natural de monitoramento passivo desse tipo de exposição.

### 1.3 Objetivo do sistema

- Ler a luminosidade ambiente continuamente através do sensor de luz.
- Classificar a leitura em estados semânticos compreensíveis (Escuro / Baixa luz / Adequado / Excesso).
- Exibir o estado atual de forma visual e imediata (ícone, cor, texto).
- Emitir recomendações e feedback tátil quando o ambiente estiver fora da faixa recomendada.
- Manter um histórico curto das últimas leituras para consulta.

### 1.4 Público-alvo

Usuários de dispositivos Wear OS que utilizam o relógio no dia a dia e desejam um alerta passivo sobre condições de iluminação, sem precisar consultar o celular.

### 1.5 Escopo do MVP (entrega da disciplina)

**Incluso no escopo:**
- Leitura do sensor de luz em tempo real.
- Classificação em 4 estados de luminosidade.
- Tela principal (Home/Monitor) com feedback visual reativo.
- Feedback tátil (vibração) na transição entre estados.
- Toggle de ligar/desligar monitoramento.
- Tela de histórico com as últimas leituras.
- Navegação por swipe entre as duas telas.

**Fora do escopo (não implementado nesta entrega):**
- Notificações push do sistema.
- Persistência de dados entre sessões (histórico não é salvo após fechar o app).
- Sincronização com aplicativo companion no celular.
- Configuração customizável de limiares pelo usuário.

---

## 2. Estrutura de Pastas (MVVM)

A aplicação segue o padrão **MVVM (Model-View-ViewModel)**, com separação clara entre a lógica de leitura de sensor, a lógica de negócio/estado, e a camada de apresentação (Jetpack Compose).

```
app/
 └── src/
      └── main/
           ├── java/com/example/protecaoocular/
           │    │
           │    ├── MainActivity.kt
           │    │     // Activity única (padrão Wear OS), hospeda o NavHost
           │    │
           │    ├── model/
           │    │    ├── LightState.kt
           │    │    │     // sealed class com os estados: Dark, Dim, Bright, TooBright
           │    │    ├── LightReading.kt
           │    │    │     // data class representando uma leitura (valor lux + timestamp)
           │    │    └── LightClassifier.kt
           │    │          // função pura que converte lux -> LightState (regra de negócio)
           │    │
           │    ├── sensor/
           │    │    └── LightSensorManager.kt
           │    │          // encapsula SensorManager e SensorEventListener
           │    │          // expõe um Flow<Float> com os valores de lux emitidos pelo sensor
           │    │
           │    ├── viewmodel/
           │    │    └── LightViewModel.kt
           │    │          // ViewModel único, compartilhado entre as telas
           │    │          // expõe StateFlow<Float> (lux atual)
           │    │          // expõe StateFlow<LightState> (estado atual)
           │    │          // expõe StateFlow<Boolean> (monitoramento ativo/pausado)
           │    │          // expõe StateFlow<List<LightReading>> (histórico em memória)
           │    │          // aciona vibração na transição de estado
           │    │
           │    ├── navigation/
           │    │    └── AppNavHost.kt
           │    │          // SwipeDismissableNavHost com as rotas "home" e "history"
           │    │
           │    ├── ui/
           │    │    ├── screens/
           │    │    │    ├── HomeScreen.kt
           │    │    │    │     // Composable: tela principal / monitor em tempo real
           │    │    │    └── HistoryScreen.kt
           │    │    │          // Composable: tela de histórico de leituras
           │    │    │
           │    │    ├── components/
           │    │    │    ├── LightRingIndicator.kt
           │    │    │    │     // Composable reutilizável: anel de progresso colorido
           │    │    │    ├── LightStatusText.kt
           │    │    │    │     // Composable: ícone + texto de status
           │    │    │    └── MonitoringToggleChip.kt
           │    │    │          // Composable: chip de ligar/desligar monitoramento
           │    │    │
           │    │    └── theme/
           │    │         ├── Color.kt
           │    │         ├── Theme.kt
           │    │         └── Type.kt
           │    │
           │    └── util/
           │         └── VibrationHelper.kt
           │               // helper para disparar vibração curta via Vibrator/VibratorManager
           │
           ├── AndroidManifest.xml
           └── res/
                └── ...
```

### 2.1 Responsabilidade das camadas

| Camada | Responsabilidade | Não deve conter |
|---|---|---|
| **Model** (`model/`) | Representar dados e regras de negócio puras (classificação de luz) | Chamadas de API Android, referência a Compose ou UI |
| **Sensor** (`sensor/`) | Isolar a integração com `SensorManager` do Android | Regras de negócio, lógica de estado da UI |
| **ViewModel** (`viewmodel/`) | Orquestrar sensor + model, expor estado observável via `StateFlow` | Referência direta a componentes de UI (Composables) |
| **View** (`ui/screens`, `ui/components`) | Renderizar o estado exposto pela ViewModel, capturar interações do usuário | Lógica de negócio, acesso direto ao sensor |
| **Navigation** (`navigation/`) | Definir rotas e transições entre telas | Lógica de estado do domínio |
| **Util** (`util/`) | Funções auxiliares reutilizáveis (ex: vibração) | Regras de negócio específicas do domínio |

---

## 3. Fluxo de Telas

### 3.1 Visão geral da navegação

```
   ┌─────────────────┐   swipe →   ┌─────────────────┐
   │   Home / Monitor  │ ──────────> │    Histórico    │
   │  (tela inicial)   │ <────────── │                 │
   └─────────────────┘   swipe ←   └─────────────────┘
```

- Navegação idiomática de Wear OS via `SwipeDismissableNavHost`.
- `"home"` é o `startDestination`.
- Não há bottom bar, menu ou botão de voltar tradicional — o padrão de gesto (swipe) é o esperado pelo usuário Wear OS.

### 3.2 Tela 1 — Home / Monitor Principal

**Rota:** `"home"`

**Responsabilidade:** exibir o estado atual da luminosidade em tempo real e permitir controle básico de monitoramento.

**Elementos da tela:**
- Ícone/emoji central que representa o estado atual (🌙 / 🌤️ / ☀️ / ⚠️).
- Anel de progresso (`CircularProgressIndicator`) ao redor da tela, com cor dinâmica conforme o estado.
- Valor numérico de lux (informação secundária, fonte menor).
- Texto de status/recomendação curto (ex: "Ambiente adequado", "Ambiente escuro — acenda uma luz").
- `ToggleChip` para ligar/desligar o monitoramento.

**Dados observados da ViewModel:** `luxAtual`, `estadoAtual`, `monitoramentoAtivo`.

**Interações do usuário:**
- Toggle de monitoramento (liga/desliga a escuta do sensor).
- Swipe horizontal para acessar o histórico.

**Efeitos colaterais:**
- Disparo de vibração curta quando `estadoAtual` muda de categoria.

### 3.3 Tela 2 — Histórico

**Rota:** `"history"`

**Responsabilidade:** exibir as últimas leituras registradas, permitindo ao usuário visualizar a variação do ambiente ao longo do tempo.

**Elementos da tela:**
- `ScalingLazyColumn` com uma lista das últimas ~15 leituras (valor de lux + tempo relativo, ex: "há 5s: 320 lux").
- Item de destaque opcional no topo com um resumo (ex: "80% do tempo em ambiente adequado").

**Dados observados da ViewModel:** `historicoDeLeituras`.

**Interações do usuário:**
- Swipe horizontal (retorno) para voltar à Home.
- Rolagem vertical (scroll) para consultar leituras mais antigas.

---

## 4. Fluxo de Dados (resumo)

```
Sensor de Luz (Hardware/Emulador)
        │
        ▼
LightSensorManager  (registra listener, emite Flow<Float>)
        │
        ▼
LightViewModel
        │  ├─ aplica LightClassifier (lux -> LightState)
        │  ├─ atualiza StateFlow<Float> (luxAtual)
        │  ├─ atualiza StateFlow<LightState> (estadoAtual)
        │  ├─ atualiza StateFlow<List<LightReading>> (histórico)
        │  └─ aciona VibrationHelper na transição de estado
        │
        ▼
HomeScreen / HistoryScreen (Composables)
        └─ observam os StateFlow via collectAsState()
           e re-renderizam a UI automaticamente
```

---

## 5. Considerações de Permissões

O sensor de luz (`TYPE_LIGHT`) é classificado como sensor de ambiente e **não exige permissão em tempo de execução** (runtime permission) no Android. É necessário apenas declarar o uso opcional do hardware no `AndroidManifest.xml`:

```xml
<uses-feature
    android:name="android.hardware.sensor.light"
    android:required="false" />
```

Isso evita a necessidade de fluxo de solicitação de permissão (`ActivityResultContracts.RequestPermission`), diferente do que ocorreria com sensores como `BODY_SENSORS` (frequência cardíaca) ou localização.
