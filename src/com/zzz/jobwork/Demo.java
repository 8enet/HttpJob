package com.zzz.jobwork;

/**
 * Created by zl on 2014/11/29.
 */
public class Demo<T> {
    public InnerDemo<T> innerDemo=new InnerDemo<>();

    public class InnerDemo<T>{
        @Override
        public String toString() {
            return getClass().toString();
        }
    }
}
