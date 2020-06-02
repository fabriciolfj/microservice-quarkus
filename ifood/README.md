##Micro services com graalvm
- Adicionando dependências:
```
 mvn quarkus:add-extension -Dextensions="jdbc-postgres, orm-panache, resteasy-jsonb, openapi, hibernate-validator"
```
- Subindo a aplicação
```
 mvn quarkus:dev
```
- Caso de erro em subir o test container, execute o comando abaixo aonde encontra-se a classe principal do teste em sua aplicação.
```
docker run -it --rm -v $PWD:$PWD -w $PWD -v /var/run/docker.sock:/var/run/docker.sock maven:3 mvn test
```