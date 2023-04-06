package com.app.questit.utils.patterns;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observerList= new ArrayList<>();
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }
    public void notifyObservers() {
        for (Observer observer : observerList)
            observer.update();

    }
}
