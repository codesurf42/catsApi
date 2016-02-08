# We love cats!

This is a simple command line utility to
get some information about our lovely animals from
public-available APIs.

To run it, you may need JDK on your path. Please refer to
https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html#CJAGAACB
or to the documentation of your system.

You may use sbt build tool or activator (which is an extended version of sbt, included) 
to run this Scala app.

    ./activator "run [ file | categories | fact ]"

e.g. to get different categories of cats, use:

    ./activator "run categories"


## Todo

- handle URL errors (bad/no connection/etc)
-/+ handle parsing errors / more functional errors-resilient approach
- more generic approach?
+ fix logging
-/+ typed URL/URIs
- we may like to go more async...
- perhaps refactor to more files