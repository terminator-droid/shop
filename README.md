
# 🛍️ Shop – мульти‑модульный проект на Spring Boot  

Репозиторий состоит из **пяти** Maven‑модулей, которые совместно реализуют простую платформу для интернет‑магазина.  
Все сервисы используют один стек (Spring Boot 4, JPA, Web MVC, Security, Kafka, Liquibase, OpenFeign, gRPC где требуется) и работают на JDK 17.  

## 📦 Обзор модулей  

| Модуль | Основная задача | Ключевые пакеты (на верхнем‑уровне) | Внешние интерфейсы |
|--------|----------------|------------------------------------|--------------------|
| **api‑gateway** | Публичный вход. Обрабатывает аутентификацию, маршрутизацию, сквозные функции (trace‑ID, проброс JWT) и делегирует запросы другим сервисам через Feign‑клиенты. | `controller`, `service`, `security.filter`, `feign`, `mapper`, `exception`, `config` | – REST‑API (`/auth/**`, эндпоинты товаров, заказов, пользователей).<br>– Feign‑клиенты к *order‑service*, *inventory‑service* и *notification‑service*. |
| **common‑lib** | Общие DTO, protobuf‑описания и общие исключения, используемые всеми сервисами. | `exception`, `kafka.dto`, `proto`, `resources/openapi‑specs` | – Предоставляет OpenAPI‑спеки и protobuf (`inventory.proto`) для генерации кода. |
| **inventory‑service** | CRUD продуктов и управление складскими остатками. Хранит данные о товарах в PostgreSQL, проверяет наличие перед заказом и защищает gRPC‑API ключом API‑key. | `controller`, `service`, `repository`, `mapper`, `security`, `grpc`, `interceptor` | – REST‑API (`/api/products/**`).<br>– gRPC (`InventoryGrpc`) используется *order‑service* для списания склада. |
| **order‑service** | Создание, хранение и жизненный цикл заказов. Служит Kafka‑продюсером, пишет‑отпишет товары, планирует повторные попытки при ошибках взаимодействия с inventory и notification. | `controller`, `service`, `repository`, `mapper`, `kafka`, `scheduler`, `security`, `interceptor` | – REST‑API (`/api/order/**`).<br>– Kafka‑продюсер (`OrderProducer`).<br>– gRPC‑клиент к *inventory‑service* для списания. |
| **notification‑service** | Хранит данные о заказах‑уведомлениях и предоставляет их шлюзу. Потребляет события заказов из Kafka. | `controller`, `service`, `repository`, `mapper`, `kafka`, `security` | – REST‑API (`/api/orders/**`).<br>– Kafka‑консюмер (`OrderConsumer`). |

## 🔐 Модель безопасности  

- **JWT** – генерируется *api‑gateway* после входа/регистрации  
- **API‑Key** – для gRPC и Kafka (проверяется `JwtApiKeyFilter`)  
- **Trace‑ID** – `TraceIdFilter` в каждом сервисе (MDC + заголовок)  

## 📡 Архитектура взаимодействия  

| От | К | Технология | Цель |
|----|---|------------|------|
| Gateway | Services | **OpenFeign** | HTTP API |
| Order | Inventory | **gRPC** | Списание товаров |
| Order | Notification | **Kafka** | События заказов |

## 🗄️ База данных  

**PostgreSQL + Liquibase** (одна БД на микросервис):

| Сервис | Таблицы |
|--------|---------|
| api-gateway | `users`, `refresh_tokens` |
| inventory | `products` |
| order | `orders`, `order_items`, `retryable_tasks` |
| notification | `orders`, `order_items` |


## 📂 Структура проекта  

```
shop/
├── api-gateway/         # Шлюз + Auth
├── common-lib/          # DTO + Proto  
├── inventory-service/   # Товары + gRPC
├── order-service/       # Заказы + Kafka
├── notification-service/ # Уведомления
├── compose.yaml         # PostgreSQL
└── pom.xml             # Parent
```