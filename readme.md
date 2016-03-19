esdi
====
Even Smaller Dependency Injection

Why?
----

Just trying out if di is possible with a much smaller codebase than all the other di frameworks calling themselves small.

It will depend on javassist, javassist will do most of the heavy lifting.


What?
-----

### First iteration

#### Usage
    
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

### Fourth iteration

Using instances for injection. Always the same instance will be delivered, does not matter
if SingletonProvider or whatever is used, unless a custom Provider is created that supports cloning.

    Esdi.onRequestFor(Bar.class).with(InjectThreadLocal.class).deliverInstance(new Bar());
    
### Fifth Iteration

#### Usage

Adding deliverThreaLocal(ThreadLocalInstance) so the initial value is fetched from that threadLocal.
Allows for repeated data injection where the data comes from an external source.

A Provider.clear() method is added to clear old data from for example a ThreadLocalProvider where it's
thread is used in another context.

    // for an operation which runs on one thread.
    
    ThreadLocal<OperationData> operationData = new ThreadLocaL<>();
    ThreadLocalProvider provider = new ThreadLocalProvider(); 
    Esdi.register(InjectOperation.class, provider);
    Esdi.onRequestFor(OperationData.class).with(InjectOperation.class).deliverThreadLocal(operationData);
    
    // in each operation setup
    operationData.set(externalOperationData);
    provider.clear();
    // from now all
    @InjectOpration OperationData operationData...
    // will give the current operationData value until thread is used for a new operation

#### Result

It's getting to compilcated to understand intuitively what the result will be...

### Sixth iteration

Removed aspectj and using javassist. Results in performance improovement. Also the class level @Inject annotation is not needed anymore which was actually the biggest pifall I run into while using it in some projects.

The ThreadLocal provider is a bit complicated in usage but it's very useful for repetive thread bound units of code execution like sessions for webapplications which are composed of a series of requests which run on different threads. The setup is a bit difficult but usually that is only an initial setup that will not have to be changed and spans only a few lines of code.
