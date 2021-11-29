package ninjabrainbot.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONObject;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;

public class UpdateChecker implements Runnable {

	static boolean hasChecked = false;

	public static synchronized void check(GUI gui) {
		if (!hasChecked) {
			hasChecked = true;
			UpdateChecker updateChecker = new UpdateChecker(gui);
			Thread t = new Thread(updateChecker);
			t.start();
		}
	}

	GUI gui;

	public UpdateChecker(GUI gui) {
		this.gui = gui;
	}

	/**
	 * Checks for updates on github.
	 * 
	 * @return A download link to the latest release, or null if on the latest
	 *         update.
	 * @throws Exception if something goes wrong, idk???
	 */
	public static VersionURL checkForUpdates() throws Exception {
		URL url;
		url = new URL("https://api.github.com/repos/Ninjabrain1/Ninjabrain-Bot/releases");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		con.setRequestMethod("GET");
		con.setRequestProperty("accept", "application/vnd.github.v3+json");
		con.setDoOutput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		System.out.println(con.getResponseMessage());
		StringBuffer response = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		con.disconnect();
		JSONArray releases = new JSONArray(response.toString());
		JSONObject latest = getLatestRelease(releases);
		String tag = latest.getString("tag_name");
		JSONArray assets = getLatestRelease(releases).getJSONArray("assets");
		if (!Main.VERSION.contentEquals(tag)) {
			return new VersionURL(getDownloadUrl(assets), getReleaseUrl(latest), tag);
		}
		return null;
	}

	private static JSONObject getLatestRelease(JSONArray releases) throws Exception {
		JSONObject latest = null;
		Instant latestReleaseTime = Instant.parse("2007-12-03T10:15:30.00Z");
		for (int i = 0; i < releases.length(); i++) {
			JSONObject release = releases.getJSONObject(i);
			if (release.getBoolean("prerelease"))
				continue;
			Instant releaseTime = Instant.parse(release.getString("published_at"));
			if (releaseTime.isAfter(latestReleaseTime)) {
				latestReleaseTime = releaseTime;
				latest = release;
			}
		}
		return latest;
	}

	private static String getReleaseUrl(JSONObject release) throws Exception {
		return release.getString("html_url");
	}

	private static String getDownloadUrl(JSONArray assets) throws Exception {
		for (int i = 0; i < assets.length(); i++) {
			JSONObject asset = assets.getJSONObject(i);
			if (asset.getString("name").endsWith(".jar")) {
				return asset.getString("browser_download_url");
			}
		}
		throw new Exception("No jar file found in latest release");
	}

	@Override
	public void run() {
		long t0 = System.currentTimeMillis();
		try {
			VersionURL url = checkForUpdates();
			if (url != null)
				gui.onNewUpdateAvailable(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Time to check for updates: " + (System.currentTimeMillis() - t0) / 1000f + " seconds.");
	}

}
