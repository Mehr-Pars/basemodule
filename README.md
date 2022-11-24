This module defines main architecture as well as Android components to be used in all projects.
*  [Features](#features)
*  [Installation](#install)
*  [Setup Counly](#setup_countly)
*  [Install Crash Handler](#install_crash_handler)
*  [Sample Application](#sample)
*  [Third Party](#third_party)
*  [Wiki](#wiki)


### <a name="features">Features</a>
1. android **[architecture components][architecture_components]** and **mvvm**
2. support **paging** architecture and tools for easy use
3. **room** database with ssot architecture and tools for easy use
4. **navigation component** and tools for simple use of safe args
5. support **RxJava**
6. reactive **network state handling** 
7. manage and enqueue request with **safeRequest** in ViewModel
8. easy config of **retrofit** for networking
9. easy config of **countly** for crash report and app logs
10. manually **handle app crash** instead of default android crash dialog 


### <a name="install">Installation</a> 
Add to _build.gradle_ (app):
```groovy
implementation 'mehrpars.mobile.lib:basemodule:2.1.0-alpha04'
```

Add to _build.gradle_ (Project). use GitHubPackages url or MehrPars artifactory:
```groovy
allprojects {
    repositories {
        // get library from GitHubPackages
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Mehr-Pars/android-packages")
            credentials {
                username = mpars_github_packages_username
                password = mpars_github_packages_consume_auth_key
            }
        }
        // or from Mehr Pars artifactory repository
        maven {
            url "http://maven2.mpars.ir/artifactory/libs-release-local"
            credentials {
                username = mpars_artifactory_username
                password = mpars_artifactory_password
            }
        }
       
    }
}
```
 
### <a name="install_crash_handler">Install Crash Handler</a>
1. add DefaultErrorActivity to project manifest as below
```xml
<activity
    android:name="mehrpars.mobile.basemodule.ui.crash_handler.DefaultErrorActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"
    android:process=":error_activity"
    android:screenOrientation="sensorPortrait" />
```
2. extend your application from BaseApp and return restart activity in getRestartActivity().
this activity will be considered as the main activity to be launched after app crash
```Kotlin
class App : BaseApp() {

    override fun getRestartActivity(): Class<out Activity> {
        return HomeActivity::class.java
    }
}
```

### <a name="setup_countly">Setup Counly</a>

[Countly](count.ly) is used as main tool for crash reporting and logging app events
1. call `initCountly(serverUrl, apiKey)` in your application class which extends BaseApp.
2. define OpenUDID_service in project manifest
```xml
<service android:name="org.openudid.OpenUDID_service">  
	<intent-filter> 
		<action android:name="org.openudid.GETUDID" />  
	</intent-filter>
</service>
```
3. add countly serverUrl to network_security_config.xml avoiding CleartextTraffic error on newer android APIs
```xml
<domain-config cleartextTrafficPermitted="true">  
	<!-- Countly Server URL -->  
	<domain includeSubdomains="true">5.63.8.226</domain>  
</domain-config>
```
 
### <a name="sample">Sample Application</a>
These concept are covered in sample app:
1.  Paging Component: using paging tools in order to load paged list from server. supporting both offline and online loading simultaneously.
2.  Room: load and cache data based on ssot pattern
 
<br>
<p align="center">
  <img src="https://mgit.mparsict.com/android/libs/basemodule/-/raw/mehrpars/screenshots/screenshot_1.png" width="250"/>
  <img src="https://mgit.mparsict.com/android/libs/basemodule/-/raw/mehrpars/screenshots/screenshot_2.png" width="250"/>
</p>
<br>
 
 
### <a name="third_party">Third Party</a>
1. [Countly][countly]: crash report and app logs
2. [Glide][glide]: media management and image loading tool 
3. [ReactiveNetwork][reactivenetwork]: an Android library listening network connection state and internet connectivity with RxJava
4. [Retrofit][retrofit]: A type-safe HTTP client for Android and Java
 
 
### <a name="wiki">Wiki</a>
You can read documents about main features here.
* [Room DataBase][room]
* [Paging Library][paging]
 
 
## License  
[Mehr Pars ICT][mp]


[mp]: https://www.mehrparsict.com
[room]: https://mgit.mparsict.com/android/libs/basemodule/-/wikis/%DA%A9%D8%AA%D8%A7%D8%A8%D8%AE%D8%A7%D9%86%D9%87-Room
[paging]: https://mgit.mparsict.com/android/libs/basemodule/-/wikis/%DA%A9%D8%AA%D8%A7%D8%A8%D8%AE%D8%A7%D9%86%D9%87-Paging-3
[architecture_components]: https://developer.android.com/topic/libraries/architecture
[countly]: https://count.ly/
[glide]: https://github.com/bumptech/glide
[reactivenetwork]: https://github.com/pwittchen/ReactiveNetwork
[retrofit]: https://github.com/square/retrofit