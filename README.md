Notification_Module
===================

Latest version: NotificationAspect 2.1.0

+ Log filter global and local
+ added source code
+ removed zip file


History:
Latest version: NotificationAspect 2.0.0.zip
Release changes:

* Removed configuration file: notify.json
* Added configuration functions instead:
	+ NotificationAspect.connectMonitoringTool("http://localhost:4567/");
	+ NotificationAspect.enable_log_to_stdout(true), use for debugging.
		Information sent to MMT logger will be sent to stdout too. Logging
		to stdout works even if MMT Monitoring tool is not connected.
		

