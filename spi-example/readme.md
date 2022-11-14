Проект для отработки технологии SPI  
-----------------------------------
* Использование технологии SPI  
* Загрузка jar из каталога providers/ с использованием URLClassLoader  

См. [https://docs.oracle.com/javase/tutorial/ext/basics/spi.html](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html)  
! *java.ext.dirs* system property is not supported, it ends in JDK 9.

См. [https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/net/URLClassLoader.html](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/net/URLClassLoader.html)  

### Сборка

```
mvn clean install
```

### Запуск

```
java -cp mainModule/target/mainModule-0.0.1-SNAPSHOT.jar  spiExample.Main
```

### Установка провайдеров

```
cp extendedHelloService/target/extendedHelloService-0.0.1-SNAPSHOT.jar target/providers/
```