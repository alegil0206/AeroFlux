# Architettura e Logica di Funzionamento del Drone

## Panoramica Generale

Il sistema drone implementa un'architettura a livelli basata sul pattern **Chain of Responsibility** per la gestione sequenziale delle decisioni di volo. Ogni drone è un'entità autonoma che opera attraverso cicli di esecuzione continui, valutando costantemente le condizioni ambientali e operazionali per garantire voli sicuri ed efficienti.

## Componenti Architetturali Principali

### 1. **DroneSystem** - Controller Principale
Il `DroneSystem` rappresenta il nucleo centrale che coordina tutte le operazioni del drone:

```
DroneSystem
├── DroneContext (stato corrente)
├── HardwareAbstractionLayer (interfaccia hardware)
├── DroneServiceFacade (servizi applicativi)
└── Chain of StepHandlers (catena di responsabilità)
```

**Responsabilità:**
- Orchestrazione del ciclo di vita del drone
- Gestione dello stato operativo (`DroneContext`)
- Coordinamento della catena di handler
- Interfacciamento con l'hardware simulato

### 2. **Chain of Responsibility Pattern**
Il sistema utilizza una sequenza ordinata di handler che processano ogni step dell'esecuzione:

```
Step Execution Flow:
1. BatteryConsumptionHandler
2. DataAcquisitionHandler  
3. GeoLocationHandler
4. FlightPlanningHandler
5. ConflictAvoidanceHandler
6. FlightControlHandler
```

Ogni handler può:
- **Continuare** l'esecuzione (return false)
- **Terminare** la catena (return true) se ha completato l'azione necessaria

## Logica di Funzionamento per Handler

### 1. **BatteryConsumptionHandler** - Gestione Energetica
**Priorità:** Massima (primo nella catena)

**Logica Decisionale:**
```
IF modalità = EMERGENCY_LANDING → SKIP
IF batteria critica (< 1000 mAh) → SET EMERGENCY_LANDING_REQUEST
IF modalità = LANDING_REQUEST → SKIP  
IF piano volo non disponibile → SKIP
IF batteria insufficiente per completare missione → SET LANDING_REQUEST
```

**Funzioni:**
- Monitoraggio continuo dei livelli energetici
- Previsione del consumo per il completamento della missione
- Attivazione automatica delle procedure di atterraggio

### 2. **DataAcquisitionHandler** - Acquisizione Dati Ambientali
**Logica di Aggiornamento:**
```
IF modalità = EMERGENCY_LANDING → SKIP

Aggiorna dati ambientali ogni N step:
- Condizioni meteorologiche (30 step)
- Support points (60 step) 
- Geo-zone (30 step)
- Autorizzazioni (10 step)
```

**Funzioni:**
- Sincronizzazione con servizi U-space esterni
- Gestione della cache locale dei dati ambientali
- Ottimizzazione delle chiamate di rete

### 3. **GeoLocationHandler** - Conformità Geografica
**Logica di Validazione:**
```
IF dati ambientali non disponibili per > 30 step → SET EMERGENCY_LANDING_REQUEST

Verifica violazioni:
- Posizione corrente vs geo-zone attive
- Stato autorizzazioni per zone attraversate
- Condizioni meteorologiche critiche nella posizione attuale
```

**Funzioni:**
- Monitoraggio real-time della compliance geografica  
- Gestione delle violazioni di geofencing
- Attivazione procedure di emergenza per non-compliance

### 4. **FlightPlanningHandler** - Pianificazione Dinamica del Volo
**Logica di Ricalcolo:**
```
IF modalità = EMERGENCY_LANDING → SKIP
IF modalità = EMERGENCY_LANDING_REQUEST → Configura atterraggio verticale

Ricalcola piano se cambiano:
- Geo-zone considerate (attive + non autorizzate)
- Condizioni meteorologiche 
- Droni in conflitto
- Destinazione target

IF destinazione in zona vietata → Trova support point alternativo
```

**Algoritmi di Pianificazione:**
- **A\* modificato** per pathfinding con zone dinamiche
- **Adattamento real-time** alle condizioni ambientali
- **Fallback su support points** per destinazioni irraggiungibili

