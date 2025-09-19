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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.base.state.ESuccess;

import jakarta.annotation.Nonnull;

/**
 * Abstract output data creator interface
 *
 * @author Philip Helger
 */
public interface IOutputDataCreator
{
  void init (@Nonnull @Nonempty String sSourceDirectory);

  void addDirectory (@Nonnull @Nonempty String sDirectoryName,
                     @Nonnull @Nonempty String sBaseName,
                     @Nonnegative int nSubDirCount,
                     @Nonnegative int nFileCount);

  void addFile (@Nonnull @Nonempty String sFileName, @Nonnull @Nonempty String sBaseName, @Nonnegative long nFileSize);

  void addFinalSums (@Nonnegative int nTotalDirs, @Nonnegative int nTotalFiles);

  @Nonnull
  ESuccess writeToFile (@Nonnull File aTarget);
}
