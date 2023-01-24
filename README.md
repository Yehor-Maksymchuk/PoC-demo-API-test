# DEMO for MasterControl

### Run api test 
```
mvn clean test -pl api
```

### Run bdd test
```
mvn clean test -pl bdd
```
### Run performance tools
```
 mvn clean gatling:test -Dlogback.configurationFile=perf-test/src/main/resources/logback.xml -Dgatling.simulationClass=com.mastercontrol.rc.CreateWorkflowSimulation -pl perf-test
 mvn clean gatling:test -Dlogback.configurationFile=perf-test/src/main/resources/logback.xml -Dgatling.simulationClass=com.mastercontrol.rc.GetWorkflowSimulation -pl perf-test
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
