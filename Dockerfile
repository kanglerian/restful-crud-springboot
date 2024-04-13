# Gunakan image Java versi 17 sebagai base image
FROM openjdk:22-ea-17-slim

# Copy JAR file aplikasi Spring Boot ke dalam image
COPY target/restful-0.0.1-SNAPSHOT.jar /app/restful-0.0.1-SNAPSHOT.jar

# Set direktori kerja ke direktori tempat JAR file disalin
WORKDIR /app

EXPOSE 8080

# Perintah untuk menjalankan aplikasi saat container dimulai
CMD ["java", "-jar", "restful-0.0.1-SNAPSHOT.jar"]