### 5. **ConflictAvoidanceHandler** - Evasione Conflitti
**Logica di Priorità:**
```
IF nessun drone nelle vicinanze → SKIP

FOR ogni drone nelle vicinanze:
  Calcola priorità basata su:
  1. Modalità volo (EMERGENCY_LANDING > normale)
  2. Altitudine (più basso = priorità maggiore)  
  3. Categoria operativa (certificata > standard)
  4. ID lessicografico (tiebreaker)

  IF conflitto potenziale:
    IF ha priorità → Continua
    ELSE → Hover o deviazione
```

**Algoritmi di Sicurezza:**
- **Volume di collisione:** 20m orizzontale, 20m verticale
- **Self-separation:** 160m + 40m buffer
- **Conflict prediction:** analisi traiettorie future
- **Head-to-head detection:** gestione conflitti frontali

### 6. **FlightControlHandler** - Controllo Attuatori
**Logica di Movimento:**
```
IF drone spento → STOP execution

IF piano volo disponibile:
  next_position = seguiPianoVolo()
  muovi_verso(next_position)
ELSE:
  IF a terra → wait
  ELSE → hover

IF destinazione raggiunta AND a terra:
  spegni_motori()
  SET FLIGHT_COMPLETED
```

**Funzioni:**
- Controllo diretto degli attuatori (motori, GPS, altimetro)
- Esecuzione fisica dei comandi di navigazione
- Gestione stati finali (atterraggio, spegnimento)

## Stati del Drone (Flight Modes)

Il drone opera secondo una macchina a stati finiti:

```
NORMAL_FLIGHT ←→ LANDING_REQUEST
      ↓              ↓
EMERGENCY_LANDING_REQUEST → EMERGENCY_LANDING
      ↓              ↓
FLIGHT_COMPLETED ← [Destinazione raggiunta]
```

**Transizioni di Stato:**
- **NORMAL_FLIGHT**: Operazione standard
- **LANDING_REQUEST**: Batteria insufficiente per missione
- **EMERGENCY_LANDING_REQUEST**: Batteria critica o violazioni sicurezza
- **EMERGENCY_LANDING**: Atterraggio verticale immediato
- **FLIGHT_COMPLETED**: Missione completata con successo

## Integrazione con Ecosistema U-space

### Servizi Esterni Consultati:
1. **Geo-Awareness**: Zone geografiche e support points
2. **Geo-Authorization**: Autorizzazioni per attraversamento zone
3. **Weather**: Condizioni meteorologiche e celle di pioggia
4. **Communication**: Status altri droni nelle vicinanze

### Pattern di Comunicazione:
- **Polling asincrono** per dati ambientali
- **Broadcasting** del proprio status via radio
- **Caching intelligente** con invalidazione temporale
- **Fallback locale** in caso di disconnessione servizi

Questa architettura garantisce che ogni drone operi come agente autonomo intelligente, capace di adattarsi dinamicamente alle condizioni operative mantenendo sempre la sicurezza come priorità assoluta.




# Architettura e Logica di Funzionamento del Drone

## Panoramica Generale

Il sistema drone implementa un'architettura a livelli basata sul pattern **Chain of Responsibility** per la gestione sequenziale delle decisioni di volo. Ogni drone è un'entità autonoma che opera attraverso cicli di esecuzione continui, valutando costantemente le condizioni ambientali e operazionali per garantire voli sicuri ed efficienti.

## Componenti Architetturali Principali

### 1. **DroneSystem** - Controller Principale
Il `DroneSystem` rappresenta il nucleo centrale che coordina tutte le operazioni del drone:

```
DroneSystem
├── DroneContext (stato corrente)
├── HardwareAbstractionLayer (interfaccia hardware)
├── DroneServiceFacade (servizi applicativi)
└── Chain of StepHandlers (catena di responsabilità)
```

**Responsabilità:**
- Orchestrazione del ciclo di vita del drone
- Gestione dello stato operativo (`DroneContext`)
- Coordinamento della catena di handler
- Interfacciamento con l'hardware simulato

### 2. **Chain of Responsibility Pattern**
Il sistema utilizza una sequenza ordinata di handler che processano ogni step dell'esecuzione:

```
Step Execution Flow:
1. BatteryConsumptionHandler
2. DataAcquisitionHandler  
3. GeoLocationHandler
4. FlightPlanningHandler
5. ConflictAvoidanceHandler
6. FlightControlHandler
```

