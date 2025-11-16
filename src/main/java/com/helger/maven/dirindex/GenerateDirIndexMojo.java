/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
import java.io.IOException;
import java.util.Comparator;
import java.util.function.Predicate;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jspecify.annotations.NonNull;

import com.helger.base.numeric.mutable.MutableInt;
import com.helger.base.string.StringHelper;
import com.helger.base.string.StringImplode;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.hierarchy.visit.DefaultHierarchyVisitorCallback;
import com.helger.collection.hierarchy.visit.EHierarchyVisitorReturn;
import com.helger.collection.stack.NonBlockingStack;
import com.helger.io.file.FileIOError;
import com.helger.io.file.FileOperations;
import com.helger.io.file.FilenameHelper;
import com.helger.io.file.IFileFilter;
import com.helger.tree.io.FileSystemFolderTree;
import com.helger.tree.util.TreeVisitor;
import com.helger.tree.withid.folder.DefaultFolderTreeItem;

/**
 * @author Philip Helger
 * @description Create the index of a directory and store it into an XML file. The information will
 *              be part of the created JAR/WAR/... file. The resulting file will reside in a custom
 *              directory of the created artifact.
 */
@Mojo (name = "generate-dirindex", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public final class GenerateDirIndexMojo extends AbstractMojo
{
  @Parameter (property = "project", required = true, readonly = true)
  MavenProject project;

  /**
   * The directory which should be indexed. This directory is mandatory to be specified. This
   * directory is included in the resulting index file.
   */
  @Parameter (property = "sourceDirectory", required = true)
  private File sourceDirectory;

  /**
   * Should the source directory be scanned recursively for files? true by default.
   */
  @Parameter (property = "recursive", defaultValue = "true")
  private boolean recursive = true;

  /**
   * An optional regular expression to index only directories that match this regular expression. If
   * it is not specified, all directories are used.
   *
   * @since 4.0.4
   */
  @Parameter (property = "dirnameRegEx")
  private String dirnameRegEx;

  /**
   * An optional regular expression to index only files that match this regular expression. If it is
   * not specified, all files are used.
   */
  @Parameter (property = "filenameRegEx")
  private String filenameRegEx;

  /**
   * Should the source directory itself be excluded from the listing? This only has an impact if
   * recursive listing is enabled.
   *
   * @since 4.0.2
   */
  @Parameter (property = "sourceChildrenOnly", defaultValue = "false")
  private boolean sourceChildrenOnly = false;

  /**
   * The directory where the temporary index file will be saved.
   *
   * @required
   * @parameter property=tempDirectory
   *            default-value="${project.build.directory}/dirindex-maven-plugin"
   */
  @Parameter (property = "tempDirectory", defaultValue = "${project.build.directory}/dirindex-maven-plugin")
  private File tempDirectory;

  /**
   * The directory within the target artifact where the file should reside. This directory is
   * relative to the tempDirectory and must not be provided. If this directory is not specified,
   * than the created target file will reside by default in the root directory of the final
   * artifact.
   */
  @Parameter (property = "targetDirectory", defaultValue = "")
  private String targetDirectory;

  /**
   * The filename within the tempDirectory and the targetDirectory to be used. The resulting file
   * will always be UTF-8 encoded.
   */
  @Parameter (property = "targetFilename", defaultValue = "dirindex.xml", required = true)
  private String targetFilename;

  // Defaults to XML
  private IOutputDataCreator m_aOutputCreator = new OutputDataCreatorXML ();

  /**
   * Define the output format to be used. The default is XML. Possible values are (case
   * insensitive): <code>xml</code> and <code>text-name-only</code>. The Default is XML.
   */
  @Parameter (property = "outputFormat", defaultValue = "xml", required = true)
  private String outputFormat;

  public void setSourceDirectory (@NonNull final File aDir) throws IOException
  {
    sourceDirectory = aDir;
    if (!sourceDirectory.isAbsolute ())
      sourceDirectory = new File (project.getBasedir (), aDir.getPath ()).getCanonicalFile ();
    if (!sourceDirectory.exists ())
      getLog ().error ("Source directory " + sourceDirectory.toString () + " does not exist!");
  }

  public void setTargetDirectory (@NonNull final String sDir)
  {
    targetDirectory = sDir;
    if (StringHelper.isNotEmpty (sDir))
    {
      final File td = new File (sDir);
      if (td.isAbsolute ())
        getLog ().error ("Target directory " + sDir + " should not be absolute");
    }
  }

  public void setTempDirectory (@NonNull final File aDir)
  {
    tempDirectory = aDir;
    if (!tempDirectory.isAbsolute ())
      tempDirectory = new File (project.getBasedir (), aDir.getPath ());
    final FileIOError aResult = FileOperations.createDirRecursiveIfNotExisting (tempDirectory);
    if (aResult.isFailure ())
      getLog ().error ("Failed to create temp directory " + aResult.toString ());
  }

  /*
   * This setter is required, because otherwise recursive would be final and the corresponding code
   * would be optimized away, even if Maven can overwrite final properties!
   */
  public void setRecursive (final boolean bRecursive)
  {
    recursive = bRecursive;
  }

  /*
   * This setter is required, because otherwise recursive would be final and the corresponding code
   * would be optimized away, even if Maven can overwrite final properties!
   */
  public void setSourceChildrenOnly (final boolean b)
  {
    sourceChildrenOnly = b;
  }

  public void setOutputFormat (final String s)
  {
    if (s.equalsIgnoreCase ("xml"))
    {
      // It's the default nothing to do here
    }
    else
      if (s.equalsIgnoreCase ("text-name-only"))
        m_aOutputCreator = new OutputDataCreatorTextNameOnly ();
      else
        getLog ().error ("The output format '" + s + "' is not supported");
  }

  private void _createOutputData (@NonNull final FileSystemFolderTree aFileTree,
                                  @NonNull final MutableInt aTotalDirs,
                                  @NonNull final MutableInt aTotalFiles) throws IOException
  {
    m_aOutputCreator.init (sourceDirectory.getCanonicalPath ());

    final NonBlockingStack <String> aAbsoluteDirStack = new NonBlockingStack <> ();
    final NonBlockingStack <String> aLoggingDirStack = new NonBlockingStack <> ();
    TreeVisitor.visitTree (aFileTree, new DefaultHierarchyVisitorCallback <> ()
    {
      @Override
      public EHierarchyVisitorReturn onItemBeforeChildren (@NonNull final DefaultFolderTreeItem <String, File, ICommonsList <File>> aItem)
      {
        final String sDirName = aItem.getID ();
        final int nSubDirCount = aItem.getChildCount ();
        final ICommonsList <File> aFiles = aItem.getData ();

        aAbsoluteDirStack.push (sDirName);
        final boolean bLogThisDirectory = !recursive || !sourceChildrenOnly || aAbsoluteDirStack.size () > 1;
        if (bLogThisDirectory)
          aLoggingDirStack.push (sDirName);

        final String sImplodedDirName = StringImplode.getImploded (FilenameHelper.UNIX_SEPARATOR, aLoggingDirStack);
        if (bLogThisDirectory)
        {
          m_aOutputCreator.addDirectory (sImplodedDirName, sDirName, nSubDirCount, aFiles == null ? 0 : aFiles.size ());
          aTotalDirs.inc ();
        }

        if (aFiles != null)
        {
          aTotalFiles.inc (aFiles.size ());
          for (final File aFile : aFiles.getSortedInline (Comparator.comparing (File::getName)))
          {
            // Directory name may be empty
            m_aOutputCreator.addFile (StringHelper.getConcatenatedOnDemand (sImplodedDirName,
                                                                            FilenameHelper.UNIX_SEPARATOR,
                                                                            aFile.getName ()),
                                      aFile.getName (),
                                      aFile.length ());
          }
        }
        return EHierarchyVisitorReturn.CONTINUE;
      }

      @Override
      public EHierarchyVisitorReturn onItemAfterChildren (@NonNull final DefaultFolderTreeItem <String, File, ICommonsList <File>> aItem)
      {
        final boolean bLogThisDirectory = !recursive || !sourceChildrenOnly || aAbsoluteDirStack.size () > 1;
        if (bLogThisDirectory)
          aLoggingDirStack.pop ();

        aAbsoluteDirStack.pop ();
        return EHierarchyVisitorReturn.CONTINUE;
      }
    });

    m_aOutputCreator.addFinalSums (aTotalDirs.intValue (), aTotalFiles.intValue ());
  }

  public void execute () throws MojoExecutionException
  {
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
    if (StringHelper.isNotEmpty (targetDirectory))
    {
      aTempTargetDir = new File (tempDirectory, targetDirectory);
      if (!aTempTargetDir.exists ())
      {
        // Ensure that the directory exists
        if (!aTempTargetDir.mkdirs ())
          throw new MojoExecutionException ("Failed to create dirindex temp-target directory " + aTempTargetDir);
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
      Predicate <File> aDirFilter = null;
      if (!recursive)
      {
        // Ignore all sub directories
        aDirFilter = x -> false;
      }
      if (StringHelper.isNotEmpty (dirnameRegEx))
      {
        aDirFilter = aDirFilter == null ? IFileFilter.filenameMatchAnyRegEx (dirnameRegEx) : aDirFilter.and (IFileFilter
                                                                                                                        .filenameMatchAnyRegEx (dirnameRegEx));
      }

      // Build the filename filter
      IFileFilter aFileFilter = null;
      if (StringHelper.isNotEmpty (filenameRegEx))
      {
        aFileFilter = IFileFilter.filenameMatchAnyRegEx (filenameRegEx);
      }

      // Build the tree to be handled
      final FileSystemFolderTree aFileSystemTree = new FileSystemFolderTree (sourceDirectory, aDirFilter, aFileFilter);

      // Convert file system tree to XML
      final MutableInt aTotalDirs = new MutableInt (0);
      final MutableInt aTotalFiles = new MutableInt (0);

      // Fill the output data
      _createOutputData (aFileSystemTree, aTotalDirs, aTotalFiles);

      // Log results
      final int nTotalDirs = aTotalDirs.intValue ();
      final int nTotalFiles = aTotalFiles.intValue ();
      getLog ().info ("Found a total of " +
                      (nTotalDirs == 1 ? "1 directory" : nTotalDirs + " directories") +
                      " and " +
                      (nTotalFiles == 1 ? "1 file" : nTotalFiles + " files"));

      // And write the result to the file
      final File aTempFile = new File (aTempTargetDir, targetFilename);
      if (m_aOutputCreator.writeToFile (aTempFile).isFailure ())
        throw new MojoExecutionException ("Failed to write target file " + aTempFile.getCanonicalPath ());
      getLog ().info ("Successfully created " + aTempFile.getCanonicalPath ());

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
