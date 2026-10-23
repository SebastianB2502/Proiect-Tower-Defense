 // Un turn de aparare. Toate campurile sunt private, iar setterii valideaza datele.
 // Fiecare tip de turn (este subclasa) are viteza de atac si upgrade-uri proprii.

public class Tower {
    public static final int NIVEL_MAX = 4;   // 3 upgrade-uri: nivelul 1 -> 2 -> 3 -> 4

    private int id;
    private String nume;
    private int damage;      // damage la fiecare lovitura
    private int cost;
    private int nivel = 1;   // creste cu upgrade-urile

    public Tower() {
        this.id = 1;
        this.nume = "Turn";
        this.damage = 8;
        this.cost = 50;
    }

    public Tower(int id, String nume) {
        setId(id);
        setNume(nume);
        this.damage = 8;
        this.cost = 50;
    }

    public Tower(int id, String nume, int damage, int cost) {
        setId(id);
        setNume(nume);
        setDamage(damage);
        setCost(cost);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID-ul trebuie sa fie pozitiv.");
        }
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        if (nume == null || nume.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi gol.");
        }
        this.nume = nume.trim();
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        if (damage < 1 || damage > 200) {
            throw new IllegalArgumentException("Damage-ul trebuie sa fie intre 1 si 200.");
        }
        this.damage = damage;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        if (cost <= 0) {
            throw new IllegalArgumentException("Costul trebuie sa fie pozitiv.");
        }
        this.cost = cost;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        if (nivel < 1 || nivel > NIVEL_MAX) {
            throw new IllegalArgumentException("Nivelul trebuie sa fie intre 1 si " + NIVEL_MAX + ".");
        }
        this.nivel = nivel;
    }

    // Tipul turnului.
    public String getTip() {
        return "Tower";
    }

    // La cate secunde loveste turnul (1 = rapid, 3 = lent)
    public int getInterval() {
        return 2;
    }

    // Doar unele tipuri de turn pot fi imbunatatite
    public boolean poateFiUpgradat() {
        return false;
    }

    // Cu cat creste damage-ul la fiecare upgrade
    public int getBonusUpgrade() {
        return 0;
    }

    // Upgrade-ul final (ultimul) este mult mai puternic si costa pretul intreg al turnului
    public boolean esteUpgradeFinal() {
        return nivel == NIVEL_MAX - 1;
    }

    // Pretul urmatorului upgrade: jumatate din pretul turnului, iar cel final pretul intreg
    public int getCostUpgrade() {
        if (esteUpgradeFinal()) {
            return cost;
        }
        return cost / 2;
    }

    // Cu cat creste damage-ul la urmatorul upgrade (cel final da de 3 ori mai mult)
    public int getBonusUrmatorulUpgrade() {
        if (esteUpgradeFinal()) {
            return getBonusUpgrade() * 3;
        }
        return getBonusUpgrade();
    }

    // Cat ai cheltuit in total pe turn: pretul de cumparare plus upgrade-urile facute
    public int getValoareTotala() {
        int total = cost;
        for (int l = 1; l < nivel; l++) {
            total += (l == NIVEL_MAX - 1) ? cost : cost / 2;
        }
        return total;
    }


    // Damage-ul dat in secunda data: loveste doar daca a trecut intervalul turnului
    public int ataca(int secunda) {
        if (secunda % getInterval() == 0) {
            return damage;
        }
        return 0;
    }

    // Imbunatateste turnul: nivel + 1 si damage mai mare. Banii se scad in Joc
    public void upgrade() {
        if (!poateFiUpgradat()) {
            throw new IllegalArgumentException("Acest turn nu poate fi imbunatatit.");
        }
        if (nivel >= NIVEL_MAX) {
            throw new IllegalArgumentException("Turnul are deja nivelul maxim (" + NIVEL_MAX + ").");
        }
        setDamage(damage + getBonusUrmatorulUpgrade());
        nivel++;
    }

    @Override
    public String toString() {
        return getTip() + " #" + id + " | " + nume + " | nivel: " + nivel
                + (nivel == NIVEL_MAX ? " (MAX)" : "")
                + " | damage: " + damage + " la " + getInterval() + "s | cost: " + cost;
    }
}