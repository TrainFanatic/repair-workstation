# TO DO LIST!

This is adapted from my Criteria A document:
**1. Definition of the problem**
My client is a leader of an organisation that aims to teach repair literacy to reduce e-waste. This organisation is part of an international school.
The organisation handles hundreds of devices that are currently poorly organised. Specifically, it is difficult to receive those who want to pick up or drop off items
The organisation’s leader has made it clear to me that the following are issues:

- Repairers do not know where certain appliances areRepairers often find semi-repaired items, but without sufficient logging. As a result, they have to do otherwise unnecessary diagnostics testing again
- Potential clients often complain about the confusing existing registration system
- Repairers are only able to function when there are ‘leaders’ around to tell them what to do.

**2.** **Product Justification**

The client has stated that this problem is important to solve because:

- Firstly, this initiative is part of the school’s sustainability initiative.
- Furthermore, it has received much more attention in recent times and as a result has been put under load stress. 
- A wider community depends on the initiative—any problems will have effects outside the school. The process therefore has to run as smoothly as possible to keep its reputation intact.

According to the client, they claim that the organisation has attempted to use other software, such as calendar invites and online forms, however they have not worked very well. Their specific use case has very specific requirements, and therefore requires a specialised solution.
The following tools were decided to be used. The names of the tools, the features they entail, and the reasoning behind using them are outlined below.

| **Tool**   | **Feature**              | **Value / Purpose**                                          |
| ---------- | ------------------------ | ------------------------------------------------------------ |
| Java       | OOP                      | Working with OOP will be beneficial in making the code easier to create and more readable as the problem deals with many real life objects. Allows for easier abstraction |
| Java Swing | GUI                      | Will allow for an accessible interface with a user who has no experience with computers otherwise. |
| mySQL      | Databases, relationships | There are several parallel data objects that need to be kept track of. Devices, repairers, regular users (donators, pick-uppers). SQL’s ability to easily access, parse, and modify parallel tables makes it optimal for |

**3. Criteria for Success**
After clarifications with the client, I came up with a set of success criteria. After communicating these with the client, they agreed.

Storage: 

1. It should be able to keep log of appliances
   1. Where they are stored within the confines of the office
   2. Which Client they belong to*To whom the object is assigned*
   3. Ability to store descriptions of the individual appliances
   4. Store the stage of the process (also known as ‘status’) of repair (eg diagnosing, fixing, repaired)

Input:

2. Store two kinds of users: Repairers and Clients

3. Clients should be able to:
   1. Login
   2. Submit an appliance and a description of it through the application
   3. Submit pictures of the appliance
   4. Choose a booking time
   5. Withdraw appliances from the system if needed.
4. Repairers should be able to:
   1. Login
   2. Have control over all descriptions, tags of all appliances
   3. They can delete appointments (see Process) assigned to them.
   4. Input their ‘free times’
   5. Post ‘updates’ on individual appliances.
5. There should be a system to signup as a Client
6. Repairers can only be added by other repairers

Process:

7. When a Client submits an Appliance, assign it to a Repairer whose free times overlap with the Client’s time. This is called an appointment
   1. If there are no repairers whose free time overlaps with the selected time, then ask the client to select a new time.

Outputs:

8. Repairers can view:
   1. All appliances, their statuses and who they belong to.
9. Clients can view:
   1. Their own appliances and their statuses
   2. View the updates of their own appliances.

Security: 

10. Anyone should be able to register as clients. This includes the login system
11. Repairers and clients have different permissions, so that only trusted individuals can edit certain information.

Storage:

12. Storage of appliances, their descriptions, users, etc. will be done using the MySQL database
    1. As for pictures/videos, mySQL allows for “BLOBs” (Binary Large Objects)

Interface:

13. There must be a user-friendly GUI
    1. When selecting dates, there should be a calendar selection dropdown
    2. Friendly menus for file selection
    3. Buttons should be the main method of navigation.