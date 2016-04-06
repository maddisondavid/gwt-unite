## Introduction ##
GWT-Unite currently ships with two examples :
  1. HelloWorld - A VERY basic bare bones Hello World Service
  1. Blog - A GWT-Unite version of the blog application outlined in the Opera Unite developers primer

The following sections explain how to get these examples up and running.

## Eclipse Setup ##
When working with GWT projects inside Eclipse I always find it easier to have a User library which points to the GWT files.  With this in mind, a User library called GWT is required in order to work with the GWT-Unite example projects.

The following steps show how to set up this library:

  1. Go to Windows -> Preferences and search for User Libraries
  1. Click New and enter GWT as the library name
  1. Click on the new library and click Add Jars
  1. Go to your GWT 1.7 installation and select:
  * gwt-dev-[windows|unix].jar
  * gwt-servlet.jar
  * gwt-user.jar

## Compiling and running the examples ##
As with any library/framework, it's always easier to understand with a quick example.  Here's how to compile the example services (and get a framework to develop your own services).

Importing the Examples project :
  1. Download the GWTUniteExamples project zip
  1. File -> Import..
  1. Existing Projects Into Workspace
  1. Archive File -> GWTUniteExamples.zip

Compiling the Hello World Service :
  1. Right click on build-helloworld.xml
  1. Select Run As -> Ant Build
  1. This will build dist/GWTUniteHelloWorld.us
  1. Drag this file onto your Opera 10.10 installation to install it
  1. The browser page will show "Hello World"