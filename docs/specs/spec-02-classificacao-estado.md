# Spec — Classificação do Estado de Luminosidade

**Feature:** Interpretação do valor de lux em estados semânticos
**Módulo relacionado:** `model/LightState.kt`, `model/LightClassifier.kt`

---

## Objetivo

Converter o valor bruto de luminosidade (lux) em um estado semântico compreensível, que sirva de base para a exibição na UI e para as recomendações emitidas ao usuário.

## Escopo

- Definição dos estados possíveis de luminosidade.
- Função pura de classificação (lux → estado).
- Definição dos limiares (thresholds) de transição entre estados.

## Fora de escopo

- Exibição visual do estado (feito na feature de Tela Home).
- Disparo de vibração ou notificações (feito na feature de Feedback Tátil).

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | O sistema deve definir 4 estados possíveis: `Dark`, `Dim`, `Bright`, `TooBright`. |
| RF-02 | O sistema deve classificar o ambiente como `Dark` quando o valor de lux for menor que 10. |
| RF-03 | O sistema deve classificar o ambiente como `Dim` quando o valor de lux estiver entre 10 (inclusive) e 300 (exclusive). |
| RF-04 | O sistema deve classificar o ambiente como `Bright` quando o valor de lux estiver entre 300 (inclusive) e 1000 (exclusive). |
| RF-05 | O sistema deve classificar o ambiente como `TooBright` quando o valor de lux for maior ou igual a 1000. |
| RF-06 | A função de classificação deve ser pura (sem efeitos colaterais), recebendo um `Float` e retornando um `LightState`. |

## Critérios de Aceite

**CA-01 — Classificação Dark**
- **Dado** um valor de lux igual a 5
- **Quando** a função de classificação é executada
- **Então** o resultado deve ser `LightState.Dark`

**CA-02 — Classificação Dim**
- **Dado** um valor de lux igual a 150
- **Quando** a função de classificação é executada
- **Então** o resultado deve ser `LightState.Dim`

**CA-03 — Classificação Bright**
- **Dado** um valor de lux igual a 500
- **Quando** a função de classificação é executada
- **Então** o resultado deve ser `LightState.Bright`

**CA-04 — Classificação TooBright**
- **Dado** um valor de lux igual a 1500
- **Quando** a função de classificação é executada
- **Então** o resultado deve ser `LightState.TooBright`

**CA-05 — Valores-limite (edge cases)**
- **Dado** valores exatamente nos limites (10, 300, 1000)
- **Quando** a função de classificação é executada
- **Então** o valor-limite deve pertencer à categoria superior (ex: 10 → `Dim`, 300 → `Bright`, 1000 → `TooBright`)

## Regras de Negócio

- RN-01: Os limiares (10 / 300 / 1000 lux) são fixos nesta versão do produto — não são configuráveis pelo usuário (fora do escopo do MVP).
- RN-02: A reclassificação deve ocorrer a cada nova leitura emitida pelo sensor, sem necessidade de intervenção do usuário.
