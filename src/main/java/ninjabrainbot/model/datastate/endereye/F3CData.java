package ninjabrainbot.model.datastate.endereye;

public class F3CData {

	public final double x, y, z, horizontalAngle, verticalAngle;
	public final MCDimension dimension;

	private F3CData(double x, double y, double z, double horizontalAngle, double verticalAngle, MCDimension dimension) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.dimension = dimension;
	}

	public static F3CData tryParseF3CString(String string) {
		if (!(string.startsWith("/execute in minecraft:"))) {
			return null;
		}
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			String world = substrings[2];
			MCDimension dimension = null;
			if (world.endsWith("overworld")) {
				dimension = MCDimension.OVERWORLD;
			} else if (world.endsWith("the_nether")) {
				dimension = MCDimension.NETHER;
			} else if (world.endsWith("the_end")) {
				dimension = MCDimension.END;
			}
			
			double x = Double.parseDouble(substrings[6]);
			double y = Double.parseDouble(substrings[7]);
			double z = Double.parseDouble(substrings[8]);
			double horizontalAngle = Double.parseDouble(substrings[9]);
			double verticalAngle = Double.parseDouble(substrings[10]);
			return new F3CData(x, y, z, horizontalAngle, verticalAngle, dimension);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
