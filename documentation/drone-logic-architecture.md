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
