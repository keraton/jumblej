package com.github.keraton;

import com.intellij.application.options.ModuleDescriptionsComboBox;
import com.intellij.execution.JavaExecutionUtil;
import com.intellij.execution.configurations.ConfigurationUtil;
import com.intellij.execution.ui.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.JavaCodeFragment;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiMethodUtil;
import com.intellij.ui.EditorTextFieldWithBrowseButton;
import com.intellij.ui.PanelWithAnchor;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.swing.*;

public class JumbleTestSettingsEditor extends SettingsEditor<JumbleTestRunConfiguration> implements PanelWithAnchor {

    private JPanel myWholePanel;
    private CommonJavaParametersPanel myCommonProgramParameters;
    private JrePathEditor myJrePathEditor;
    private LabeledComponent<EditorTextFieldWithBrowseButton> myJumbleClass;
    private Project myProject;
    private JComponent myAnchor;
    private LabeledComponent<ModuleDescriptionsComboBox> myModule;

    private final ConfigurationModuleSelector myModuleSelector;

    public JumbleTestSettingsEditor(Project myProject) {
        this.myProject = myProject;
        myModuleSelector = new ConfigurationModuleSelector(myProject, myModule.getComponent());
        myModule.getComponent().addActionListener(e -> myCommonProgramParameters.setModuleContext(myModuleSelector.getModule()));
        myAnchor = UIUtil.mergeComponentsWithAnchor(myJumbleClass, myCommonProgramParameters, myJrePathEditor, myModule);
        ClassBrowser.createApplicationClassBrowser(myProject, myModuleSelector).setField(getMainClassField());
        myJrePathEditor.setDefaultJreSelector(DefaultJreSelector.fromSourceRootsDependencies(myModule.getComponent(), getMainClassField()));

    }

    @Override
    protected void resetEditorFrom(@NotNull JumbleTestRunConfiguration configuration) {
        myCommonProgramParameters.reset(configuration);
        myModuleSelector.reset(configuration);

        getMainClassField().setText(configuration.getClassToJumble() != null ? configuration.getClassToJumble().replaceAll("\\$", "\\.") : "");
        myJrePathEditor.setPathOrName(configuration.getAlternativeJrePath(), configuration.isAlternativeJrePathEnabled());
    }


    @Override
    protected void applyEditorTo(@NotNull JumbleTestRunConfiguration configuration) throws ConfigurationException {
        myCommonProgramParameters.applyTo(configuration);
        myModuleSelector.applyTo(configuration);
        final String className = getMainClassField().getText();
        final PsiClass aClass = myModuleSelector.findClass(className);
        configuration.setClassToJumble(aClass != null ? JavaExecutionUtil.getRuntimeQualifiedName(aClass) : className);
        configuration.setAlternativeJrePath(myJrePathEditor.getJrePathOrName());
        configuration.setAlternativeJrePathEnabled(myJrePathEditor.isAlternativeJreSelected());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myWholePanel;
    }

    private void createUIComponents() {
        myJumbleClass = new LabeledComponent<>();
        myJumbleClass.setComponent(new EditorTextFieldWithBrowseButton(myProject, true, new JavaCodeFragment.VisibilityChecker() {
            @Override
            public Visibility isDeclarationVisible(PsiElement declaration, PsiElement place) {
                if (declaration instanceof PsiClass) {
                    final PsiClass aClass = (PsiClass)declaration;
                    if (ConfigurationUtil.MAIN_CLASS.value(aClass) && PsiMethodUtil.findMainMethod(aClass) != null || place.getParent() != null && myModuleSelector.findClass(((PsiClass)declaration).getQualifiedName()) != null) {
                        return Visibility.VISIBLE;
                    }
                }
                return Visibility.NOT_VISIBLE;
            }
        }));

    }

    public EditorTextFieldWithBrowseButton getMainClassField() {
        return myJumbleClass.getComponent();
    }

    @Override
    public JComponent getAnchor() {
        return myAnchor;
    }

    @Override
    public void setAnchor(@Nullable JComponent anchor) {
        this.myAnchor = anchor;
        myJumbleClass.setAnchor(anchor);
        myCommonProgramParameters.setAnchor(anchor);
        myJrePathEditor.setAnchor(anchor);
        myModule.setAnchor(anchor);
    }
}
