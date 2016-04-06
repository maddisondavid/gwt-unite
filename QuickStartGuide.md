## Getting Started ##
Firstly, welcome to GWT-Unite, the easiest way to develop Opera Unite application.  This quick start guide will show you how to write your first Hello World application in just a few lines of code.

### Installing the Template Project ###
Before going any further you need to import the template project and setup your Eclipse environment.
  1. Download the TemplateProject from the [downloads page](http://code.google.com/p/gwt-unite/downloads/list)
  1. Select File->Import
  1. Select Genearal->Existing projects into workspace
  1. Select the download Template Project

The Template Project will now appear as a project in your Eclipse workspace

### Setting up Eclipse ###
One last thing before we get started is to tell the template project where your GWT libraries are located. To do this :
  1. Open Windows->Preferences
  1. Search for libraries.
  1. Add a library called GWT
  1. Add the gwt-windows-dev.jar and gwt-user.jar Jar files from your GWT installation.
  1. Click OK

The GWT template project will now compile within Eclipse.

### Building the Template Project ###
The template project is a complete (mini) GWT-Unite application which we should build in order to make sure everything is setup.

  1. Edit the gwt-unite-application-builder.xml
  1. Change the GWT\_HOME property to point to your GWT installation home (i.e. c:/program files/gwt-windows-1.7.1)
```
<property name="GWT_HOME" value="c:/program files/gwt-windows-1.7.1"/>
```
  1. Execute the ANT build.xml file

At this stage the GWT compiler will be invoked and the template project built.  After it's finished, the dist directory will contain a Template.ua file.

### Installing the Application ###
Now the fun bit:

  1. Drag the Template.ua file across to your Opera 10.10 browser.

Opera will notice that it's a Unite application and offer the standard dialogue.  Once installed the index page of the template project will be shown, showing some information about the request.

Congratulations, you just built and installed your first GWT-Unite app!