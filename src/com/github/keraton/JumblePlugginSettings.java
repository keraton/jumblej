package com.github.keraton;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JumblePlugginSettings implements Configurable {

    private LabeledComponent<TextFieldWithBrowseButton> myJarPathComponent;
    private JComponent myWholePanel;

    @Nls
    @Override
    public String getDisplayName() {
        return "JumbleJ";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return myWholePanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        String jarPath = myJarPathComponent.getComponent().getText();
        JumbleOptions.getInstance().setPath(jarPath);
    }


    private void createUIComponents() {
        myJarPathComponent = new LabeledComponent<>();
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        textFieldWithBrowseButton.addBrowseFolderListener("Find Jumble JAR File", null, null,
                new FileChooserDescriptor(false, false, true, true, false, false));

        if (JumbleOptions.getInstance().getPath() != null ) {
            textFieldWithBrowseButton.setText(JumbleOptions.getInstance().getPath());
        }
        myJarPathComponent.setComponent(textFieldWithBrowseButton);
    }
}
