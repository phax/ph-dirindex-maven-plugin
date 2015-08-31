#ph-dirindex-maven-plugin

A Maven 3.x plugin for creating file list for later retrieval

## Maven configuration
```xml
<plugin>
  <groupId>com.helger.maven</groupId>
  <artifactId>ph-dirindex-maven-plugin</artifactId>
  <version>1.1.1</version>
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
     Default: ``
  * `String` **targetFilename**  
     The filename within the `tempDirectory` and the `targetDirectory` to be used. The resulting file will always be UTF-8 encoded.
     Default: `dirindex.xml`

---

On Twitter: <a href="https://twitter.com/philiphelger">Follow @philiphelger</a>
