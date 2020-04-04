FROM registry.apps.man.poznan.pl/agrobank/sgms-java-apps-base-image:1.0.0

ARG JAR_FILE

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""

# Add a user to run our application so that it doesn't need to run as root
RUN adduser -D -s /bin/sh app
WORKDIR /home/app

ADD entrypoint.sh entrypoint.sh
ADD wait-for-it.sh wait-for-it.sh

RUN chmod 755 entrypoint.sh && chown app:app entrypoint.sh \
	&& chmod 755 wait-for-it.sh && chown app:app wait-for-it.sh

USER app

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

ADD ${JAR_FILE} sgms-app.jar