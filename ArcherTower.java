// Arcas: loveste rapid (la fiecare secunda), dar cu damage mic. Pret 80
public class ArcherTower extends Tower {

    public ArcherTower() {
        super(1, "Archer", 10, 80);
    }

    // pretul (80) si damage-ul (10) sunt fixe pentru acest tip de turn
    public ArcherTower(int id, String nume) {
        super(id, nume, 10, 80);
    }

    public ArcherTower(int id, String nume, int damage, int cost) {
        super(id, nume, damage, cost);
    }

    @Override
    public String getTip() {
        return "Archer";
    }

    @Override
    public int getInterval() {
        return 1;
    }

    @Override
    public boolean poateFiUpgradat() {
        return true;
    }

    @Override
    public int getBonusUpgrade() {
        return 4;
    }
}