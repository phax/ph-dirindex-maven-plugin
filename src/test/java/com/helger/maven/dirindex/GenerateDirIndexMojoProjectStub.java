/**
 * Copyright (C) 2014-2018 Philip Helger (www.helger.com)
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

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.codehaus.plexus.util.ReaderFactory;

import com.helger.commons.collection.impl.CommonsArrayList;

public final class GenerateDirIndexMojoProjectStub extends MavenProjectStub
{
  /**
   * Default constructor
   */
  public GenerateDirIndexMojoProjectStub ()
  {
    final MavenXpp3Reader pomReader = new MavenXpp3Reader ();
    Model model;
    try
    {
      model = pomReader.read (ReaderFactory.newXmlReader (new File (getBasedir (), "pom.xml")));
      setModel (model);
    }
    catch (final Exception e)
    {
      throw new RuntimeException (e);
    }

    setGroupId (model.getGroupId ());
    setArtifactId (model.getArtifactId ());
    setVersion (model.getVersion ());
    setName (model.getName ());
    setUrl (model.getUrl ());
    setPackaging (model.getPackaging ());

    final Build build = new Build ();
    build.setFinalName (model.getArtifactId ());
    build.setDirectory (getBasedir () + "/target");
    build.setSourceDirectory (getBasedir () + "/src/main/java");
    build.setOutputDirectory (getBasedir () + "/target/classes");
    build.setTestSourceDirectory (getBasedir () + "/src/test/java");
    build.setTestOutputDirectory (getBasedir () + "/target/test-classes");
    setBuild (build);

    setCompileSourceRoots (new CommonsArrayList<> (getBasedir () + "/src/main/java"));
    setTestCompileSourceRoots (new CommonsArrayList<> (getBasedir () + "/src/test/java"));
  }

  /** {@inheritDoc} */
  @Override
  public File getBasedir ()
  {
    return new File (super.getBasedir () + "/src/test/resources/poms/unittest1/");
  }
}
