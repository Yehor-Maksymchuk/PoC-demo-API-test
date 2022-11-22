# DEMO for MasterControl

### Run api test 
```
mvn clean test -pl api
```

### Run bdd test
```
mvn clean test -pl bdd
```

### Run allure report 
* Allure for api module
```
mvn allure:serve -pl api
```
* Allure for bdd module
```
mvn allure:serve -pl bdd
```