Ogni handler può:
- **Continuare** l'esecuzione (return false)
- **Terminare** la catena (return true) se ha completato l'azione necessaria

### **Fase 1: Gestione Energetica** - Priorità Critica
**Principio**: *L'energia è vita - senza batteria il drone è un pericolo*

Il drone **costantemente monitora** i suoi livelli energetici e **predice** il consumo necessario per completare la missione. Questa valutazione ha **priorità assoluta** su qualsiasi altra decisione operativa.

**Scenari Decisionali:**
- **Batteria Critica** (< 10% rimasta): Atterraggio di emergenza immediato
- **Energia Insufficiente per Missione**: Ricerca del punto di atterraggio più vicino
- **Livelli Normali**: Continua con le altre valutazioni

**Filosofia**: Meglio una missione interrotta che un drone precipitato

### **Fase 2: Acquisizione Informazioni Ambientali** - Consapevolezza Situazionale
**Principio**: *Decisioni informate richiedono dati aggiornati*

Il drone **mantiene una visione aggiornata** dell'ambiente operativo consultando periodicamente i servizi informativi dell'ecosistema U-space.

**Informazioni Monitorate:**
- **Condizioni Meteorologiche**: Vento, pioggia, visibilità
- **Zone Geografiche**: Aree ristrette, no-fly zones, zone temporanee
- **Autorizzazioni**: Permessi per attraversare zone controllate  
- **Punti di Supporto**: Aeroporti, eliporti, aree di emergenza disponibili

**Strategia di Aggiornamento**: Bilanciamento tra informazioni fresche e conservazione risorse di comunicazione

### **Fase 3: Conformità Geografica** - Rispetto delle Normative
**Principio**: *Il rispetto delle regole garantisce l'integrazione sicura nello spazio aereo*

Il drone **verifica continuamente** la sua conformità alle regolamentazioni spaziali, assicurandosi di non violare zone ristrette o operare senza le autorizzazioni necessarie.

**Controlli di Conformità:**
- **Posizione Attuale**: Verifica di non essere in zona vietata
- **Traiettoria Pianificata**: Controllo autorizzazioni per il percorso previsto
- **Condizioni Ambientali**: Rispetto dei limiti operativi (vento, visibilità)

**Azioni Correttive**: Se rileva violazioni o rischi di violazione, attiva procedure di atterraggio controllato

### **Fase 4: Pianificazione del Percorso** - Ottimizzazione Dinamica
**Principio**: *Un buon piano si adatta alle circostanze*

Il drone **ricalcola dinamicamente** il percorso ottimale verso la destinazione, considerando tutti i vincoli ambientali e operativi raccolti nelle fasi precedenti.

**Fattori di Pianificazione:**
- **Zone da Evitare**: Aree non autorizzate, condizioni meteo avverse
- **Ottimizzazione Energetica**: Percorso più efficiente in termini di consumo
- **Sicurezza**: Rotte che mantengono distanze di sicurezza da ostacoli
- **Fallback**: Destinazioni alternative se quella primaria diventa irraggiungibile

**Algoritmi**: Il drone utilizza tecniche di ricerca del percorso che considerano tutti i vincoli simultaneamente

### **Fase 5: Gestione dei Conflitti** - Coordinamento con Altri Droni
**Principio**: *La condivisione sicura dello spazio aereo richiede coordinamento*

Il drone **negozia implicitamente** con altri droni nelle vicinanze per evitare conflitti e collisioni, utilizzando regole di priorità standardizzate.

**Regole di Priorità** (in ordine decrescente):
1. **Droni in Emergenza**: Hanno sempre la priorità assoluta
2. **Altitudine**: I droni più bassi hanno precedenza  
3. **Categoria Operativa**: Droni certificati hanno priorità su quelli standard
4. **Identificativo**: In caso di parità, decide l'ordine alfabetico

**Meccanismi di Evasione**:
- **Hovering**: Sostare in posizione fino a risoluzione conflitto
- **Deviazione**: Modificare temporaneamente la rotta  
- **Cambiamento Altitudine**: Salire o scendere per evitare interferenze

### **Fase 6: Controllo del Movimento** - Esecuzione Fisica
**Principio**: *L'esecuzione precisa delle decisioni è fondamentale per la sicurezza*

