package ir.map.g221.containers.factory;

import ir.map.g221.containers.Container;

public interface Factory {
    Container createContainer(ContainerStrategy strategy);
}
