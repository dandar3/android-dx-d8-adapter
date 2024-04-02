# android-dx-d8-adapter

## How to build

```
mvn clean package
```

## How to install

Install latest `build-tools` (that will come with D8.jar)

```
> cd /d %ANDROID_SDK%
> tools\bin\sdkmanager "build-tools;34.0.0"
> dir build-tools\34.0.0\lib\d*.jar
```

Save your newly built `dx.jar` next to it.

```
copy "target\dx-d8-adapter-1.0.jar" "%ANDROID_SDK%\build\tools\34.0.0\lib"
```

Restart Eclipse and rebuild your application project.

## Thanks to

* `iRootS` for raising the issue and helping with initial design and testing (Feb 2023)
* `Bolfish` for testing (Apr 2024) 