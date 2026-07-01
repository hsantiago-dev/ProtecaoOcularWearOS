# Spec — Histórico de Leituras

**Feature:** Consulta das últimas leituras de luminosidade
**Módulo relacionado:** `ui/screens/HistoryScreen.kt`, `model/LightReading.kt`, `viewmodel/LightViewModel.kt`

---

## Objetivo

Permitir que o usuário consulte a variação recente da luminosidade do ambiente, reforçando a percepção de que os dados são coletados continuamente ao longo do tempo.

## Escopo

- Armazenamento em memória das últimas N leituras (lux + timestamp relativo).
- Tela de listagem do histórico (rota `"history"`).

## Fora de escopo

- Persistência do histórico em disco/banco de dados (o histórico é perdido ao fechar o app).
- Gráficos avançados (linha, barra) — a versão inicial é uma lista textual.
- Exportação ou compartilhamento do histórico.

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | O sistema deve manter em memória, na ViewModel, uma lista com as últimas 15 leituras de lux. |
| RF-02 | Cada item do histórico deve conter o valor de lux e o tempo relativo desde a leitura (ex: "há 5s"). |
| RF-03 | Ao ultrapassar 15 leituras armazenadas, a leitura mais antiga deve ser descartada (estrutura tipo fila/FIFO). |
| RF-04 | A tela de Histórico deve exibir a lista em ordem cronológica decrescente (mais recente primeiro), usando `ScalingLazyColumn`. |
| RF-05 | O histórico não deve ser atualizado enquanto o monitoramento estiver pausado. |

## Critérios de Aceite

**CA-01 — Registro de novas leituras**
- **Dado** que o monitoramento está ativo
- **Quando** uma nova leitura de lux é recebida do sensor
- **Então** ela deve ser adicionada ao topo da lista de histórico exibida na tela de Histórico

**CA-02 — Limite de itens**
- **Dado** que o histórico já possui 15 leituras armazenadas
- **Quando** uma nova leitura é recebida
- **Então** a leitura mais antiga deve ser removida, mantendo o total em 15 itens

**CA-03 — Lista vazia**
- **Dado** que o aplicativo acabou de ser aberto e nenhuma leitura foi processada ainda
- **Quando** o usuário acessa a tela de Histórico
- **Então** a tela deve exibir um estado vazio apropriado (ex: "Nenhuma leitura registrada ainda"), sem travar ou exibir erro

**CA-04 — Monitoramento pausado**
- **Dado** que o monitoramento está pausado
- **Quando** o usuário acessa a tela de Histórico
- **Então** a lista deve exibir apenas as leituras registradas até o momento da pausa, sem novos itens sendo adicionados

## Regras de Negócio

- RN-01: O histórico é mantido apenas em memória (não sobrevive ao fechamento do processo do aplicativo).
- RN-02: A ordenação é sempre cronológica decrescente (mais recente no topo).
