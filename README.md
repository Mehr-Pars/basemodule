## Installation

Add to _build.gradle_ (app):
```groovy
implementation 'mehrpars.mobile.lib:basemodule:1.2.9'
```

Add to _build.gradle_ (Project):
```groovy
allprojects {
    repositories {
        
        maven {
            url "http://maven2.mpars.ir/artifactory/libs-release-local"
            credentials {
                username = "${mpars_artifactory_username}"
                password = "${mpars_artifactory_password}"
            }
        }
       
    }
}
```
 
  ## Setup Counly  
1. Call `initCountly(serverUrl, apiKey)` in your application class which extends BaseApp.
2. Define OpenUDID_service in project manifest
```xml
<service android:name="org.openudid.OpenUDID_service">  
	<intent-filter> 
		<action android:name="org.openudid.GETUDID" />  
	</intent-filter>
</service>
```
3. Add countly serverUrl to network_security_config.xml avoiding CleartextTraffic error on newer android APIs
```xml
<domain-config cleartextTrafficPermitted="true">  
	<!-- Countly Server URL -->  
	<domain includeSubdomains="true">5.63.8.226</domain>  
</domain-config>
```
 
 
## License  
[Mehr Pars ICT][mp]


[mp]: https://www.mehrparsict.com