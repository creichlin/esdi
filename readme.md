esdi
====
Even Smaller Dependency Injection

Why?
----

Just trying out if di is possible with a much smaller codebase than all the others calling themselves small.

It will depend on aspectJ, aspectJ will do most of the heavy lifting.


What?
-----

### First iteration
    
    @Inject  // classes with injectable fields need to be annotated so far (due to aspectj hasfield() experimental feature not working)
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


### Second iteration

Use own annotation that extend from @Inject to get instances from different sources.
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
    // pojo.qux will be a new instance for every instance of Qux
    
    
    
    
    