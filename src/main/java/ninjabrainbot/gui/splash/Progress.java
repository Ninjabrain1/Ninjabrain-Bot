package ninjabrainbot.gui.splash;

import java.util.ArrayList;

public class Progress {

	private static ProgressKeeper pk = new ProgressKeeper();
	private static Splash splash;

	public static void init(Splash splash) {
		Progress.splash = splash;
	}

	public static void setTask(String name, float progressWhenComplete) {
		float totalProgress = pk.setCurrentTask(name, progressWhenComplete);
		splash.setProgress(pk.label(), totalProgress, progressWhenComplete);
	}

	public static void startCompoundTask(String name, float progressWhenComplete) {
		float totalProgress = pk.startCompoundTask(name, progressWhenComplete);
		splash.setProgress(pk.label(), totalProgress, progressWhenComplete);
	}

	public static void endCompoundTask() {
		pk.endCompoundTask();
	}

}

class ProgressKeeper {

	ArrayList<String> labels;
	ArrayList<Float> progressWhenStarted;
	ArrayList<Float> progressWhenComplete;

	String currentName = "";
	float currentSubTaskProgress = 0;

	public ProgressKeeper() {
		labels = new ArrayList<>();
		progressWhenStarted = new ArrayList<>();
		progressWhenComplete = new ArrayList<>();
		labels.add("");
		progressWhenStarted.add(0f);
		progressWhenComplete.add(1f);
	}

	public float setCurrentTask(String name, float progressWhenComplete) {
		float totalProgress = ((1f - currentSubTaskProgress) * progressWhenStarted()) + (currentSubTaskProgress * progressWhenComplete());
		currentSubTaskProgress = progressWhenComplete;
		currentName = name;
		return totalProgress;
	}

	public float startCompoundTask(String name, float progressWhenComplete) {
		float totalProgress = ((1f - currentSubTaskProgress) * progressWhenStarted()) + (currentSubTaskProgress * progressWhenComplete());
		float totalProgressComplete = ((1f - progressWhenComplete) * progressWhenStarted()) + (progressWhenComplete * progressWhenComplete());
		labels.add(parentLabel() + name);
		currentName = "";
		this.progressWhenStarted.add(totalProgress);
		this.progressWhenComplete.add(totalProgressComplete);
		currentSubTaskProgress = 0;
		return totalProgress;
	}

	public void endCompoundTask() {
		float totalProgress = progressWhenComplete();
		progressWhenStarted.remove(progressWhenStarted.size() - 1);
		progressWhenComplete.remove(progressWhenComplete.size() - 1);
		currentSubTaskProgress = progressWhenStarted() + (totalProgress - progressWhenComplete()) / progressWhenComplete();
	}

	public String parentLabel() {
		return labels.get(labels.size() - 1);
	}

	public String label() {
		return parentLabel() + currentName;
	}

	private float progressWhenStarted() {
		return progressWhenStarted.get(progressWhenStarted.size() - 1);
	}

	private float progressWhenComplete() {
		return progressWhenComplete.get(progressWhenComplete.size() - 1);
	}

}