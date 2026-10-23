import java.util.ArrayList;
import java.util.List;

 // O harta pe care se pun turnuri are capacitate (cate turnuri incap) si o lista de turnuri.

public class Harta {
    private int id;
    private String denumire;
    private int capacitate;
    private List<Tower> turnuri = new ArrayList<>();

    public Harta() {
        this.id = 1;
        this.denumire = "Harta";
        this.capacitate = 5;
    }

    public Harta(int id, String denumire, int capacitate) {
        setId(id);
        setDenumire(denumire);
        setCapacitate(capacitate);
    }

    public Harta(int id, String denumire, int capacitate, List<Tower> turnuri) {
        setId(id);
        setDenumire(denumire);
        setCapacitate(capacitate);
        setTurnuri(turnuri);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID-ul hartii trebuie sa fie pozitiv.");
        }
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        if (denumire == null || denumire.trim().isEmpty()) {
            throw new IllegalArgumentException("Denumirea hartii nu poate fi goala.");
        }
        this.denumire = denumire.trim();
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        if (capacitate <= 0) {
            throw new IllegalArgumentException("Capacitatea trebuie sa fie mai mare decat 0.");
        }
        if (capacitate < turnuri.size()) {
            throw new IllegalArgumentException("Capacitatea e mai mica decat numarul de turnuri existente.");
        }
        this.capacitate = capacitate;
    }

    public List<Tower> getTurnuri() {
        return new ArrayList<>(turnuri);
    }

    public void setTurnuri(List<Tower> turnuri) {
        if (turnuri == null) {
            throw new IllegalArgumentException("Lista de turnuri nu poate fi null.");
        }
        if (turnuri.size() > capacitate) {
            throw new IllegalArgumentException("Prea multe turnuri pentru capacitatea hartii.");
        }
        this.turnuri = new ArrayList<>(turnuri);
    }

    // Metode ale hartii

    public boolean estePlina() {
        return turnuri.size() >= capacitate;
    }

    public void adaugaTurn(Tower turn) {
        if (turn == null) {
            throw new IllegalArgumentException("Turnul nu poate fi null.");
        }
        if (estePlina()) {
            throw new IllegalArgumentException("Harta este plina.");
        }
        turnuri.add(turn);
    }

    // Returneaza turnul cu ID-ul dat sau null daca nu exista
    public Tower gasesteTurn(int id) {
        for (Tower t : turnuri) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    // Sterge turnul si il returneaza, sau returneaza null daca nu exista
    public Tower stergeTurn(int id) {
        Tower t = gasesteTurn(id);
        if (t != null) {
            turnuri.remove(t);
        }
        return t;
    }

    public List<Tower> filtreazaDupaDamage(int damageMinim) {
        List<Tower> rezultat = new ArrayList<>();
        for (Tower t : turnuri) {
            if (t.getDamage() >= damageMinim) {
                rezultat.add(t);
            }
        }
        return rezultat;
    }


    // Inamicul merge spre baza; in fiecare secunda turnurile care sunt gata de atac il lovesc.
    // Armura inamicului scade din fiecare lovitura, deci loviturile mici conteaza mai putin.

    public boolean simuleazaVal(Inamic inamic) {
        for (int secunda = 1; secunda <= inamic.getTimpDrum(); secunda++) {
            int viataInainte = inamic.getViata();
            for (Tower t : turnuri) {
                inamic.primesteDamage(t.ataca(secunda));
            }
            int damageDat = viataInainte - inamic.getViata();
            System.out.println("Secunda " + secunda + ": turnurile dau " + damageDat + " damage, "
                    + inamic.getNume() + " are " + inamic.getViata() + " viata.");
            if (inamic.esteMort()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Harta #").append(id).append(" | ").append(denumire)
                .append(" | turnuri: ").append(turnuri.size()).append("/").append(capacitate);
        for (Tower t : turnuri) {
            sb.append("\n    ").append(t);
        }
        return sb.toString();
    }
}