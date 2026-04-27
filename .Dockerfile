FROM tomcat:10.1-jdk17

LABEL authors="FMRZ"

# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your built WAR file
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]