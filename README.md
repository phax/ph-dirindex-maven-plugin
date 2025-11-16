# ph-dirindex-maven-plugin

[![javadoc](https://javadoc.io/badge2/com.helger.maven/ph-dirindex-maven-plugin/javadoc.svg)](https://javadoc.io/doc/com.helger.maven/ph-dirindex-maven-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.helger.maven/ph-dirindex-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.helger.maven/ph-dirindex-maven-plugin) 

A Maven 3.x plugin for creating file list for later retrieval. This can be used to create a file list of one or more arbitrary directories to save it to a file. The resulting file can be used from another JAR file as a "table of contents".

Note: the directory index file is created in a temporary directory (usually somewhere below `target`) and than added to the build path internally.

# Maven configuration

```xml
<plugin>
  <groupId>com.helger.maven</groupId>
  <artifactId>ph-dirindex-maven-plugin</artifactId>
  <version>5.0.1</version>
  <executions>
    <execution>
      <goals>
        <goal>generate-dirindex</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <sourceDirectory>${basedir}/src/main/resources/</sourceDirectory>
    <filenameRegEx>.*\.test</filenameRegEx>
    <targetDirectory>test-files</targetDirectory>
    <targetFilename>dirindex.xml</targetFilename>
    <outputFormat>xml</outputFormat>
  </configuration>
</plugin>
```

Configuration items are:

* `File` **sourceDirectory**  
   The directory which should be indexed. This directory is mandatory to be specified. This directory is included in the resulting index file. No default present.
* `boolean` **recursive**  
   Should the source directory be scanned recursively for files?  
   Defaults to `true`
* `String` **dirnameRegEx** (since v4.0.4)  
   An optional regular expression to index only directory names that match this regular expression. If it is not specified, all directories are used.
* `boolean` **sourceChildrenOnly** (since v4.0.2)  
   Should the source directory itself be excluded from the listing? This only has an impact if recursive listing is enabled.
   Defaults to `false`
* `String` **filenameRegEx**  
   An optional regular expression to index only files that match this regular expression. If it is not specified, all files are used.
* `File` **tempDirectory**  
   The directory where the temporary index file will be saved.
   Default: `${project.build.directory}/dirindex-maven-plugin`
* `String` **targetDirectory**  
   The directory within the target artifact where the file should reside. This directory is relative to the `tempDirectory` and must not be provided. If this directory is not specified, than the created target file will reside by default in the root directory of the final artifact.
   Default: *empty String*
* `String` **targetFilename**  
   The filename within the `tempDirectory` and the `targetDirectory` to be used. The resulting file will always be UTF-8 encoded.
   Default: `dirindex.xml`
* `String` **outputFormat**  
   Defines the format of the result. Possible values are:
    * `xml` - create XML output. This is the default value. Uses the UTF-8 character encoding.
    * `text-name-only` (since 4.0.2) - creates simple text output with one line per directory and file. Uses LF (`\n`) as the line delimiter. Uses the UTF-8 character encoding.

# Output format
Example output for a `dirindex.xml` file (taken from the [ph-schematron](https://github.com/phax/ph-schematron/) project):

```xml
<index sourcedirectory="P:\git\ph-schematron\ph-schematron-testfiles\src\main\resources\test-sch" totaldirs="23" totalfiles="137">
  <directory name="test-sch" basename="test-sch" subdircount="12" filecount="25" />
  <file name="test-sch/CellarBook.sch" basename="CellarBook.sch" filesize="3366" />
  <file name="test-sch/example-3-5.sch" basename="example-3-5.sch" filesize="785" />
  <file name="test-sch/example-3-8.sch" basename="example-3-8.sch" filesize="1455" />
  <file name="test-sch/example-4-2.sch" basename="example-4-2.sch" filesize="1449" />
  <file name="test-sch/example-5-2.sch" basename="example-5-2.sch" filesize="1519" />
  <file name="test-sch/example-6-1.sch" basename="example-6-1.sch" filesize="1526" />
  <file name="test-sch/example-7-2.sch" basename="example-7-2.sch" filesize="1017" />
  <file name="test-sch/example-8-5.sch" basename="example-8-5.sch" filesize="1034" />
  <file name="test-sch/example-9.sch" basename="example-9.sch" filesize="959" />
  <file name="test-sch/example-assert.sch" basename="example-assert.sch" filesize="1107" />
  <file name="test-sch/mets_general_rules-ISO.sch" basename="mets_general_rules-ISO.sch" filesize="2945" />
  <file name="test-sch/pattern-example.sch" basename="pattern-example.sch" filesize="1202" />
  <file name="test-sch/pattern-example-with-includes.sch" basename="pattern-example-with-includes.sch" filesize="819" />
  <file name="test-sch/schematron-additional-constraints.sch" basename="schematron-additional-constraints.sch" filesize="1934" />
  <file name="test-sch/schematron-annex-g.sch" basename="schematron-annex-g.sch" filesize="638" />
  <file name="test-sch/schematron-svrl.sch" basename="schematron-svrl.sch" filesize="7487" />
  <file name="test-sch/tcdl2.0.tsdtf.sch" basename="tcdl2.0.tsdtf.sch" filesize="18789" />
  <file name="test-sch/universalTests.sch" basename="universalTests.sch" filesize="2533" />
  <file name="test-sch/universalTests2.sch" basename="universalTests2.sch" filesize="2533" />
  <file name="test-sch/universalTests-xslt2.sch" basename="universalTests-xslt2.sch" filesize="2442" />
  <file name="test-sch/universalTests-xslt2-multidocument.sch" basename="universalTests-xslt2-multidocument.sch" filesize="2446" />
  <file name="test-sch/valid01.sch" basename="valid01.sch" filesize="875" />
  <file name="test-sch/valid02.sch" basename="valid02.sch" filesize="1022" />
  <file name="test-sch/valid03.sch" basename="valid03.sch" filesize="289" />
  <file name="test-sch/VariableTests.sch" basename="VariableTests.sch" filesize="880" />
  <directory name="test-sch/atgov" basename="atgov" subdircount="1" filecount="1" />
  <file name="test-sch/atgov/ATGOV-UBL-T10.sch" basename="ATGOV-UBL-T10.sch" filesize="2800" />
  <directory name="test-sch/atgov/include" basename="include" subdircount="0" filecount="2" />
  <file name="test-sch/atgov/include/ATGOV-T10-abstract.sch" basename="ATGOV-T10-abstract.sch" filesize="3232" />
  <file name="test-sch/atgov/include/ATGOV-UBL-T10-test.sch" basename="ATGOV-UBL-T10-test.sch" filesize="5133" />
  <directory name="test-sch/atnat" basename="atnat" subdircount="1" filecount="1" />
  <file name="test-sch/atnat/ATNAT-UBL-T10.sch" basename="ATNAT-UBL-T10.sch" filesize="2800" />
  <directory name="test-sch/atnat/include" basename="include" subdircount="0" filecount="2" />
  <file name="test-sch/atnat/include/ATNAT-T10-abstract.sch" basename="ATNAT-T10-abstract.sch" filesize="2943" />
  <file name="test-sch/atnat/include/ATNAT-UBL-T10-test.sch" basename="ATNAT-UBL-T10-test.sch" filesize="4053" />
  <directory name="test-sch/biicore" basename="biicore" subdircount="1" filecount="4" />
  <file name="test-sch/biicore/BIICORE-UBL-T01.sch" basename="BIICORE-UBL-T01.sch" filesize="2804" />
  <file name="test-sch/biicore/BIICORE-UBL-T10.sch" basename="BIICORE-UBL-T10.sch" filesize="2808" />
  <file name="test-sch/biicore/BIICORE-UBL-T14.sch" basename="BIICORE-UBL-T14.sch" filesize="2814" />
  <file name="test-sch/biicore/BIICORE-UBL-T15.sch" basename="BIICORE-UBL-T15.sch" filesize="2808" />
</index>
```

Explanation of the three elements:
* `index` - root element:
    * `@sourcedirectory` the absolute source directory on disc that was scanned
    * `@totaldirs` the overall number of directories contained (recursive)
    * `@totalfiles` the overall number of files contained (recursive) 
* `directory` - represents a single directory
    * `@name` the full directory name relative to the starting directory
    * `@basename` the name of the directory without any paths
    * `@subdircount` the number of contained sub-directories (excluding `.` and `..`)
    * `@filecount` the number of contained files in this directory (not recursive)
* `file` - represents a single file
    * `@name` the full filename including the directories relative to the starting directory
    * `@basename` the name of the file without any paths
    * `@filesize` the size of the file in bytes

# News and noteworthy

v5.0.1 - 2025-11-16
* Updated to ph-commons 12.1.0
* Using JSpecify annotations

v5.0.0 - 2025-08-25
* Requires Java 17 as the minimum version
* Updated to ph-commons 12.0.0

v4.0.4 - 2025-02-06
* Avoid empty slash at the beginning of a list, if `sourceChildrenOnly` is `true`. See [#9](https://github.com/phax/ph-dirindex-maven-plugin/issues/9) - thx @reda-alaoui
* Added new configuration property `dirnameRegEx` that can control in what directories to descent in recursive mode

v4.0.3 - 2025-02-05
* If the property `sourceChildrenOnly` is used, the root path is not contained in the output. See [#8](https://github.com/phax/ph-dirindex-maven-plugin/issues/8) - thx @reda-alaoui

v4.0.2 - 2025-02-04
* Added new configuration property `sourceChildrenOnly` to omit the root directory from listing. See [#6](https://github.com/phax/ph-dirindex-maven-plugin/issues/6) - thx @reda-alaoui
* Added new configuration property `outputFormat` to be able to create different outputs. See [#7](https://github.com/phax/ph-dirindex-maven-plugin/issues/7) - thx @reda-alaoui

v4.0.1 - 2023-07-01
* Removed the `maven-compat` dependencies for Maven 4 compatibility
* Build against Maven 3.5.0

v4.0.0 - 2023-01-08
* Using Java 11 as the baseline
* Updated to ph-commons 11

v3.0.3 - 2021-03-22
* Updated to ph-commons 10

v3.0.2 - 2020-03-11
* Release with recent library versions

v3.0.1 - 2019-06-13
* Improved logging
* Using annotation based Maven configuration

v3.0.0 - 2018-08-06
* Updated to ph-commons 9.0.0

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
It is appreciated if you star the GitHub project if you like it.