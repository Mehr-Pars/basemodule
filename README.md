## Installation

Add to _build.gradle_ (app):
```groovy
implementation 'mehrpars.mobile.lib:basemodule:1.2.8'
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
 
## License  
[Mehr Pars ICT][mp]


[mp]: https://www.mehrparsict.com