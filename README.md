##Introduction
This plugin framework provide the ability to develop host application and plugins separately. More precisely, host application and plugins are   compiled and packaged into different apk files. 

###Features
* Plugin apks are no need to be installed. Plugin apks could be placed anywhere, even be downloaded.
* Host application can access the resources in plugin.
* Android components, like Activity and Service, in plugin can be called in host application without installation.

##Usage
The three projects in Samples directories show the basic usage of the plugin framework. 

1. Import Plugin1 and Plugin2 projects and make them be dependent on PluginSDK. Make sure the scope of module PluginSDK is "Provided";
![](http://img5.tuchuang.org/uploads/2014/11/QQ20141104_1.png)
2. Compile the Plugin1 and Plugin2 to "Plugin1.apk" and "Plugin2.apk"
3. Import PluginHost project and make it be dependent on PluginSDK.
3. Copy the "Plugin1.apk" and "Plugin2.apk" to assets directory in PluginHost project.
4. Compile and run the PluginHost.

##Implementation
[The article](http://zjmdp.github.io/2014/07/22/a-plugin-framework-for-android/) written in Chinese shows the basci principle.

##Contributing
All contributions are welcome!