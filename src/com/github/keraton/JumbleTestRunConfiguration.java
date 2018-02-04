package com.github.keraton;

import com.intellij.execution.CommonJavaRunConfigurationParameters;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.JavaRunConfigurationExtensionManager;
import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.util.JavaParametersUtil;
import com.intellij.execution.util.ProgramParametersUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JumbleTestRunConfiguration extends ModuleBasedConfiguration<JavaRunConfigurationModule>
        implements CommonJavaRunConfigurationParameters{

    private static final String CLASS_TO_JUMBLE = "classToJumble";
    private static final String V_M_PARAMETERS = "vMParameters";
    private static final String PROGRAM_PARAMETERS = "programParameters";
    private static final String WORKING_DIRECTORY = "workingDirectory";

    private String alternativeJrePath;
    private boolean alternativeJrePathEnabled;
    private String vMParameters;
    private boolean passParentEnvs;
    private Map<String, String> envs = new HashMap<>();
    private String workingDirectory;
    private String programParameters;
    private String classToJumble;

    JumbleTestRunConfiguration(Project project, JumbleTestRunConfigurationFactory factory, String name) {
        super(name, new JavaRunConfigurationModule(project, false), factory);

    }


    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new JumbleTestSettingsEditor(getProject());
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return JumbleTestRunProfileState.aJumbleTestRunProfileState(executionEnvironment, this);
    }

    @Override
    public void readExternal(Element element) {
        super.readExternal(element);

        JavaRunConfigurationExtensionManager.getInstance().readExternal(this, element);
        //DefaultJDOMExternalizer.readExternal(this, element);
        this.readModule(element);
        EnvironmentVariablesComponent.readExternal(element, this.getEnvs());

        this.classToJumble = element.getAttributeValue(CLASS_TO_JUMBLE);
        this.vMParameters = element.getAttributeValue(V_M_PARAMETERS);
        this.programParameters = element.getAttributeValue(PROGRAM_PARAMETERS);
        this.workingDirectory = element.getAttributeValue(WORKING_DIRECTORY);
    }

    @Override
    public void writeExternal(@NotNull Element element) {
        super.writeExternal(element);

        if (this.classToJumble != null) element.setAttribute(CLASS_TO_JUMBLE, this.classToJumble);
        if (this.vMParameters != null) element.setAttribute(V_M_PARAMETERS, this.vMParameters);
        if (this.programParameters != null) element.setAttribute(PROGRAM_PARAMETERS, this.programParameters);
        if (this.workingDirectory != null) element.setAttribute(WORKING_DIRECTORY, this.workingDirectory);

        JavaRunConfigurationExtensionManager.getInstance().writeExternal(this, element);
        //DefaultJDOMExternalizer.writeExternal(this, element);
        this.writeModule(element);
        EnvironmentVariablesComponent.writeExternal(element, this.getEnvs());
    }


    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        JavaParametersUtil.checkAlternativeJRE(this);
        ProgramParametersUtil.checkWorkingDirectoryExist(this, getProject(), null);
        final JavaRunConfigurationModule configurationModule = getConfigurationModule();
        configurationModule.checkModuleAndClassName(getClassToJumble(), "No Production Class");
        configurationModule.checkModuleAndClassName(getClassToJumble() + "Test", "No Test Class");
        if (JumbleOptions.getInstance().getPath() == null) {
            throw new RuntimeConfigurationError("Please set the Jumble Settings in the preferences");
        }
    }

    @Override
    public Collection<Module> getValidModules() {
        return Arrays.asList(ModuleManager.getInstance(getProject()).getModules());
    }

    @Override
    public void setProgramParameters(@Nullable String s) {
        this.programParameters = s;
    }

    @Nullable
    @Override
    public String getProgramParameters() {
        return programParameters;
    }

    @Override
    public void setWorkingDirectory(@Nullable String s) {
        this.workingDirectory = s;
    }

    @Nullable
    @Override
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public void setEnvs(@NotNull Map<String, String> map) {
        this.envs.clear();
        this.envs.putAll(map);
    }

    @NotNull
    @Override
    public Map<String, String> getEnvs() {
        return envs;
    }

    @Override
    public void setPassParentEnvs(boolean b) {
        this.passParentEnvs = b;
    }

    @Override
    public boolean isPassParentEnvs() {
        return passParentEnvs;
    }

    @Override
    public void setVMParameters(String s) {
        this.vMParameters = s;
    }

    @Override
    public String getVMParameters() {
        return vMParameters;
    }

    @Override
    public boolean isAlternativeJrePathEnabled() {
        return alternativeJrePathEnabled;
    }

    @Override
    public void setAlternativeJrePathEnabled(boolean b) {
        this.alternativeJrePathEnabled = b;
    }

    @Nullable
    @Override
    public String getAlternativeJrePath() {
        return alternativeJrePath;
    }

    @Override
    public void setAlternativeJrePath(String s) {
        this.alternativeJrePath = s;
    }

    @Nullable
    @Override
    public String getRunClass() {
        return null;
    }

    @Nullable
    @Override
    public String getPackage() {
        return null;
    }

    public String getClassToJumble() {
        return classToJumble;
    }

    public void setClassToJumble(String classToJumble) {
        this.classToJumble = classToJumble;
    }
}
