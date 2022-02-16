
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
# build java jar file path
ARG PORTFOLIO_IOT_PATH=target/portfolioIoT-0.0.1-SNAPSHOT.jar
#
# making work directory
RUN mkdir /portfolio
#
# spring boot package
CMD ["./mvnw","clean","package"]
#
# copy build jar file
COPY ${PORTFOLIO_IOT_PATH} /portfolio/portfolioIoT.jar
#
# volumne setting
VOLUME /portfolio

#
# java start
ENTRYPOINT ["java", "-jar", "/portfolio/portfolioIoT.jar"]


