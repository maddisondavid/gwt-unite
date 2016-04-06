## Using Files ##
In order to use any file system activity in your application, you must first include the feature in the applications config.xml.  The following is an example of including the file feature :

```
  <feature name="http://xmlns.opera.com/fileio">
  </feature>
```

## Virtual File System ##
The local file system is exposed to GWT-Unite applications via the _org.gwtunite.client.file.FileSystem_ class.  The class does not give complete access to the filesystem as this would be a security risk.  Instead it provides a virtual filesystem with the following roots:

> | Application | Provides access to all files within the application (Read Only)
> | Shared      | A place where the application can read and write files to (Read/Write)
> | Storage     | A place, provided by the runtime where the application can store state.  This area will be deleted when the application is removed (Read/Write)

## Reading A File ##
The following shows a quick example of opening and reading a file:

```
	try {
		File sharedDir = FileSystem.mountSharedDirectory();
		File myFile = sharedDir.resolve("MyFile.txt");
		FileStream fileStream = myFile.open(FileMode.READ, FileMode.WRITE);
		response.writeLine(fileStream.readLine());
	} finally {
		fileStream.close()	
	}
```

Here you can see we first must mount a particular root of the virtual file system, which provides us a File representing the physical directory.  _Resolve_ is used to find a file in the directory and return a handle to it.  Once we have a handle, the _Open_ method gives us a _FileStream_ on which we have methods to read the data.

## FileMode ##
When opening a file you need to specify one or more _FileModes_.  There are four different modes with the following semmantics:

  * READ
> > Open the file for reading only, placing the file pointer at the begining of the file.  The file MUST exist.

  * WRITE
> > Open the file for writing only, placing the file pointer at the begining of the file.  The file may not exist, but if it does, it's length is truncated to 0 bytes.

  * APPEND
> > Open the file for reading and writing, placing the file pointer at the end of the file.  The file may not exist.

  * UPDATE
> > Open the file for reading and writing, placing the file pointer at the end of the file.  The file MUST exist.

The _open_ method may take any number of different modes, however if there is an invalid mode combination, or the mode does not make sense on the given file (i.e opening as WRITE on a read only mountpoint), an IOException is thrown.

## Shared Directory Hints ##
If a shared directory is used anywhere in your application, the user will be presented with a dialog box asking for the location of the shared directory when the application is installed.  It is possible to provide hints as to where this directory should possibly be using the File Features folderhint parameter. i.e. :

```
  <feature name="http://xmlns.opera.com/fileio">
    <param name="folderhint" value="home" />
  </feature>
```

Possible values for the folderhint are :

  * home - The user's default home directory, or other appropriate directory (My documents on Windows, /home/username/ on Linux, /Users/username/ on Mac)
  * pictures - The user's default pictures directory
  * music - The user's default music directory, such as /home/username/Documents/My Videos on Ubuntu)
  * video - The user's default video directory
  * documents - The user's default documents directory ( such as /home/username/Documents on Ubuntu)
  * downloads - If the user has a default downloads directory
  * desktop - The desktop, where applicable.