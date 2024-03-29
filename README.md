## Яндекс Практикум: проект "Share It"

Технологии: Java + Spring Boot + Maven + Lombok + JUnit + RESTful API + PostgreSQL + Docker

Описание: двумодульный проект по бронированию и аренде различных вещей. С помощью сервиса можно посмотреть статус предметов и выбрать подходящие, применив поиск по позициям.

_generated from yandex-praktikum/java-shareit_

**Бэкенд часть для сервиса шеринга (от англ. share — «делиться») вещей.**

Реализация модели данных

В проекте 4 пакета сущностей:  item, booking, request и user. В каждом из этих пакетов свои контроллеры, сервисы, репозитории.

Без реализации собственного класса контроллера, интерфейса сервиса и его имплементации реализована сущность comment.

Пакет markers содержит в себе интерфейсы тегирования

* public interface Create {}
* public interface Update {}

Реализация интерфейсов происходит в группировке ограничений аннотации @NotBlank при валидации Создания или Обновления @Validated

* @NotBlank(groups = Create.class)
* @NotBlank(groups = Update.class)

Так же в пакете markers содержится класс часто используемых констант Constants.java

Пакет exception содержит в себе классы расширяющие (продолжающие) суперкласс RuntimeException, а так же помощники и обработчики ошибок

Создание DTO-объектов и мапперов
Разделение объектов, которые хранятся в базе данных и которые возвращаются пользователям. Для реализация отдельная версия каждого класса, с которой будут работать пользователи — DTO (Data Transfer Object). Mapper-классы — помогают преобразовывать объекты модели в DTO-объекты и обратно.





# **Ветка add-controllers**
_Разработка контроллеров_

Основные сценарии, которые поддерживает приложение:

* Добавление новой вещи. Происходит по эндпойнту POST /items. На вход поступает объект ItemDto. userId в заголовке X-Sharer-User-Id (данный заголовок  содержится в классе констант) — это идентификатор пользователя, который добавляет вещь. Именно этот пользователь — владелец вещи. Идентификатор владельца поступает на вход в каждом из запросов, рассмотренных далее.
* Редактирование вещи. Эндпойнт PATCH /items{id}. Изменить можно название, описание и статус доступа к аренде. Редактировать вещь может только её владелец.
* Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{id}. Информацию о вещи может просмотреть любой пользователь.
* Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.
* Поиск вещи потенциальным арендатором. Пользователь передаёт в строке запроса текст, и система ищет вещи, содержащие этот текст в названии или описании. Происходит по эндпойнту /items/search?text={text}, в text передаётся текст для поиска.

# **Add-bookings**
_Создание базы данных_

В БД по одной таблице для каждой из основных сущностей, а также таблица, где хранятся отзывы.

Управление созданием базы данных происходит с помощью Hibernate
механизм создания схемы установлен по умолчанию
В application.properties установлены следующие настройки:
spring.jpa.hibernate.ddl-auto=update

Что значит:
Обновляет схему только при необходимости. Например, если в сущность было добавлено новое поле, то оно просто изменит таблицу добавив новый столбец без разрушения данных. Он никогда не удаляет существующие таблицы или столбцы, даже если они больше не требуются приложению

spring.sql.init.mode=always

always — всегда инициализировать базу данных

Данные настройки позволяют не использовать настройки schema.sql и data.sql

Если потребуется более тонкий контроль над изменениями в базе данных. можно использовать файлы data.sql  и  schema.sql  в Spring.

В application.properties необходимо установить следующие настройки:
spring.jpa.hibernate.ddl-auto=none

Если мы используем инициализацию на основе сценариев, то есть через schema.sql и data.sql , а также инициализацию Hibernate, то их совместное использование может вызвать некоторые проблемы.

Чтобы решить эту проблему, мы можем полностью отключить выполнение команд DDL Hibernate, которые Hibernate использует для создания/обновления таблиц:

spring.jpa.hibernate.ddl-auto=none

Можно использовать данный код для файла schema.sql


DROP TABLE IF EXISTS bookings, items, requests, users, comments;

