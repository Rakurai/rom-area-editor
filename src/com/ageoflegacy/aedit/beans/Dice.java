package com.ageoflegacy.aedit.beans;

/**
 * Dice class to represent a dice roll.
 */
public class Dice {

	private int numberOfDice;
	private int numberOfSides;
	private int bonus;

	/**
	 * Create a default pair of dice to roll.
	 */
	public Dice() {
		numberOfDice = 2;
		numberOfSides = 6;
		bonus = 0;
	}

	/**
	 * Create a dice with no bonus.
	 *
	 * @param dice
	 *            number of dice
	 * @param sides
	 *            number of sides
	 */
	public Dice(int dice, int sides) {
		numberOfDice = Math.abs(dice);
		numberOfSides = Math.abs(sides);
		bonus = 1;
	}

	/**
	 * Create a dice with explicity parameters
	 *
	 * @param dice
	 *            number of dice
	 * @param sides
	 *            number of sides each dice has
	 * @param bonus
	 *            bonus to add
	 */
	public Dice(int dice, int sides, int bonus) {
		numberOfDice = Math.abs(dice);
		numberOfSides = Math.abs(sides);
		this.bonus = bonus;
	}

	/**
	 * Create a dice from a roll string in the form numberDsides+bonus
	 *
	 * @param roll
	 *            numberDsides+bonus
	 */
	public Dice(String roll) {
		roll = roll.toLowerCase();

		String numDice, numSides, numBonus = "0";
		numDice = roll.substring(0, roll.indexOf('d'));

		if (roll.indexOf('+') == -1)
			numSides = roll.substring(roll.indexOf('d') + 1, roll.length());
		else {
			numSides = roll.substring(roll.indexOf('d') + 1, roll.indexOf('+'));
			numBonus = roll.substring(roll.indexOf('+') + 1, roll.length());
		}
		numberOfDice = Integer.parseInt(numDice.trim());
		numberOfSides = Integer.parseInt(numSides.trim());
		bonus = Integer.parseInt(numBonus.trim());
	}

	/**
	 * Get the number of dice
	 *
	 * @return number of dice integer
	 */
	public int getNumberOfDice() {
		return numberOfDice;
	}

	/**
	 * Get the number of sides each dice has
	 *
	 * @return number of sides integer
	 */
	public int getNumberOfSides() {
		return numberOfSides;
	}

	/**
	 * Get the bonus value
	 *
	 * @return bonus integer
	 */
	public int getBonus() {
		return bonus;
	}

	/**
	 * Report the dice as a String of the former numberDsides + bonus
	 *
	 * @return numberDsides+bonus
	 */
	@Override
	public String toString() {
		String temp;
		temp = Integer.toString(numberOfDice) + "d" + Integer.toString(numberOfSides);
		temp += "+" + Integer.toString(bonus);

		return temp;
	}

}
