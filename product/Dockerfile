# Docker context is meant to be the root project dir
FROM openjdk:8 as builder

WORKDIR /root/build
COPY . .
RUN chmod +x gradlew
RUN ./gradlew :product:fatJar

FROM openjdk:8-alpine

WORKDIR /root/e-shop-product

COPY --from=builder /root/build/product/build/libs/product-fat-1.0-SNAPSHOT.jar product.jar

CMD ["java", "-jar", "product.jar"]
