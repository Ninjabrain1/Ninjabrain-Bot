# Ninjabrain Bot

An accurate stronghold calculator for Minecraft speedrunning. Ninjabrain Bot uses Bayesian inference to combine eye measurements, player error, and Minecraft's stronghold-generation mechanics, producing ranked locations with estimated confidence. For the derivation of the core algorithm, read [Predicting stronghold locations with Bayesian statistics](triangulation.pdf).


![Ninjabrain Bot calculating a stronghold from two eye throws](docs/assets/demo.gif)

## Contents

- [Quick start](#quick-start)
- [Video guides](#video-guides)
- [Features](#features)
- [How it works](#how-it-works)
- [FAQ](#faq)
- [Project history](#project-history)

## Quick start

1. Download the [latest release](https://github.com/Ninjabrain1/Ninjabrain-Bot/releases/latest).
2. Run the downloaded JAR.
3. Throw an eye of ender, look directly at it, and press `F3+C`.
4. Move to a new position and repeat. The calculator ranks potential stronghold locations and shows how confident it is in its predictions.

## Video guides

- [Beginner guide (2026)](https://www.youtube.com/watch?v=XmikooWmIAM) by K4yfour
- [Longer guide](https://www.youtube.com/watch?v=Rx8i7e5lu7g) by Four

## Features

### Core calculator

- Stronghold prediction from any number of eye throws
- Estimated probability for each candidate stronghold location
- Models player measurement error and stronghold-generation mechanics
- Built-in standard deviation calibration
- Subpixel angle adjustment
- Direction and distance updates while travelling

### Additional features

- Blind-coordinate evaluation from the Nether
- Fossil divine calculations, including combined divine and eye measurements
- High-precision [boat measurements](https://github.com/Ninjabrain1/Ninjabrain-Bot/wiki/Boat-measurements)
- [All Advancements mode](https://github.com/Ninjabrain1/Ninjabrain-Bot/wiki/All-advancements-mode-with-Minecraft-version-1.20-and-later)
- OBS overlay that automatically hides when no result is displayed
- [HTTP API](https://github.com/Ninjabrain1/Ninjabrain-Bot/wiki/API) for integrations
- Configurable themes, hotkeys, and community translations

## How it works

Traditional triangulation intersects the lines created by two eye throws and effectively assumes that both angles were measured perfectly. In practice, manually aiming at an eye introduces error, especially over long distances.

Ninjabrain Bot treats each measured angle as a noisy observation. It uses a calibrated normal distribution to model the player's measurement error, then applies Bayes' theorem to update the probability of candidate stronghold chunks after every throw. The prior model accounts for stronghold rings, chunk and biome snapping, and the fact that an eye points towards the nearest stronghold.

The displayed certainty is an estimated probability produced by this model. Its reliability depends on the model's assumptions and on using a standard deviation that reflects the player's actual measurement accuracy.

## FAQ

<details>
<summary><strong>Is this legal to use in speedruns?</strong></summary>

Yes, stronghold calculators were unbanned for Minecraft speedruns on December 9, 2021. Ninjabrain Bot is also listed as a community speedrunning resource.

Check the current category rules before submitting a run.

- [Calculators Unbanned announcement](https://www.speedrun.com/mc/news/j430mgx5)
- [Ninjabrain Bot speedrun.com resource](https://www.speedrun.com/mc/resources/8xmdp)

</details>

<details>
<summary><strong>The calculator said it was 100% certain but still missed the stronghold. What went wrong?</strong></summary>

You most likely moved while throwing one of the eyes. It takes longer than many players expect to come to a complete stop. Your coordinates may appear stationary in the F3 screen while client-server desynchronization still affects the throw.

Wait before throwing, or push yourself into a corner between two blocks to stop quickly. This also appears to prevent the same desynchronization issue.

The certainty value is an estimate based on the measurements and configured standard deviation. Incorrect measurements or an unrealistically low standard deviation can make the result overconfident.

</details>

<details>
<summary><strong>Can I use subpixel adjustment, like in perfect travel?</strong></summary>

Yes. In the advanced settings, assign hotkeys for `+0.01 to last angle` and `-0.01 to last angle`. These hotkeys manually adjust the angle from your most recent `F3+C` measurement.

For example, at a resolution of 1920x1027, an eye with a wide middle eye slit may require pressing the `+0.01` hotkey. See the [perfect travel document](https://docs.google.com/document/d/1JTMOIiS-Hl6_giEB0IQ5ki7UV-gvUXnNmoxhYoSgEAA/edit#heading=h.agb0mdup7ims) for more detail.

</details>

<details>
<summary><strong>What does the "Display stronghold location" setting mean?</strong></summary>

This setting only changes how the predicted location is presented. It does not affect calculation accuracy.

- **(4, 4)** shows the coordinates of the starter staircase.
- **(8, 8)** shows the `(8, 8)` position in the stronghold chunk.
- **Chunk** shows the stronghold's chunk coordinates.

</details>

<details>
<summary><strong>What is "Standard deviation" in the settings?</strong></summary>

Standard deviation describes how accurately you measure eye angles. A lower value tells the calculator to trust your readings more. Setting it lower than your real measurement error can make the calculator overconfident.

Use **Calibrate standard deviation** in the advanced settings to estimate your value. Accuracy varies with FOV, resolution, mouse sensitivity, and experience, but typical ranges are:

- `0.050-0.200` when measuring in Quake Pro
- `0.020-0.040` when measuring at 30 FOV
- `0.005-0.010` when measuring at 30 FOV with subpixel adjustment

</details>

<details>
<summary><strong>What is "Crosshair correction" in the settings?</strong></summary>

Crosshair correction compensates for crosshair misalignment caused by certain combinations of resolution and in-game settings. If your setup is not listed below, leave the correction at `0`.

- 1440p fullscreen with GUI scale 3: use `0.026`.
- 1440p fullscreen with GUI scale 6/Auto: use `0.104`.

</details>

<details>
<summary><strong>What is "Show angle errors" in the settings?</strong></summary>

Angle errors show how far each measured angle differs from the angle to the predicted stronghold, assuming that prediction is correct.

They can help you practise eye measurement and provide a sanity check during runs. Abnormally large errors may mean that an eye was measured incorrectly or thrown while you were still moving.

</details>

<details>
<summary><strong>What is "Use advanced stronghold statistics" in the settings?</strong></summary>

Advanced stronghold statistics use the existence of other strongholds and the fact that an eye points towards the closest one to improve the probability model. This should remain enabled for almost all users.

</details>

<details>
<summary><strong>What is "Show direction to stronghold" in the settings?</strong></summary>

This setting shows the direction and distance from your current position to the predicted stronghold.

After enabling it, press `F3+C` while looking down to update the direction from your current position. This works in both the Overworld and Nether; in the Nether, you do not need to look down.

The direction is displayed as both a raw angle and a relative angle showing how far left or right to turn.

</details>

<details>
<summary><strong>What does the "Lock calculator" hotkey do?</strong></summary>

The hotkey toggles locked mode. While locked:

- Reset, undo, and subpixel-adjustment hotkeys are disabled.
- **Auto reset when idle for 15 minutes** will not reset the calculator.
- `F3+C` updates the direction and distance from your current location instead of recording another eye throw. Enable **Show direction to stronghold** to display the direction.

</details>

## Project history

Ninjabrain Bot began in November 2020, during the era of Throwpro, when calculated travel was still emerging. Existing calculators used simpler models that were unreliable at long distances, so the first version introduced a probability based approach that accounted for measurement error.

The project initially ran as a Twitch chat bot before becoming the standalone desktop application maintained today. An early example of the calculator being used in a run can be seen [here](https://youtu.be/zK96gjkLTGc?t=880).

## License

Ninjabrain Bot is licensed under the [GNU General Public License v3.0](LICENSE).
