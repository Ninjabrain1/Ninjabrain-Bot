package ninjabrainbot.data.divine;

public class BuriedTreasure {

	public final int x, z;

	public BuriedTreasure(int x, int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Fossil && ((Fossil) obj).x == x;
	}

	/**
	 * Returns a BuriedTreasure object if the given string is the result of an F3+I
	 * command on 9,9 in any chunk in the overworld.
	 */
	public static BuriedTreasure parseF3I(String string) {
		if (!string.startsWith("/setblock "))
			return null;
		String[] substrings = string.split(" ");
		if (substrings.length != 5)
			return null;
		try {
			int x = Integer.parseInt(substrings[1]);
			int z = Integer.parseInt(substrings[3]);
			if (Math.floorMod(x, 16) == 9 && Math.floorMod(z, 16) == 9) {
				return new BuriedTreasure(Math.floorDiv(x, 16), Math.floorDiv(z, 16));
			}
			return null;
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
