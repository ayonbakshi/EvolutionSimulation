import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Herbivore extends Organism {

	private long timeBorn;
	public Herbivore(int generation,Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double energy, double metabolism) {
		super(generation, pos, angle, speed, detectRadius, eggCycle, carnivorePoints, energy, metabolism, 5000);
		timeBorn = GamePane.timeElapsed;
		img = DrawArea.hImg;
	}

	public double detectItem() {

		double shortestDistance = -1;
		int indexOfClosest = -1;
		for (int i = 0; i < DrawArea.carnivores.size(); i++) {
			Point hPoint = DrawArea.carnivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance < detectRadius && (distance < shortestDistance || shortestDistance == -1)) {
				shortestDistance = distance;
				indexOfClosest = i;
			}

		}

		if (shortestDistance == -1) {
			double shortestDistanceFood = -1;
			int indexOfClosestFood = -1;
			for (int i = 0; i < DrawArea.food.size(); i++) {
				Point hPoint = DrawArea.food.get(i).getPoint();
				double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
				if (distance < detectRadius && (distance < shortestDistanceFood || shortestDistanceFood == -1)) {
					shortestDistanceFood = distance;
					indexOfClosestFood = i;
				}
			}
			if (shortestDistanceFood == -1)
				return this.angle;
			else {
				double angle = Math.atan2(pos.y - DrawArea.food.get(indexOfClosestFood).getPoint().y,
						pos.x - DrawArea.food.get(indexOfClosestFood).getPoint().x);
				angle = Math.toDegrees(angle);

				if (angle >= 0 && angle <= 180) {
					angle = 180 - angle;
				} else if (angle >= -180 && angle <= 0) {
					angle = 180 - angle;
				}
				return angle;
			}

		} else {
			double angle = Math.atan2(pos.y - DrawArea.carnivores.get(indexOfClosest).getPoint().y,
					pos.x - DrawArea.carnivores.get(indexOfClosest).getPoint().x);
			angle = Math.toDegrees(angle);

			if (angle >= 0 && angle <= 180) {
				angle = 180 - angle;
			} else if (angle >= -180 && angle <= 0) {
				angle = 180 - angle;
			}

			angle = (angle + 180) % 360;
			return angle;
		}
	}

	public void layEgg(){
		if(GamePane.timeElapsed>sinceLastEgg+eggCycle && energy > Main.energyReq){
			sinceLastEgg=GamePane.timeElapsed;
			DrawArea.eggs.add(new Egg(generation + 1, new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, metabolism, chaseLength));
			energy-=Main.energyReq*2/3;
			if (energy < 0)
				energy = 0;
		}
	}
	
	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Type\t\tHerbivore</span></pre><html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Generation\t\t" + String.valueOf(generation) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y + ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Angle\t\t" + (int) angle + " deg</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Speed\t\t" + speed + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">R. Detection\t" +  detectRadius + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Egg Counter\t" + eggCycle + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Carnivorism\t" + carnivorePoints + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Energy\t\t" + new DecimalFormat("#.##").format(energy) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Metabolism\t" + new DecimalFormat("#.##").format(metabolism) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Time Alive\t" + (GamePane.timeElapsed - timeBorn) + "</span></pre></html>");

		return stats;

	}
	
	public ArrayList<String> getFinalStats(){
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("Type:  Herbivore");
		stats.add("Position:  (" + pos.x + ", " + pos.y + ")");
		stats.add("Angle:  " + (int) angle + " deg");
		stats.add("Speed:  " + speed);
		stats.add("R. Detection:  " +  detectRadius);
		stats.add("Egg Counter:  " + eggCycle);
		stats.add("Carnivorism:  " + carnivorePoints);
		stats.add("Energy:  " + new DecimalFormat("#.##").format(energy));
		stats.add("Metabolism:  " + new DecimalFormat("#.##").format(metabolism) );
		stats.add("Chase Length:  " + chaseLength);
		stats.add("Time Alive:  " + (GamePane.timeElapsed - timeBorn));
		return stats;
	}
	
	public void eat() {
		for (int i = 0; i < DrawArea.food.size(); i++) {
			Point hPoint = DrawArea.food.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 24) {
				energy += (DrawArea.food.get(i).getNutrition() * metabolism / 100.0);
				if (energy > Main.maximumEnergy)
					energy = Main.maximumEnergy;
				if (DrawArea.food.get(i) == StatsPanel.selectedFood)
					StatsPanel.selectedFood = null;
				DrawArea.food.remove(i);
				i--;
			}
		}
	}
}



