
#
# docker java setting
FROM adoptopenjdk/openjdk11
#
# maintainer setting
MAINTAINER "fain9301@yahoo.com"
#
# server port default setting
EXPOSE 8080
#
# making work directory
RUN mkdir /portfolio
#
# making config directory
RUN mkdir /portfolio/config
#
# making log directory
RUN mkdir /portfolio/log
#
# spring boot package
CMD ["./mvnw","clean","package"]
#
# build java jar file path
ARG PORTFOLIO_IOT_PATH=target/*.jar
#
# copy build jar file
COPY ${PORTFOLIO_IOT_PATH} /portfolio/portfolioIoT.jar
#
# volume setting config folder
VOLUME /portfolio/config
#
# volume setting log folder
VOLUME /portfolio/log
#
# java start
#ENTRYPOINT ["java","-Dspring.profiles.active=", "-jar", "/portfolio/portfolioIoT.jar"]
ENTRYPOINT ["java", "-jar", "/portfolio/portfolioIoT.jar"]



