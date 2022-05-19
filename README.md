# Ninjabrain Bot
An accurate stronghold calculator for Minecraft speedrunning. Achieves better results than regular calculators by accounting for user error and stronghold generation mechanics. See [triangulation.pdf](https://github.com/Ninjabrain1/Ninjabrain-Bot/blob/main/triangulation.pdf) for an explanation of the underlying mathematics.

![NinjabrainBot](https://i.imgur.com/oLJo3Cn.png)

## Getting started

Download the [latest release](https://github.com/Ninjabrain1/Ninjabrain-Bot/releases/latest) and run the jar. To use the calculator, throw an ender eye, look at it, and press F3+C, and repeat for as many eyes as you like. The calculator will give you a certainty percentage of how confident it is that the predicted location is correct. If you want the certainty improved you should also change the standard deviation in the settings (see the FAQ down below for how to do this). 

If you are used to perfect travel and want the same accuracy you need to set hotkeys for "+0.01/-0.01 to last angle" in the advanced options, and calibrate your standard deviation (can also be done in the advanced options).

## Video guide

[Full tutorial](https://www.youtube.com/watch?v=Rx8i7e5lu7g)  by Four

## Features
* Most accurate known triangulation algorithm
    * Accounts for user error and all known stronghold generation mechanics
    * The certainty value describes how much the prediction should be trusted
* Subpixel adjustment
* Evaluation of blind coords (press F3+C in the nether)
* Fossil divine
    * Look at the fossil start and press F3+I to get the divine coords
    * Works with blind coord evaluation
    * Ender eye throws can be combined with divine for increased precision
* OBS overlay (auto hides in OBS when it's not showing anything)
* Lots of quality of life features

## History

Ninjabrain bot was first created in November 2020, in the era of Throwpro. The bot was created because the calculators at the time were too inaccurate, especially for calculated travel which was just beeing pioneered at the time. The two most common alternatives, Throwpro and FastStronghold, used models that were too simple and were not accurate at long distances. However, back then Ninjabrain bot only existed in a more primitive form, as a twitch chat bot. The first example of it being used in a real run can be seen [here](https://youtu.be/zK96gjkLTGc?t=880).

## FAQ

#### Is this legal to use in speedruns?
Yes, as of the calculator unban 2021-12-09 it is legal for runs submitted on speedrun.com.

#### The calculator said it was 100% certain but still missed the stronghold, what went wrong?
You most likely moved when you were throwing one of the eyes. It takes way longer than most people think for the player to fully come to a stop, even when your coordinates in F3 are completely stationary you still have to wait a while before throwing the eye (because of weird client-server desync). If you want to come to a stop quickly I recommend pushing yourself into a corner between two blocks, this also seems to prevent the aforementioned client-server desync. 

#### Can I do subpixel adjustment, like in perfect travel, with the bot?
Yes. Under advanced options in the settings you can set hotkeys for "+0.01 to last angle" and "-0.01 to last angle". Pressing those hotkeys will manually change the angle of the last F3+C you did. So for example, if your resolution is 1920x1027 and your eye has a wide middle eye-slit, you would press the "+0.01 to last angle"-hotkey to adjust the angle. See the [perfect travel document](https://docs.google.com/document/d/1JTMOIiS-Hl6_giEB0IQ5ki7UV-gvUXnNmoxhYoSgEAA/edit#heading=h.agb0mdup7ims) for more details.

#### What does the "Display stronghold location" setting mean?
It is just a setting that says how the stronghold location should be presented, it does not impact the accuracy of the calculator in any way. 
* **(4, 4)** will show you the coordinates of the starter staircase
* **(8, 8)** will show you the coordinates of (8, 8) in the stronghold chunk (the center of the chunk)
* **Chunk** will show you the chunk coordinates of the stronghold

#### What is "Standard deviation" in the settings?
Simply put, the standard deviation describes how accuretely you measure ender eye angles. The better you are at measuring ender eyes the lower the standard deviation should be. Setting the standard deviation to a small value means that the bot will trust your readings more, but setting the standard deviation too low means that the bot will be overconfident in its results. To find out your optimal standard deviation you can use the "Calibrate standard deviation" funciton available in the settings. Each players own standard deviation depends on a lot of factors such as FOV, resolution, mouse sensitivity, and experience measuring ender eyes, but the value should typically be in the range
* 0.050 - 0.200, if you're measuring eyes in quake pro
* 0.020 - 0.040, if you're measuring eyes in 30 FOV
* 0.005 - 0.010, if you're measuring eyes in 30 FOV and use subpixel adjustment

#### What is "Show angle errors" in the settings?
Angle errors tell you how wrongly you measured each ender eye (assuming that the predicted stronghold location is correct). This can be used to practice your ender eye measuring accuracy by trying to get as low errors as possible. It can also be used as a sanity check in runs, if the angle errors are abnormally large you may have measured something wrong, or moved while you were throwing one of the eyes.

#### What is "Use advanced stronghold statistics" in the settings?
"Advanced stronghold statistics" will use the existence of other strongholds and the fact that the eye points towards the closest one to make a better prediction. This improves the accuracy and it should be turned on for 99.9% of users. 

#### What is "Show direction to stronghold" in the settings?
With this setting enabled the bot will tell you the direction you need to go to get to the stronghold. If you press F3+C while looking down the bot will tell you what direction the stronghold is from your current position, which is useful if you have gone off angle while traveling. This will also update the distance to the stronghold, and can be done in both the overworld and the nether (in the nether you don't have to look down, however). The stronghold direction is presented as
both a raw angle, as well as a relative angle (meaning how much leftward or rightward you have to turn to be on the right angle).

#### What is the "Lock calculator" hotkey in the settings?
Pressing this hotkey will toggle "Locked mode", with this mode enabled:
- Hotkeys for Reset, Undo, and subpixel adjustment will be disabled, so that you do not change the result by accident.
- If "Auto reset when idle for 15 minutes" is enabled, the calculator will not auto reset.
- Pressing F3+C will not count as another eye throw, but will rather update the distance and direction to the
stronghold from your current location (keep in mind that in order to see the direction to the stronghold you have to enable "Show direction to stronghold" in the settings).
