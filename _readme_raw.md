Short description about what your library exactly does.
*  [Features](#features)
*  [Installation](#install)


### <a name="features">Features</a>
1. feature 1 
2. feature 2


### <a name="install">Installation</a> 
Add to _build.gradle_ (app):
```groovy
implementation 'mehrpars.mobile.lib:librarysample:0.0.1'
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
 
 
### <a name="sample">Sample Application</a>
Describe concepts your Sample module covers based on main module features.
<br>
<p align="center">
  <img src="https://mgit.mparsict.com/android/base/project-sample/raw/master/screenshots/screenshot_1.png" width="250"/>
  <img src="https://mgit.mparsict.com/android/base/project-sample/raw/master/screenshots/screenshot_2.png" width="250"/>
</p>
<br>

 
## License  
[Mehr Pars ICT][mp]


[mp]: https://www.mehrparsict.com