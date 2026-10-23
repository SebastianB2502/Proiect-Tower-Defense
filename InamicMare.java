// Inamic mare: apare dupa valul 5. Lent, dar cu multa viata, damage mare si armura
public class InamicMare extends Inamic {

    public InamicMare() {
        super("Gigant", 500, 100, 120, 10, 5);
    }

    public InamicMare(String nume, int viata, int damage) {
        super(nume, viata, damage);
    }

    public InamicMare(String nume, int viata, int damage, int recompensa, int timpDrum, int armura) {
        super(nume, viata, damage, recompensa, timpDrum, armura);
    }

    @Override
    public String getTip() {
        return "Inamic mare";
    }
}