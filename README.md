[![License: MPL2](https://img.shields.io/badge/license-mpl2-ff69b4.svg)](https://www.mozilla.org/en-US/MPL/2.0/)

# InfoArchive SIP Creator Command Line Tool

The [EMC InfoArchive](http://www.emc.com/enterprise-content-management/infoarchive/) SIP Creator is a command line
tool that makes it quick and easy to create SIPs from common types of source systems, such RDBMS, CSV, filesystem,
XML etc. It uses the [SIP SDK](https://github.com/Enterprise-Content-Management/infoarchive-sip-sdk) to create SIPs
but enables you to create SIPs in many situations without writing any code. 

It has long been perceived by partners and customers that creating SIPs is difficult. Along with the SIP SDK the SIP Screator
aims to make the process simpler and faster by allowing you to create SIPs without writing code. 


### A note about EMC Documentum xDB 

Some of the modules require an EMC Documentum xDB license to use, the tool does not come with such a license. Instead you need to acquire one through Dell/EMC. The tool is fully functional even without
those modules but then you cannot use xDB as a staging database nor create SIPs from data residing in xDB.

To build you need to have xdb-api and xdb-impl jars in your local maven repository along with a text file called .xdb.license.txt in the root project, containing a valid xDB license key.



