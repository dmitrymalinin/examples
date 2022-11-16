Проект для отработки очереди ActiveMQ
---------------------------------------
* queue - Использование очереди в режиме Producer-Consumer
* queueRequestor - Использование очереди в режиме Request-Response

См. [https://docs.oracle.com/javaee/7/tutorial/partmessaging.htm](https://docs.oracle.com/javaee/7/tutorial/partmessaging.htm)  
[Artemis REST Interface](https://activemq.apache.org/components/artemis/documentation/2.25.0/rest.html)


### Установка artemis
```
export ARTEMIS_HOME=/home/dm/apache-artemis-2.26.0
mkdir /home/dm/artemisData
cd /home/dm/artemisData
${ARTEMIS_HOME}/bin/artemis create myMQbroker
user: artemisUser
password: artemis
Allow anonymous access: Y
```

### Запуск artemis

* На консоли

```
/home/dm/artemisData/myMQbroker/bin/artemis run
```

* В фоновом режиме

```
/home/dm/artemisData/myMQbroker/bin/artemis-service start
```

### Настройка

* Установка HttpSession timeout для админки

В artemis.profile добавить `-Dhawtio.sessionTimeout=60000`

* Установка host name

В broker.xml установить `<name>localhost</name>` и далее в `<acceptor>`

## Запуск тестовых приложений

Сборка

```
mvn clean install
```

Сервис queueRequestor

```
java -cp /home/dm/git/examples/artemis-example/target/classes:/home/dm/.m2/repository/org/apache/activemq/artemis-jakarta-client-all/2.26.0/artemis-jakarta-client-all-2.26.0.jar -XX:+ShowCodeDetailsInExceptionMessages queueRequestor.TestQueueRequestorService
```

Клиент queueRequestor

```
java -cp /home/dm/git/examples/artemis-example/target/classes:/home/dm/.m2/repository/org/apache/activemq/artemis-jakarta-client-all/2.26.0/artemis-jakarta-client-all-2.26.0.jar -XX:+ShowCodeDetailsInExceptionMessages queueRequestor.TestQueueRequestorClient
```