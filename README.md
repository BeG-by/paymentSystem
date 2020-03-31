# Платежная система
Финальный проект курса Java developer. Дата защиты: 31.03.2020

### Функциональность:
##### 1. Пользователь
* Создание, удаление пополнение баланса валютных кашельков
* Перевод денег на кошелек другого пользователя с конвертацией валюты
* Создание депозита с возможностью забрать его по истеченю срока вклада
* Взятие кредита, погашение кредита
* Получение актуальных курсов валют

##### 2. Администратор
* CRUD операции над пользователем
* СRUD операции над новыми типами депозитов и кредитов
* Поиск депозитов, кредитов, трансферов по дате, пользователю и т.д.
* Отправка сообщений на почту пользователя (актуальные курсы валют, сообщение о блокировке)

##### 3. Дополнительно
* Реистрация и авторизация по токену (без Spring Security)
* Автоматическое обновление статуса депозита, кредита
* Автоматическое очищение токенов с базы
* Простые модульные тесты сервисов

### Стэк:
* **Технологии:** Java, Spring Boot, Spring Data JPA, Maven, JUnit, Slf4j, Apache Tomcat
* **База данных:** MySQL

### Примечания:
* Курсы валют подтягиваются библиотекой Jsoup (https://myfin.by/converter)
* Отправка сообщений – JavaMailSender
