package com.github.keraton;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class JumbleTestRunConfigurationFactory extends ConfigurationFactory {

    private static final String FACTORY_NAME = "Demo configuration factory";

    public JumbleTestRunConfigurationFactory(JumbleTestRunConfigurationType jumbleTestRunConfigurationType) {
        super(jumbleTestRunConfigurationType);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new JumbleTestRunConfiguration(project, this, "Demo");
    }

    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}
