# Simple JCR Listener

This JCR listener will listen to events in a given path of the JCR 

## Steps to use

1. Create a service user via /crx/explorer - call the user systemlistener
2. Change the users permissions to allow for `jcr:all` under /
3. Add a an Apache Sling Service User Mapper Service Amendment with `core=systemlistener`
4. Install the bundle

## Steps to build 

1. Have AEM running on port 4502 since the project is going to auto deploy the bundle to AEM
2. Run `mvn clean install`
3. Check /system/console for the bundle to be running
4. Create a sling logger for `com.patlego.sling.project.core.listener` with INFO since this will log a lot of information
5. Once complete stop the bundle which will release the session

# Contributor
[Patrique Legault](https://twitter.com/_patlego)