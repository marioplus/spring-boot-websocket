package com.demo.websocket.websocket.notify;

import com.demo.websocket.websocket.WebSocketServer;
import com.demo.websocket.websocket.common.Request;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager extends Observable {

    private ConcurrentHashMap<Integer, IEventListener> events;

    private static class SingletonHolder {
        private static final EventManager INSTANCE = new EventManager();
    }

    private EventManager(){
        events = new ConcurrentHashMap<>();
    }

    public static final EventManager getInstance(){
        return  SingletonHolder.INSTANCE;
    }

    public synchronized void addEvent(int eventCode, IEventListener eventListener){
        events.put(eventCode,eventListener);
    }

    public synchronized void deleteEvent(IEventListener iEventListener){
        if(iEventListener==null)
            throw new NullPointerException("移除不存在的事件。");
        boolean contains = events.contains(iEventListener);
        if(contains){
            events.remove(iEventListener.getCode());
        }
    }

    public void notifyEvent(WebSocketServer ws, Request request){
        IEventListener eventListener = events.get(request.getCode());
        if(eventListener != null){
            eventListener.fire(ws,request.getDate());
        }
    }

}
