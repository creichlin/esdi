esdi
====
Even Smaller Dependency Injection

Why?
----

Just trying out if di is possible with a much smaller codebase than all the others colling themselves small.

It will depend on aspectJ, aspectJ will do most of the work.


What?
-----

First iteration:
    
    class Foo {
      @Inject
      private Bar bar;
    }

    class Bar {
    
    }

    Esdi esdi = new Esdi();
    // interfaces would work too
    esdi.for(Foo.class).create(Foo.class); 
    esdi.for(Bar.class).create(Bar.class);

    esdi.get(Foo.class); // will return a Foo instance with field bar set


