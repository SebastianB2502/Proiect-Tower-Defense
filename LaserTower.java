// Laser: cel mai scump turn (500), se deblocheaza dupa valul 5. Loveste la 2 secunde, foarte tare
public class LaserTower extends Tower {

    public LaserTower() {
        super(1, "Laser", 60, 500);
    }

    // pretul (500) si damage-ul (60) sunt fixe pentru acest tip de turn
    public LaserTower(int id, String nume) {
        super(id, nume, 60, 500);
    }

    public LaserTower(int id, String nume, int damage, int cost) {
        super(id, nume, damage, cost);
    }

    @Override
    public String getTip() {
        return "Laser";
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
        return 20;
    }
}