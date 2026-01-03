# Sun Compass


**A vanilla-friendly survival tool for finding your way.**

The Sun Compass is a utility item that uses the position of the sun to cast a shadow on the board, helping you navigate during the day without relying on "magic" coordinate systems or debug screens.

![Sun Compass Preview](https://github.com/user-attachments/assets/8a5c6c2a-158a-4d83-a887-672f5abf3196)

## 🧭 Manual & Usage Guide

**The Sun Compass is not a magic item.** Unlike a standard compass that points to World Spawn, the Sun Compass works like a similiarly to a real-world sun dial. It is a navigational tool that requires you to understand the position of the sun and the time of day.

### The Core Concept
The compass displays a shadow cast by the central stick. This shadow is **fixed to the world's sun position**. As you turn or as time passes, the shadow moves around the board. To find North, you must align the shadow correctly based on the current time.

### How to Find North

#### 1. The Sunrise Baseline (6:00 AM / Tick 0)
Vanilla Minecraft days start at **6:00 AM**. At this specific time:
* The Sun is rising in the **East**.
* **Instruction:** Rotate yourself until the shadow points to the **East** marker on the compass board.
* **Result:** You are now facing **North**.

#### 2. Compensating for Time (The Clockwise Shift)
As the day progresses, the sun moves across the sky. To keep finding North, you must manually compensate for the time passed since 6:00 AM.
* **The Rule:** For every in-game hour that passes, you must **shift the compass clockwise**.
* **Example:**
    * **Time:** 8:00 AM (2 hours after sunrise).
    * **Action:** Align the shadow to East, then rotate yourself (and the compass) roughly **30 degrees clockwise** (2 "hours" on a clock face) to find True North.

### Summary Table
| In-Game Time | Shadow Alignment for North |
| :--- | :--- |
| **6:00 AM** (Sunrise) | Align shadow to left 6 hour mark or **East** (E) |
| **12:00 PM** (Noon) | 6 hours has passed, so align shadow to the upper 12 hour mark, or **North** (N) |
| **6:00 PM** (Sunset) | 12 hours has passed, so align shadow to right 6 hour mark or **West** (W) |

*Note: This tool requires practice. Direction accuracy is entirely based on time estimation. To find other directions than north, simply rotate by some degrees after finding north. For example, west would be 90 degrees, or 6 hours right, from north.*

## 📦 Obtaining
**Currently, this item is a Creative-only utility.**
There is no survival crafting recipe implemented yet. To obtain the Sun Compass, you must use one of the following methods:

1.  **Creative Mode:** Open the Creative Inventory and search for "Sun Compass".
2.  **Command:** Use the chat command:
    `/give @p suncompassmod:sun_compass`
## Why is East and West swapped?
This serves the function of time shifting easier than if it was the other way around. For example, at noon (tick time 6000, or 12 PM), to find North you simply point to N (or the upper 12) then to find west you would also simply move the shadow to point W (or the right 6).
This way, shifting clockwise perserves directionality. Having it the "correct" way around would introduce extra mental calculations for no reason.

## ⚠️ Compatibility & Status
* **Version:** 0.1.1 (Pre-release)
* **Loader:** NeoForge (1.21.1)
* **Possible issues:** potentially might fail in situations where there is no sun or otherwise time logic has been modified.
* **Mod Compatibility:** There has no tests with other mods yet. Please suggest mods to pair this with and I will do my best to add compatbility (currently looking to add compatbility with TerraFirmaCraft).

## 🤝 Acknowledgements
**Special thanks to David Canterbury** for the original concept and for naming the "Sun Compass.".
* This is the video by him that specifically inspired this project: https://www.youtube.com/watch?v=7EfDYFDCl14&t=131s

## Final note
I'm new to modding, github, and while I'm familiar with programming, this is an entirely new level for me. But I'm looking to improve and hopefully keep adding new mods in the future!

---
*Built with NeoForge for Minecraft 1.21.*
