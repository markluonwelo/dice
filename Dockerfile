FROM openjdk:11
COPY . /usr/dice-app
WORKDIR /usr/dice-app
RUN ./mvnw clean install

WORKDIR /usr/dice-app/target

CMD ["java", "-jar", "dice.jar"]