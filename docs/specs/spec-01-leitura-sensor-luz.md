# Spec — Leitura do Sensor de Luz

**Feature:** Captura de dados do sensor de luminosidade ambiente
**Módulo relacionado:** `sensor/LightSensorManager.kt`

---

## Objetivo

Capturar, de forma contínua, os valores de luminosidade ambiente (lux) emitidos pelo sensor de luz do dispositivo (`Sensor.TYPE_LIGHT`), disponibilizando-os para consumo pela camada de ViewModel.

## Escopo

- Registro e desregistro do listener do sensor conforme o ciclo de vida da aplicação e do estado de monitoramento.
- Emissão dos valores lidos através de um `Flow<Float>`.
- Verificação de disponibilidade do sensor no dispositivo/emulador.

## Fora de escopo

- Interpretação ou classificação do valor lido (feito em outra feature).
- Persistência dos valores lidos em disco ou banco de dados.

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | O sistema deve verificar se o sensor `TYPE_LIGHT` está disponível no dispositivo ao iniciar. |
| RF-02 | O sistema deve registrar um `SensorEventListener` para o sensor de luz quando o monitoramento estiver ativo. |
| RF-03 | O sistema deve emitir cada novo valor de lux recebido através de um `Flow<Float>`. |
| RF-04 | O sistema deve desregistrar o listener do sensor quando o monitoramento for pausado ou a tela for destruída (`onPause`/`onStop`). |
| RF-05 | O sistema deve utilizar `SENSOR_DELAY_NORMAL` (ou equivalente) como taxa de amostragem, suficiente para atualização em tempo real sem consumo excessivo de bateria. |

## Critérios de Aceite

**CA-01 — Sensor disponível**
- **Dado** que o dispositivo/emulador possui o sensor `TYPE_LIGHT`
- **Quando** a aplicação é iniciada
- **Então** o listener é registrado com sucesso e nenhum erro é lançado

**CA-02 — Sensor indisponível**
- **Dado** que o dispositivo não possui o sensor `TYPE_LIGHT`
- **Quando** a aplicação tenta registrar o listener
- **Então** o sistema não deve travar (crash) e deve tratar o caso graciosamente (ex: log de aviso)

**CA-03 — Emissão de valores em tempo real**
- **Dado** que o listener está registrado e o monitoramento está ativo
- **Quando** o valor de luminosidade muda no sensor (real ou virtual, via emulador)
- **Então** o novo valor deve ser emitido pelo `Flow<Float>` em até ~1 segundo

**CA-04 — Liberação de recursos**
- **Dado** que o monitoramento é pausado pelo usuário ou a Activity sai de foreground
- **Quando** o evento de pausa ocorre
- **Então** o listener do sensor deve ser desregistrado, evitando consumo de bateria em segundo plano

## Regras de Negócio

- RN-01: Nenhum valor deve ser emitido enquanto o monitoramento estiver pausado (RF definido na feature de Controle de Monitoramento).
