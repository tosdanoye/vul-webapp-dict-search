#Use the Maven image as the build stage
FROM maven:3.9.0 AS build

RUN mvn --version
RUN java -version

# Set the working directory in the build stage
WORKDIR /app

# Copy the project's pom.xml to the image
COPY . .

# Download the project dependencies and build the project
RUN mvn clean install

# Switch to the Tomcat image
FROM tomcat:10.1.13-jre17-temurin-jammy

# Set the working directory in the final stage
WORKDIR /usr/local/tomcat/webapps

# Copy the WAR file from the build stage to the container
COPY --from=build /app/target/WebSearch-0.0.1-SNAPSHOT.war ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat when the container runs
CMD ["catalina.sh", "run"]