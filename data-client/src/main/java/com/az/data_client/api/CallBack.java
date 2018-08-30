package com.az.data_client.api;

import io.reactivex.functions.Consumer;

public class CallBack<T> {
    public Consumer<T> onSuccess;
    public Consumer<Throwable> onException;
    public  CallBack(){
        onSuccess = new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {

            }
        };
        onException = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        };
    }
    public  CallBack(Consumer<T> onSuccess){
        this.onSuccess = onSuccess;
        onException = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        };
    }

    public CallBack(Consumer<T> onSuccess, Consumer<Throwable> onException) {
        this.onSuccess = onSuccess;
        this.onException = onException;
    }
}