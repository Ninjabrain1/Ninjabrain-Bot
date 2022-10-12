package ninjabrainbot.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import ninjabrainbot.Main;

public class UpdateChecker implements Runnable {

	private static boolean hasChecked = false;

	public static synchronized void check(Consumer<VersionURL> urlConsumer) {
		if (!hasChecked) {
			hasChecked = true;
			UpdateChecker updateChecker = new UpdateChecker(urlConsumer);
			Thread t = new Thread(updateChecker);
			t.start();
		}
	}

	Consumer<VersionURL> urlConsumer;

	public UpdateChecker(Consumer<VersionURL> urlConsumer) {
		this.urlConsumer = urlConsumer;
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
		if (compareVersions(getSemanticVersion(tag), getSemanticVersion(Main.VERSION)) == 1) {
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
				urlConsumer.accept(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Time to check for updates: " + (System.currentTimeMillis() - t0) / 1000f + " seconds.");
	}
	
	private static int[] getSemanticVersion(String s) {
		try {
			Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+");
			Matcher m = p.matcher(s);
			if (m.find()) {
				String[] split = m.group().split("\\.");
				int[] version = new int[split.length];
				for (int i = 0; i < split.length; i++) {
					version[i] = Integer.parseInt(split[i]);
				}
				return version;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns 1 if a is newer than b, -1 if b is newer than a, and 0 if they are equivalent
	 */
	private static int compareVersions(int[] a, int[] b) {
		if (a == b)
			return 0;
		if (b == null)
			return 1;
		if (a == null)
			return -1;
		int m = Math.max(a.length, b.length);
		for (int i = 0; i < m; i++) {
			int v_a = i < a.length ? a[i] : 0;
			int v_b = i < b.length ? b[i] : 0;
			if (v_a > v_b)
				return 1;
			if (v_b > v_a)
				return -1;
		}
		return 0;
	}

}
