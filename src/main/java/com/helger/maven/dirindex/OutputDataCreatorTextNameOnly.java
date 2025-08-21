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
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.annotation.Nonempty;
import com.helger.base.state.ESuccess;
import com.helger.io.file.SimpleFileIO;

/**
 * {@link IOutputDataCreator} implementation for plain text providing each line
 * per name.
 *
 * @author Philip Helger
 */
public class OutputDataCreatorTextNameOnly implements IOutputDataCreator
{
  private final StringBuilder m_aSB = new StringBuilder ();

  public void init (@Nonnull @Nonempty final String sSourceDirectory)
  {}

  public void addDirectory (@Nonnull @Nonempty final String sDirectoryName,
                            @Nonnull @Nonempty final String sBaseName,
                            @Nonnegative final int nSubDirCount,
                            @Nonnegative final int nFileCount)
  {
    m_aSB.append (sDirectoryName).append ('/').append ('\n');
  }

  public void addFile (@Nonnull @Nonempty final String sFileName,
                       @Nonnull @Nonempty final String sBaseName,
                       @Nonnegative final long nFileSize)
  {
    m_aSB.append (sFileName).append ('\n');
  }

  public void addFinalSums (@Nonnegative final int nTotalDirs, @Nonnegative final int nTotalFiles)
  {}

  @Nonnull
  public ESuccess writeToFile (@Nonnull final File aTarget)
  {
    return SimpleFileIO.writeFile (aTarget, m_aSB.toString ().getBytes (StandardCharsets.UTF_8));
  }
}
