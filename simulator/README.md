Di seguito una panoramica dell'architettura finale, suddivisa in due macro-moduli: **drone-system** e **platform**. Ognuno ha una struttura modulare che segue i principi di separazione delle responsabilità, astrazione e inversione delle dipendenze.

---

## 1. Drone-System

Questo modulo contiene il "core" del drone, definito in tre livelli principali:  
- **Domain Layer:** Definisce il modello di dominio (entità e value object) e le interfacce (contratti) dei servizi.  
- **Application Layer:** Orchestration e casi d’uso (es. MissionManager) che coordinano il comportamento del drone.  
- **Infrastructure Layer:** Implementazioni concrete dei servizi e accesso all’hardware (sensori, telecamera, integrazioni REST).

### **Domain Layer (drone-system)**
**Package:** `com.drone.domain`

- **Model (com.drone.domain.model)**
  - `Coordinates.java` – Rappresenta la posizione geografica.
  - `Path.java` – Rappresenta il percorso da seguire.
  - `LandingZone.java` – Incapsula l'esito dell'analisi per un atterraggio sicuro.
  - *(Opzionale)* `BatteryStatus.java` – Valore o entità per lo stato della batteria.

- **Service Interfaces (com.drone.domain.service)**
  - **Navigazione (com.drone.domain.service.navigation)**
    - `NavigationService.java` – Metodi per calcolare il percorso, seguire il percorso, verificare se è stata raggiunta la destinazione, ottenere la posizione attuale.
  - **Batteria (com.drone.domain.service.battery)**
    - `BatteryService.java` – Interfaccia per ottenere il livello di batteria e verificare se è in stato critico.
  - **Radio (com.drone.domain.service.radio)**
    - `RadioService.java` – Interfaccia per inviare e ricevere messaggi radio.
  - **Atterraggio (com.drone.domain.service.landing)**
    - `LandingService.java` – Interfaccia per valutare la zona d’atterraggio (usando la telecamera) ed eseguire l’atterraggio.

### **Application Layer (drone-system)**
**Package:** `com.drone.application`

- **Mission (com.drone.application.mission)**
  - `MissionManager.java` – Classe che coordina il flusso di missione: richiede il percorso, controlla la batteria, gestisce l'atterraggio, invia messaggi radio.

- **(Opzionale) Use Cases (com.drone.application.usecase)**
  - Casi d’uso specifici che possono orchestrare scenari più complessi.

### **Infrastructure Layer (drone-system)**
**Package:** `com.drone.infrastructure`

- **Service Implementations (com.drone.infrastructure.serviceimpl)**
  - **Navigazione (com.drone.infrastructure.serviceimpl.navigation)**
    - `NavigationServiceImpl.java` – Implementa la logica di calcolo e follow del percorso.
  - **Batteria (com.drone.infrastructure.serviceimpl.battery)**
    - `BatteryServiceImpl.java` – Implementa l'interfaccia BatteryService utilizzando il **HardwareAbstractionLayer** (quando applicabile).
  - **Radio (com.drone.infrastructure.serviceimpl.radio)**
    - `RadioServiceImpl.java` – (Implementazione "reale" per scenari in cui il drone dispone di una radio autonoma; in simulazione verrà sostituita).
  - **Atterraggio (com.drone.infrastructure.serviceimpl.landing)**
    - `LandingServiceImpl.java` – Implementa la logica di atterraggio sicuro, utilizzando il **CameraService** per l’analisi.

- **Sensor Components (com.drone.infrastructure.sensor)**
  - `HardwareAbstractionLayer.java` – Accesso a sensori generici (GPS, batteria, radio se non simulata).
  - `CameraService.java` – Servizio per acquisire e analizzare l’immagine per l’atterraggio sicuro.

- **Integration (com.drone.infrastructure.integration)**
  - *(Opzionale)* Client per integrazione con servizi esterni (meteo, geo-awareness, autorizzazioni).

---

## 2. Platform (Simulatore)

