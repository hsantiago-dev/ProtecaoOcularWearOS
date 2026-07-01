# Spec — Feedback Tátil (Vibração)

**Feature:** Alerta por vibração na transição de estado de luminosidade
**Módulo relacionado:** `util/VibrationHelper.kt`, `viewmodel/LightViewModel.kt`

---

## Objetivo

Notificar o usuário, através de vibração, quando o ambiente muda de categoria de luminosidade, permitindo que ele perceba a mudança sem precisar olhar para o relógio constantemente.

## Escopo

- Disparo de vibração curta na transição entre estados (`LightState`).
- Controle para evitar vibrações repetidas por pequenas oscilações dentro da mesma categoria.

## Fora de escopo

- Vibração a cada leitura do sensor (não deve ocorrer).
- Configuração de padrões de vibração customizados pelo usuário.
- Notificações do sistema (push/heads-up).

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | O sistema deve disparar uma vibração curta (ex: 200ms) sempre que o `LightState` mudar de uma categoria para outra. |
| RF-02 | O sistema não deve disparar vibração quando o valor de lux mudar mas permanecer na mesma categoria. |
| RF-03 | O sistema deve utilizar a API de `Vibrator`/`VibratorManager` adequada à versão do Android em uso. |
| RF-04 | O sistema não deve disparar vibração quando o monitoramento estiver pausado. |

## Critérios de Aceite

**CA-01 — Vibração na transição**
- **Dado** que o estado atual é `Bright`
- **Quando** o valor de lux cai e o novo estado classificado é `Dim`
- **Então** o dispositivo deve vibrar uma vez, de forma curta

**CA-02 — Ausência de vibração dentro da mesma categoria**
- **Dado** que o estado atual é `Bright`
- **Quando** o valor de lux oscila entre 400 e 600 (permanecendo em `Bright`)
- **Então** nenhuma vibração deve ser disparada

**CA-03 — Sem vibração com monitoramento pausado**
- **Dado** que o monitoramento está pausado
- **Quando** um novo valor de lux seria recebido (hipoteticamente)
- **Então** nenhuma vibração deve ocorrer, pois não há leitura ativa

**CA-04 — Múltiplas transições em sequência**
- **Dado** que o valor de lux muda rapidamente passando por `Dim` → `Bright` → `TooBright`
- **Quando** cada transição de categoria ocorre
- **Então** uma vibração distinta deve ocorrer para cada mudança de categoria, sem sobreposição que trave a próxima vibração

## Regras de Negócio

- RN-01: A vibração é disparada exclusivamente pela ViewModel, ao detectar divergência entre o estado anterior e o novo estado classificado — a camada de UI não deve conter essa lógica.
