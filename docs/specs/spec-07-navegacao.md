# Spec — Navegação entre Telas

**Feature:** Navegação entre Home e Histórico
**Módulo relacionado:** `navigation/AppNavHost.kt`

---

## Objetivo

Prover uma navegação idiomática de Wear OS entre a tela principal (Home) e a tela de Histórico, utilizando o padrão de gesto de swipe.

## Escopo

- Definição das rotas do `SwipeDismissableNavHost`.
- Transição entre as rotas `"home"` e `"history"`.
- Compartilhamento da mesma instância de ViewModel entre as duas telas.

## Fora de escopo

- Telas adicionais além de Home e Histórico.
- Bottom navigation bar ou menus (não recomendados para Wear OS).
- Deep links externos.

## Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | O sistema deve utilizar `SwipeDismissableNavHost` com duas rotas: `"home"` e `"history"`. |
| RF-02 | A rota `"home"` deve ser a `startDestination` da navegação. |
| RF-03 | O usuário deve conseguir navegar de `"home"` para `"history"` através de gesto de swipe horizontal. |
| RF-04 | O usuário deve conseguir retornar de `"history"` para `"home"` através de gesto de swipe (padrão de dismissal do Wear Compose). |
| RF-05 | A `LightViewModel` deve ser compartilhada entre as duas rotas, evitando duplicação de estado ou de listeners do sensor. |

## Critérios de Aceite

**CA-01 — Tela inicial correta**
- **Dado** que o aplicativo é aberto
- **Quando** a navegação é inicializada
- **Então** a tela exibida deve ser a Home (`"home"`)

**CA-02 — Navegação para Histórico**
- **Dado** que o usuário está na tela Home
- **Quando** realiza o gesto de swipe horizontal esperado pelo Wear OS
- **Então** a tela de Histórico deve ser exibida

**CA-03 — Retorno para Home**
- **Dado** que o usuário está na tela de Histórico
- **Quando** realiza o gesto de swipe de retorno
- **Então** a tela Home deve ser exibida novamente, com o estado atualizado (não uma versão "congelada")

**CA-04 — Consistência de estado entre telas**
- **Dado** que o usuário navega entre Home e Histórico múltiplas vezes
- **Quando** o valor de lux muda durante essa navegação
- **Então** tanto a Home quanto o Histórico devem refletir o mesmo estado de dados, sem inconsistência entre as telas (mesma fonte de verdade: a ViewModel compartilhada)

## Regras de Negócio

- RN-01: Não deve haver nenhuma outra tela ou rota além de `"home"` e `"history"` nesta versão do produto.
