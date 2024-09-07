package ninjabrainbot.io.savestate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface IFileAccessor {

	ObjectOutputStream getObjectOutputStream() throws IOException;

	ObjectInputStream getObjectInputStream() throws IOException;

	void deleteFile();

}
