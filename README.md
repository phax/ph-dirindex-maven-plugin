# ph-dirindex-maven-plugin

A Maven 3.x plugin for creating file list for later retrieval. This can be used to create a file list of one or more arbitrary directories to save it to a file. The resulting file can be used from another JAR file as a "table of contents". 

# News and noteworthy
  * v3.0.0 - work in progress
    * Updated to ph-commons 9.0.0

# Maven configuration
```xml
<plugin>
  <groupId>com.helger.maven</groupId>
  <artifactId>ph-dirindex-maven-plugin</artifactId>
  <version>2.0.0</version>
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
  </configuration>
</plugin>
```

Configuration items are:

  * `File` **sourceDirectory**  
     The directory which should be indexed. This directory is mandatory to be specified. This directory is included in the resulting index file. No default present.
  * `String` **filenameRegEx**
     An optional regular expression to index only files that match this regular expression. If it is not specified, all files are used.
  * `boolean` **recursive**  
     Should the source directory be scanned recursively for files?  
     Defaults to `true`
  * `File` **tempDirectory**  
     The directory where the temporary index file will be saved.
     Default: `${project.build.directory}/dirindex-maven-plugin`
  * `String` **targetDirectory**  
     The directory within the target artifact where the file should reside. This directory is relative to the `tempDirectory` and must not be provided. If this directory is not specified, than the created target file will reside by default in the root directory of the final artifact.
     Default: *empty String*
  * `String` **targetFilename**  
     The filename within the `tempDirectory` and the `targetDirectory` to be used. The resulting file will always be UTF-8 encoded.
     Default: `dirindex.xml`

