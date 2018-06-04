# Release Helper

## What is this ?

A doc that helps you release the react-native package by bypassing the manual overhead.

### How to use it ?

Well , its fairly simple there is a script called UpdateCordova.sh in the scripts folder , run it and enter what it asks you.It will ask you for the path of
the cordova directory and the latest version of the framework, thats it !!  

It will create a branch , download the required files , update the package.json ,create a tag and a PR.All you have
do is add an assignee get it merged and run npm publish
