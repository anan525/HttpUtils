package com.httptools;

public class SelfHttpClient {

    private SelfDispatcher selfDispatcher;

    public SelfDispatcher dispatcher() {
        return selfDispatcher;
    }

    private SelfHttpClient(Builder builder) {
        selfDispatcher = builder.selfDispatcher;
    }

    public SelfCall newCall(SelfRequest request) {
        return SelfRealCall.newCall(this, request);
    }


    public final static class Builder {

        private SelfDispatcher selfDispatcher;

        public Builder() {
            selfDispatcher = new SelfDispatcher();
        }

        public Builder selfDispatcher(SelfDispatcher selfDispatcher) {
            this.selfDispatcher = selfDispatcher;
            return this;
        }

        public SelfHttpClient build() {
            return new SelfHttpClient(this);
        }
    }
}
