# Spec — Controle de Monitoramento (Toggle)

**Feature:** Ligar/desligar a leitura ativa do sensor
**Módulo relacionado:** `ui/components/MonitoringToggleChip.kt`, `viewmodel/LightViewModel.kt`

---

## Objetivo

Permitir que o usuário controle manualmente se o aplicativo deve continuar lendo o sensor de luz, evitando alertas/vibrações indesejadas e proporcionando controle explícito sobre o comportamento do app.

## Escopo

- Componente de UI tipo `ToggleChip` na tela Home.
- Estado observável de monitoramento ativo/pausado na ViewModel.
- Efeito do toggle sobre o registro/desregistro do listener do sensor.

## Fora de escopo

- Persistência da preferência entre sessões do app (a cada abertura, o monitoramento inicia ativo por padrão).
- Agendamento automático de pausas (ex: pausar à noite).

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | A tela Home deve exibir um `ToggleChip` indicando se o monitoramento está ativo ou pausado. |
| RF-02 | Ao acionar o toggle para "pausado", o sistema deve desregistrar o listener do sensor de luz. |
| RF-03 | Ao acionar o toggle para "ativo", o sistema deve registrar novamente o listener do sensor de luz. |
| RF-04 | O estado do monitoramento deve ser exposto pela ViewModel através de um `StateFlow<Boolean>`. |
| RF-05 | O monitoramento deve iniciar em estado "ativo" por padrão, ao abrir o aplicativo. |

## Critérios de Aceite

**CA-01 — Pausar monitoramento**
- **Dado** que o monitoramento está ativo
- **Quando** o usuário aciona o toggle para pausar
- **Então** o sensor deve parar de emitir novas leituras e a UI deve indicar visualmente o estado pausado

**CA-02 — Retomar monitoramento**
- **Dado** que o monitoramento está pausado
- **Quando** o usuário aciona o toggle para retomar
- **Então** o sensor deve voltar a emitir leituras e a UI deve refletir o valor atualizado em tempo real novamente

**CA-03 — Estado inicial**
- **Dado** que o aplicativo é aberto pela primeira vez em uma sessão
- **Quando** a Home é exibida
- **Então** o monitoramento deve estar ativo por padrão, sem necessidade de ação do usuário

**CA-04 — Nenhum efeito colateral ao pausar**
- **Dado** que o monitoramento é pausado
- **Quando** o valor de lux muda no sensor (real ou virtual)
- **Então** nenhuma atualização deve ocorrer na tela, nem vibração deve ser disparada, até que o monitoramento seja retomado

## Regras de Negócio

- RN-01: O toggle é a única forma de controle manual sobre a leitura do sensor nesta versão do produto.
