# CompilePlugin
A plugin tool used on Android Studio.



features:  checkout selected files(file type: aar) from subversion,and compile to Android project.

steps:

1. Create a configuration file of XML type,placed in the project's root location.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<dependencies>
    <compile>
        <svn>****</svn>
        <file>****</file>
        <file>****</file>
    </compile>
    <compile>
        <svn>***</svn>
        <file>***</file>
    </compile>
</dependencies>
```



1. Set build.gradle in the root directory

```groovy
dependencies {
        classpath 'com.android.tools.build:gradle:2.3.3'
        classpath files('libplugin.jar')
    }

ext {
    compileConfig = [
            enable : true, //Controls whether or not to configure the configuration file
            xmlName: "compile.xml" // Profile file name
    ]
}
```

3. set build.gradle in the model project

```groovy
apply plugin: 'com.gwcd.compile'
```

