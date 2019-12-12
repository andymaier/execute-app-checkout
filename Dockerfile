FROM openjdk
COPY target/*.jar .
CMD ["java","-jar","checkout-1.0.0.jar"]