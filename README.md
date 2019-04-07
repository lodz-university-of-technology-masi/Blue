# Masi Blue
Projekt aplikacji webowej mającej za zadanie zautomatyzować i ułatwić redakcję testów
rekrutacyjnych w dwóch językach oraz ich wykonywanie przez kandydatów. Od aplikacji
oczekuje się intuicyjnego interfejsu, który usprawni tłumaczenie testu między językami. 

# Stos Technologiczny
 - [Postgresql] - The World's Most Advanced Open Source Relational Database
 - [Spring Boot] - Easily create stand-alone, production-grade Spring based Applications that you can "just run".
 - [Vue.js] - Progressive JavaScript framework

# Skład Zespołu
 - Maciej Wyrzuc [Scrum Master]
 - Joanna Górczak
 - Jerzy Pakuła
 - Maciej Jahn
 - Maciej Liźniewicz
 
# Budowanie aplikacji
Aplikacja jest w pełni budowalna za pomocą jednej komendy używając narzędzia [Maven].
Aby zbudować aplikację, będąc w głównym folderze użyj komendy
`$ mvn clean install`
Aby uruchomić aplikację, użyj komendy
`$mvn --projects backend spring-boot:run`

# Szybsze developowanie
Używając wbudowanego w Vue.js webpack-dev-servera, możliwe jest szybsza obserwowacja wprowadzonych zmian w frontendowej części aplikacji. W tym celu będąc w folderze frontend należy użyć komendy
`$npm run serve`
Teraz wszystkie zmiany wprowadzone w plikach frontendowych będą momentalnie przeładowywane w przeglądarce

#Docker
Aplikacja posiada swój Dockerfile dzięki czemu możliwe jest stworzenie obrazu dockerowego i użycie go do serwowania aplikacji. Aby stworzyć obraz dockerowy, będąc w głownym folderze użyj komendy
`$docker build . --tag rekrutacjaApp:<wersja aplikacji>`
Aby uruchomić aplikację w kontenerze użyj komendy
`$docker run -d -p 8088:8088 rekrutacjaApp:<wersja aplikacji>`
Aplikacja będzie dostepna pod portem 8088



   [Postgresql]: <https://www.postgresql.org/>
   [Spring Boot]: <https://spring.io/projects/spring-boot>
   [Vue.js]: <https://vuejs.org/>
   [Maven]: <https://maven.apache.org/>