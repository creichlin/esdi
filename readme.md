esdi
====
Even Smaller Dependency Injection

Why?
----

Just trying out if di is possible with a much smaller codebase than all the other di frameworks calling themselves small.

It will depend on aspectJ, aspectJ will do most of the heavy lifting.


What?
-----

### First iteration

#### Usage
    
    @Inject
    class Foo {
      @Inject private Bar bar;
    }

    interface Bar {
      ...
    }

    class BarImplementation {
      ...
    }

    
    Esdi.onRequestFor(Foo.class).deliver(Foo.class); 
    Esdi.onRequestFor(Bar.class).deliver(BarImplementation.class);

    Foo foo1 = Esdi.get(Foo.class); // will create a Foo instance with field bar set
    Foo foo2 = new Foo(); // will also have the field bar set
    
#### Result

aspectj cannot find classes with fields with a certain annotation. For this every single class which uses injection
needs to be annotated with the inject annotation additionally to the annotated fields.

there is an experimental hasfield() function in aspectJ but it seems to not work so far.


### Second iteration

#### Usage

Use own annotation annotated with @Inject to get instances from different sources.
So far I have only singleton and threadlocal in mind. The current/default will create a new instance on each request.
    
    Esdi.register(InjectSingleton.class, SingletonProvider.class);
    Esdi.register(InjectThreadLocal.class, ThreadLocalProvider.class);
    
    Esdi.onRequestFor(Foo.class).with(InjectSingleton.class).deliver(Foo.class);
    Esdi.onRequestFor(Bar.class).with(InjectThreadLocal.class).deliver(Bar.class);
    Esdi.onRequestFor(Qux.class).deliver(Qux.class);
    
    
    @Inject
    class Pojo {
      @InjectSingleton private Foo foo;
      @InjectThreadLocal private Bar bar;
      @Inject private Qux qux;
    }
    
    Pojo pojo = new Pojo();
    
    // pojo.foo will be the same instance for the whole application (unless you fiddle around with classloaders)
    // pojo.bar will be the same instance for calls from the same thread
    // pojo.qux will be a new instance for every instance of Pojo
    
#### Result

Annotation cannot extend another annotation so the custom inject annotations need to be annotated with @Inject.

So far all annotation are checked if there is an injectable instance available. This can not be used in Production. To
prevent this a lot of runtime checking needs to be done. With a lot of short lived classes this can be a performance
problem.

Therefore, in the long term, aspectJ is probably not viable but some other bytecode manipulation lib might do the trick.
It will be more work though.

### Third iteration

Clean up code, document, add some tests.
    
#### Result

A lot of fancy javadoc.    
    
