Сервис учёта поставок для Северной Фруктовой Компании
=====================================================

### Постановка задачи

Имеется 3 поставщика, каждый из поставщиков может поставлять 2 вида груш и 2 вида яблок. Поставщики заранее сообщают свои цены на виды продукции на определенный период поставок.  

Необходимо:  
 - создать интерфейс приемки поставок от поставщиков. В одной поставке от поставщика может быть несколько видов продукции.   
 - создать отчёт. За выбранный период показать поступление видов продукции по поставщикам с итогами по весу и стоимости.  

Требования:  
Данные приложения должны сохранятся в БД PostgreSQL путем формирования таблиц из объектов Backend.  
Backend – Java, Spring Framework, Hibernate.

### Описание БД

ER диаграмма находится в файле [db/db-er.pdf](db/db-er.pdf)  
![ER диаграмма](db/db-er.png "ER диаграмма")  
БД создаётся из java сущностей. Определены следующие сущности:  
 - *Supplier* - Поставщик  
 - *Product* - Вид продукции  
 - *Period* - Период поставок  
 - *Price* - Прайс лист  
 - *PriceProduct* - Позиция в прайс листе  
 - *Shipment* - Поставка  
 - *ShipmentProduct* - Позиция в поставке

### API

Спецификация Open API находится в файлах:
 - [api/api-spec.yaml](api/api-spec.yaml)  
 - [api-spec.html](https://dmitrymalinin.github.io/sevfruit-supplier-service/api/api-spec.html)  

### Контроллеры 

 - *SupplierController* - */supplier* - Операции с поставщиками  
 - *ProductController* - */product* - Операции с видами продукции  
 - *PeriodController* - */period* - Операции с периодами
 - *PriceController* - */price* - Операции с прайс листами
 - *ShipmentController* - */shipment* - Операции с поставками
 
### Запуск сервиса и настройка

Настроечные параметры в *application.properties*:  

```
spring.datasource.url=jdbc:postgresql://localhost:5432/training
spring.datasource.username=sevfruituser
spring.datasource.password=password

spring.jpa.open-in-view=true
spring.jpa.properties.jakarta.persistence.create-database-schemas=true
spring.jpa.properties.hibernate.hbm2ddl.halt_on_error=true

server.error.include-message=always

app.security.api-key=12345
```
Пользователь БД должен иметь разрещение *CREATE* для заданной в URL БД: `GRANT CREATE ON DATABASE training TO sevfruituser;`  

За заполнение начальными данными отвечает параметр *app.db.init.enabled*, за создание БД - *spring.jpa.properties.jakarta.persistence.schema-generation.database.action*  
Запуск сервиса с созданием БД и заполнением начальными данными: 

```
java -Dapp.db.init.enabled=true -Dspring.jpa.properties.jakarta.persistence.schema-generation.database.action=create -jar supplier-service.jar
```
Таблицы БД создаются в схеме *sevfruit*.

При последующих запусках создавать таблицы БД не требуется:  

```
java -jar supplier-service.jar
```


### Сборка документации в HTML

Проверить корректность документации:  
`redocly lint --skip-rule=no-server-example.com api-spec.yaml`

Собрать HTML:  
`redocly build-docs api-spec.yaml --output=api-spec.html`

### Ссылки

 - Spring Boot. Database Initialization [https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.data-initialization](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.data-initialization)  
 - Jakarta Persistence [https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1](https://jakarta.ee/specifications/persistence/3.1/jakarta-persistence-spec-3.1)  
 - dbdiagram [https://dbdiagram.io/d](https://dbdiagram.io/d)  
 - OpenAPI [https://spec.openapis.org/oas/latest.html](https://spec.openapis.org/oas/latest.html)  
 - ReDoc [https://redocly.github.io/redoc/](https://redocly.github.io/redoc/)  
