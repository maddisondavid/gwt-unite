## Getting Started ##
Firstly, welcome to GWT-Unite, the easiest way to develop Opera Unite application.  This quick start guide will show you how to write your first Hello World application in just a few lines of code.

### Installing the Template Project ###
Before going any further you need to import they template project and setup your Eclipse environment;  don't worry, it's quite straight forward.  Download the TemplateProject from the GWT-Unite downloads page and use File->Import->Existing projects into workspace to import the template project.

### Setting up Eclipse ###
One last thing before we get started is to tell the template project where your GWT libraries are located. To do this, open Windows->Preferences and search for libraries.  Add a library called GWT and add the gwt-windows-dev.jar and gwt-user.jar Jar files from your GWT installation.  The GWT template project should now compile within Eclipse.

### Building the Template Project ###
The template project is a complete (mini) GWT-Unite application which we should build in order to make sure everything is setup.  Unfortunately, at this stage you'll need to edit the gwt-unite-application-builder.xml script and set the GWT\_HOME property to point to your GWT installation home.  In future versions this requirement will be removed.

Run the ANT build.xml file, the application builder will invoke the GWT compiler and then perform the packaging of the application.  When it's finished, the dist directory (you'll need to refresh the template project), will contain the Template.ua file.

### Installing the Application ###
Now the fun bit, simply drag the Template.ua file across to your Opera 10.10 browser.  Opera will notice that it's a Unite application and offer the standard dialogue.  Once installed the index page of the template project will be shown, showing some information about the request.

Congratulations, you just built and installed your first GWT-Unite app!