package ninjabrainbot.calculator;

public class TriangulationResult extends Chunk {

	public final int fourfour_x, fourfour_z;
	public final boolean success;
	
	/**
	 * Creates a triangulation result.
	 */
	public TriangulationResult(int chunkX, int chunkZ, double accuracy) {
		super(chunkX, chunkZ, accuracy);
		this.fourfour_x = 16 * chunkX + 4;
		this.fourfour_z = 16 * chunkZ + 4;
		this.success = Double.isFinite(accuracy) && accuracy > 0.0005;
	}
	
	/**
	 * Creates a failed triangulation result.
	 */
	public TriangulationResult() {
		this(0, 0, 0.0);
	}

}
