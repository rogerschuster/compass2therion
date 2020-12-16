# compass2therion
Converter for Compass to Therion

This is a converter for cave surveying data. It translates data from [Compass](https://www.fountainware.com/compass/) format (`*.dat`) to the format used by [Therion](https://therion.speleo.sk/) (`*.th`).
Compass has an user friendly editor for quickly and comfortably entering, organizing, error checking, and visualizing large amounts of survey data. Therion at the other hand has a built-in map editor with many predefined symbols for cave mapping. Therion allows the creation of high quality cave maps.

**compass2therion** is meant to make use of the best of both worlds. After a caving trip you enter the survey data in Compass and use compass2therion to convert to Therion which you use to draw up your final cave map or atlas. Of course you also can use compass2therion to migrate your existing Compass data to Therion.

**compass2therion** treats your files nice! Your Compass cave files will be opened read-only and never damaged even if something runs terribly wrong. It also will warn you if you by accident try to overwrite an existing Therion file so you can choose another file name for output. compass2therion tries to transfer as much information as possible from source to target and to make as less as possible "guesses" about how to interpret the input. A few conversations are required though. Therion for example does not support all input units Compass does. This means the measures have to be converted. While Compass allows almost any alphanumeric values to identify the individual surveys Therion supports a limited range of values. compass2therion emits warnings if it finds invalid survey names to support the caver in renaming the surveys manually.

compass2therion is written in Java and requires at least a Java environment in Version 11 or later. Check out [Amazon Corretto](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html) or [OpenJDK](https://adoptopenjdk.net/) for details.