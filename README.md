excel-reporter
==============

Load data from Excel spreadsheets and print out PDF reports

This project requires Maven to be compiled. To build and run:

    cd excel-reporter/
    mvn package
    java -jar target/excel-reporter-$VERSION.jar $SRCDIR $CELL $OUTPUT

where in the last line

* $SRCDIR is the source directory for all Excel spreadsheets
* $CELL is the cell reference to sum (for example C5)
* $OUTPUT is the output PDF

To generate Eclipse project files, run:

    mvn eclipse:eclipse
    
Note that this is project is not compatible with M2E Eclipse plugin

Maven issues
============

* maven-shade-plugin throwed a SecurityException. Refer to [this blog post][1]

BUGS
========

* The report currently is not generated because it can't access an image file
* The design must be compiled ad build time, not at runtime!!!
* The final artifact (plus all dependencies) is 30MB!!!

[1]: http://www.jswaffconsulting.com/2012/03/11/java-lang-securityexception-no-manifest-section-for-signature-file-entry/
