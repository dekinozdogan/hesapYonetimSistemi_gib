# Hesap Yönetim Sistemi

## Proje Amacı
Kullanıcılar, hesap oluşturma, güncelleme, silme, bakiye yatırma ve çekme gibi işlemleri gerçekleştirebilir. Ayrıca sistem, her bir işlemin detaylarını veritabanında saklar ve işlem geçmişini izlemeyi sağlar.

## Kullanılan Teknolojiler
- **Java 11+**: Projede ana programlama dili olarak Java 11 ve üzeri sürümleri kullanılmıştır.
- **Postman**: API endpoint'lerini test etmek için kullanılmıştır.
- **Spring Boot**: Uygulamanın backend kısmı için Spring Boot framework'ü tercih edilmiştir.
- **Lombok**: Daha sade ve anlaşılır kod yazmak için kullanılmıştır (getter, setter, toString, equals gibi metodlar otomatik generate edilmiştir).
- **JUnit5 ve Mockito**: Yazılım testleri için JUnit5 ve Mockito kullanılmıştır. Bu testler, servis ve controller katmanlarının doğruluğunu doğrulamak amacıyla yazılmıştır.
- **MySQL**: Veritabanı olarak MySQL kullanılmıştır. Veritabanı işlemleri, Spring Data JPA aracılığıyla gerçekleştirilir.
- **CrudRepository**: Temel CRUD işlemleri için kullanılır. Basit veri erişimi işlemleri için kullanımı kolaydır.
- **JpaRepository**: JpaRepository, CrudRepository'den türetilmiştir ve daha gelişmiş özellikler sunar. Veritabanı işlemleri için daha kapsamlı bir çözüm sağlar.
- **QueryDsl**: Dinamik sorgular oluşturmak için kullanılır. Özellikle karmaşık sorgular yazmak ve veritabanı sorgularını daha esnek bir şekilde oluşturmak için faydalıdır.
- **MapStruct**: Java sınıfları arasındaki veri dönüşümlerini otomatikleştiren bir araçtır. Entity ve DTO sınıfları arasındaki dönüşümleri kolaylaştırarak, manuel dönüştürme kodlarının yazılmasını engeller.
- **Maven**: Proje bağımlılıkları ve yönetimi için Maven kullanılmıştır.
- **Unit Test**: Uygulama için birim testleri yazılmıştır. Bu testler, uygulamanın farklı bileşenlerini bağımsız olarak test etmeyi sağlar.
- **Entegrasyon Testi**: Uygulamanın farklı bileşenlerinin birlikte çalışıp çalışmadığını test etmek için yazılmıştır. Veritabanı, API ve diğer dış servislerle olan etkileşimleri kontrol eder.

## Proje Özellikleri

### Hesap İşlemleri
- **Hesap Oluşturma**: Kullanıcılar yeni hesaplar açabilir. Aynı kimlik numarasına sahip birden fazla hesap eklenmesine izin verilmez.
- **Hesap Güncelleme**: Mevcut hesapların sahip bilgileri, soyadı, adı ve bakiye gibi alanları güncellenebilir.
- **Hesap Silme**: Kullanıcılar mevcut hesaplarını silebilirler.

### Para İşlemleri
- **Para Yatırma**: Hesaba belirli bir miktarda para yatırılabilir. Ancak, bakiyenin belirli bir üst sınırı aşması engellenir.
- **Para Çekme**: Hesaptan belirli bir tutarda para çekilebilir. Bakiyenin sıfırın altına düşmesine izin verilmez.

### Testler
- **Unit Testleri**: Spring Boot, JUnit5 ve Mockito kullanılarak uygulamanın işlevselliği test edilmiştir.
- **Controller Testleri**: Controller katmanındaki API endpoint'lerinin doğru çalışıp çalışmadığını doğrulamak için MockMvc ile testler yapılmıştır.

## API Sonuçları

### `POST /hesap`
Yeni bir hesap oluşturulmasını sağlar. Aynı kimlik numarasına sahip bir hesap eklenemez.
- **Yanıtlar**:
  - `201 Created`: Hesap başarıyla oluşturulmuştur.
  - `409 Conflict`: Aynı kimlik numarasına sahip bir hesap zaten mevcut.

### `PUT /hesap/{hesapId}`
Mevcut bir hesabı günceller. Hesap sahibinin adı, soyadı ve bakiye gibi bilgiler güncellenebilir.
- **Yanıtlar**:
  - `200 OK`: Hesap başarıyla güncellenmiştir.
  - `404 Not Found`: Hesap bulunamamıştır.

### `DELETE /hesap/{hesapId}`
Mevcut bir hesabı siler.
- **Yanıtlar**:
  - `200 OK`: Hesap başarıyla silinmiştir.
  - `404 Not Found`: Hesap bulunamamıştır.

### `PUT /hesap/paraYatir/{hesapId}/{miktar}`
Hesaba belirli bir miktarda para yatırılmasını sağlar.
- **Yanıtlar**:
  - `200 OK`: Para yatırma işlemi başarılı olmuştur.
  - `400 Bad Request`: Yatırılmak istenen miktar geçerli değildir.
  - `409 Conflict`: Yatırılmak istenen miktar, hesap bakiyesini aşmaktadır.

### `PUT /hesap/paraCek/{hesapId}/{miktar}`
Hesaptan belirli bir tutarda para çekilmesini sağlar.
- **Yanıtlar**:
  - `200 OK`: Para çekme işlemi başarılı olmuştur.
  - `400 Bad Request`: Çekilmek istenen miktar geçerli değildir.
  - `409 Conflict`: Çekilmek istenen miktar, mevcut bakiyeyi aşmaktadır.
