
# Prova Finale di Ingegneria del Software - AA 2021-2022
*Leggi in un'altra lingua: [english](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/blob/main/README.md), [italiano](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/blob/main/README.it.md)*

Questo progetto è stato realizzato per il soddisfacimento dei requisiti della Prova finale di  
Ingegneria del Software sez. Prof. San Pietro (A.A. 2021-2022), per il C.d.L. in Ingegneria Informatica del Politecnico di Milano
![Logo Eryantis](src/main/resources/images/LogoReduced.png)

Trasposizione del gioco da tavolo [Eryantis](https://www.craniocreations.it/prodotto/eriantys/) in un gioco online.

Il progetto consiste nell’implementazione del gioco come sistema composto da un singolo server in grado di gestire una partita alla volta e multipli client (uno per giocatore) che possono partecipare ad una sola partita. Per l'implementazione viene utilizzato il pattern MVC (Model-View-Controller). La comunicazione tra client e server è realizzata tramite la serializzazione di messaggi implementati come oggetti Java e tramite socket di Java.net.
L'utente può interagire con il gioco tramite una di due interfacce: una Command Line Interface e una Graphical User Interface.

## Documentazione

### UML
I diagrammi delle classi (in modello UML) sono disponibili qui:
- [UML Iniziali](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/uml_diagrams/initial)
- [UML Finali](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/uml_diagrams/final)

### JavaDoc
La documentazione del progetto, consultabile [qui](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/javadoc), è stata realizzata con le tecniche di documentazione di Java (JavaDoc) e presenta una descrizione di tutte le classi e dei loro metodi (ed eventuali attributi) pubblici. La documentazione è consultabile.

### Test Coverage
Il testing formale di unità realizzato con JUnit si è focalizzato sul model. Il coverage report risultante per questa parte del progetto è consultabile [qui](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/test_coverage_report).


### Librerie e Plugins
In questa sezione viene presentata una breve descrizione delle librerie e dei plugin utilizzati per la realizzazione del progetto.
|Libreria/Plugin|Descrizione|
|---------------|-----------|
|__Maven__|Strumento di gestione del progetto, automazione della compilazione e gestione delle dipendenze.|
|__JUnit__|Framework di unit testing.
|__JavaFx__|Libreria utilizzata per la realizzazione di interfacce grafiche.|


## Funzionalità
### Funzionalità Sviluppate
- Regole Complete
- CLI
- GUI
- 3 Funzionalità Avanzate:
    - __Persistenza:__ lo stato della partita corrente viene salvato su disco in modo che la partita possa essere ripresa anche a seguito dell’interruzione dell’esecuzione del server.
    - __Resilienza alle disconnessioni:__ a un giocatore che si disconnette è permesso riconnettersi alla partita.
    - __Tutte le carte personaggio:__ sono stati inseriti tutti i character presenti nel gioco da tavolo.


## Compilazione e packaging
### Eseguibili
Il file eseguibile (.jar) è stato realizzato con l'ausilio di Maven Shade Plugin.
[Qui](https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet/tree/main/deliveries/executable) è fornito il jar precompilato.
Per compilare i jar autonomamente utilizzare i comandi compile e package di Maven.

## Esecuzione
Questo progetto richiede Java 17 o superiore per essere eseguito correttamente.

### Client
In questa sezione viene descritto come eseguire il client di Eryantis con l'interfaccia utente desiderata.

#### CLI
Per lanciare la CLI digitare da terminale il comando:
```
java -jar PSP15.jar --cli
```
oppure
```
java -jar PSP15.jar --c
```
E' anche possibile lanciare la CLI in versione semplificate (senza disegni ma con indicazioni completamente testuali) eseguendo il seguente comando da terminale:
```
java -jar PSP15.jar --simple```
```
oppure
```
java -jar PSP15.jar --s
```

Al fine di ottenere la migliore esperienza possibile mentre si gioca con la CLI su Windows consigliamo l'utilizzo di [Windows Terminal](https://github.com/Microsoft/Terminal) come terminale.

#### GUI
Per poter lanciare la modalità GUI sono disponibili due opzioni:
- effettuare doppio click sull'eseguibile ```NOMEJAR.jar```
- digitare da terminale il comando: ```java -jar NOMEJAR.jar```

### Server
Per lanciare il server digitare da terminale il comando:
```
java -jar PSP15.jar --server [<port_number>]
```
oppure
```
java -jar PSP15.jar -server [<port_number>]
```
#### Parametri
E' possibile specificare la porta del server (il valore di default è __2351__).

## Componenti del gruppo
In questa sezione vengono elencati in ordine alfabetico i componenti del gruppo (codice PSP15) che ha realizzato questo progetto.
- [__Alessandro Ferri__](https://github.com/FerriAlessandro)
- [__Alessandro Gabbini__](https://github.com/alessandroGabbini)
- [__Angelo Giovanni Gaillet__](https://github.com/aggaillet)
