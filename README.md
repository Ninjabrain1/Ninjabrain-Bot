# Ninjabrain Bot
An accurate stronghold calculator for minecraft speedrunning. Achieves better results than regular calculators by accounting for user error and stronghold generation mechanics. See [triangulation.pdf](https://github.com/Ninjabrain1/Ninjabrain-Bot/blob/main/triangulation.pdf) for an explanation of the underlying mathematics.

![NinjabrainBot](https://i.imgur.com/WVBWnrC.png)

## Getting started

Download the [latest release](https://github.com/Ninjabrain1/Ninjabrain-Bot/releases/latest) and run the jar. To use the calculator, throw an ender eye, look at it, and press F3+C, and repeat for as many eyes as you like. The calculator will give you a certainty percentage of how confident it is that the predicted location is correct. If you want the certainty improved you should also change the standard deviation in the settings (see the FAQ down below for how to do this). 

If you are used to perfect travel and want the same accuracy you need to set hotkeys for "+0.01/-0.01 to last angle" in the advanced options, and calibrate your standard deviation (can also be done in the advanced options).

## History

Ninjabrain bot was first created in November 2020, in the era of Throwpro. The bot was created because the calculators at the time were too inaccurate, especially for calculated travel which was just beeing pioneered at the time. The two most common alternatives, Throwpro and FastStronghold, used models that were too simple and were not accurate at long distances. However, back then Ninjabrain bot only existed in a more primitive form, as a twitch chat bot. The first example of it being used in a real run can be seen [here](https://youtu.be/zK96gjkLTGc?t=880).

## FAQ

#### Is this legal to use in speedruns?
Yes, as of the calculator unban 2021-12-09 it is legal for runs submitted on speedrun.com.

#### The calculator said it was 100% certain but still missed the stronghold, what went wrong?
You most likely moved when you were throwing one of the eyes. It takes way longer than most people think for the player to fully come to a stop, even when your coordinates in F3 are completely stationary you still have to wait a while before throwing the eye (I think this happens because of client-server desync, but i'm not sure). If you want to come to a stop quickly I recommend pushing yourself into a corner between two blocks. 

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

