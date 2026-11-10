# Gym SaaS Management System

A multi-tenant gym management platform. Many gyms (tenants) use one system to manage staff, trainers, members, classes, equipment and membership payments.

## Features

- Multi-tenant (shared database, `tenant_id` on every tenant-owned table)
- Multi-currency and multi-language (English, Arabic, Spanish, French)
- Identity management with Thunder ID
- Fixed roles with RBAC: Super Admin, Admin, Manager, Trainer, Member
- Staff onboarding and role assignment
- Classes, class sessions, and trainer assign/unassign
- Member onboarding and class registration
- Membership plans, subscriptions and payments (payment gateway abstraction, Paystack test mode)
- Equipment management

## Tech stack

| Area | Choice |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot (Web, Data JPA, Security, Validation) |
| Database | PostgreSQL |
| Migrations | Flyway |
| Identity | Thunder ID |
| Build | Maven |

## Prerequisites

- JDK 21
- PostgreSQL 16+ (installed locally, or run with Docker)
- Git

## Getting started

### 1. Database

Option A: Docker

```bash
docker compose up -d
```

Option B: local PostgreSQL

```sql
CREATE USER gym WITH PASSWORD 'gym_pass';
CREATE DATABASE gymsaas OWNER gym;
```

### 2. Configuration

`src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gymsaas
    username: gym
    password: gym_pass
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  flyway:
    enabled: true
```

Use environment variables for real passwords and keys. Never commit secrets.

### 3. Run

```bash
./mvnw spring-boot:run
```

Flyway creates the tables on startup and Hibernate only validates them.

## Project structure

```
com.example.gymsaas
├── common/       shared base entities, config, exceptions
├── tenant/       gyms (tenants)
├── identity/     users, roles, permissions
├── staff/        staff profiles, trainers
├── member/       members
├── gymclass/     classes, sessions, trainer assignment, registration
├── billing/      currencies, plans, subscriptions, payments
├── equipment/    equipment and categories
└── i18n/         languages, translations
```

Each feature has `entity` and `repository` packages. `controller`, `service` and `dto` packages are added as features are built.

## Database migrations

| File | Contents |
|---|---|
| V1 | currencies, languages |
| V2 | tenants |
| V3 | users |
| V4 | roles, permissions, user_roles, role_permissions |
| V5 | staff_profiles, trainers, members |
| V6 | gym_classes, class_sessions, trainer_assignments, class_registrations |
| V7 | membership_plans, subscriptions, payments |
| V8 | equipment_categories, equipment, translations |
| V9 | seed data (currencies, 5 roles, permissions, role links) |

Never edit a migration that has already run. Add a new one instead.

## Design decisions

- **Shared tables for tenants.** One schema, with `tenant_id` on every tenant-owned table.
- **Fixed roles.** The five roles are seeded by migration and are the same for every gym.
- **No passwords stored.** Thunder ID handles sign-in, passwords and tokens. The `users` table links to it through `external_identity_id`.
- **Payment gateway abstraction.** The app talks to a `PaymentGateway` interface. Paystack (test mode) is one implementation, so switching provider means adding one class.

## Status

- [x] Project setup
- [x] Database migrations and seed data
- [x] JPA entities and repositories
- [x] Tenant context and Thunder ID authentication
- [ ] RBAC
- [ ] Onboarding (tenant, staff, members)
- [ ] Classes, trainers, registration
- [ ] Equipment
- [ ] Plans, subscriptions, payments
- [ ] i18n and caching
- [ ] API documentation (Swagger/OpenAPI)

## Documents

- Product Design Document (PDD)
- Technical Design Document (TDD)
- ER diagram (dbdiagram.io)
