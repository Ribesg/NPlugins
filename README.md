[![Build Status](https://travis-ci.org/Ribesg/NPlugins.svg?branch=master)](https://travis-ci.org/Ribesg/NPlugins)
NPlugins
=====
Informations about the project:
	http://dev.bukkit.org/bukkit-plugins/ncore/

Bamboo (Build server):
	http://ci.ribesg.fr/
JIRA (issues, bugs, suggestions):
	http://jira.ribesg.fr/
Confluence (Wiki):
	http://wiki.ribesg.fr/

Interacting with the NPlugins suite:
```xml
...
<repositories>
	<repository>
		<id>ribesg-releases</id>
		<name>Ribesg's Release Repository</name>
		<url>http://repo.ribesg.fr/content/repositories/releases</url>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>fr.ribesg.bukkit.ncore</groupId>
		<artifactId>NCore</artifactId>
		<version>0.6.9</version>
	</dependency>
</dependencies>
...
```
Then you can just listen to available events, or if you want to use some Node API, wait ~5 ticks then get it from the Core.

Snapshots are available too, in the following repository:
```xml
<repository>
	<id>ribesg-snapshots</id>
	<name>Ribesg's Snapshot Repository</name>
	<url>http://repo.ribesg.fr/content/repositories/snapshots</url>
</repository>
```
