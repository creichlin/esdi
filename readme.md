esdi
====
Even Smaller Dependency Injection

Why?
----

Just trying out if di is possible with a much smaller codebase than all the others calling themselves small.

It will depend on aspectJ, aspectJ will do most of the heavy lifting.


What?
-----

First iteration:
    
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
    esdi.onRequestFor(Bar.class).deliver(BarImplementation.class);

    Foo foo1 = esdi.get(Foo.class); // will create a Foo instance with field bar set
    Foo foo2 = new Foo(); // will also have the field bar set


