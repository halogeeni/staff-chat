/*
 * The MIT License
 *
 * Copyright 2016 Oskar Gusgård, Aleksi Rasio, Joel Vainikka, Joona Vainikka.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Chat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aleksirasio
 */
public class Backlog {
    
    // singleton pattern
    private static Backlog backlogInstance = new Backlog();
    private final List<Message> backlog;
    private final List<Observer> observers;

    public static Backlog getInstance() {
        return backlogInstance;
    }

    protected Backlog() {
        this.backlog = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    // observer pattern is utilized to notify connected users on new entries 
    // and server messages
    // synchronized is used to enforce thread safety
    public synchronized void registerObserver(Observer observer) {
        // notify observers on user login
        for (Observer o : observers) {
            o.notifyServerMessage(observer.toString() + " has joined.");
        }
        observers.add(observer);
    }

    // synchronized is used to enforce thread safety
    public synchronized void deregisterObserver(Observer observer) {
        // notify observers on user logout
        for (Observer o : observers) {
            o.notifyServerMessage(observer.toString() + " has left.");
        }
        observers.remove(observer);
    }

    public synchronized void addMessage(Message msg) {
        // add entry to history
        backlog.add(msg);
        // notify observers about a new entry
        for (Observer observer : observers) {
            observer.notifyNewMessage(e);
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append(System.lineSeparator());
        builder.append("Complete server backlog: ");
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());

        for (Message msg : backlog) {
            builder.append(msg.getDetailedEntry());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