# Output format
Example output for a dirindex.xml file (taken from the [ph-schematron](https://github.com/phax/ph-schematron/) project):

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
  <directory name="test-sch/biicore/include" basename="include" subdircount="0" filecount="8" />
  <file name="test-sch/biicore/include/BIICORE-T01-abstract.sch" basename="BIICORE-T01-abstract.sch" filesize="78109" />
  <file name="test-sch/biicore/include/BIICORE-T10-abstract.sch" basename="BIICORE-T10-abstract.sch" filesize="71422" />
  <file name="test-sch/biicore/include/BIICORE-T14-abstract.sch" basename="BIICORE-T14-abstract.sch" filesize="68173" />
  <file name="test-sch/biicore/include/BIICORE-T15-abstract.sch" basename="BIICORE-T15-abstract.sch" filesize="73309" />
  <file name="test-sch/biicore/include/BIICORE-UBL-T01-test.sch" basename="BIICORE-UBL-T01-test.sch" filesize="141960" />
  <file name="test-sch/biicore/include/BIICORE-UBL-T10-test.sch" basename="BIICORE-UBL-T10-test.sch" filesize="129622" />
  <file name="test-sch/biicore/include/BIICORE-UBL-T14-test.sch" basename="BIICORE-UBL-T14-test.sch" filesize="121279" />
  <file name="test-sch/biicore/include/BIICORE-UBL-T15-test.sch" basename="BIICORE-UBL-T15-test.sch" filesize="133115" />
  <directory name="test-sch/biiprofiles" basename="biiprofiles" subdircount="1" filecount="3" />
  <file name="test-sch/biiprofiles/BIIPROFILES-UBL-T10.sch" basename="BIIPROFILES-UBL-T10.sch" filesize="2824" />
  <file name="test-sch/biiprofiles/BIIPROFILES-UBL-T14.sch" basename="BIIPROFILES-UBL-T14.sch" filesize="2830" />
  <file name="test-sch/biiprofiles/BIIPROFILES-UBL-T15.sch" basename="BIIPROFILES-UBL-T15.sch" filesize="2824" />
  <directory name="test-sch/biiprofiles/include" basename="include" subdircount="0" filecount="6" />
  <file name="test-sch/biiprofiles/include/BIIPROFILES-T10-abstract.sch" basename="BIIPROFILES-T10-abstract.sch" filesize="2452" />
  <file name="test-sch/biiprofiles/include/BIIPROFILES-T14-abstract.sch" basename="BIIPROFILES-T14-abstract.sch" filesize="2268" />
  <file name="test-sch/biiprofiles/include/BIIPROFILES-T15-abstract.sch" basename="BIIPROFILES-T15-abstract.sch" filesize="2268" />
  <file name="test-sch/biiprofiles/include/BIIPROFILES-UBL-T10-test.sch" basename="BIIPROFILES-UBL-T10-test.sch" filesize="2651" />
  <file name="test-sch/biiprofiles/include/BIIPROFILES-UBL-T14-test.sch" basename="BIIPROFILES-UBL-T14-test.sch" filesize="2508" />
  <file name="test-sch/biiprofiles/include/BIIPROFILES-UBL-T15-test.sch" basename="BIIPROFILES-UBL-T15-test.sch" filesize="2508" />
  <directory name="test-sch/biirules" basename="biirules" subdircount="1" filecount="7" />
  <file name="test-sch/biirules/BIIRULES-Facturae-T10.sch" basename="BIIRULES-Facturae-T10.sch" filesize="2962" />
  <file name="test-sch/biirules/BIIRULES-UBL-T01.sch" basename="BIIRULES-UBL-T01.sch" filesize="2936" />
  <file name="test-sch/biirules/BIIRULES-UBL-T02.sch" basename="BIIRULES-UBL-T02.sch" filesize="2836" />
  <file name="test-sch/biirules/BIIRULES-UBL-T03.sch" basename="BIIRULES-UBL-T03.sch" filesize="2836" />
  <file name="test-sch/biirules/BIIRULES-UBL-T10.sch" basename="BIIRULES-UBL-T10.sch" filesize="2940" />
  <file name="test-sch/biirules/BIIRULES-UBL-T14.sch" basename="BIIRULES-UBL-T14.sch" filesize="2946" />
  <file name="test-sch/biirules/BIIRULES-UBL-T15.sch" basename="BIIRULES-UBL-T15.sch" filesize="2940" />
  <directory name="test-sch/biirules/include" basename="include" subdircount="0" filecount="17" />
  <file name="test-sch/biirules/include/BIIRULES-Facturae-T10-test.sch" basename="BIIRULES-Facturae-T10-test.sch" filesize="9650" />
  <file name="test-sch/biirules/include/BIIRULES-T01-abstract.sch" basename="BIIRULES-T01-abstract.sch" filesize="6894" />
  <file name="test-sch/biirules/include/BIIRULES-T01-codes.sch" basename="BIIRULES-T01-codes.sch" filesize="6968" />
  <file name="test-sch/biirules/include/BIIRULES-T02-abstract.sch" basename="BIIRULES-T02-abstract.sch" filesize="3203" />
  <file name="test-sch/biirules/include/BIIRULES-T03-abstract.sch" basename="BIIRULES-T03-abstract.sch" filesize="3233" />
  <file name="test-sch/biirules/include/BIIRULES-T10-abstract.sch" basename="BIIRULES-T10-abstract.sch" filesize="10216" />
  <file name="test-sch/biirules/include/BIIRULES-T10-codes.sch" basename="BIIRULES-T10-codes.sch" filesize="7589" />
  <file name="test-sch/biirules/include/BIIRULES-T14-abstract.sch" basename="BIIRULES-T14-abstract.sch" filesize="9724" />
  <file name="test-sch/biirules/include/BIIRULES-T14-codes.sch" basename="BIIRULES-T14-codes.sch" filesize="6983" />
  <file name="test-sch/biirules/include/BIIRULES-T15-abstract.sch" basename="BIIRULES-T15-abstract.sch" filesize="10752" />
  <file name="test-sch/biirules/include/BIIRULES-T15-codes.sch" basename="BIIRULES-T15-codes.sch" filesize="7589" />
  <file name="test-sch/biirules/include/BIIRULES-UBL-T01-test.sch" basename="BIIRULES-UBL-T01-test.sch" filesize="9377" />
  <file name="test-sch/biirules/include/BIIRULES-UBL-T02-test.sch" basename="BIIRULES-UBL-T02-test.sch" filesize="2801" />
  <file name="test-sch/biirules/include/BIIRULES-UBL-T03-test.sch" basename="BIIRULES-UBL-T03-test.sch" filesize="2801" />
  <file name="test-sch/biirules/include/BIIRULES-UBL-T10-test.sch" basename="BIIRULES-UBL-T10-test.sch" filesize="13213" />
  <file name="test-sch/biirules/include/BIIRULES-UBL-T14-test.sch" basename="BIIRULES-UBL-T14-test.sch" filesize="12110" />
  <file name="test-sch/biirules/include/BIIRULES-UBL-T15-test.sch" basename="BIIRULES-UBL-T15-test.sch" filesize="13408" />
  <directory name="test-sch/dknat" basename="dknat" subdircount="1" filecount="1" />
  <file name="test-sch/dknat/DKNAT-UBL-T10.sch" basename="DKNAT-UBL-T10.sch" filesize="2800" />
  <directory name="test-sch/dknat/include" basename="include" subdircount="0" filecount="2" />
  <file name="test-sch/dknat/include/DKNAT-T10-abstract.sch" basename="DKNAT-T10-abstract.sch" filesize="2616" />
  <file name="test-sch/dknat/include/DKNAT-UBL-T10-test.sch" basename="DKNAT-UBL-T10-test.sch" filesize="3324" />
  <directory name="test-sch/erpel" basename="erpel" subdircount="0" filecount="6" />
  <file name="test-sch/erpel/documentdispadvfeb11.sch" basename="documentdispadvfeb11.sch" filesize="9132" />
  <file name="test-sch/erpel/documentinvoicefeb11.sch" basename="documentinvoicefeb11.sch" filesize="12127" />
  <file name="test-sch/erpel/documentofferfeb11.sch" basename="documentofferfeb11.sch" filesize="8477" />
  <file name="test-sch/erpel/documentorderconffeb11.sch" basename="documentorderconffeb11.sch" filesize="9716" />
  <file name="test-sch/erpel/documentorderfeb11.sch" basename="documentorderfeb11.sch" filesize="9473" />
  <file name="test-sch/erpel/documentrequestforquotefeb11.sch" basename="documentrequestforquotefeb11.sch" filesize="11334" />
  <directory name="test-sch/error" basename="error" subdircount="0" filecount="8" />
  <file name="test-sch/error/invalid-pattern-is-a-missing.sch" basename="invalid-pattern-is-a-missing.sch" filesize="144" />
  <file name="test-sch/error/invalid-phase-has-invalid-pattern.sch" basename="invalid-phase-has-invalid-pattern.sch" filesize="140" />
  <file name="test-sch/error/invalid-phase-has-invalid-pattern2.sch" basename="invalid-phase-has-invalid-pattern2.sch" filesize="277" />
  <file name="test-sch/error/invalid-rule-extends-missing.sch" basename="invalid-rule-extends-missing.sch" filesize="167" />
  <file name="test-sch/error/invalid-xml-no-schematron1.sch" basename="invalid-xml-no-schematron1.sch" filesize="129" />
  <file name="test-sch/error/invalid-xml-no-schematron2.sch" basename="invalid-xml-no-schematron2.sch" filesize="128" />
  <file name="test-sch/error/invalid-xpath-assert-test.sch" basename="invalid-xpath-assert-test.sch" filesize="178" />
  <file name="test-sch/error/invalid-xpath-rule-context.sch" basename="invalid-xpath-rule-context.sch" filesize="184" />
  <directory name="test-sch/eugen" basename="eugen" subdircount="1" filecount="5" />
  <file name="test-sch/eugen/EUGEN-UBL-T01.sch" basename="EUGEN-UBL-T01.sch" filesize="2796" />
  <file name="test-sch/eugen/EUGEN-UBL-T10.sch" basename="EUGEN-UBL-T10.sch" filesize="2925" />
  <file name="test-sch/eugen/EUGEN-UBL-T14.sch" basename="EUGEN-UBL-T14.sch" filesize="2931" />
  <file name="test-sch/eugen/EUGEN-UBL-T15.sch" basename="EUGEN-UBL-T15.sch" filesize="2925" />
  <file name="test-sch/eugen/EUGEN-UBL-T19.sch" basename="EUGEN-UBL-T19.sch" filesize="2804" />
  <directory name="test-sch/eugen/include" basename="include" subdircount="0" filecount="14" />
  <file name="test-sch/eugen/include/EUGEN-T01-abstract.sch" basename="EUGEN-T01-abstract.sch" filesize="3802" />
  <file name="test-sch/eugen/include/EUGEN-T01-codes.sch" basename="EUGEN-T01-codes.sch" filesize="29856" />
  <file name="test-sch/eugen/include/EUGEN-T10-abstract.sch" basename="EUGEN-T10-abstract.sch" filesize="6339" />
  <file name="test-sch/eugen/include/EUGEN-T10-codes.sch" basename="EUGEN-T10-codes.sch" filesize="5471" />
  <file name="test-sch/eugen/include/EUGEN-T14-abstract.sch" basename="EUGEN-T14-abstract.sch" filesize="5469" />
  <file name="test-sch/eugen/include/EUGEN-T14-codes.sch" basename="EUGEN-T14-codes.sch" filesize="30952" />
  <file name="test-sch/eugen/include/EUGEN-T15-abstract.sch" basename="EUGEN-T15-abstract.sch" filesize="6331" />
  <file name="test-sch/eugen/include/EUGEN-T15-codes.sch" basename="EUGEN-T15-codes.sch" filesize="30952" />
  <file name="test-sch/eugen/include/EUGEN-T19-abstract.sch" basename="EUGEN-T19-abstract.sch" filesize="8011" />
  <file name="test-sch/eugen/include/EUGEN-UBL-T01-test.sch" basename="EUGEN-UBL-T01-test.sch" filesize="3821" />
  <file name="test-sch/eugen/include/EUGEN-UBL-T10-test.sch" basename="EUGEN-UBL-T10-test.sch" filesize="7219" />
  <file name="test-sch/eugen/include/EUGEN-UBL-T14-test.sch" basename="EUGEN-UBL-T14-test.sch" filesize="5973" />
  <file name="test-sch/eugen/include/EUGEN-UBL-T15-test.sch" basename="EUGEN-UBL-T15-test.sch" filesize="7182" />
  <file name="test-sch/eugen/include/EUGEN-UBL-T19-test.sch" basename="EUGEN-UBL-T19-test.sch" filesize="9387" />
  <directory name="test-sch/itnat" basename="itnat" subdircount="1" filecount="1" />
  <file name="test-sch/itnat/ITNAT-UBL-T10.sch" basename="ITNAT-UBL-T10.sch" filesize="2800" />
  <directory name="test-sch/itnat/include" basename="include" subdircount="0" filecount="2" />
  <file name="test-sch/itnat/include/ITNAT-T10-abstract.sch" basename="ITNAT-T10-abstract.sch" filesize="4323" />
  <file name="test-sch/itnat/include/ITNAT-UBL-T10-test.sch" basename="ITNAT-UBL-T10-test.sch" filesize="6068" />
  <directory name="test-sch/nogov" basename="nogov" subdircount="1" filecount="3" />
  <file name="test-sch/nogov/NOGOV-UBL-T10.sch" basename="NOGOV-UBL-T10.sch" filesize="2800" />
  <file name="test-sch/nogov/NOGOV-UBL-T14.sch" basename="NOGOV-UBL-T14.sch" filesize="2806" />
  <file name="test-sch/nogov/NOGOV-UBL-T15.sch" basename="NOGOV-UBL-T15.sch" filesize="2800" />
  <directory name="test-sch/nogov/include" basename="include" subdircount="0" filecount="6" />
  <file name="test-sch/nogov/include/NOGOV-T10-abstract.sch" basename="NOGOV-T10-abstract.sch" filesize="4486" />
  <file name="test-sch/nogov/include/NOGOV-T14-abstract.sch" basename="NOGOV-T14-abstract.sch" filesize="3322" />
  <file name="test-sch/nogov/include/NOGOV-T15-abstract.sch" basename="NOGOV-T15-abstract.sch" filesize="4490" />
  <file name="test-sch/nogov/include/NOGOV-UBL-T10-test.sch" basename="NOGOV-UBL-T10-test.sch" filesize="6777" />
  <file name="test-sch/nogov/include/NOGOV-UBL-T14-test.sch" basename="NOGOV-UBL-T14-test.sch" filesize="4369" />
  <file name="test-sch/nogov/include/NOGOV-UBL-T15-test.sch" basename="NOGOV-UBL-T15-test.sch" filesize="6777" />
  <directory name="test-sch/nonat" basename="nonat" subdircount="1" filecount="4" />
  <file name="test-sch/nonat/NONAT-UBL-T10.sch" basename="NONAT-UBL-T10.sch" filesize="2800" />
  <file name="test-sch/nonat/NONAT-UBL-T14.sch" basename="NONAT-UBL-T14.sch" filesize="2806" />
  <file name="test-sch/nonat/NONAT-UBL-T15.sch" basename="NONAT-UBL-T15.sch" filesize="2800" />
  <file name="test-sch/nonat/NONAT-ubl-T17.sch" basename="NONAT-ubl-T17.sch" filesize="2927" />
  <directory name="test-sch/nonat/include" basename="include" subdircount="0" filecount="9" />
  <file name="test-sch/nonat/include/NONAT-T10-abstract.sch" basename="NONAT-T10-abstract.sch" filesize="3618" />
  <file name="test-sch/nonat/include/NONAT-T14-abstract.sch" basename="NONAT-T14-abstract.sch" filesize="2957" />
  <file name="test-sch/nonat/include/NONAT-T15-abstract.sch" basename="NONAT-T15-abstract.sch" filesize="3618" />
  <file name="test-sch/nonat/include/NONAT-T17-abstract.sch" basename="NONAT-T17-abstract.sch" filesize="6148" />
  <file name="test-sch/nonat/include/NONAT-T17-codes.sch" basename="NONAT-T17-codes.sch" filesize="5909" />
  <file name="test-sch/nonat/include/NONAT-UBL-T10-test.sch" basename="NONAT-UBL-T10-test.sch" filesize="4955" />
  <file name="test-sch/nonat/include/NONAT-UBL-T14-test.sch" basename="NONAT-UBL-T14-test.sch" filesize="3798" />
  <file name="test-sch/nonat/include/NONAT-UBL-T15-test.sch" basename="NONAT-UBL-T15-test.sch" filesize="5003" />
  <file name="test-sch/nonat/include/NONAT-ubl-T17-test.sch" basename="NONAT-ubl-T17-test.sch" filesize="5786" />
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
  

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
On Twitter: <a href="https://twitter.com/philiphelger">@philiphelger</a>
