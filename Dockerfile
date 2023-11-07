FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD target/order-purchase.jar order-purchase.jar
ENTRYPOINT ["java","-jar","/order-purchase.jar"]