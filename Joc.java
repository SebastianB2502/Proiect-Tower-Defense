import java.util.List;
import java.util.Scanner;

public class Joc {
    private static final int CAPACITATE_HARTA = 10;       // numarul maxim de turnuri (fix)
    private static final int VAL_UPGRADE_FINAL = 10;        // upgrade-ul final se deblocheaza dupa acest val
    private static final int VAL_DEBLOCARE_WIZARD = 3;    // wizard-ul se deblocheaza dupa acest val
    private static final int VAL_DEBLOCARE_LASER = 5;     // laserul se deblocheaza dupa acest val

    private Harta harta;                                  // o singura harta (null pana o creezi)
    private Scanner scanner = new Scanner(System.in);

    private int bani = 500;                               // banii cu care incepe jucatorul
    private int valCurent = 1;                            // numarul valului urmator
    private int viataBaza = 300;                          // baza pe care o aperi
    private boolean jocTerminat = false;

    public void start() {
        int optiune;
        do {
            afiseazaMeniu();
            optiune = citesteInt("Alege optiunea: ");
            try {
                executa(optiune);
            } catch (IllegalArgumentException e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        } while (optiune != 0 && !jocTerminat);
        System.out.println("La revedere!");
    }

    private void afiseazaMeniu() {
        System.out.println("\n   ♖ TOWER DEFENSE ♖   ");
        System.out.println("Bani: " + bani + " | Valul curent: " + valCurent + " | Viata baza: " + viataBaza);
        System.out.println("1. Creeaza harta");
        System.out.println("2. Adauga turn pe harta");
        System.out.println("3. Afiseaza turnurile");
        System.out.println("4. Sterge turn");
        System.out.println("5. Imbunatateste un turn (upgrade)");
        System.out.println("6. Filtreaza turnurile dupa damage minim");
        System.out.println("7. Simuleaza un val de inamici");
        System.out.println("0. Inchide aplicatia");
    }

    private void executa(int optiune) {
        switch (optiune) {
            case 1: creeazaHarta(); break;
            case 2: adaugaTurn(); break;
            case 3: afiseazaTurnuriHarta(); break;
            case 4: stergeTurn(); break;
            case 5: upgradeTurn(); break;
            case 6: filtreazaTurnuri(); break;
            case 7: simuleazaVal(); break;
            case 0: break;
            default: System.out.println("Optiune inexistenta. Alege din nou.");
        }
    }

    //  Operatiile din meniu

    private void creeazaHarta() {
        if (harta != null) {
            throw new IllegalArgumentException("Harta exista deja.");
        }
        int id = citesteInt("ID harta: ");
        String denumire = citesteText("Denumire: ");
        harta = new Harta(id, denumire, CAPACITATE_HARTA);
        System.out.println("Harta a fost creata. Capacitate: maxim " + CAPACITATE_HARTA + " turnuri.");
    }

    private void adaugaTurn() {
        Harta h = obtineHarta();
        System.out.println("Tip turn:");
        System.out.println("  1) Archer (80 bani)  - rapid, damage mic");
        System.out.println("  2) Cannon (200 bani) - lent, damage mare");
        if (wizardDeblocat()) {
            System.out.println("  3) Wizard (300 bani) - magie, damage mare");
        } else {
            System.out.println("  3) Wizard (300 bani) - blocat, se deblocheaza dupa valul "
                    + VAL_DEBLOCARE_WIZARD);
        }
        if (laserDeblocat()) {
            System.out.println("  4) Laser (500 bani)  - cel mai puternic");
        } else {
            System.out.println("  4) Laser (500 bani)  - blocat, se deblocheaza dupa valul "
                    + VAL_DEBLOCARE_LASER);
        }
        int tip = citesteInt("Alege tipul: ");
        if (tip < 1 || tip > 4) {
            throw new IllegalArgumentException("Tip invalid.");
        }
        if (tip == 3 && !wizardDeblocat()) {
            throw new IllegalArgumentException("Wizard-ul este blocat. Termina valul "
                    + VAL_DEBLOCARE_WIZARD + " ca sa-l deblochezi.");
        }
        if (tip == 4 && !laserDeblocat()) {
            throw new IllegalArgumentException("Laserul este blocat. Termina valul "
                    + VAL_DEBLOCARE_LASER + " ca sa-l deblochezi.");
        }
        int id = citesteInt("ID turn: ");
        if (h.gasesteTurn(id) != null) {
            throw new IllegalArgumentException("Exista deja un turn cu acest ID.");
        }
        String nume = citesteText("Nume: ");

        // pretul, damage-ul si viteza sunt fixe, definite in clasa fiecarui tip de turn
        Tower turn;
        if (tip == 1) {
            turn = new ArcherTower(id, nume);
        } else if (tip == 2) {
            turn = new CannonTower(id, nume);
        } else if (tip == 3) {
            turn = new WizardTower(id, nume);
        } else {
            turn = new LaserTower(id, nume);
        }

        if (bani < turn.getCost()) {
            throw new IllegalArgumentException("Nu ai destui bani. Ai " + bani
                    + ", iar turnul costa " + turn.getCost() + ".");
        }
        h.adaugaTurn(turn);   // daca harta e plina, arunca eroare si nu se scad banii
        bani -= turn.getCost();
        System.out.println("Turn cumparat pentru " + turn.getCost() + " bani. Ti-au ramas " + bani + ".");
    }

    private void afiseazaTurnuriHarta() {
        Harta h = obtineHarta();
        if (h.getTurnuri().isEmpty()) {
            System.out.println("Harta nu are turnuri.");
            return;
        }
        System.out.println(h);
    }

    private void stergeTurn() {
        Harta h = obtineHarta();
        int id = citesteInt("ID turn: ");
        Tower turn = h.stergeTurn(id);
        if (turn != null) {
            int inapoi = turn.getValoareTotala() / 2;   // 50% din banii cheltuiti pe turn
            bani += inapoi;
            System.out.println("Turn sters. Ai primit inapoi " + inapoi + " bani (50%). Bani: " + bani + ".");
        } else {
            System.out.println("Turnul nu exista.");
        }
    }

    private void upgradeTurn() {
        Harta h = obtineHarta();
        int id = citesteInt("ID turn: ");
        Tower t = h.gasesteTurn(id);
        if (t == null) {
            throw new IllegalArgumentException("Turnul nu exista.");
        }
        if (!t.poateFiUpgradat()) {
            throw new IllegalArgumentException("Un turn de tip " + t.getTip() + " nu poate fi imbunatatit.");
        }
        if (t.getNivel() >= Tower.NIVEL_MAX) {
            throw new IllegalArgumentException("Turnul are deja nivelul maxim (" + Tower.NIVEL_MAX + ").");
        }
        if (t.esteUpgradeFinal() && valCurent <= VAL_UPGRADE_FINAL) {
            throw new IllegalArgumentException("Upgrade-ul final se deblocheaza dupa valul "
                    + VAL_UPGRADE_FINAL + ".");
        }
        int pret = t.getCostUpgrade();
        String eticheta = t.esteUpgradeFinal() ? "UPGRADE FINAL" : "Upgrade";
        System.out.println(eticheta + " nivel " + t.getNivel() + " -> " + (t.getNivel() + 1)
                + ": pret " + pret + " bani, damage +" + t.getBonusUrmatorulUpgrade() + ".");
        if (bani < pret) {
            throw new IllegalArgumentException("Nu ai destui bani. Ai " + bani + ".");
        }
        boolean eraFinal = t.esteUpgradeFinal();
        t.upgrade();
        bani -= pret;
        if (eraFinal) {
            System.out.println("WOW! Upgrade final facut!");
        }
        System.out.println("Turn imbunatatit! " + t);
        System.out.println("Bani ramasi: " + bani + ".");
    }

    private void filtreazaTurnuri() {
        Harta h = obtineHarta();
        int minim = citesteInt("Damage minim: ");
        List<Tower> rezultat = h.filtreazaDupaDamage(minim);
        if (rezultat.isEmpty()) {
            System.out.println("Niciun turn nu se potriveste.");
            return;
        }
        for (Tower t : rezultat) {
            System.out.println(t);
        }
    }

    private void simuleazaVal() {
        Harta h = obtineHarta();
        Inamic inamic = creeazaInamic();
        System.out.println("Valul " + valCurent + ": " + inamic);
        if (h.simuleazaVal(inamic)) {
            bani += inamic.getRecompensa();
            valCurent++;
            System.out.println("Ai castigat " + inamic.getRecompensa() + " bani! Acum ai " + bani + ".");
            if (valCurent == VAL_DEBLOCARE_WIZARD + 1) {
                System.out.println("Ai deblocat turnul Wizard!");
            }
            if (valCurent == VAL_DEBLOCARE_LASER + 1) {
                System.out.println("Ai deblocat turnul Laser!");
                System.out.println("Atentie: de acum vin giganti: lenti, cu multa viata, damage mare si ARMURA.");
                System.out.println("Armura scade din fiecare lovitura, deci Archer-ii (lovituri mici) fac mai putin damage.");
            }
            if (valCurent == VAL_UPGRADE_FINAL + 1) {
                System.out.println("Upgrade-ul final al turnurilor este acum deblocat!");
            }
        } else {
            viataBaza -= inamic.getDamage();
            System.out.println(inamic.getNume() + " a ajuns la baza si a dat " + inamic.getDamage()
                    + " damage. Viata bazei: " + Math.max(viataBaza, 0) + ".");
            if (viataBaza <= 0) {
                jocTerminat = true;
                System.out.println("Baza a fost distrusa! Joc terminat, ai ajuns la valul " + valCurent + ".");
            } else {
                System.out.println("Cumpara sau imbunatateste turnuri si incearca din nou.");
            }
        }
    }

    //  Metode ajutatoare

     // Pana la valul 5 vin goblini: rapizi (6 secunde), slabi si fara armura
     // Dupa aceea vin giganti: lenti (10 secunde), cu armura 5, iar viata lor creste cu 15% pe val
     // Recompensele sunt mici, ca sa trebuiasca sa alegi atent pe ce cheltuiesti banii

    private Inamic creeazaInamic() {
        if (valCurent > VAL_DEBLOCARE_LASER) {
            int k = valCurent - VAL_DEBLOCARE_LASER - 1;
            int viata = (int) (500 * Math.pow(1.15, k));
            int damage = 100 + 15 * k;
            int recompensa = 120 + 8 * k;
            return new InamicMare("Gigant", viata, damage, recompensa, 10, 5);
        }
        return new Inamic("Goblin", 60 + 40 * valCurent, 10 * valCurent, 70 + 10 * valCurent, 6, 0);
    }

    private boolean wizardDeblocat() {
        return valCurent > VAL_DEBLOCARE_WIZARD;
    }

    private boolean laserDeblocat() {
        return valCurent > VAL_DEBLOCARE_LASER;
    }

    // Returneaza harta sau da eroare daca nu a fost creata inca
    private Harta obtineHarta() {
        if (harta == null) {
            throw new IllegalArgumentException("Nu ai creat inca harta. Alege optiunea 1.");
        }
        return harta;
    }

    private int citesteInt(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Introdu un numar valid.");
            }
        }
    }

    private String citesteText(String mesaj) {
        System.out.print(mesaj);
        return scanner.nextLine();
    }
}