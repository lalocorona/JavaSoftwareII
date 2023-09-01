WGU-C195-APPOINTMENT SCHEDULER
By EDUARDO CORONA VIRGEN
ecoron7@wgu.edu
VERSION: 1.0
08/07/2023

IntelliJ IDEA 2023.1 (Community Edition)
Build #IC-231.8109.175, built on March 28, 2023,
Runtime version: 17.0.6+10-b829.5 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

Java SDK 17.0.7
JavaFX-SDK 11.0.2

MySQL Connector driver: mysql-connector-j-8.0.33

SCENARIO
You are working for a software company that has been contracted to develop a GUI-based scheduling desktop application.
The contract is with a global consulting organization that conducts business in multiple languages and has main offices
in Phoenix, Arizona; White Plains, New York; Montreal, Canada; and London, England. The consulting organization has
provided a MySQL database that the application must pull data from. The database is used for other systems, so its
structure cannot be modified.

The organization outlined specific business requirements that must be met as part of the application. From these
requirements, a system analyst at your company created solution statements for you to implement in developing the
application. These statements are listed in the requirements section.

Your company acquires Country and First-Level-Division data from a third party that is updated once per year. These
tables are prepopulated with read-only data. Please use the attachment “Locale Codes for Region and Language” to review
division data. Your company also supplies a list of contacts, which are prepopulated in the Contacts table; however,
administrative functions such as adding users are beyond the scope of the application and done by your company’s
IT support staff. Your application should be organized logically using one or more design patterns and generously
commented using Javadoc so your code can be read and maintained by other programmers.

APPLICATION
This project is a developed GUI-based scheduling desktop application. A consulting organization provided a
MySQL database that the application pulls data from. This application serves as an appointment scheduler
with various functions such as displaying appointments in a tableview. The application allows you to update
create appointments as well as pulls customer data, appointment data, and reporting data.

HOW TO RUN
At start of application you can log in the application by using the text boxes and pressing the login button. There
are labels with features such as the current language and displays the zoneID location of the local machine. Another
label that gets displayed is the timezone of the current machine. There is also a feature that gets the default
language from the machine and adjusts the language of the text if the language detected is French. If there is an
appointment within 15 minutes of the current time, there will be an informational display letting you know.

The appointment view features a tableview of all the appointments that are located in the MySQL database. There are
two radio buttons when selected will change the data that is located in the database. If "Appointment By Week" is
selected, all the appointments during the current week will be displayed. If "Appointment By Month" is selected, all
the appointments during the month will be displayed. There are five buttons with different functions. When selecting
"New Appointment" this will load the scene to add appointments when you selected an item. Selecting "Update Appointment"
will load and pass appointments to another scene for editing. To delete an appointment, you must have an appointment
selected for it to be deleted in the MySQL database. When selecting "View Customers", the scene will change and load
a tableview of customers.

The customers scene has similar functions like creating customers, updating customers and deleting customers. A feature
of deleting customers includes logical validation to insure a customer doesn't have an appointment. If the customer has
an appointment, you will get an error message.

There are also reports that are generated. A user can get a report detailing a customers schedule when the contact ID
is selected. There is also reporting for counting the amount of appointments when a month is selected as well as the
type of appointments. I also included another report that gets the count of customers based on the country.



