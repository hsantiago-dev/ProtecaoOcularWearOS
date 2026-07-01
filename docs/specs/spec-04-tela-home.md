# Spec — Tela Home / Monitor Principal

**Feature:** Exibição em tempo real do estado de luminosidade
**Módulo relacionado:** `ui/screens/HomeScreen.kt`, `ui/components/LightRingIndicator.kt`, `ui/components/LightStatusText.kt`

---

## Objetivo

Apresentar ao usuário, de forma visual e imediata, o estado atual de luminosidade do ambiente, incluindo recomendações quando a condição estiver fora da faixa adequada.

## Escopo

- Layout da tela principal do aplicativo (rota `"home"`).
- Renderização reativa conforme o `StateFlow<LightState>` e `StateFlow<Float>` da ViewModel.
- Exibição de recomendação textual conforme o estado.

## Fora de escopo

- Lógica de classificação do estado (feito na feature de Classificação).
- Navegação para a tela de histórico (feito na feature de Navegação).
- Toggle de monitoramento (feito na feature de Controle de Monitoramento).

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | A tela deve exibir um ícone/emoji central que represente o estado atual (`Dark` = 🌙, `Dim` = 🌤️, `Bright` = ☀️, `TooBright` = ⚠️☀️). |
| RF-02 | A tela deve exibir um anel de progresso (`CircularProgressIndicator`) ao redor da tela, com cor correspondente ao estado atual. |
| RF-03 | A tela deve exibir o valor numérico atual de lux, como informação secundária (fonte menor que o ícone). |
| RF-04 | A tela deve exibir um texto curto de status/recomendação, conforme a tabela de mensagens abaixo. |
| RF-05 | A tela deve atualizar todos os elementos visuais automaticamente a cada nova leitura, sem necessidade de ação do usuário. |
| RF-06 | As transições de cor entre estados devem ser suavizadas com animação (`animateColorAsState`). |

### Tabela de mensagens por estado

| Estado | Cor | Ícone | Mensagem |
|---|---|---|---|
| `Dark` | Azul petróleo | 🌙 | "Ambiente escuro — acenda uma luz antes de continuar" |
| `Dim` | Âmbar suave | 🌤️ | "Pouca luz no ambiente" |
| `Bright` | Verde | ☀️ | "Ambiente adequado" |
| `TooBright` | Vermelho/laranja | ⚠️ | "Luz muito intensa — pode causar cansaço visual" |

## Critérios de Aceite

**CA-01 — Renderização inicial**
- **Dado** que o app é aberto pela primeira vez
- **Quando** a Home é exibida
- **Então** o ícone, anel, valor de lux e mensagem devem refletir a primeira leitura recebida do sensor

**CA-02 — Atualização em tempo real**
- **Dado** que a tela Home está visível e o monitoramento está ativo
- **Quando** o valor de lux muda no sensor (real ou virtual)
- **Então** o ícone, cor do anel, valor numérico e mensagem devem ser atualizados automaticamente, sem necessidade de recarregar a tela

**CA-03 — Transição visual suave**
- **Dado** que o estado muda de uma categoria para outra (ex: `Dim` → `Bright`)
- **Quando** a transição ocorre
- **Então** a mudança de cor deve ser animada, não instantânea/abrupta

**CA-04 — Consistência entre valor e mensagem**
- **Dado** qualquer valor de lux exibido na tela
- **Quando** o usuário observa a mensagem de status
- **Então** a mensagem deve corresponder exatamente ao estado classificado para aquele valor (conforme spec de Classificação)

## Regras de Negócio

- RN-01: Quando o monitoramento estiver pausado, a tela deve manter a última leitura exibida, sinalizando visualmente que está pausada (ex: opacidade reduzida ou texto "Monitoramento pausado").
