# QTI to Learnosity Converter

This project is a specialised command-line tool written in Java and built with Gradle. The tool converts QTI files
(Question and Test Interoperability, version 2.1) into the Learnosity format. This facilitates the integration and
portability of educational resources. The tool is currently a work-in-progress and only basic local conversion of
simple interactions is provided. However, many more features are planned and will be implemented later.

## Prerequisites

Your system should have Java version 21 or higher, and Gradle version 8.5+ installed.

## Supported Problem Types

The following QTI 2.1 simple interactions are currently supported:
- choice
- match
- order
- textEntry
- associate
- upload

More types would be added in due course of time. Support for the aforementioned types is limited for now. Media types
and UI configuration options will not be mapped.

## Usage

Here's a quick step on how to use the tool:

You need to input two arguments. The first one corresponds to the source directory path where your QTI 2.1 files are
stored, while the second argument is the desired output directory for your Learnosity format files.

For example, if your source directory is at `./data/qti` and you wish to output the converted files
to `./data/learnosity`, you would input:

```sh
java -jar qti-learnosity-converter-fat.jar ./data/qti ./data/learnosity
```

Replace the source and destination paths according to your directory.

## Getting Started

Clone or download the project to your local machine and navigate to the directory in your terminal:

```sh
git clone https://git.epam.com/epm-elrd/qti-learnosity.git
cd qti-learnosity
```

## Building the Project

To compile and build the project, execute the following commands:

```sh
gradlew build
```

This gradle command builds and tests the project as a library without dependencies.

You also can assemble a fat JAR file (self-contained executable JAR file that includes all dependencies)
by running the following command:

```sh
gradlew fatJar
```

## Running the Project

After successfully building the project, navigate to the `app/build/libs` directory:

```sh
cd ./app/build/libs
```

Run the generated jar file with the command:

```sh
java -jar qti-learnosity-converter-fat.jar [sourceDir] [outputDir]
```

Replace [sourceDir] and [outputDir] with appropriate directories.

## Additional documentation

[TextEntry Validation Mapping Approach](docs/textentry-to-closetext-mapping.md)  
[Upload Interaction Mapping Approach](docs/upload-interaction-approach.md)

## License

This project is licensed under the [MIT License](./LICENSE.md).
