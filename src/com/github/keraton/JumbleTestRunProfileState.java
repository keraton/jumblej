package com.github.keraton;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.application.BaseJavaApplicationCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.util.JavaParametersUtil;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

public class JumbleTestRunProfileState extends BaseJavaApplicationCommandLineState<JumbleTestRunConfiguration> {

    private String originalParameters = "";


    public static JumbleTestRunProfileState aJumbleTestRunProfileState(ExecutionEnvironment environment, @NotNull JumbleTestRunConfiguration configuration) {
        String original = configuration.getProgramParameters();

        // Add -c or classpath
        String cParameters = OrderEnumerator.orderEntries(configuration.getConfigurationModule().getModule())
                                        .recursively()
                                        .getPathsList()
                                        .getPathsString();

        String testedClass = configuration.getClassToJumble();

        String jumbleParameters = original + " -c \"" + cParameters + "\" " + testedClass;

        // This is danger !!!, mutable things !!
        configuration.setProgramParameters(jumbleParameters);

        JumbleTestRunProfileState jumbleTestRunProfileState = new JumbleTestRunProfileState(environment, configuration);
        jumbleTestRunProfileState.originalParameters = original;

        return jumbleTestRunProfileState;
    }

    private JumbleTestRunProfileState(ExecutionEnvironment environment, @NotNull JumbleTestRunConfiguration configuration) {
        super(environment, configuration);
    }

    @Override
    protected JavaParameters createJavaParameters() throws ExecutionException {
        JavaParameters params = new JavaParameters();
        String jreHome = myConfiguration.isAlternativeJrePathEnabled() ? myConfiguration.getAlternativeJrePath() : null;
        params.setJdk(JavaParametersUtil.createProjectJdk(myConfiguration.getProject(), jreHome));
        params.setEnv(myConfiguration.getEnvs());
        this.setupJavaParameters(params);

        // Bring back the original
        myConfiguration.setProgramParameters(originalParameters);

        // Jumble-jar path
        // Need to change this !!!
        String jumbleFile = JumbleOptions.getInstance().getPath();
        params.setJarPath(FileUtil.toSystemDependentName(jumbleFile));
        return params;

    }
}
