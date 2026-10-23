// Tun: loveste lent (la 3 secunde), dar cu damage mare. Pret 200
public class CannonTower extends Tower {

    public CannonTower() {
        super(1, "Cannon", 45, 200);
    }

    // pretul (200) si damage-ul (45) sunt fixe pentru acest tip de turn
    public CannonTower(int id, String nume) {
        super(id, nume, 45, 200);
    }

    public CannonTower(int id, String nume, int damage, int cost) {
        super(id, nume, damage, cost);
    }

    @Override
    public String getTip() {
        return "Cannon";
    }

    @Override
    public int getInterval() {
        return 3;
    }

    @Override
    public boolean poateFiUpgradat() {
        return true;
    }

    @Override
    public int getBonusUpgrade() {
        return 15;
    }
}