Il simulatore gestisce la logica di simulazione, fornendo implementazioni simulate per servizi critici come la batteria e la radio.  
Il modulo **platform** ha una dipendenza da **drone-system** ma non viceversa, quindi le interfacce per Battery e Radio sono definite nel drone-system, e il simulatore fornisce le implementazioni.

### **Simulazione dei Servizi**
**Package:** `com.platform.simulation`

- **Battery Simulata (com.platform.simulation.battery)**
  - `BatteryServiceSimulated.java` – Implementa `BatteryService` per fornire dati simulati (es. batteria che si scarica nel tempo).

- **Radio Simulata (com.platform.simulation.radio)**
  - `RadioServiceSimulated.java` – Implementa `RadioService` per simulare comunicazioni radio.  
    - Gestisce una “mappa” dei droni simulati (registrati con un identificativo e posizione) e invia messaggi solo ai droni entro un raggio di trasmissione.

- **Gestione dei Droni Simulati (com.platform.simulation.drone)**
  - `SimulatedDrone.java` – Classe che rappresenta il drone all’interno del simulatore (es. con id e posizione).
  
- **Runner del Simulatore**
  - `SimulationRunner.java` – Classe principale che crea i droni, li registra nel sistema simulato e li avvia.  
    - Qui si istanziano le implementazioni simulate (BatteryServiceSimulated e RadioServiceSimulated) e si inietta tutto nel MissionManager del drone.

### **Integrazione nel Simulatore**
Nel `SimulationRunner`, per ogni drone:
- Si crea un'istanza di **SimulatedDrone** e la si registra presso il servizio radio simulato.
- Si passa l’implementazione simulata di `BatteryService` e `RadioService` al `MissionManager` del drone.
- Il drone, durante la sua esecuzione, userà questi servizi per ottenere informazioni sulla batteria e per comunicare con altri droni simulati.

---

## Riassunto delle Classi/Interfacce da Implementare

### **Nel modulo drone-system:**

**Domain:**
- **Model:**  
  - `Coordinates.java`  
  - `Path.java`  
  - `LandingZone.java`  
  - *(Opzionale)* `BatteryStatus.java`
- **Service Interfaces:**  
  - `NavigationService.java`  
  - `BatteryService.java`  
  - `RadioService.java`  
  - `LandingService.java`

**Application:**
- `MissionManager.java` (e eventuali casi d’uso aggiuntivi)

**Infrastructure:**
- **Service Implementations:**  
  - `NavigationServiceImpl.java`  
  - `BatteryServiceImpl.java` *(versione "reale", eventualmente non usata in simulazione)*  
  - `RadioServiceImpl.java` *(versione "reale")*  
  - `LandingServiceImpl.java`
- **Sensor:**  
  - `HardwareAbstractionLayer.java`  
  - `CameraService.java`
- **Integration:**  
  - *(Opzionale, per servizi esterni: meteo, geo, autorizzazioni)*

---

### **Nel modulo platform (Simulatore):**

- **Battery Simulation:**  
  - `BatteryServiceSimulated.java`
- **Radio Simulation:**  
  - `RadioServiceSimulated.java`
- **Gestione dei Droni Simulati:**  
  - `SimulatedDrone.java`
- **Runner del Simulatore:**  
  - `SimulationRunner.java`

---

## Considerazioni Finali

- **Dipendenze:**  
  Il modulo drone-system definisce le interfacce che il simulatore implementa. In questo modo il drone non ha una dipendenza diretta dal simulatore, evitando ciclicità.
- **Iniezione delle Dipendenze:**  
  Il simulatore, all'avvio, istanzia le implementazioni simulate e le inietta nel `MissionManager` (o in altri componenti) del drone.
- **Modularità e Scalabilità:**  
  L'architettura permette di passare facilmente da un ambiente simulato a uno reale (ad es. sostituendo le implementazioni simulate con quelle concrete) senza modificare la logica di dominio o applicativa.

Questa architettura finale garantisce che il drone, pur essendo parte di un sistema simulato, mantenga una separazione chiara tra logica di business, infrastruttura e simulazione, rendendo il sistema altamente manutenibile, testabile e scalabile.