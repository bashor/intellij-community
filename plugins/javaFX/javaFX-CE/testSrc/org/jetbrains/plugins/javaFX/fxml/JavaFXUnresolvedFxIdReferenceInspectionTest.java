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
package org.jetbrains.plugins.javaFX.fxml;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.application.PluginPathManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.javaFX.fxml.codeInsight.inspections.JavaFxUnresolvedFxIdReferenceInspection;

import java.util.List;

/**
 * User: anna
 * Date: 1/10/13
 */
public class JavaFXUnresolvedFxIdReferenceInspectionTest extends AbstractJavaFXQuickFixTest {

  @Override
  protected void enableInspections() {
    myFixture.enableInspections(new JavaFxUnresolvedFxIdReferenceInspection());
  }

  public void testUnknownRef() throws Exception {
    doTest("Controller");
  }

  public void testRootType() throws Exception {
    myFixture.configureByFiles(getTestName(true) + ".fxml");
    final List<IntentionAction> intentionActions = myFixture.filterAvailableIntentions(getHint("unknown"));
    assertEmpty(intentionActions);
  }

  public void testFieldsFromControllerSuper() throws Exception {
    myFixture.addClass("import javafx.scene.control.RadioButton;\n" +
                       "public class SuperController {\n" +
                       "    public RadioButton option1;\n" +
                       "}\n");
    myFixture.addClass("public class Controller extends SuperController {}");
    final String testFxml = getTestName(true) + ".fxml";
    myFixture.configureByFile(testFxml);
    myFixture.testHighlighting(true, false, false, testFxml);
  }

  private void doTest(final String controllerName) {
    myFixture.configureByFiles(getTestName(true) + ".fxml", controllerName + ".java");
    final IntentionAction singleIntention = myFixture.findSingleIntention(getHint("unknown"));
    assertNotNull(singleIntention);
    myFixture.launchAction(singleIntention);
    myFixture.checkResultByFile(controllerName + ".java", controllerName + "_after.java", true);
  }

  @Override
  protected String getHint(String tagName) {
    return "Create Field '" + tagName + "'";
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return PluginPathManager.getPluginHomePath("javaFX") + "/testData/inspections/unresolvedFxId/";
  }
}
