package ninjabrainbot.io;

public class VersionURL {
	public final String url;
	public final String html_url;
	public final String tag;

	public VersionURL(String url, String html_url, String tag) {
		this.url = url;
		this.html_url = html_url;
		this.tag = tag;
	}
}