# Digital Wallet API

**Digital Wallet API**, kullanıcıların hesap yönetimi, bakiye işlemleri, para transferleri ve döviz kuru sorgulamaları yapabilmesini sağlayan, **Spring Boot** tabanlı bir finans uygulamasıdır.  

Proje; **JWT tabanlı güvenlik**, **Redis cache**, **DB replikasyonu**, **event-driven audit logging**, **Strategy Pattern ile transaction yönetimi** ve **Docker tabanlı container mimarisi** gibi kurumsal özellikler barındırır.  

</br>

## 🚀 Kullanılan Teknolojiler

- **Java 17**  
- **Spring Boot 3+**  
- **Spring Data JPA (Hibernate)**  
- **PostgreSQL** (Docker Compose üzerinden)  
- **Redis** (JWT blacklist & cache)  
- **Spring Security + JWT**  
- **Swagger (OpenAPI)** → API dokümantasyonu  
- **Prometheus, Zipkin, Grafana** → Monitoring & metrics  
- **Maven** → Dependency & build management  
- **Docker & Docker Compose**  
- **Flyway** → Veritabanı migration yönetimi, schema version kontrolü ve otomatik güncellemeler.


</br>

## 📂 Mimari Yapı

Proje **katmanlı mimari** ve **tasarım desenleri** üzerine kuruludur:

- **Controller** → REST API uçları (`UserController`, `TransactionController`, `BalanceController`, `AuthenticationController`, `ExchangeController`)  
- **Service** → İş mantığı, SOLID prensipleri (abstract & concrete class’lar)  
- **Repository** → Spring Data JPA ile veritabanı erişimi  
- **Model (Entity)** → `User`, `Balance`, `Transaction`, `AuditLog` gibi domain sınıfları  
- **DTO** → Request & Response veri transfer objeleri  
- **Config**  
  - `ReplicationRoutingDataSource` → Read/Write DB ayrımı (replikasyon)  
  - `RedisConfig` → Cache ve JWT blacklist yönetimi  
  - `SwaggerConfig`, `TracingFilter`, `PasswordEncoderConfig`  
- **Exception Handling** → `GlobalExceptionHandler` ve özel exception sınıfları  
- **Event & Listener** → Audit log mekanizması (`AuditEvent`, `AuditEventListener`)  
- **Strategy Pattern** → Transaction tiplerinin yönetimi (`Deposit`, `Withdraw`, `Transfer`, `Credit`, `Debt`)  
- **AOP (Aspect)** → `ReadOnlyAspect` ile DB yönlendirmesi  
- **Util** → Global yardımcı sınıflar (örn. `GlobalContext`)  

---

## ⚙️ Kurulum ve Çalıştırma

### 1. Gereklilikler
- Java 17+
- Maven 3.8+
- Docker & Docker Compose

### 2. Ortam Değişkenleri
`.env` dosyasında aşağıdaki parametreler tanımlanmalıdır:

```env
POSTGRES_USER=yourDBUser
POSTGRES_PASSWORD=yourDBPassword
POSTGRES_DB=yourDatabase
POSTGRES_PORT_MASTER=yourPortMaster
POSTGRES_PORT_REPLICA=yourPortReplica

# JWT Configuration
JWT_SECRET=MySuperSecretKeyMySuperSecretKey123!
JWT_EXPIRATION=86400000 # 1 günlük
```



#### 2.1 Veritabanı Migration
Projede **Flyway** kullanılarak veritabanı migration’ları yönetilmektedir.  
Docker Compose ile PostgreSQL başlatıldığında Flyway migration’ları otomatik çalışır ve veritabanı schema’sı güncel tutulur.

### 3. Servisleri Başlatma

#### Uygulamayı direkt olarak aşağıdaki docker compose komutuyla çalıştırabilirsiniz.

```env
docker compose up -d
```
#### Eğer uygulama içerisinde değişiklik yaptıktan sonra çalıştırmak isterseniz : 

```env
# Maven temizleme ve build (testleri atlayarak)
mvn clean
mvn install -DskipTests

# target klasöründeki jar dosyasını kök dizine taşı ve adını değiştir
cp target/demo-0.0.1-SNAPSHOT.jar ./app.jar

# Docker işlemleri
docker compose down -v
docker compose up -d

```


### 4. API Dokümantasyonu

Swagger UI →
```env
http://localhost:8081/swagger-ui/index.html
```

<br>

## 🔑 Öne Çıkan Özellikler

- JWT Authentication & Authorization

- Access & Refresh Token desteği

- Redis üzerinden Token blacklist mekanizması

- Veritabanı Replikasyonu

- Read/Write ayrımı (ReplicationRoutingDataSource)

- Audit Logging

- Event-driven mimari ile tüm kritik işlemlerin kaydı

- Transaction Yönetimi

- Strategy Pattern ile farklı transaction tipleri yönetilir

- Exception Handling

- Global Exception Handler ile merkezi hata yönetimi

- Monitoring & Observability

- Prometheus entegrasyonu

- TracingFilter ile her request için trace ID loglama


<br>


## 📊 API Örnekleri

**Swagger UI** üzerinden API örnekleri → 

![user contoller](https://github.com/MerveKaradas/insider-project/blob/5d2a02ef91be0ef5cbb184ea7e808ab593938fb8/docs/images/image.png)![transaction](https://github.com/MerveKaradas/insider-project/blob/5d2a02ef91be0ef5cbb184ea7e808ab593938fb8/docs/images/image-1.png)![balance-exchange](https://github.com/MerveKaradas/insider-project/blob/5d2a02ef91be0ef5cbb184ea7e808ab593938fb8/docs/images/image-2.png)



<br>

## 🛡️ Güvenlik

JWT Authentication → Her endpoint güvenlik kontrolünden geçer

Redis Blacklist → Token invalidation (logout & güvenlik)

Role-Based Access Control (RBAC) → Kullanıcı rolleri (USER, ADMIN)

<br>

## 📈 İzleme & Loglama



 **Grafana** →
  Sistem ve uygulama metriklerinin görselleştirilmesi için kullanılan araç.

 **Zipkin** →
  Dağıtık tracing (izleme) için kullanılan servis, request flow takibi sağlar.

 **Prometheus** →
  Sistem metrikleri (CPU, Memory, Request count) toplayan monitoring aracı.

 **Audit Log** →
  Kritik kullanıcı ve işlem hareketlerinin kaydı için kullanılan log sistemi.

 **TracingFilter** →
  Request bazlı trace ID ile log takibi ve hataların izlenmesi.

<br>




## 📌 Yol Haritası
Bu noktada proje hala geliştirme sürecinde olup ileride uygulanması planan yapılar aşağıdaki gibi olacaktır.

- Unit & Integration test coverage artırma

- CI/CD pipeline ekleme (GitHub Actions / Jenkins)

- Kubernetes üzerinde deployment (ölçeklenebilir mimari için)

- Rate Limiting (Bucket4j) ile API koruması