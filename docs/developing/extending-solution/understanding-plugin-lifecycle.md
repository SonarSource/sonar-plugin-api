# Understanding plugins lifecycle

> :warning: The lifespan of Plugin instances is implementation-dependent.
> Products like SonarLint, which has a long lifespan, could decide to retain a `Plugin` instance and extensions in memory. The plugin classloader will also stay in memory for several analyses, so be particularly careful with static instances