CREATE TABLE IF NOT EXISTS users
(
id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name  VARCHAR(255)                            NOT NULL,
email VARCHAR(512)                            NOT NULL,
CONSTRAINT pk_user PRIMARY KEY (id),
CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
description VARCHAR(1024) NOT NULL,
requestor_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
created TIMESTAMP WITHOUT TIME ZONE,
CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items
(
id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name        VARCHAR(255)                            NOT NULL,
description VARCHAR(1024)                           NOT NULL,
available   BOOL,
owner_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
request_id BIGINT REFERENCES requests(id) ON DELETE CASCADE,
CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
start_date TIMESTAMP WITHOUT TIME ZONE,
end_date TIMESTAMP WITHOUT TIME ZONE,
item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
booker_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
status VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS comments
(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
text VARCHAR(4096) NOT NULL,
item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
author_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
created_date TIMESTAMP WITHOUT TIME ZONE,
CONSTRAINT pk_comment PRIMARY KEY (id)
);

## Реализация функции бронирования

Основные сценарии, которые поддерживает приложение:

* Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings. После создания запрос находится в статусе WAITING — «ожидает подтверждения».
* Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи. Затем статус бронирования становится либо APPROVED, либо REJECTED. Эндпоинт — PATCH /bookings/{id}?approved={approved}, параметр approved может принимать значения true или false.
* Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование. Эндпоинт — GET /bookings/{id}.
* Получение списка всех бронирований текущего пользователя. Эндпоинт — GET /bookings?state={state}. Параметр state необязательный и по умолчанию равен ALL (англ. «все»). Также он может принимать значения CURRENT (англ. «текущие»), PAST (англ. «завершённые»), FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»). Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
* Получение списка бронирований для всех вещей текущего пользователя. Эндпоинт — GET /bookings/owner?state={state}. Этот запрос имеет смысл для владельца хотя бы одной вещи. Работа параметра state аналогична его работе в предыдущем сценарии.
* Добавление отзывов.Отзыв можно добавить по эндпоинту POST /items/{id}/comment.
* Отзывы можно увидеть по двум эндпоинтам — по GET /items/{id} для од

## ветка add-item-request

_Добавление запроса вещи_

Пользователь создаёт такой запрос, когда не может найти нужную вещь, воспользовавшись поиском, но при этом надеется, что у кого-то она всё же имеется. Другие пользователи могут просматривать подобные запросы и, если у них есть описанная вещь и они готовы предоставить её в аренду, добавлять нужную вещь в ответ на запрос.

* POST /requests — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает, какая именно вещь ему нужна.
* GET /requests — получить список своих запросов вместе с данными об ответах на них. Для каждого запроса указывается описание, дата и время создания и список ответов в формате: id вещи, название, id владельца. Так в дальнейшем, используя указанные id вещей, можно получить подробную информацию о каждой вещи. Запросы возвращаются в отсортированном порядке от более новых к более старым.
* GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями. С помощью этого эндпоинта пользователи могут просматривать существующие запросы, на которые они могли бы ответить. Запросы сортируются по дате создания: от более новых к более старым. Результаты возвращаются постранично. Для этого нужно передать два параметра: from — индекс первого элемента, начиная с 0, и size — количество элементов для отображения.
* GET /requests/{id} — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.
  Добавление опции ответа на запрос

Добавлена возможность при создании вещи указать id запроса, в ответ на который создаётся нужная вещь. Добавлено поле requestId в тело запроса POST /items. Сохраняется возможность добавить вещь и без указания requestId.

Добавление пагинации к существующим эндпоинтам

Добавлена пагинация в эндпоинты GET /items, GET /items/search, GET /bookings и GET /bookings/owner. Параметры такие же, как и для эндпоинта на получение запросов вещей: номер первой записи и желаемое количество элементов для отображения.

Добавление тестов

Написаны тесты, проверяющие реализацию на соответствие требованиям.

## ветка add-docker

Приложение ShareIt разбито на два — shareIt-server и shareIt-gateway.

Приложение shareIt-server содержит всю основную логику.
Приложение shareIt-gateway содержит валидацию входных данных.
Каждое из приложений запускается как самостоятельное Java-приложение, а их общение происходит через REST.
Настроен запуск ShareIt через Docker.

Приложения shareIt-server, shareIt-gateway и база данных PostgreSQL запускаются в отдельном Docker-контейнере каждый. Их взаимодействие настроено через Docker Compose.
 
