# RegEdit
_Windows Registry editing on the JVM_

### Gradle
```groovy
compile group: 'org.jire.regedit', name: 'regedit', version: '1.0.0'
```
### Maven
```xml
<dependency>
  <groupId>org.jire.regedit</groupId>
  <artifactId>regedit</artifactId>
  <version>1.0.0</version>
</dependency>
```

---

## Reading values

In Java:

```java
String javaHome = HKEY.LOCAL_MACHINE.get("SOFTWARE\\JavaSoft\\Java Development Kit\\1.8", "JavaHome");
System.out.println("Your Java install directory is \"" + javaHome + "\"");
```

In Kotlin:

```kotlin
val javaHome = HKEY.LOCAL_MACHINE["SOFTWARE\\JavaSoft\\Java Development Kit\\1.8", "JavaHome"]
println("Your Java install directory is \"$javaHome\"")
```

## Writing values

In Java:

```java
HKEY.LOCAL_MACHINE.set("SOFTWARE\\JavaSoft\\Java Development Kit\\1.8", "JavaHome", "C:\\Program Files\Java\\jdk1.8.0_112");
```

In Kotlin:

```kotlin
HKEY.LOCAL_MACHINE["SOFTWARE\\JavaSoft\\Java Development Kit\\1.8", "JavaHome"] = "C:\\Program Files\Java\\jdk1.8.0_112"
```

## Creating keys

```java
HKEY.LOCAL_MACHINE.create("SOFTWARE\\JavaSoft\\Java Development Kit\\1.8\\JavaHome");
```

## Deleting keys

```java
HKEY.LOCAL_MACHINE.deleteKey("SOFTWARE\\JavaSoft\\Java Development Kit\\1.8");
```

## Deleting values

```java
HKEY.LOCAL_MACHINE.deleteValue("SOFTWARE\\JavaSoft\\Java Development Kit\\1.8", "JavaHome");
```