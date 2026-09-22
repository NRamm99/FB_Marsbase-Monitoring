# FB Marsbase Monitoring

Skoleprojekt, hvor sensorer sender målinger til Mars HQ.
Serveren kan håndtere flere sensorer samtidig med flere tråde.

## Funktioner

- Sensorerne sender en måling hvert 5. sekund.
- Beskeder sendes i formatet `TEMP | 22.5`, `O2 | 20.9` eller `CO2 | 987.2`.
- Serveren parser beskederne til `SensorMeasurement`-objekter.
- Serveren tjekker målingerne og viser en alarm, hvis en værdi er uden for grænsen.
- Behandlede målinger gemmes i `mars.log` med dato og tidspunkt.

De sensortyper, der findes i den nuværende kode, er temperatur (`TEMP`), ilt (`O2`)
og CO2 (`CO2`).

## Sådan startes projektet

1. Start `mars.server.MarsServer`.
2. Start `mars.client.SensorClient` med én af disse parametre:
   - `TEMP`
   - `O2`
   - `CO2`
3. Start eventuelt flere sensorklienter samtidig.

Serveren lytter på port `5001`. Logfilen `mars.log` bliver oprettet i projektets
arbejdsmappe og får nye målinger tilføjet løbende.
