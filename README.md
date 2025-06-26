# SpyFilter-RU
Простой мод, позволяющий переключать видимость SPY-чата.
Сделан в частности под сервер MineToday, для модерации.

## Использование
По умолчанию на клавишу `J`, но может быть переназначена в настройках управления — раздел **SpyFilter**.
Изначально при запуске игры SPY-чат виден, и по нажатию включается фильтр, который позволяет не отображать в чате все сообщения, которые соответствуют некому паттерну.

- Что фильтруется?
  - По Regex: `^SPY:.*?:.*$`
  - Простыми словами: `SPY: ник: сообщение`
  - Также все сообщения, в которых содержатся `[SPY BOOK]` & `[SPY SIGN]`. Их поиск не реализован по Regex паттерну из-за технических особенностей отображения на сервере, где может быть несколько строк с разделениями через `\n`.

# SpyFilter-EN
A simple mod that allows toggling the visibility of SPY-chat messages.  
Originally made for the MineToday server to assist with moderation.

## Usage
By default, the toggle key is `J`, but it can be changed in the control settings under the **SpyFilter** category.  
When the game launches, SPY-chat is visible. Pressing the toggle key activates a filter that hides all messages matching certain patterns.

- What gets filtered?
  - By Regex: `^SPY:.*?:.*$`
  - In plain terms: `SPY: username: message`
  - Also filters any messages containing `[SPY BOOK]` or `[SPY SIGN]`. These are not detected using Regex due to technical limitations — on the server, such messages may span multiple lines separated by `\n`.

# modrinth

- https://modrinth.com/mod/spyfilter
