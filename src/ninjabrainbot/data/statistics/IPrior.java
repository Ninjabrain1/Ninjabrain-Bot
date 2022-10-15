package ninjabrainbot.data.statistics;

import ninjabrainbot.data.stronghold.Chunk;

public interface IPrior {
	
	public Iterable<Chunk> getChunks();
	
}
