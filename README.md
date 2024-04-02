# android-dx-d8-adapter

## How to build

```
mvn clean package
```

## How to install

1. Install `build-tools` version `33.0.3` (last version that has `d8.jar` built with Java 8):

```
> cd /d %ANDROID_SDK%
> tools\bin\sdkmanager "build-tools;33.0.3"
> dir build-tools\34.0.0\lib\d*.jar
```

2. Save your newly built JAR as `dx.jar` next to `d8.jar` in `build-tools\34.0.0\lib`:

```
copy "target\dx-d8-adapter-1.0.jar" "%ANDROID_SDK%\build-tools\34.0.0\lib\dx.jar"
```

3. Restart Eclipse.

4. Rebuild your application project.

5. See `Console` > `Android` view:

```
Android DX to D8 adapter
Version 1.0 (Feb 2023
https://github.com/dandar3/android-dx-d8-adapter

DX called with:
 verbose    = false
 forceJumbo = false
 jarOutput  = true
 outName    = U:\Workspaces\Android\[...]\demo\bin\dexedLibs\androidx-arch-core-common-95b383e1e508bb35b6c34b9ff46ba8ce.jar
 fileNames  = [U:\Workspaces\Android\[...]\android-androidx-arch-core-common\bin\androidx-arch-core-common.jar]

D8 called with:
  jar       = U:\Android\android-sdk-eclipse\build-tools\33.0.3\lib\d8.jar
  arguments = [--output, U:\Workspaces\Android\Eclipse [ADT]\__TESTS\AndroidX Browser\demo\bin\dexedLibs\androidx-arch-core-common-95b383e1e508bb35b6c34b9ff46ba8ce.jar, U:\Workspaces\Android\Eclipse [ADT]\__TESTS\AndroidX Browser\github\android-androidx-arch-core-common\bin\androidx-arch-core-common.jar]
  elapsed  = 00:00:00.794
```

## Thanks to

* `iRootS` for raising the issue and helping with initial design and testing (Feb 2023)
* `Bolfish` for testing (Apr 2024) 