A user guide for the app is provided in the repository.


Clone repository

## install json-server:
Write this command into the commandline
```
npm install -g json-server
```

## Insert ip-adress into project

Open the file smartshoppinglist/gradle.properties and assign your ip-adress to HostIp as follows HostIp=[ip-adress]:3000.
On emulator use 10.0.2.2 as ip-adress.
On mobile-phone use your computer local ip-adress
Local ip-adress can be read by writing ipconfig in the console.

## Start json-server:

### When using Emulator:

This does currently not work.
```
json-server --watch db.json
```
If the app is run in the emulator use 10.0.2.2 to access your devices localhost.


### When using Mobile-Phone:

json-server --host ip-adress  --watch db.json

If the app is run on a mobile-phone make sure the mobile-phone is in the same network as the computer the server is hosted on
and use your local ip-adress.
