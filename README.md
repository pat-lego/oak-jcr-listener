# Simple JCR Listener

This JCR listener will listen to events in a given path of the JCR 

## Steps to use (AEM Classic)

1. Create a service user via /crx/explorer - call the user systemlistener
2. Change the users permissions to allow for `jcr:all` under /
3. Add a an Apache Sling Service User Mapper Service Amendment with `aem-debugging-jcr-listener=systemlistener`
4. Install the bundle

## Steps to use (AEMaaCS)

1. Run the following repo init script, change the path `var` to whatever path you want, PID: org.apache.sling.jcr.repoinit.RepositoryInitializer.6894e2f0-9485-46e4-9241-f43e5de9778c.cfg.json
```
// Configuration created by Apache Sling JCR Installer
{
  "scripts":[
    "create service user systemlistener",
    "set ACL for systemlistener\r\n    allow jcr:all on /var/sitemaps\r\nend"
  ]
}
```
2. Set the following sling service user amendement, PID: org.apache.sling.serviceusermapping.impl.ServiceUserMapperImpl.amended.6094ef96-ec0c-4070-97e2-84724ea08902.cfg.json
```
// Configuration created by Apache Sling JCR Installer
{
  "user.mapping":[
    "aem-debugging-jcr-listener:systemlistener=systemlistener"
  ]
}
```

## Steps to build 

1. Have AEM running on port 4502 since the project is going to auto deploy the bundle to AEM
2. Run `mvn clean install`
3. Check /system/console for the bundle to be running
4. Create a sling logger for `com.patlego.sling.project.core.listener` with INFO since this will log a lot of information
5. Once complete stop the bundle which will release the session

# Contributor
[Patrique Legault](https://twitter.com/_patlego)