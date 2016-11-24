[![License: MPL2](https://img.shields.io/badge/license-mpl2-ff69b4.svg)](https://www.mozilla.org/en-US/MPL/2.0/)

# InfoArchive SIP Creator Command Line Tool

The [EMC InfoArchive](http://www.emc.com/enterprise-content-management/infoarchive/) SIP Creator is a command line
tool that makes it quick and easy to create SIPs from common types of source systems, such RDBMS, CSV, filesystem,
XML etc. It uses the [SIP SDK](https://github.com/Enterprise-Content-Management/infoarchive-sip-sdk) to create SIPs
but enables you to create SIPs in many situations without writing any code. 

It has long been perceived by partners and customers that creating SIPs is difficult. Along with the SIP SDK the SIP Screator
aims to make the process simpler and faster by allowing you to create SIPs without writing code. 

## Quickstart

The SIP Creator tool looks for a configuration file called conf.yml in the current working directory. The configuration file specifies the steps that the tool should execute. After build to run a simple configuration you can: 

```shell
$cd build/distributions ; unzip sipcreator.zip ; cd sipcreator
$cat > conf.yml
main:
  echo: Hello!
$./bin/sipcreator 
Hello!
```

For more complex examples refer to the end2end tests. 

## A note about EMC Documentum xDB 

Some of the modules require an EMC Documentum xDB license to build and use. The tool does not come with such a license. Instead you need to acquire one through Dell/EMC. The tool is fully functional even without
those modules but you cannot use any module requiring xDB, such as using xDB as a staging database, create SIPs from data residing in xDB or quering xDB.

To build with xDB you need to: 

* Have xdb-api and xdb-impl jars in a repository of your own. 

** If you have access to a maven repository with those jars you can just add a property to gradle.properties in gradle user home called 'iaSipCreatorMavenRepo' and it will be automatically picked up.
 
** If you don't have access to such a maven repository you can either

*** install them in your local maven repository

*** Or put them in a lib folder in the project root.
 
* Have a license to xDB placed in a text file called .xdb.license.txt in the root project

* Append the -Pxdb property to the build. 




