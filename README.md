Testing and Monitoring related modules
======================================

!! Complete refactoring - merged Notification and Test init modules into one package.

Latest version:
---------------

* Testing and Monitoring related modules 3.0.0
* Main module names (and packages) have been changed.
* Initialization changed, read docs.
* Direct logging is no more possible, only use annotations.
* Considerable improvements to stability of connection to MMT.

Previous versions:
------------------

# NotificationAspect 2.2.2
* Added missing log mask handling to regular aspects

# NotificationAspect 2.2.1
* Removed dead global aspect files


# NotificationAspect 2.2.0
* Downgraded to java 6 (a.k.a Java 1.6 )
* Added source of TestAspect
* NotifAndTest.jar contains bytecode for both aspects

# NotificationAspect 2.1.0
+ Log filter global and local
+ added source code
+ removed zip file


# NotificationAspect 2.0.0.zip
Release changes:
* Removed configuration file: notify.json
* Added configuration functions instead:
	+ NotificationAspect.connectMonitoringTool("http://localhost:4567/");
	+ NotificationAspect.enable_log_to_stdout(true), use for debugging.
		Information sent to MMT logger will be sent to stdout too. Logging
		to stdout works even if MMT Monitoring tool is not connected.
		

