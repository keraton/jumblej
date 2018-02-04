package com.github.keraton;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.Nullable;

@State(name = "jumbleOptions", storages = @Storage("jumble.xml"))
public class JumbleOptions implements PersistentStateComponent<JumbleOptions.State> {

    static class State {

        String jarPath;

    }

    State myState = new State();

    @Nullable
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(State state) {
        this.myState = state;
    }

    @Override
    public void noStateLoaded() {

    }

    @Nullable
    public String getPath() {
        return myState.jarPath;
    }

    public void setPath(String path) {
        myState.jarPath = path;
    }


    public static JumbleOptions getInstance() {
        return ServiceManager.getService(JumbleOptions.class);
    }


}
