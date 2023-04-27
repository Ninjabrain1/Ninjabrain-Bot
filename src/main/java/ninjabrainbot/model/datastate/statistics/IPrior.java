package ninjabrainbot.model.datastate.statistics;

import ninjabrainbot.model.datastate.stronghold.Chunk;

public interface IPrior {

	Iterable<Chunk> getChunks();

}
