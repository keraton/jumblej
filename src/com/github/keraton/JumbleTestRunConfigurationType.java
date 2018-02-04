package com.github.keraton;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class JumbleTestRunConfigurationType implements ConfigurationType {

    @Override
    public String getDisplayName() {
        return "JumbleTest";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Jumble Run Configuration Type";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.General.ExclMark;
    }

    @NotNull
    @Override
    public String getId() {
        return "JUMBLEJ_RUN_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] {new JumbleTestRunConfigurationFactory(this)};
    }
}
