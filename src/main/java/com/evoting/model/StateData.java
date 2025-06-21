package com.evoting.model;

import java.util.List;

public class StateData {
    public List<State> states;

    public static class State {
        public String name;
        public String code;
        public List<String> cities;
    }
}
