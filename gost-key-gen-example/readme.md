Создание ключей по алгоритму ГОСТ с использованием BouncyCastle
----------------------------------------------------------------

* Создание ключей по алгоритму ГОСТ
* Создание сертификата для тестовой среды ЕСИА  
* Запись сертификата и ключей в KeyStore
* Чтение сертификата и ключей из KeyStore
* Экспорт сертификата в PEM

см. [https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html](https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html)  
[Побег из Крипто Про. ГОСТ 34.10-2012 edition](https://habr.com/ru/post/440882/)

Экспорт сертификата в PEM

```
keytool -exportcert -alias test -rfc \
-keystore test_keystore_gost.pfx -storepass changeit -storetype pkcs12 \
-providername BC -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath /home/dm/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.65/bcprov-jdk15on-1.65.jar \
-file test_keystore_gost.pem \
-v
```

Просмотр содержимого KeyStore

```
keytool -list -alias test \
-keystore test_keystore_gost.pfx -storepass changeit -storetype pkcs12 \
-providername BC -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider -providerpath /home/dm/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.65/bcprov-jdk15on-1.65.jar \
-v
```