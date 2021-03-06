/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.idea.devkit.dom.impl;

import com.intellij.pom.PomTarget;
import com.intellij.pom.PomTargetPsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.util.xml.DomTarget;
import org.jetbrains.idea.devkit.dom.Extension;
import org.jetbrains.idea.devkit.dom.ExtensionPoint;

/**
 * @author Dmitry Avdeev
 *         Date: 10/10/11
 */
public abstract class ExtensionImpl implements Extension {

  @Override
  public ExtensionPoint getExtensionPoint() {
    PsiElement declaration = getChildDescription().getDeclaration(getManager().getProject());
    if (declaration instanceof PomTargetPsiElement) {
      PomTarget target = ((PomTargetPsiElement)declaration).getTarget();
      if (target instanceof DomTarget) {
        final ExtensionPoint extensionPoint = (ExtensionPoint)((DomTarget)target).getDomElement();
        return extensionPoint.isValid() ? extensionPoint : null;
      }
    }
    return null;
  }
}
