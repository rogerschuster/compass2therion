# compass2therion
## Converter for Compass to Therion

This is a converter for cave surveying data. It translates data from [Compass](https://www.fountainware.com/compass/) format (`*.dat`) to the format used by [Therion](https://therion.speleo.sk/) (`*.th`).
Compass has an user friendly editor for quickly and comfortably entering, organizing, error checking, and visualizing large amounts of survey data. Therion at the other hand has a built-in map editor with many predefined symbols required for cave mapping. Therion allows the creation of high quality cave maps.

**compass2therion** is meant to make use of the best of both worlds. After a caving trip you enter the survey data in Compass and use compass2therion to convert them to Therion which you use to draw up your final cave map or atlas. Of course you also can use compass2therion to migrate your existing Compass data to Therion.

**compass2therion** treats your files nice! Your Compass cave files will be opened read-only and never modified and never damaged even if something runs terribly wrong. It also will warn you if you by accident try to overwrite an existing Therion file so you can choose another file name for output. compass2therion tries to transfer as much information as possible from input to output but to make as less as possible "guesses" about how to interpret the input. A few conversions are required though. Therion for example does not support all input units Compass does. This means those measures will be converted to something that Therion can handle. Lengths and passage dimensions in feet and inches will be converted to feet, angles in degrees and minutes to degrees, and angles in "quads" will be handled in degrees.

While Compass allows almost any alphanumeric values to identify the individual surveys Therion supports a limited range of values. compass2therion emits warnings if it finds invalid survey names. The program offers an optional argument that allows the automated renaming of problematic surveys. Anyway, any automated modifications will only affect the output file, never the input data!

**compass2therion** is written in Java and requires at least a Java environment in Version 11 or later. Check out [Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html) or [OpenJDK](https://adoptium.net/?variant=openjdk11) for downloads. 

The usage is quite simple. Open a command window / console, change to the location where you have downloaded **compass2therion** to and execute the command 

`java -jar compass2therion-<VERSION>-jar-with-dependencies.jar --input [path to compass file] --output [path to therion output file] --cavename [cave name]`

Optional arguments:

`--renamesurvey (renames surveys with non-alphanumeric characters)`

Replace the section `<VERSION>` in the above command with the real version number of the downloaded program file. I recommend to put each of the three required parameters in quotation marks. If you omit all parameters the program prints a help message.
