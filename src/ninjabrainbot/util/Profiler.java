package ninjabrainbot.util;

import java.util.HashMap;

public class Profiler {
	
	private static boolean enabled = false;
	static Timer root = new Timer(null, "root");
	static Timer active = root;
	
	public static void stopAndStart(String timer) {
		if (enabled) {
			stop();
			start(timer);
		}
	}
	
	public static void start(String timer) {
		if (enabled) {
			active = active.startChild(timer);
		}
	}
	
	public static void stop() {
		if (enabled) {
			active = active.stop();
		}
	}
	
	public static void print() {
		if (enabled) {
			active.updateTotalTime();
			root.print(0);
		}
	}
	
}
class Timer {
	
	Timer parent;
	HashMap<String, Timer> children;
	String name;
	long totalTime;
	long startTime;
	
	Timer(Timer parent, String name){
		this.parent = parent;
		this.name = name;
		totalTime = 0L;
		startTime = System.currentTimeMillis();
	}

	public Timer startChild(String name) {
		if (children == null) {
			children = new HashMap<String, Timer>();
		}
		if (!children.containsKey(name)) {
			children.put(name, new Timer(this, name));
		}
		Timer child = children.get(name);
		child.startTime = System.currentTimeMillis();
		return child;
	}
	
	public Timer stop() {
		totalTime += System.currentTimeMillis() - startTime;
		return parent;
	}
	
	public void updateTotalTime() {
		long t = System.currentTimeMillis();
		totalTime += t - startTime;
		startTime = t;
		if (parent != null) {
			parent.updateTotalTime();
		}
	}
	
	public void print(int depth) {
		System.out.print("|");
		for (int i = 0; i < depth; i++) {
			System.out.print(" |");
		}
		System.out.print("-");
		System.out.println(name);
		System.out.print("|");
		for (int i = 0; i < depth; i++) {
			System.out.print(" |");
		}
		System.out.print(" ");
		if (parent == null)
			System.out.println(totalTime / 1000f + " s");
		else
			System.out.println(totalTime / 1000f + " s, (" + String.format("%.1f", 100f * totalTime / parent.totalTime) + "%)");
		if (children != null)
			children.entrySet().stream().sorted((a, b) -> -Long.compare(a.getValue().totalTime, b.getValue().totalTime)).forEach((e) -> { e.getValue().print(depth + 1);});
	}
	
}