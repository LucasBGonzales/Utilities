# Utilities

Utility code that I can reuse.

## krythos.util.abstract_interfaces

This package is where some simple abstract extensions of various interfaces will exist. This way, I can implement the abstract class of whichever interface I want to use, and not have to provide an implementation for every function in the original interface. Basically, the abstract classes here provide a simple defaul empty function for interfaces.

## krythos.util.csv_parser

Used for parsing a csv file into a database for easy use. To use, pass a File that points to a csv file to the constructor of CSVParser. (Alternatively, you can use the function loadCSV(File) to load post-construction). Then use CSVParser.getDatabase() to get the database object. use get(int, int) to get information from a specific row & column. set(int, int, String) will set information in that specific row & column. The toString() function will list the full CSV database.
Presently, the parser doesn't save new data.

## krythos.util.logger

My simple debug logger. Can be used to print messages directly, or set specific log levels and print message by-level of warning, debug, info, error.

## krythos.util.swing

Where I'm putting custom Swing GUI stuff.

* CComboBox. This is a work-in-progress. Trying to create a good drop-down list.
* Dialogs. These are custom dialog boxes. So far, only one. InputAreaDialog uses a JTextArea to gather a String from the user in such a manner that the user can use multiple lines. The box is resizable.
* DropSelection. This is a work-in-progress. Still trying to create a good drop-down list.
