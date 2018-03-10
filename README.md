# EmpMgmtBE
## ReST Services
1. GET  api/employees/all             -Gets all employess status=active + inactive
2. GET  api/employees                 -Gets all the active employees
3. GET  api/employees/{id}            -Gets a single active employee
4. POST api/employee                  -Create a new employee
5. PUT  api/employees/{id}            -Update an employee
6. DELETE api/employees/{id}          -Delete an employee (by status=inactive)
7. DELETE api/actual/employees/{id}   -Delete an employee from DB
8. DELETE api/actual/Employees        -Delete all the employees from DB
9. POST api/employees/Injest          -Bulk create list of employees from a file

## Functionality supported
### Employee Injest: 
To import list of employees from the pre-filled csv formatted file. 
Steps:
 1. Click on button "Employee Injest"
 2. Browse the pre-filled comma (,) separated file as per below format
 3. The CSV format is described below. Click Here to get Template [a link](https://github.com/kameshGithub/empmgmtweb/blob/master/Employees.csv)
```
EmpID, First Name, Middle Initial, Last Name, Date of Birth(YYYY-DD), Date of Employment(YYYY-MM-DD)
```
 4. Click on "Submit"
 5. On success a message will be displayed and user can navigate to view all the list of employees.
 6. By default the employees created will be with status="ACTIVE"
 7. It uses POST method to send file as multipart.

## Build from source
### Mongo DB
1. Start the mongo db
2. Get the monogo db related setting to update in the application

### ReST Application
1. Copy source
```sh
mkdir be
cd be
git clone https://github.com/kameshGithub/empmgmt.git
```
2. Till the external configuration or proxy/gateway/service discovery feature is implemented, below manual configuration is required.
```
1. Go to .\src\main\java\com\kamesh\empmgmt\employee\controller\EmployeeController.java
2. Go to Line 46, and replace the URL of your Front-end application. 
3. Go to .\src\main\resources\application.properties  and change the required settings e.g. mongo db related.
```
3. If you already have one, change the configuration accordingly.
```
mvn compile
mvn spring:boot run
```
4. The server will be launched at http://localhost:5000/
