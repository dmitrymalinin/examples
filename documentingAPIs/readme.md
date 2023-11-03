#Документирование REST API 

##redocly
###Установка npm  

`sudo apt install npm`  

Увеличить таймаут при необходимости:  
`sudo npm config set fetch-timeout 600000`  

###Установка redocly

Установить redocly:  
`sudo npm i -g @redocly/cli@latest`  

Проверить:  
`redocly --version`  

###Сборка документации в HTML

Проверить корректность документации:  
`redocly lint --skip-rule=no-server-example.com api_spec.yaml`

Собрать HTML:  
`redocly build-docs api_spec.yaml --output=api_spec.html`

###Ссылки

[OpenAPI Specification v3.1.0](https://spec.openapis.org/oas/v3.1.0)  
[Swagger OpenAPI Specification ](https://swagger.io/specification/)  
[Redocly's visual reference to OpenAPI](https://redocly.com/docs/openapi-visual-reference/schemas/)  
[Redocly CLI](https://redocly.com/docs/cli/)  
[redoc https://github.com/Redocly/redoc](https://github.com/Redocly/redoc)  
[Keycloak Integration – OAuth2 and OpenID with Swagger UI](https://www.baeldung.com/keycloak-oauth2-openid-swagger)  
[Курс по документированию REST API](https://starkovden.github.io/index.html)  
[Госуслуги. Как создать свою спецификацию OpenAPI](https://info.gosuslugi.ru/articles/Как_создать_свою_спецификацию_OpenAPI/)	

[Postman](https://www.postman.com/)  
[Markdown](https://commonmark.org/)  

[Проектирование веб-API RESTFUL](https://learn.microsoft.com/ru-ru/azure/architecture/best-practices/api-design)  
[Использование HTTP методов для создания RESTful сервисов](https://restapitutorial.ru/lessons/httpmethods.html)

[keycloak-quickstarts. JAX-RS Resource Server](https://github.com/keycloak/keycloak-quickstarts/tree/latest/jakarta/jaxrs-resource-server)  
[Swagger. Bearer Authentication](https://swagger.io/docs/specification/authentication/bearer-authentication/)  
