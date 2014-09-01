/**
 * Copyright (C) 2014 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.maven.dirindex;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.slf4j.impl.StaticLoggerBinder;

import com.helger.commons.collections.ContainerHelper;
import com.helger.commons.collections.NonBlockingStack;
import com.helger.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.helger.commons.io.file.ComparatorFileName;
import com.helger.commons.io.file.FileIOError;
import com.helger.commons.io.file.FileOperations;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.file.filter.FilenameFilterMatchAnyRegEx;
import com.helger.commons.io.file.iterate.FileSystemFolderTree;
import com.helger.commons.microdom.IMicroDocument;
import com.helger.commons.microdom.IMicroElement;
import com.helger.commons.microdom.impl.MicroDocument;
import com.helger.commons.microdom.serialize.MicroWriter;
import com.helger.commons.mutable.MutableInt;
import com.helger.commons.string.StringHelper;
import com.helger.commons.tree.utils.walk.TreeWalker;
import com.helger.commons.tree.withid.folder.DefaultFolderTreeItem;
import com.helger.commons.xml.serialize.XMLWriterSettings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Philip Helger
 * @goal generate-dirindex
 * @phase generate-resources
 * @description Create the index of a directory and store it into an XML file.
 *              The information will be part of the created JAR/WAR/... file.
 *              The resulting file will reside in a custom directory of the
 *              created artifact.
 */
@SuppressFBWarnings (value = { "UWF_UNWRITTEN_FIELD", "NP_UNWRITTEN_FIELD" }, justification = "set via maven property")
public final class GenerateDirIndexMojo extends AbstractMojo
{
  /**
   * The Maven Project.
   *
   * @parameter property="project"
   * @required
   * @readonly
   */
  MavenProject project;

  /**
   * The directory which should be index. This directory must be specified! This
   * directory is included in the resulting index file.
   *
   * @required
   * @parameter property=sourceDirectory
   */
  private File sourceDirectory;

  /**
   * An optional regular expression to index only files that match this regular
   * expression. If it is not specified, all files are used.
   *
   * @parameter property=filenameRegEx
   */
  private String filenameRegEx;

  /**
   * Should the source directory be scanned recursively for files? true by
   * default.
   *
   * @parameter property="recursive" default-value="true"
   */
  private boolean recursive = true;

  /**
   * The directory where the temporary index file will be saved.
   *
   * @required
   * @parameter property=tempDirectory
   *            default-value="${project.build.directory}/dirindex-maven-plugin"
   */
  private File tempDirectory;

  /**
   * The directory within the target artifact where the file should reside. This
   * directory is relative to the tempDirectory and must not be provided. If
   * this directory is not specified, than the created target file will reside
   * by default in the root directory of the final artifact.
   *
   * @parameter property=targetDirectory default-value=""
   */
  private String targetDirectory;

  /**
   * The filename within the tempDirectory and the targetDirectory to be used.
   * The resulting file will always be UTF-8 encoded.
   *
   * @required
   * @parameter property=targetFilename default-value="dirindex.xml"
   */
  private String targetFilename;

  public void setSourceDirectory (@Nonnull final File aDir)
  {
    sourceDirectory = aDir;
    if (!sourceDirectory.isAbsolute ())
      sourceDirectory = new File (project.getBasedir (), aDir.getPath ());
    if (!sourceDirectory.exists ())
      getLog ().error ("Source directory " + sourceDirectory.toString () + " does not exist!");
  }

  public void setTempDirectory (@Nonnull final File aDir)
  {
    tempDirectory = aDir;
    if (!tempDirectory.isAbsolute ())
      tempDirectory = new File (project.getBasedir (), aDir.getPath ());
    final FileIOError aResult = FileOperations.createDirRecursiveIfNotExisting (tempDirectory);
    if (aResult.isFailure ())
      getLog ().error ("Failed to create temp directory " + aResult.toString ());
  }

  /*
   * This setter is required, because otherwise recursive would be final and the
   * corresponding code would be optimized away, even if Maven can overwrite
   * final properties!
   */
  public void setRecursive (final boolean bRecursive)
  {
    recursive = bRecursive;
  }

