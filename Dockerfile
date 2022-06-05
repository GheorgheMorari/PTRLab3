FROM hseeberger/scala-sbt:8u222_1.3.5_2.13.1
WORKDIR /

ADD build.sbt /
ADD project/ /project/
ADD src/ /src/

#CMD sbt compile run -Dsbt.rootdir=true