Il drone **traduce le decisioni** prese nelle fasi precedenti in **comandi fisici** per i suoi attuatori (motori, sistemi di controllo).

**Modalità Operative:**
- **Volo Pianificato**: Seguire il percorso calcolato verso la destinazione
- **Hovering**: Mantenersi in posizione stabile  
- **Atterraggio**: Procedura controllata per raggiungere il suolo
- **Emergenza**: Atterraggio verticale immediato

## Stati Operativi del Drone

Il drone può trovarsi in **cinque modalità operative principali**, ognuna con comportamenti e priorità specifiche:

### **Volo Normale**
- **Condizione**: Tutte le condizioni operative sono normali
- **Comportamento**: Segue il piano di volo ottimizzato verso la destinazione
- **Priorità**: Efficienza e completamento della missione

### **Richiesta di Atterraggio**  
- **Condizione**: Batteria insufficiente per completare la missione
- **Comportamento**: Cerca il punto di atterraggio più vicino e sicuro
- **Priorità**: Preservazione delle risorse energetiche

### **Richiesta di Atterraggio di Emergenza**
- **Condizione**: Batteria critica o violazioni di sicurezza
- **Comportamento**: Atterraggio immediato nel punto più vicino possibile
- **Priorità**: Sicurezza assoluta

### **Atterraggio di Emergenza**
- **Condizione**: Situazione critica che richiede atterraggio verticale
- **Comportamento**: Discesa verticale controllata nella posizione attuale
- **Priorità**: Minimizzare i rischi di incidente

### **Missione Completata**
- **Condizione**: Destinazione raggiunta con successo
- **Comportamento**: Spegnimento dei sistemi e attesa di nuove istruzioni
- **Priorità**: Conservazione energia e disponibilità per nuove missioni

## Integrazione con l'Ecosistema U-space

Il drone non opera isolato, ma è **parte integrante di un ecosistema** di servizi che garantiscono la gestione coordinata dello spazio aereo urbano.

### **Servizi di Supporto Consultati:**

**Geo-Awareness (Consapevolezza Geografica)**
- Fornisce informazioni sulle zone geografiche e i loro stati
- Indica punti di supporto disponibili per atterraggi di emergenza
- Aggiorna su modifiche temporanee dello spazio aereo

**Geo-Authorization (Autorizzazioni Geografiche)**  
- Gestisce i permessi per attraversare zone controllate
- Verifica la validità delle autorizzazioni in tempo reale
- Coordina l'accesso alle aree condivise

**Weather (Servizio Meteorologico)**
- Fornisce condizioni meteo attuali e previste
- Identifica celle di pioggia e condizioni avverse
- Supporta decisioni di pianificazione basate sul meteo

**Communication (Comunicazione Inter-Drone)**
- Facilita la condivisione di posizioni e intenzioni tra droni
- Abilita il coordinamento per l'evasione dei conflitti
- Mantiene la consapevolezza situazionale dell'ambiente condiviso

### **Modalità di Interazione:**
- **Consultazione Periodica**: Aggiornamento regolare delle informazioni ambientali
- **Notifiche Push**: Ricezione immediata di allerte critiche
- **Condivisione Status**: Broadcasting continuo della propria posizione e stato
- **Negoziazione Implicita**: Coordinamento automatico con altri droni

## Filosofia di Gestione delle Incertezze

### **Principio di Precauzione**
Quando il drone si trova di fronte a **informazioni incomplete o incerte**, adotta sempre l'approccio più conservativo per garantire la sicurezza.

### **Degradazione Graziosa**  
In caso di **perdita di servizi esterni**, il drone non si ferma ma **riduce le sue capacità operative** mantenendo le funzioni essenziali di sicurezza.

### **Ridondanza Decisionale**
**Ogni decisione critica** viene validata da più sistemi indipendenti per evitare errori singoli che potrebbero compromettere la sicurezza.

### **Adattamento Continuo**
Il drone **aggiorna costantemente** la sua comprensione dell'ambiente e **adatta le sue strategie** in base alle nuove informazioni disponibili.

---

**Risultato**: Un sistema di controllo del drone che privilegia sempre la sicurezza, mantiene alta consapevolezza situazionale e si adatta dinamicamente alle condizioni operative, garantendo voli autonomi affidabili nell'ambiente urbano complesso.
