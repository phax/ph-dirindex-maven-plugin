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

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import com.helger.commons.io.resource.ClassPathResource;

public final class GenerateDirIndexMojoTestCase extends AbstractMojoTestCase
{
  /**
   * This test requires an installed version of the plugin in the local Maven
   * repository. So call <code>mvn install</code> to run this test for the first
   * time. Afterwards the test can be run again until the next modification
   * occurs.
   *
   * @throws Exception
   *         if any
   */
  public void testSomething () throws Exception
  {
    final File aPOM = getTestFile ("src/test/resources/poms/unittest1/pom.xml");
    assertNotNull (aPOM);
    assertTrue (aPOM.exists ());

    assertTrue ("You may need to build on the commandline once!",
                new ClassPathResource ("/" + getPluginDescriptorLocation ()).exists ());

    final GenerateDirIndexMojo aMojo = (GenerateDirIndexMojo) lookupMojo ("generate-dirindex", aPOM);
    assertNotNull (aMojo);
    aMojo.execute ();
  }
}
