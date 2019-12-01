<details>
<summary>Table of Content</summary>

# Table of Content
1. [Installation](#installation)
    <details>
    <summary> Tomcat Installation</summary>

    1. [Tomcat Installation](#tomcat-installation)
       <details>

       1. [Tomcat installation on Ubuntu](#tomcat-installation-on-ubuntu)
       2. [Steps](#steps)
       </details>
    2. [MySQL Installation](#mysql-installation)
    3. [Project Installations](#project-installation)
    4. [Android App Installation](#android-app-installation)
    </details>
</details>

# Timetabler-v2.0
Server-side application for the Timetable Scheduling application.

## Installation
Packages, libraries in use and the project’s code are installed and maintained by Maven.
All installation instructions can be found on [Apache Maven site](https://maven.apache.org)

### Tomcat Installation
Other packages required for installation are:
Tomcat installation instructions can be found [here](https://tomcat.apache.org/tomcat-9.0-doc/setup.html),
for both Windows and Linux operating systems.

#### Tomcat installation on Ubuntu.
Tomcat requires Java to run run, install openJDK 11, OpenJDK is also required for project class files.

##### Steps
Open terminal by pressing _CTRL+ALT+T_ to run these commands.

create tomcat user:

`sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat`

Download tomcat:

`wget http://www-eu.apache.org/dist/tomcat/tomcat-9/v9.0.14/bin/apache-tomcat-9.0.14.tar.gz -P /tmp`

Extract tomcat to /opt/tomcat:

`sudo tar xf /tmp/apache-tomcat-9*.tar.gz -C /opt/tomcat`

Create a symbolic link to handle other versions:

`sudo ln -s /opt/tomcat/apache-tomcat-9.0.14 /opt/tomcat/latest`

Let tomcat user have access to tomcat dir:

`sudo chown -RH tomcat: /opt/tomcat/latest`

Scripts inside bin dir must have executable flag, so run this:

`sudo sh -c 'chmod +x /opt/tomcat/latest/bin/*.sh'`

Create a systemd Unit file to run tomcat as a service:

`sudo nano /etc/systemd/system/tomcat.service`

Copy and paste this into file <tomcat.service>

```
[Unit]
Description=Tomcat 9 servlet container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/default-java"
Environment="JAVA_OPTS=-Djava.security.egd=file:///dev/urandom -Djava.awt.headless=true"

Environment="CATALINA_BASE=/opt/tomcat/latest"
Environment="CATALINA_HOME=/opt/tomcat/latest"
Environment="CATALINA_PID=/opt/tomcat/latest/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ExecStart=/opt/tomcat/latest/bin/startup.sh
ExecStop=/opt/tomcat/latest/bin/shutdown.sh

[Install]
WantedBy=multi-user.target
```

*Change the jdk directory to the JAVA_HOME you choose*

Notify the systemd of the new unit file:

`sudo systemctl daemon-reload`

Start tomcat service:

`sudo systemctl start tomcat`

Automatically start tomcat service at boot:

`sudo systemctl enable tomcat`

Adjust firewall to accept port 8080 for TCP connections
*(if you don’t have ufw install it by running `sudo apt install ufw` 
in terminal)*:

`sudo ufw allow 8080/tcp`

Configure tomcat web management interface to access and work with
management tools:

```
</opt/tomcat/latest/conf/tomcat-users.xml>
<tomcat-users>
<!--
    Comments
-->
   <role rolename="admin-gui"/>
   <role rolename="manager-gui"/>
   <user username="admin" password="admin_password" roles="admin-gui,manager-gui"/>
</tomcat-users>
```

Tomcat remote access
file: 

manager: /opt/tomcat/latest/webapps/manager/META-INF/context.xml

or

host-manager: /opt/tomcat/latest/webapps/host-manager/META-INF/context.xml

Restart tomcat service

`sudo systemctl restart tomcat`

Check that tomcat installed properly by going to 
*http://localhost:8080*, it should open a tomcat home page.

#### MySQL installation
Run:

`sudo apt install mysql-server`

on the terminal.

#### Project Installations
Run the project deploy script file *deploy.sh* 
after installing the project’s dependencies above.
The file can be found the project’s root directory, ie *Timetabler-v2.0/*, or 
if you are on Windows OS run this command from the project’s root folder:

`mvn clean install`

This will create a *timetabler.war* file in the folder *target/*.

Copy and paste the file in the tomcat directory under webapps, 
it will be under tomcat/latest/webapps/.

Open this url [localhost](http://localhost:8080/timetabler) 
to determine if the installation was successful.

#### Android App installation
Android application installation can be found in the 
release section of github packages, 
which can be found [here](https://github.com/ben-mathu/TimetablerApp/releases)
Download it to your device and tap on the file to install it.
