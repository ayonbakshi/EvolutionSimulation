import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Stores and updates stats of objects present in simulation
 */
public class DrawArea extends BufferedImage {

	/**
	 * images of objects in simulation
	 */
	static ArrayList<Carnivore> carnivores = new ArrayList<Carnivore>();
	static ArrayList<Herbivore> herbivores = new ArrayList<Herbivore>();
	static ArrayList<Food> food = new ArrayList<Food>();
	static ArrayList<Egg> eggs = new ArrayList<Egg>();
	static BufferedImage hImg = null, cImg = null, eImg = null, fImg = null;
	
	private Graphics2D g = null;
	
	/**
	 * width and height of area where simulation is drawn
	 */
	static int width, height;

	/**
	 * starting preferences for stats entered by user
	 */
	static private int startingCarnivores, startingHerbivores;
	static private int startMinSpeed, startMaxSpeed;
	static private int startMinRad, startMaxRad;
	static private int startMinEgg, startMaxEgg;
	static private double startMinEnergy, startMaxEnergy;
	static private double startMinMetabolism, startMaxMetabolism;
	static private double startMinFood, startMaxFood;
	static private long chaseLength;

	/**
	 * DrawArea constructor
	 * @param startingCarnivores amount of carnivores to start
	 * @param startingHerbivores amount of herbivores to start
	 * @param startMinSpeed minimum starting speed
	 * @param startMaxSpeed maximum starting speed
	 * @param startMinRad minimum starting radius of detection
	 * @param startMaxRad maximum starting radius of detection
	 * @param startMinEgg minimum starting amount of eggs
	 * @param startMaxEgg maximum starting amount of eggs
	 * @param startMinEnergy minimum starting amount of energy
	 * @param startMaxEnergy maximum starting amount of energy
	 * @param startMinMetabolism minimum starting metabolism speed
	 * @param startMaxMetabolism maximum starting matabolism speed
	 * @param startMinFood minimum amount of starting food
	 * @param startMaxFood maximum amount of starting food
	 * @param chaseLength chase length for all organisms
	 */
	public DrawArea(int startingCarnivores, int startingHerbivores, int startMinSpeed, int startMaxSpeed,
			int startMinRad, int startMaxRad, int startMinEgg, int startMaxEgg, double startMinEnergy,
			double startMaxEnergy, double startMinMetabolism, double startMaxMetabolism, double startMinFood,
			double startMaxFood, long chaseLength) {
		super(2000, 2000, BufferedImage.TYPE_INT_ARGB);

		this.startingCarnivores = startingCarnivores;
		this.startingHerbivores = startingHerbivores;

		this.startMinSpeed = startMinSpeed;
		this.startMaxSpeed = startMaxSpeed;

		this.startMinRad = startMinRad;
		this.startMaxRad = startMaxRad;

		this.startMinEgg = startMinEgg;
		this.startMaxEgg = startMaxEgg;

		this.startMinEnergy = startMinEnergy;
		this.startMaxEnergy = startMaxEnergy;

		this.startMinMetabolism = startMinMetabolism;
		this.startMaxMetabolism = startMaxMetabolism;

		this.startMinFood = startMinFood;
		this.startMaxFood = startMaxFood;

		this.chaseLength = chaseLength;

		width = getWidth();
		height = getHeight();

		g = (Graphics2D) createGraphics();
		try {
			hImg = ImageIO.read(new File("images/herbivore.png"));
			cImg = ImageIO.read(new File("images/carnivore.png"));
			eImg = ImageIO.read(new File("images/egg.png"));
			fImg = ImageIO.read(new File("images/food.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < startingHerbivores; i++) {
			herbivores.add(new Herbivore( 1,
					new Point((int) (Math.random() * (width - 16) + 8), // x
							(int) (Math.random() * (height - 16) + 8)), // y
					Math.random() * 360.0, // angle
					(int) (Math.random() * ((startMaxSpeed - startMinSpeed) + 1) + startMinSpeed), // speed
					(int) (Math.random() * ((startMaxRad - startMinRad) + 1) + startMinRad), // dRadius
					(int) (Math.random() * ((startMaxEgg - startMinEgg) + 1) + startMinEgg), // EggCycle
					(int) ((Math.random() * 8) + 2), // carnivorepoints
					(Math.random() * ((startMaxEnergy - startMinEnergy) + 1.0) + startMinEnergy), // energy
					(Math.random() * ((startMaxMetabolism - startMinMetabolism) + 1.0) + startMinMetabolism)));// chase length

		}

		for (int i = 0; i < startingCarnivores; i++) {
			carnivores.add(new Carnivore( 1,
					new Point((int) (Math.random() * (width - 16) + 8), // x
							(int) (Math.random() * (height - 16) + 8)), // y
					Math.random() * 360.0, // angle
					(int) (Math.random() * ((startMaxSpeed - startMinSpeed) + 1) + startMinSpeed), // spd
					(int) (Math.random() * ((startMaxRad - startMinRad) + 1) + startMinRad), // dRadius
					(int) (Math.random() * ((startMaxEgg - startMinEgg) + 1) + startMinEgg), // eggCycle
					10, // carnivorePoints
					(Math.random() * ((startMaxEnergy - startMinEnergy) + 1.0) + startMinEnergy), // energy
					(Math.random() * ((startMaxMetabolism - startMinMetabolism) + 1.0) + startMinMetabolism), // metabolism
					chaseLength));// chase length
		}

	}

	/**
	 * Update positions of organisms
	 */
	public void updatePositions() {
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).move(width, height);
		}

		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).move(width, height);
		}
	}

	/**
	 * Check if organisms are able to eat
	 */
	public void eat() {
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).eat();
		}

		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).eat();
		}
	}

	/**
	 * Check if organisms are ready to lay eggs
	 */
	public void layEggs() {
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).layEgg();
		}

		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).layEgg();
		}
	}

	/**
	 * Check if eggs are ready to hatch
	 */
	public void hatchEggs() {
		for (int i = 0; i < eggs.size(); i++) {
			if (eggs.get(i).hatch()) {
				if (eggs.get(i) == StatsPanel.selectedEgg)
					StatsPanel.selectedEgg = null;
				eggs.remove(i);
				i--;

			}
		}
	}

	/**
	 * Incrementally decay the nutrition of food that is not eaten
	 */
	public void decayFood() {
		for (int i = 0; i < food.size(); i++) {
			food.get(i).decayNutrition();
			if (food.get(i).getNutrition() == 0) {
				if (DrawArea.food.get(i) == StatsPanel.selectedFood)
					StatsPanel.selectedFood = null;
				food.remove(i);
				i--;
			}
		}
	}

	/**
	 * Check if organisms have sufficient energy to remain alive
	 */
	public void energyCheck() {
		for (int i = 0; i < carnivores.size(); i++) {
			carnivores.get(i).energyUse();
			if (carnivores.get(i).getEnergy() <= 0) {
				if (carnivores.get(i) == StatsPanel.selectedOrg)
					StatsPanel.selectedOrg = null;
				carnivores.remove(i);
				i--;
			}
		}
		for (int i = 0; i < herbivores.size(); i++) {
			herbivores.get(i).energyUse();
			if (herbivores.get(i).getEnergy() <= 0) {
				if (herbivores.get(i) == StatsPanel.selectedOrg)
					StatsPanel.selectedOrg = null;
				herbivores.remove(i);
				i--;
			}
		}
	}

	/**
	 * Spawn food randomly every 3 ticks
	 */
	public void spawnFood() {
		if (GamePane.tickCounter % 3 == 0) {
			food.add(new Food(
					new Point((int) (Math.random() * (width - 16) + 8), (int) (Math.random() * (height - 16) + 8)),
					(Math.random() * ((startMaxFood - startMinFood) + 1.0) + startMinFood)));
		}
	}

	/**
	 * Check if organism is able to eat
	 */
	public void eatCheck() {
		for (int i = 0; i < carnivores.size(); i++)
			carnivores.get(i).eat();
		for (int i = 0; i < herbivores.size(); i++)
			herbivores.get(i).eat();
	}

	/**
	 * Draw trails behind organisms when the move
	 */
	public synchronized void drawTrails() {
		g.setColor(new Color(0, 255, 0, 100));
		for (int i = 0; i < carnivores.size(); i++) {
			ArrayList<Point> points = carnivores.get(i).prevPoints;
			int stroke = 1;
			g.setStroke(new BasicStroke(stroke));
			for (int j = 0; j < points.size() - 1; j++) {
				if (points.get(j) == null || points.get(j + 1) == null) {
					stroke++;
					g.setStroke(new BasicStroke(stroke));
				} else {
					if (GamePane.drawRegion.contains(points.get(j)))
						g.drawLine(points.get(j).x, points.get(j).y, points.get(j + 1).x, points.get(j + 1).y);
				}
			}
		}

		for (int i = 0; i < herbivores.size(); i++) {
			ArrayList<Point> points = herbivores.get(i).prevPoints;
			int stroke = 1;
			g.setStroke(new BasicStroke(stroke));
			for (int j = 0; j < points.size() - 1; j++) {
				if (points.get(j) == null || points.get(j + 1) == null) {
					stroke++;
					g.setStroke(new BasicStroke(stroke));
				} else {
					if (GamePane.drawRegion.contains(points.get(j)))
						g.drawLine(points.get(j).x, points.get(j).y, points.get(j + 1).x, points.get(j + 1).y);
				}
			}
		}
	}

	/**
	 * Draw oval around selected organism
	 */
	public synchronized void drawSelected() {
		if (StatsPanel.selectedOrg != null && StatsPanel.selectedEgg == null) {
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(2));
			g.drawOval(StatsPanel.selectedOrg.getPoint().x - 32, StatsPanel.selectedOrg.getPoint().y - 32, 64, 64);
			int dr = StatsPanel.selectedOrg.getDetectRadius();
			g.setStroke(new BasicStroke(1));
			g.setColor(Color.red);
			g.drawOval(StatsPanel.selectedOrg.getPoint().x - dr, StatsPanel.selectedOrg.getPoint().y - dr, dr * 2,
					dr * 2);
		} else if (StatsPanel.selectedOrg == null && StatsPanel.selectedEgg != null) {
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(2));
			g.drawOval(StatsPanel.selectedEgg.getPoint().x - 16, StatsPanel.selectedEgg.getPoint().y - 16, 32, 32);
		} else if (StatsPanel.selectedOrg == null && StatsPanel.selectedEgg == null
				&& StatsPanel.selectedFood != null) {
			g.setColor(Color.green);
			g.setStroke(new BasicStroke(2));
			g.drawOval(StatsPanel.selectedFood.getPoint().x - 16, StatsPanel.selectedFood.getPoint().y - 16, 32, 32);
		}
	}

	/**
	 * Update buffered image depicting the simulation
	 */
	public void updateImage() {
		/**
		 * Draws and paints background
		 */
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		/**
		 * Rotates image of organism. Only works if image is a SQUARE.
		 * Otherwise, image will be cropped when rotated
		 */

		double locX = cImg.getWidth() / 2;
		double locY = cImg.getHeight() / 2;
		for (int i = 0; i < eggs.size(); i++) {
			if (GamePane.drawRegion.contains(eggs.get(i).getPoint()))
				g.drawImage(eggs.get(i).getImage(), eggs.get(i).getPoint().x - 8, eggs.get(i).getPoint().y - 8, null);
		}

		for (int i = 0; i < food.size(); i++) {
			if (GamePane.drawRegion.contains(food.get(i).getPoint().x, food.get(i).getPoint().y))
				g.drawImage(food.get(i).getImage(), food.get(i).getPoint().x - 8, food.get(i).getPoint().y - 8, null);
		}

		drawTrails();
		for (int i = 0; i < carnivores.size(); i++) {
			if (GamePane.drawRegion.contains(carnivores.get(i).getPoint())) {
				AffineTransform tx = AffineTransform
						.getRotateInstance(Math.toRadians(360 - carnivores.get(i).getAngle()), locX, locY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				g.drawImage(op.filter(carnivores.get(i).getImage(), null),
						carnivores.get(i).getPoint().x - (cImg.getWidth() / 2),
						carnivores.get(i).getPoint().y - (cImg.getHeight() / 2), null);
			}
		}
		for (int i = 0; i < herbivores.size(); i++) {
			if (GamePane.drawRegion.contains(herbivores.get(i).getPoint())) {
				AffineTransform tx = AffineTransform
						.getRotateInstance(Math.toRadians(360 - herbivores.get(i).getAngle()), locX, locY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				g.drawImage(op.filter(herbivores.get(i).getImage(), null),
						herbivores.get(i).getPoint().x - (hImg.getWidth() / 2),
						herbivores.get(i).getPoint().y - (hImg.getHeight() / 2), null);
			}
		}
		drawSelected();
	}

	/**
	 * Add a carnivore to user-specified position
	 * @param pos position to spawn new carnivore
	 */
	public static void addCarnivore(Point pos) {
		carnivores.add(new Carnivore(1, pos, Math.random() * 360.0, // angle
				(int) (Math.random() * ((startMaxSpeed - startMinSpeed) + 1) + startMinSpeed), // spd
				(int) (Math.random() * ((startMaxRad - startMinRad) + 1) + startMinRad), // dRadius
				(int) (Math.random() * ((startMaxEgg - startMinEgg) + 1) + startMinEgg), // eggCycle
				10, // carnivorePoints
				(Math.random() * ((startMaxEnergy - startMinEnergy) + 1.0) + startMinEnergy), // energy
				(Math.random() * ((startMaxMetabolism - startMinMetabolism) + 1.0) + startMinMetabolism), // metabolism
				15000));
	}

	/**
	 * Add a herbivore to user-specified position
	 * @param pos position to spawn new herbivore
	 */
	public static void addHerbivore(Point pos) {
		herbivores.add(new Herbivore(1, pos, Math.random() * 360.0, // angle
				(int) (Math.random() * ((startMaxSpeed - startMinSpeed) + 1) + startMinSpeed), // spd
				(int) (Math.random() * ((startMaxRad - startMinRad) + 1) + startMinRad), // dRadius
				(int) (Math.random() * ((startMaxEgg - startMinEgg) + 1) + startMinEgg), // eggCycle
				10, // carnivorePoints
				(Math.random() * ((startMaxEnergy - startMinEnergy) + 1.0) + startMinEnergy), // energy
				(Math.random() * ((startMaxMetabolism - startMinMetabolism) + 1.0) + startMinMetabolism)));
	}

	/**
	 * Return last organism if only one organism left
	 * @return last organism
	 */
	public Organism checkEnd() {
		if(herbivores.size() + carnivores.size() == 1){
			if(herbivores.size() != 0){
				return herbivores.get(0);
			}
			return carnivores.get(0);
		}
		return null;
	}

}
