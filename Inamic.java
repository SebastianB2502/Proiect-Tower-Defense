  //Un inamic care vine pe drum. Daca nu este oprit in timp, ajunge la baza si ii da damage.
  //Toate campurile sunt private, iar setterii valideaza datele.

public class Inamic {
    private String nume;
    private int viata;
    private int damage;        // cat damage da bazei daca ajunge la ea
    private int recompensa;    // banii primiti cand este oprit
    private int timpDrum;      // in cate secunde ajunge la baza (mic = rapid)
    private int armura;        // se scade din fiecare lovitura primita


    public Inamic() {
        this.nume = "Goblin";
        this.viata = 100;
        this.damage = 10;
        this.recompensa = 100;
        this.timpDrum = 6;
        this.armura = 0;
    }

    public Inamic(String nume, int viata, int damage) {
        setNume(nume);
        setViata(viata);
        setDamage(damage);
        this.recompensa = 100;
        this.timpDrum = 8;
        this.armura = 0;
    }

    public Inamic(String nume, int viata, int damage, int recompensa, int timpDrum, int armura) {
        setNume(nume);
        setViata(viata);
        setDamage(damage);
        setRecompensa(recompensa);
        setTimpDrum(timpDrum);
        setArmura(armura);
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        if (nume == null || nume.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele inamicului nu poate fi gol.");
        }
        this.nume = nume.trim();
    }

    public int getViata() {
        return viata;
    }

    public void setViata(int viata) {
        if (viata <= 0) {
            throw new IllegalArgumentException("Viata inamicului trebuie sa fie pozitiva.");
        }
        this.viata = viata;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage-ul inamicului trebuie sa fie pozitiv.");
        }
        this.damage = damage;
    }

    public int getRecompensa() {
        return recompensa;
    }

    public void setRecompensa(int recompensa) {
        if (recompensa < 0) {
            throw new IllegalArgumentException("Recompensa nu poate fi negativa.");
        }
        this.recompensa = recompensa;
    }

    public int getTimpDrum() {
        return timpDrum;
    }

    public void setTimpDrum(int timpDrum) {
        if (timpDrum < 1 || timpDrum > 30) {
            throw new IllegalArgumentException("Timpul de drum trebuie sa fie intre 1 si 30 de secunde.");
        }
        this.timpDrum = timpDrum;
    }

    public int getArmura() {
        return armura;
    }

    public void setArmura(int armura) {
        if (armura < 0 || armura > 50) {
            throw new IllegalArgumentException("Armura trebuie sa fie intre 0 si 50.");
        }
        this.armura = armura;
    }

    // Tipul inamicului, suprascris in subclasa
    public String getTip() {
        return "Inamic";
    }

     // Inamicul pierde viata cand este lovit de turnuri. Armura scade din fiecare lovitura, dar o lovitura face mereu cel putin 1 damage

    public void primesteDamage(int damageprimit) {
        if (damageprimit <= 0) {
            return;
        }
        int damageEfectiv = Math.max(1, damageprimit - armura);
        viata -= damageEfectiv;
        if (viata < 0) {
            viata = 0;
        }
    }

    public boolean esteMort() {
        return viata <= 0;
    }

    @Override
    public String toString() {
        return getTip() + " " + nume + " | viata: " + viata + " | damage: " + damage
                + " | armura: " + armura + " | ajunge la baza in " + timpDrum
                + "s | recompensa: " + recompensa;
    }
}