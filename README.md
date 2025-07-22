# kafka-with-quarkus

Proyek ini adalah aplikasi Quarkus yang mengintegrasikan Kafka untuk mengonsumsi data pengguna dari 
API eksternal (randomuser.me) dan menyimpannya ke database PostgreSQL.

## Prasyarat

Sebelum memulai, pastikan memiliki hal-hal berikut terinstal di sistem:

1. Java Development Kit (JDK) 17 
2. Apache Maven 3.8.6 
3. PostgreSQL Server 
4. Apache Kafka

## Langkah 1: Persiapan Database PostgreSQL

- Buat Database
name : demo_api
user : postgres
password: 1234

- Buat Tabel 'users' menggunakan DDL yang telah disediakan pada project

## Jalankan ZooKeeper Server

Masuk pada command prompt pada directory kafka dan execute:
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

## Jalankan Kafka Server
Masuk pada command prompt baru pada directory kafka dan execute:
.\bin\windows\kafka-server-start.bat .\config\server.properties

### Membangun dan Menjalankan Aplikasi

Buka terminal dan jalankan : ./mvn quarkus:dev

### Publikasikan Data Pengguna

Setelah aplikasi berjalan, Anda dapat memicu UserProducer untuk mengambil data dari randomuser.me 
dan mempublikasikannya ke topik Kafka random-user.

curl http://localhost:8080/users/publish

Anda akan melihat output Users published to Kafka! di browser/terminal.

Di terminal tempat Anda menjalankan quarkus:dev, Anda akan melihat log dari UserProducer yang menunjukkan bahwa pesan telah berhasil dipublikasikan ke Kafka, 
dan log dari UserConsumer yang menunjukkan bahwa pengguna telah berhasil dimasukkan ke database.

### Verifikasi Data di Database
Anda dapat memverifikasi bahwa data telah disimpan di database PostgreSQL.