  @Nonnull
  private IMicroDocument _getAsXML (@Nonnull final FileSystemFolderTree aFileTree) throws IOException
  {
    final String sBase = sourceDirectory.getCanonicalPath ();
    final IMicroDocument aDoc = new MicroDocument ();
    aDoc.appendComment ("This file was automatically generated by dirindex-maven-plugin. Please do NOT edit!");
    final IMicroElement eRoot = aDoc.appendElement ("index");
    eRoot.setAttribute ("sourcedirectory", sBase);
    final NonBlockingStack <String> aDirs = new NonBlockingStack <String> ();
    final MutableInt aTotalDirs = new MutableInt (0);
    final MutableInt aTotalFiles = new MutableInt (0);
    TreeWalker.walkTree (aFileTree,
                         new DefaultHierarchyWalkerCallback <DefaultFolderTreeItem <String, File, List <File>>> ()
                         {
                           @Override
                           public void onItemBeforeChildren (@Nonnull final DefaultFolderTreeItem <String, File, List <File>> aItem)
                           {
                             final String sDirName = aItem.getID ();
                             final int nSubDirCount = aItem.getChildCount ();
                             final List <File> aFiles = aItem.getData ();

                             aDirs.push (sDirName);

                             final String sImplodedDirName = StringHelper.getImploded (FilenameHelper.UNIX_SEPARATOR,
                                                                                       aDirs);
                             final IMicroElement eDir = eRoot.appendElement ("directory");
                             eDir.setAttribute ("name", sImplodedDirName);
                             eDir.setAttribute ("basename", sDirName);
                             eDir.setAttribute ("subdircount", Integer.toString (nSubDirCount));
                             eDir.setAttribute ("filecount", aFiles == null ? 0 : aFiles.size ());
                             aTotalDirs.inc ();

                             if (aFiles != null)
                             {
                               aTotalFiles.inc (aFiles.size ());
                               for (final File aFile : ContainerHelper.getSorted (aFiles,
                                                                                  new ComparatorFileName (Locale.US)))
                               {
                                 final IMicroElement eFile = eRoot.appendElement ("file");
                                 eFile.setAttribute ("name",
                                                     sImplodedDirName +
                                                         FilenameHelper.UNIX_SEPARATOR +
                                                         aFile.getName ());
                                 eFile.setAttribute ("basename", aFile.getName ());
                                 eFile.setAttribute ("filesize", aFile.length ());
                               }
                             }
                           }

                           @Override
                           public void onItemAfterChildren (@Nonnull final DefaultFolderTreeItem <String, File, List <File>> aItem)
                           {
                             aDirs.pop ();
                           }
                         });
    eRoot.setAttribute ("totaldirs", aTotalDirs.intValue ());
    eRoot.setAttribute ("totalfiles", aTotalFiles.intValue ());
    if (false)
      System.out.println (MicroWriter.getXMLString (eRoot));
    return aDoc;
  }

  public void execute () throws MojoExecutionException
  {
    StaticLoggerBinder.getSingleton ().setMavenLog (getLog ());
    if (tempDirectory == null)
      throw new MojoExecutionException ("No dirindex temp directory specified!");
    if (tempDirectory.exists () && !tempDirectory.isDirectory ())
      throw new MojoExecutionException ("The specified dirindex temp directory " +
                                        tempDirectory +
                                        " is not a directory!");
    if (!tempDirectory.exists ())
    {
      // Ensure that the directory exists
      if (!tempDirectory.mkdirs ())
        throw new MojoExecutionException ("Failed to create dirindex temp directory " + tempDirectory);
    }

    File aTempTargetDir;
    if (StringHelper.hasText (targetDirectory))
    {
      aTempTargetDir = new File (tempDirectory, targetDirectory);
      if (!aTempTargetDir.exists ())
      {
        // Ensure that the directory exists
        if (!aTempTargetDir.mkdirs ())
          throw new MojoExecutionException ("Failed to create dirindex temp-traget directory " + aTempTargetDir);
      }
    }
    else
      aTempTargetDir = tempDirectory;

    if (sourceDirectory == null)
      throw new MojoExecutionException ("No dirindex source directory specified!");
    if (sourceDirectory.exists () && !tempDirectory.isDirectory ())
      throw new MojoExecutionException ("The specified dirindex source directory " +
                                        sourceDirectory +
                                        " is not a directory!");
    if (!sourceDirectory.exists ())
      throw new MojoExecutionException ("The specified dirindex source directory " +
                                        sourceDirectory +
                                        " does not exist!");

    try
    {
      // Build the index
      FilenameFilter aDirFilter = null;
      if (!recursive)
        aDirFilter = new FilenameFilter ()
        {
          public boolean accept (final File aDir, final String sName)
          {
            return false;
          }
        };

      // Build the filename filter
      FilenameFilter aFileFilter = null;
      if (StringHelper.hasText (filenameRegEx))
        aFileFilter = new FilenameFilterMatchAnyRegEx (filenameRegEx);

      // Build the tree to be handled
      final FileSystemFolderTree aFileTree = new FileSystemFolderTree (sourceDirectory, aDirFilter, aFileFilter);

      // Convert file system tree to XML
      final IMicroDocument aDoc = _getAsXML (aFileTree);

      // And write the XML to the file
      final File aTempFile = new File (aTempTargetDir, targetFilename);
      SimpleFileIO.writeFile (aTempFile, MicroWriter.getXMLString (aDoc), XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ);

      // Add output directory as a resource-directory
      final Resource aResource = new Resource ();
      aResource.setDirectory (aTempTargetDir.getAbsolutePath ());
      aResource.addInclude (aTempFile.getName ());
      aResource.setFiltering (false);
      aResource.setTargetPath (targetDirectory);
      project.addResource (aResource);
    }
    catch (final IOException ex)
    {
      throw new MojoExecutionException ("Failed to build directory index!", ex);
    }
  }
}
