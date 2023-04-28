Использование RESTEasy
=======================
* SE Bootstrap
* resteasy-jackson2-provider
* Версионирование API с использованием заголовка Content-Type
* Использование maven-assembly-plugin для сборки jar с зависимостями
* Использование maven-war-plugin для сборки war
* Развёртывание на Tomcat 10.1.8
* systemd unit

см.  
1. Jakarta RESTFul Web Services [https://docs.jboss.org/resteasy/docs/6.2.1.Final/userguide/html_single/index.html](https://docs.jboss.org/resteasy/docs/6.2.1.Final/userguide/html_single/index.html)  
2. Jakarta RESTful Web Services. Java SE Bootstrap [https://jakarta.ee/specifications/restful-ws/3.1/jakarta-restful-ws-spec-3.1.html#java-se](https://jakarta.ee/specifications/restful-ws/3.1/jakarta-restful-ws-spec-3.1.html#java-se)  
3. An Introduction to the RESTEasy SeBootstrap Usage [https://resteasy.dev/2022/11/01/sebootstrap-usage/](https://resteasy.dev/2022/11/01/sebootstrap-usage/)  
4. Apache Maven Assembly Plugin Usage [https://maven.apache.org/plugins/maven-assembly-plugin/usage.html](https://maven.apache.org/plugins/maven-assembly-plugin/usage.html)  

##### Сборка
war: `mvn clean compile war:war`  
jar: `mvn clean package`  

##### Развёртывание на Tomcat 10.1.8

Собрать war  
Скопировать war в каталог webapps  
Сервис будет доступен по адресу http://localhost:8080/resteasy-example/

##### Создание файла юнита и запуск сервиса

```
	vim /etc/systemd/system/resteasy-example.service
	systemctl daemon-reload
	systemctl start resteasy-example
```
Необходимые для работы сервиса файлы следует располагать в корневой файловой системе, т.к. другие файловые системы могут быть не примонтированы к моменту запуска сервиса.  

см.  
1. Working with systemd unit files [https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/8/html/configuring_basic_system_settings/assembly_working-with-systemd-unit-files_configuring-basic-system-settings](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/8/html/configuring_basic_system_settings/assembly_working-with-systemd-unit-files_configuring-basic-system-settings)  
2.  Working with systemd targets [https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/8/html/configuring_basic_system_settings/working-with-systemd-targets_configuring-basic-system-settings](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/8/html/configuring_basic_system_settings/working-with-systemd-targets_configuring-basic-system-settings)  
3. Managing Services with systemd [https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/system_administrators_guide/chap-managing_services_with_systemd](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/system_administrators_guide/chap-managing_services_with_systemd)  
4. Process exit codes with special meanings [https://tldp.org/LDP/abs/html/exitcodes.html](https://tldp.org/LDP/abs/html/exitcodes.html)  
3. A list of signals and what they mean [https://www-uxsup.csx.cam.ac.uk/courses/moved.Building/signals.pdf](https://www-uxsup.csx.cam.ac.uk/courses/moved.Building/signals.pdf)  
