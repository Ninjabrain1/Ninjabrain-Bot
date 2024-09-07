package ninjabrainbot.io.savestate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TempFileAccessor implements IFileAccessor {

	private final File file;

	public TempFileAccessor(String fileName) {
		file = new File(System.getProperty("java.io.tmpdir"), fileName);
	}

	public ObjectOutputStream getObjectOutputStream() throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		return new ObjectOutputStream(fileOutputStream);
	}

	public ObjectInputStream getObjectInputStream() throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		return new ObjectInputStream(fileInputStream);
	}

	@Override
	public void deleteFile() {
		file.delete();
	}

}
