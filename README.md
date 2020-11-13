# Utilities

Utility code that I can reuse.

## krythos.util.abstract_interfaces

This package is where some simple abstract extensions of various interfaces will exist. This way, I can implement the abstract class of whichever interface I want to use, and not have to provide an implementation for every function in the original interface. Basically, the abstract classes here provide a default empty function for interfaces, which will clean up code in utilizing projects.

## krythos.util.csv_parser

Used for parsing a csv file into a database for easy use. To use, pass a File that points to a csv file to the constructor of CSVParser. (Alternatively, you can use the function loadCSV(File) to load post-construction). Then use CSVParser.getDatabase() to get the database object. use get(int, int) to get information from a specific row & column. set(int, int, String) will set information in that specific row & column. The toString() function will list the full CSV database.
Presently, the parser doesn't save new data. I was developing this to use in another project, but I have since changed to using XMLs in that project and thus am not currently planning to complete this.

## krythos.util.dimensional_arraylist

Contains two classes. The first is ArrayList2D, which is simply a wrapper class that makes using a 2-dimensional arraylist simpler to do. 
The second class is MultiDimensionalArrayList. This was an experiment to create an arraylist of indefinite dimensions. It actually sort of works, but may not have much practical use.

## krythos.util.file_search

This class can be used to return a List of file directories inside of a given folder directory (as Strings). Parameters are used to refine search criteria. You can specify how much depth into sub-directories to go (including no limit), specify which file extensions to return (or all files regardless of extension), and specify whether you want to include folder directories in the returned list, or only files.

## krythos.util.logger

My debug logger. Can be used to print messages directly to console, or set specific log levels and print message by-level of warning, debug, info, error. I have also added logging to file, and it can be used to open a simple message dialog.

## krythos.util.swing

Where I'm putting custom Swing GUI stuff.

* CComboBox. This is a work-in-progress. Trying to create a good drop-down list.
* Dialogs. These are custom dialog boxes. So far, only one. InputAreaDialog uses a JTextArea to gather a String from the user in such a manner that the user can use multiple lines. The box is resizable.
* DropSelection. WIP, but mostly working as intended. It creates a drop-down selection menu for a JTextComponent. You define a list of options, those options are displayed based on what the user is typing, you can limit how many are shown at once, and it uses listeners to know when something was selected. At present, it only works on JInternalFrames (I was developing it for use in a JInternalFrame), but I intend to try and make it universal to JFrames or JInternalFrames.
