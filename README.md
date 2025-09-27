Изучение JPA2
=============

Создан из архетипа: co.ntier:spring-mvc-archetype

Примеры из книги: Pro JPA2 - Mastering the Java Persistence API

## Технологии

- Spring Framework 6.2.11
- Hibernate 6.4.10.Final
- Jakarta Persistence API 3.2.0
- Maven
- H2 Database (для тестов)

## Уже изучено

- [x] Базовые аннотации JPA (@Entity, @Id, @GeneratedValue)
- [x] Отношения между сущностями (@OneToOne, @ManyToOne, @ManyToMany)
- [x] Встраиваемые объекты (@Embedded, @Embeddable)
- [x] Коллекции (@ElementCollection)
- [x] Перечисления (@Enumerated)
- [x] Временные типы (@Temporal)
- [x] EntityManager (persist, find, remove, flush)
- [x] Транзакции (@Transactional)
- [x] Настройка JPA с Spring

## Темы для изучения

### Основы JPA
- [ ] JPQL (Java Persistence Query Language)
- [ ] Criteria API
- [ ] Named Queries и Native Queries
- [ ] Fetch стратегии (LAZY vs EAGER)
- [ ] Каскадные операции (CascadeType)
- [ ] Orphan Removal

### Продвинутые темы
- [ ] Наследование сущностей (@Inheritance)
  - [ ] SINGLE_TABLE
  - [ ] JOINED
  - [ ] TABLE_PER_CLASS
- [ ] Composite Primary Keys (@IdClass, @EmbeddedId)
- [ ] Версионирование (@Version) и Optimistic Locking
- [ ] Pessimistic Locking
- [ ] Entity Listeners и Callbacks (@PrePersist, @PostLoad и т.д.)
- [ ] @MappedSuperclass
- [ ] Second Level Cache
- [ ] Query Cache

### Производительность
- [ ] N+1 Select Problem
- [ ] Batch Fetching
- [ ] Entity Graphs (@EntityGraph)
- [ ] Pagination
- [ ] Projection и DTO
- [ ] Bulk операции (UPDATE, DELETE)

### Hibernate специфика
- [ ] @Formula
- [ ] @Filter
- [ ] @Where
- [ ] Custom Types
- [ ] Interceptors
- [ ] Event Listeners

### Spring Data JPA
- [ ] Repository интерфейсы
- [ ] Query Methods
- [ ] @Query аннотация
- [ ] Specifications
- [ ] Auditing (@CreatedDate, @LastModifiedDate)
- [ ] Pagination и Sorting

## Запуск тестов

```bash
mvn test
```
