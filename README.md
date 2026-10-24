# LiquidAndroid

An Android Liquid Glass **simulator / tuning playground**. The first version establishes the app shell, interactive material controls, a detailed high-contrast test backdrop, and automated Android build CI.

## What is here

- Interactive scene with a draggable highlight and a detailed backdrop designed to make edge treatments visible.
- Live controls for refraction intensity, blur appearance, chromatic edge separation, tint, and rim lighting.
- Regular / Clear material presets and a toggleable backdrop grid.
- Android CI builds a debug APK, runs unit tests, and uploads the APK as a workflow artifact.

## Build

Requirements: JDK 17 and Android SDK (API 35).

```bash
gradle --no-daemon :app:assembleDebug
gradle --no-daemon :app:testDebugUnitTest
```

The CI workflow is in [android-ci.yml](.github/workflows/android-ci.yml). Every push to `main` and every pull request runs the build and test jobs.

## Fidelity roadmap

This is a visual simulator prototype, not a claim that Android can replace the OS-level iOS Liquid Glass material. The current renderer is a Canvas-based visual model: its refraction and dispersion controls adjust stylized edge cues rather than physically sampling and displacing a live backdrop. The next fidelity milestone is an Android 13+ AGSL shader that samples a captured backdrop, computes a rounded-rectangle signed-distance field, displaces samples along the surface normal, separates RGB channels, and computes Fresnel-like rim lighting. Older Android versions should get an explicitly labeled fallback. Sensor-driven light, accessibility/reduced-transparency behavior, frame-time measurements, and screenshot/device comparison tests are further milestones.

For useful visual comparison, keep the same high-detail scene behind every material and compare at the same screen size and brightness.
