// Vrajitor: loveste la 2 secunde, cu damage mare. Pret 300, se deblocheaza dupa valul 3
public class WizardTower extends Tower {

    public WizardTower() {
        super(1, "Wizard", 35, 300);
    }

    // pretul (300) si damage-ul (35) sunt fixe pentru acest tip de turn
    public WizardTower(int id, String nume) {
        super(id, nume, 35, 300);
    }

    public WizardTower(int id, String nume, int damage, int cost) {
        super(id, nume, damage, cost);
    }

    @Override
    public String getTip() {
        return "Wizard";
    }

    @Override
    public int getInterval() {
        return 2;
    }

    @Override
    public boolean poateFiUpgradat() {
        return true;
    }

    @Override
    public int getBonusUpgrade() {
        return 10;
    }
}