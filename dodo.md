
Consumabil
- id (Long)
- denumire (String)
- cantitate (int)
- unitateMasura (String)
- locatie (Locatie)

 Solicitare
- id (Long)
- tip (enum: MUTARE_ECHIPAMENT, FOLOSIRE_CONSUMABIL)
- echipament (Echipament) // sau consumabil
- solicitant (User)
- dataSolicitare (LocalDateTime)
- status (enum: IN_ASTEPTARE, APROBAT, RESPINS)
- aprobatDe (User)
- dataAprobare (LocalDateTime)
- motiv (String)