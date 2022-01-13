# Algorithm Model API

This is the API model for algorithms.  It consists of core interfaces that all algorithmic implementations
must use. In addition this API uses a `ServiceProvider` interface that implementations must also
provide a concrete implementation.

## SOLID Principles

This API is designed with SOLID principles in mind, particularly the separation of the 
logic from the interfaces, and that consuming applications will work against the interface
not the concrete implementation.

## The Model

The model consists of the following key interfaces:

- `Parameter<T>`: Represents a parameter that is used by a `Function` or `Model`.
  Parameters can have a `name` and an optional `variable` assigned to it so that
  the underlying operation can identify the parameter by either or both properties.
  There are several subinterfaces defined in the model (only a few common ones described below):

  - `ScalarParameter`: Represents a parameter containing a `Scalar` value.

  - `FunctionParameter<T>`: contains a `Function<T>` as the parameter value. As common
    sub-subinterface is also provided: `ScalarFunctionParameter`, which is a parameter that
    holds a `Function` that returns a `Scalar` value.
  
  - `VectorParameter<T>`: contains a `Vector<T>` as the parameter value. Like `FunctionParameter<T>`,
    there is a commonly used sub-subinterace for `ScalarVector` parameters, aptly named
    `ScalarVectorParameter`.

- `Coefficient<T>`: Represents an output value from a `Model` operation. 

- `Function<T>`: Represents a function that returns a single result of type `T`. `Function`s can
  can pass in any number of `Parameter`s as required to perform the operation. A commonly used
  sub-interface is provided for convenience: `ScalarFunction`, which is effectively syntactic
  sugar for `Function<Scalar>`.

- `Model`: Represents an operation that returns one or more `Coefficient`s

- `Vector<T>`: Represents a set of values of `T` that can be used by a `Function` or `Model`

- `Matrix`: Represents a collection of `Vector` objects. This is still experimental.

## Factories

There are several factory interfaces that provide methods for creating implementations of the 
model above. These factories are accessed from the `ModelProvider` (discussed below):

- `VectorFactory`: contains methods for instantiating `Vector` instances

- `ParameterFactory`: contains methods for instantiating `Parameter` instances

- `CoefficientFactory`: contains methods for instantiating `Coefficient` instances

- `MatrixFactory`: contains methods for instantiating `Matrix` instances

## The Model Provider

The `ModelProvider` interface serves two purposes:

1. It contains accessor methods for the factories above
2. It becomes the entry point for consumers to access the implementation model

An implementation jar would include the following file in the jar file:

`/META-INF/services/io.xmljim.algorithms.model.provider.ModelProvider`

This file would contain the fully qualified class name of the implementation of `ModelProvider`.
For example, assume there is an implementation called `org.acme.algorithms.provider.ModelProviderImpl` -
this would be what you would include the file.

Provided that both this jar and the implementation jar on are on the classpath, a consuming class
would instantiate the implementation as follows:

```java
Iterable<ModelProvider> modelProviders = ServiceLoader.load(ModelProvider.class);
//depending on whether there are more than one possible instances, you could iterate
//however, we'll assume only one instance here
ModelProvider provider = modelProviders.iterator().next();
```

## Use By other Function APIs

There are two "reference" implementations that use the Model API: Financial and Statistics.
There is also a default implementation jar that implements this model: "algorithms-model-impl"
(locate the dependency in the maven `pom.xml` file).