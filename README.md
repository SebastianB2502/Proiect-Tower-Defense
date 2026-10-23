# ♖ Tower Defense (Java, consolă)

Joc de strategie în consolă, făcut în **Java** pentru cursul de **Programare Orientată pe Obiect**.
Aperi o bază de valuri de inamici: cumperi turnuri pe o hartă cu 10 locuri, le îmbunătățești și pornești valurile.
Banii sunt puțini, deci trebuie să alegi cu grijă pe ce îi cheltuiești.

## Cum se joacă

- Pornești cu **500 de bani**, o bază cu **300 de viață** și valul **1**.
- Există o singură hartă, cu loc pentru **maxim 10 turnuri**.
- La fiecare val vine un inamic care merge spre bază timp de câteva secunde. În fiecare secundă, turnurile care sunt „gata" îl lovesc.
  - Dacă inamicul moare la timp, primești bani și treci la valul următor.
  - Dacă ajunge la bază, îi scade viața bazei și repeți valul.
- Jocul se termină când viața bazei ajunge la 0 (valurile nu au limită).
- Când ștergi un turn primești înapoi **50%** din banii cheltuiți pe el (preț + upgrade-uri).

### Ce se deblochează

| După valul | Ce se deblochează |
|---|---|
| 3 | Turnul **Wizard** |
| 5 | Turnul **Laser** și inamicii mari (**Giganți**), care au armură |
| 10 | **Upgrade-ul final** al turnurilor (nivelul 3 → 4) |

## Turnurile

| Turn | Preț | Lovește | Damage pe lovitură (nivel 1 / 2 / 3 / 4) | Preț upgrade (1→2, 2→3, 3→4) |
|---|---|---|---|---|
| Archer | 80 | la 1 s | 10 / 14 / 18 / **30** | 40 / 40 / **80** |
| Cannon | 200 | la 3 s | 45 / 60 / 75 / **120** | 100 / 100 / **200** |
| Wizard | 300 | la 2 s | 35 / 45 / 55 / **85** | 150 / 150 / **300** |
| Laser | 500 | la 2 s | 60 / 80 / 100 / **160** | 250 / 250 / **500** |

Archer lovește des, dar slab, iar Cannon lovește rar, dar tare. Un upgrade obișnuit costă jumătate din prețul turnului.
Upgrade-ul final costă prețul întreg și dă un bonus de 3 ori mai mare.

## Inamicii

`n` = numărul valului.

| | Goblin (valurile 1–5) | Gigant (valurile 6+, `k = n − 6`) |
|---|---|---|
| Viață | 60 + 40·n | 500 · 1,15^k |
| Damage dat bazei | 10·n | 100 + 15·k |
| Recompensă | 70 + 10·n | 120 + 8·k |
| Ajunge la bază în | 6 secunde | 10 secunde |
| Armură | 0 | 5 |

**Armura** se scade din fiecare lovitură (minim 1 damage). Loviturile mici ale Archer-ilor ajută mai puțin împotriva Giganților,
deci va trebui să treci la Cannon, Wizard sau Laser.

## Meniul

```
1. Creeaza harta
2. Adauga turn pe harta
3. Afiseaza turnurile
4. Sterge turn
5. Imbunatateste un turn (upgrade)
6. Filtreaza turnurile dupa damage minim
7. Simuleaza un val de inamici
0. Inchide aplicatia
```

## Cum rulezi proiectul

### În IntelliJ IDEA

1. **File → Open** și alegi folderul proiectului.
2. Dacă fișierele nu sunt recunoscute: click dreapta pe folder → **Mark Directory as → Sources Root**.
3. Alegi un JDK (17 sau mai nou) la **File → Project Structure → Project**.
4. Deschizi `Main.java` și rulezi cu săgeata verde de lângă `main`.

### Din linia de comandă

```bash
javac *.java
java Main
```

Dacă simbolurile din titlu apar ca `?`, rulează cu `java -Dfile.encoding=UTF-8 Main` sau setează UTF-8 în consolă.

## Structura proiectului

| Fișier | Rol |
|---|---|
| `Main.java` | Pornește jocul |
| `Joc.java` | Meniul, banii, valul curent, viața bazei, crearea inamicilor |
| `Harta.java` | Harta cu 10 locuri, lista de turnuri și simularea valului |
| `Tower.java` | Clasa părinte a turnurilor |
| `ArcherTower.java`, `CannonTower.java`, `WizardTower.java`, `LaserTower.java` | Tipurile de turnuri (`extends Tower`) |
| `Inamic.java` | Inamicul de bază (Goblin) |
| `InamicMare.java` | Gigantul (`extends Inamic`) |

## Concepte de POO folosite

- **Încapsulare:** câmpuri `private`, cu getteri și setteri care validează datele.
- **Constructori:** unul implicit și doi cu parametri în fiecare clasă.
- **Moștenire:** turnurile moștenesc `Tower`, iar `InamicMare` moștenește `Inamic`.
- **Polimorfism și suprascriere:** `getInterval()`, `getTip()`, `poateFiUpgradat()` și `getBonusUpgrade()` sunt suprascrise în subclase.
- **Compoziție:** `Joc` are o `Harta`, iar `Harta` are o `List<Tower>`.
- **Excepții:** erorile (de exemplu „Harta este plină") sunt aruncate cu `IllegalArgumentException` și prinse în meniu.

## Cum schimbi dificultatea

- Viața, damage-ul și recompensa inamicilor: metoda `creeazaInamic()` din `Joc.java`.
- Banii de început și viața bazei: câmpurile `bani` și `viataBaza` din `Joc.java`.
- Numărul de locuri de pe hartă: constanta `CAPACITATE_HARTA` din `Joc.java`.
- Prețul, damage-ul, viteza și bonusul de upgrade al unui turn: constructorii și metodele din clasa turnului respectiv.
