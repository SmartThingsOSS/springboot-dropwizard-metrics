# Release Process

This repo uses semantic versions and derives the version from git tags.

To build a new release create a github tag with `vN.M.L`.  This will trigger
CircleCI to start a new release build and publish artifacts to Bintray.
