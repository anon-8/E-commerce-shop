package com.anon.ecom.config;
public interface Mapper<A,B> {
    B mapTo(A a);
    A mapFrom(B b);
}
