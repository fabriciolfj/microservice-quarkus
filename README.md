##Micro services com graalvm
- Adicionando dependências:
```
 mvn quarkus:add-extension -Dextensions="jdbc-postgres, orm-panache, resteasy-jsonb, openapi, hibernate-validator"
 mvn quarkus:add-extension -Dextensions="opentracing"
 mvn quarkus:add-extension -Dextensions="smallrye-opentracing"
 mvn quarkus:add-extension -Dextensions="metrics"
```
- Subindo a aplicação
```
 mvn quarkus:dev
```
- Caso de erro em subir o test container, execute o comando abaixo aonde encontra-se a classe principal do teste em sua aplicação.
```
docker run -it --rm -v $PWD:$PWD -w $PWD -v /var/run/docker.sock:/var/run/docker.sock maven:3 mvn test
```

- Para proteger nossa aplicação, estou utilizando o keycloak.

- Para uso de trace, codigo utilizado para monitoramento, usamos o jaeger e também o promethes (ambos são responsáveis por metricas da aplicação, mas posso utilizar um ou outro).

- Comando para criar a imagem do promethes
```
docker build -f Dockerfile.prometheus -t prometheus-ifood .
```
- Links:
```
http://localhost:9090/graph?g0.range_input=1h&g0.expr=up&g0.tab=1&g1.range_input=1h&g1.expr=&g1.tab=1 --promethes
http://localhost:16686/search -- jaeger
http://localhost:3000/login -- grafana
```
