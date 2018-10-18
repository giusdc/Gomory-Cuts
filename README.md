#Progetto per il corso di Algortimi e Modelli di Ottimizzazione Discreta
##Implemetazione dei tagli di Gomory al problema del minimum weighted vertex cover
###(Di Cosmo - Mastropietro - Nedia)

Il progetto Java prevede una interfaccia grafica che permette la sottomissione delle istanze di test 
dell'algoritmo nel formato presente nel file "frb30-15-mis". All'interno vi è anche un'ulteriore, semplice,
istanza di prova usata per testare il sistema realizzato. Ulteriormente, c'è la possibilità di 
inserire una directory in cui vengono scritti i file di formulazione e soluzione dei PL
scritti da Gurobi. 

Sono selezionabili 6 tipi di taglio:
* interi 
* frazionari
* interi e frazionari
* intero
* frazionario
* intero e frazionario

È inseribile il numero di iterazioni dell'algoritmo. Nel caso delle prime tre modalità, l'aumento delle
dimensioni non è lineare, al contrario delle ultime tre, per questo si raccomanda di non esagerare
col valore del relativo campo. È presente, comunque, un controllo che blocca il computo se la soluzione 
non migliora nella parta intera del suo valore.

Applicando i tagli al solo problema di minimum weighted vertex cover, è possibile anche visualizzare il 
grafo del problema.

Al termine della risoluzione, è disponibile un grafico dell'andamento del valore della funzione obiettivo,
una lista dei tempi di aggiunta dei tagli di gomory e calcolo della soluzione ad ogni iterazione e
la lista dei file di output creati da Gurobi.
